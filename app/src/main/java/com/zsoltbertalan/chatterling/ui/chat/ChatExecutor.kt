package com.zsoltbertalan.chatterling.ui.chat

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.zsoltbertalan.chatterling.domain.api.ChatterlingRepository
import com.zsoltbertalan.chatterling.domain.model.ChatElement
import com.zsoltbertalan.chatterling.ext.CurrentTime
import com.zsoltbertalan.chatterling.ui.chat.ChatStore.Intent
import com.zsoltbertalan.chatterling.ui.chat.ChatStore.State
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

val randomMessages = listOf(
	"Hello", "That's a long message to test the receiving of messages.", "Cheers",
	"Absolutely", "I will see if that is possible"
)

const val ONE_HOUR = 60 * 60 * 1000
const val TAIL_TIME_GAP = 20 * 1000

class ChatExecutor(
	private val chatterlingRepository: ChatterlingRepository,
	val mainContext: CoroutineDispatcher,
	private val ioContext: CoroutineDispatcher,
	private val currentTime: CurrentTime,
) : CoroutineExecutor<Intent, BootstrapIntent, State, Message, Nothing>(mainContext) {

	override fun executeAction(action: BootstrapIntent, getState: () -> State) {
		when (action) {
			is BootstrapIntent.ShowChat -> {
				scope.launch(ioContext) {
					chatterlingRepository.getChatMessageFlow().collect { messages ->
						if (messages.isNotEmpty()) {
							val latestMessage = getState().chat.lastOrNull() as? ChatElement.ChatMessage
							latestMessage?.removeTailIfNeeded(messages.first()) {
								scope.launch(mainContext) { dispatch(Message.RemoveTailFromLastMessage) }
							}
							val chatElements = messages
								.addTails()
								.addTimeStamps(getState().chat)
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

	/**
	 * Send user message, but also emulate responses by randomly adding 0, 1 or 2 messages
	 */
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

/**
 * The latest message needs its tail removed if:
 * 	- New message is from the same sender
 * 	- New message is within [TAIL_TIME_GAP] milliseconds
 */
private fun ChatElement.ChatMessage.removeTailIfNeeded(
	newMessage: ChatElement.ChatMessage,
	dispatch: () -> Unit
) {
	val isTheSameSender = isReceivedMessage == newMessage.isReceivedMessage
	val isRecentMessage = timestamp + TAIL_TIME_GAP >= newMessage.timestamp
	if (isTheSameSender && isRecentMessage) dispatch()
}

/**
 * No tail is saved into the database, so all messages need to go through this function to add them to the list.
 *
 * The message needs will have tail if:
 * 	- It's the latest message
 * 	- Next message is from the other sender
 * 	- Next message is NOT within [TAIL_TIME_GAP] milliseconds
 */
private fun List<ChatElement.ChatMessage>.addTails(): List<ChatElement.ChatMessage> {
	return windowed(2, partialWindows = true) { list ->
		val isLAstElement = list.size == 1
		val isNotTheSameSender = list.size == 2 && list[0].isReceivedMessage != list[1].isReceivedMessage
		val isNotRecentMessage = list.size == 2 && (list[0].timestamp + TAIL_TIME_GAP < list[1].timestamp)
		if (isLAstElement || isNotTheSameSender || isNotRecentMessage)
			list[0].copy(isTailed = true)
		else
			list[0]
	}
}

/**
 * No timestamp is saved into the database, so all messages need to go through this function to add them to the list.
 */
private fun List<ChatElement.ChatMessage>.addTimeStamps(initialChatElements: List<ChatElement>): List<ChatElement> {
	return fold(mutableListOf<ChatElement>()) { list, message ->
		if (needsToAddTimestamp(initialChatElements, list, message)) {
			val instant = Instant.fromEpochMilliseconds(message.timestamp).toLocalDateTime(TimeZone.UTC)
			list.add(
				ChatElement.ChatTimestamp(
					instant.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() },
					"${instant.time.hour}:${instant.time.minute}"
				)
			)
		}
		list.add(message)
		list
	}.toList()
}

/**
 * We need to add timestamp when:
 * 	- firstMessage: first message ever
 * 	- firstOldMessageForAnHour: there is a gap between initial messages from the database
 * 	- firstNewMessageForAnHour: there is a gap between the last message and new message added now
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
