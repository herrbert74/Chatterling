package com.zsoltbertalan.chatterling.ui.chat

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.zsoltbertalan.chatterling.domain.api.ChatterlingRepository
import com.zsoltbertalan.chatterling.ext.apiRunCatching
import com.zsoltbertalan.chatterling.ui.chat.ChatStore.Intent
import com.zsoltbertalan.chatterling.ui.chat.ChatStore.SideEffect
import com.zsoltbertalan.chatterling.ui.chat.ChatStore.State
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

val randomMessages = listOf(
	"Hello", "That's a long message to test the receiving of messages.", "Cheers",
	"Absolutely", "I will see if that is possible"
)

class ChatExecutor(
	private val chatterlingRepository: ChatterlingRepository,
	val mainContext: CoroutineDispatcher,
	private val ioContext: CoroutineDispatcher,
) : CoroutineExecutor<Intent, BootstrapIntent, State, Message, SideEffect>(mainContext) {

	override fun executeAction(action: BootstrapIntent, getState: () -> State) {
		when (action) {
			is BootstrapIntent.ShowChat -> {
				scope.launch {
					scope.launch(ioContext) {
						apiRunCatching { chatterlingRepository.getAllChat() }
							.onSuccess {
								withContext(mainContext) { dispatch(Message.ShowChat(it)) }
							}.onFailure {
								withContext(mainContext) { dispatch(Message.ShowError(it)) }
								publish(SideEffect.ShowToast)
							}
					}.join()
					scope.launch(ioContext) {
						chatterlingRepository.getChatMessageFlow().collect {
							withContext(mainContext) { dispatch(Message.ShowChat(it)) }
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

	private fun sendMessage(message: String) {
		scope.launch(ioContext) {
			chatterlingRepository.sendMessage(message)

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
				chatterlingRepository.receiveMessage(message)
			}
		}
	}

}