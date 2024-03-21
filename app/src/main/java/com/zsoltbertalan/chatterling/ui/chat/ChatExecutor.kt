package com.zsoltbertalan.chatterling.ui.chat

import androidx.compose.ui.text.decapitalize
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.zsoltbertalan.chatterling.domain.api.ChatterlingRepository
import com.zsoltbertalan.chatterling.domain.model.ChatElement
import com.zsoltbertalan.chatterling.ext.CurrentTime
import com.zsoltbertalan.chatterling.ui.chat.ChatStore.Intent
import com.zsoltbertalan.chatterling.ui.chat.ChatStore.SideEffect
import com.zsoltbertalan.chatterling.ui.chat.ChatStore.State
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import timber.log.Timber
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

val randomMessages = listOf(
	"Hello", "That's a long message to test the receiving of messages.", "Cheers",
	"Absolutely", "I will see if that is possible"
)

const val ONE_HOUR = 60 * 60 * 1000

class ChatExecutor(
	private val chatterlingRepository: ChatterlingRepository,
	val mainContext: CoroutineDispatcher,
	private val ioContext: CoroutineDispatcher,
	private val currentTime: CurrentTime,
) : CoroutineExecutor<Intent, BootstrapIntent, State, Message, SideEffect>(mainContext) {

	override fun executeAction(action: BootstrapIntent, getState: () -> State) {
		when (action) {
			is BootstrapIntent.ShowChat -> {
				scope.launch(ioContext) {
					chatterlingRepository.getChatMessageFlow().collect { messages ->
						if (messages.isNotEmpty()) {
							val chatElements = messages.addTimeStamps(getState().chat)
							Timber.d("zsoltbertalan* addTimeStamps: $chatElements")
							withContext(mainContext) { dispatch(Message.AddElements(chatElements)) }
						}
					}
				}
			}
		}
	}

	override fun executeIntent(intent: Intent, getState: () -> State) {
		when (intent) {
			is Intent.SendMessageClicked -> sendMessage(intent.message)
		}
	}

	//Send user message, but also emulate responses by randomly adding 0, 1 or 2 messages
	private fun sendMessage(message: String) {
		scope.launch(ioContext) {
			val chatMessage = ChatElement.ChatMessage(text = message, timestamp = currentTime.get())
			chatterlingRepository.sendMessage(chatMessage)

			when (Random.nextInt(0, 3)) {
				0 -> receiveMessage()
				1 -> receiveMessage(2)
				2 -> {} //Do nothing
			}
		}
	}

	private fun receiveMessage(count: Int = 1) {
		for (i in 0 until count) {
			val message = randomMessages[Random.nextInt(0, randomMessages.size)]
			val delay = Random.nextInt(0, 40)
			scope.launch(ioContext) {
				delay(delay.seconds)
				val chatMessage =
					ChatElement.ChatMessage(
						text = message, timestamp = currentTime.get(), isReceivedMessage = true
					)
				chatterlingRepository.sendMessage(chatMessage)
			}
		}
	}

}

private fun List<ChatElement.ChatMessage>.addTimeStamps(initialChatElements: List<ChatElement>): List<ChatElement> {
	return fold(mutableListOf<ChatElement>()) { list, message ->
		if (needsToAddTimestamp(initialChatElements, list, message)) {
			val instant = Instant.fromEpochMilliseconds(message.timestamp).toLocalDateTime(TimeZone.UTC)
			list.add(ChatElement.ChatTimestamp(
				instant.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() },
				"${instant.time.hour}:${instant.time.minute}")
			)
		}
		list.add(message)
		list
	}.toList()
}

/**
 * We need to add timestamp when:
 * 	firstMessage: first message ever
 * 	firstOldMessageForAnHour: there is a gap between initial messages from the database
 * 	firstNewMessageForAnHour: there is a gap between the last message and new message added now
 */
private fun needsToAddTimestamp(
	initialChatElements: List<ChatElement>,
	list: MutableList<ChatElement>,
	message: ChatElement.ChatMessage
): Boolean {

	val firstMessage = initialChatElements.isEmpty() && list.isEmpty()

	val firstOldMessageForAnHour = list.isNotEmpty()
		&& (list.last() as ChatElement.ChatMessage).timestamp + ONE_HOUR < message.timestamp

	val firstNewMessageForAnHour = initialChatElements.isNotEmpty() && list.isEmpty()
		&& (initialChatElements.last() as ChatElement.ChatMessage).timestamp + ONE_HOUR < message.timestamp

	return firstMessage || firstOldMessageForAnHour || firstNewMessageForAnHour

}
