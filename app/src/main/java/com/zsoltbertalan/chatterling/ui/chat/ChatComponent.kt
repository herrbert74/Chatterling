package com.zsoltbertalan.chatterling.ui.chat

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.zsoltbertalan.chatterling.domain.model.ChatElement.ChatMessage
import com.zsoltbertalan.chatterling.ext.asValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

interface ChatComp {

	fun onItemClicked(chatMessage: ChatMessage)

	fun onSendMessageClicked(message: String)

	fun onBackClicked()

	val state: Value<ChatStore.State>

	val sideEffects: Flow<ChatStore.SideEffect>

	sealed class Output {
		data class Selected(val chatMessage: ChatMessage) : Output()
	}

}

class ChatComponent(
	componentContext: ComponentContext,
	val chatExecutor: ChatExecutor,
	private val output: FlowCollector<ChatComp.Output>,
	private val finishHandler: () -> Unit,
) : ChatComp, ComponentContext by componentContext {

	private var chatStore: ChatStore =
		ChatStoreFactory(LoggingStoreFactory(DefaultStoreFactory()), chatExecutor).create(stateKeeper)

	override fun onItemClicked(chatMessage: ChatMessage) {
		CoroutineScope(chatExecutor.mainContext).launch {
			output.emit(ChatComp.Output.Selected(chatMessage = chatMessage))
		}
	}

	override fun onSendMessageClicked(message: String) {
		chatStore.accept(ChatStore.Intent.SendMessageClicked(message))
	}

	override fun onBackClicked() {
		CoroutineScope(chatExecutor.mainContext).launch {
			finishHandler.invoke()
			chatStore.dispose()
		}
	}

	override val state: Value<ChatStore.State>
		get() = chatStore.asValue()

	override val sideEffects: Flow<ChatStore.SideEffect>
		get() = chatStore.labels

}
