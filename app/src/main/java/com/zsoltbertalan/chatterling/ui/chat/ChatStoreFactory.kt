package com.zsoltbertalan.chatterling.ui.chat

import com.arkivanov.essenty.statekeeper.StateKeeper
import com.arkivanov.essenty.statekeeper.consume
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.zsoltbertalan.chatterling.domain.model.ChatElement
import com.zsoltbertalan.chatterling.ui.chat.ChatStore.Intent
import com.zsoltbertalan.chatterling.ui.chat.ChatStore.State

class ChatStoreFactory(
	private val storeFactory: StoreFactory,
	private val chatExecutor: ChatExecutor,
) {

	fun create(stateKeeper: StateKeeper): ChatStore =
		object : ChatStore, Store<Intent, State, Nothing> by storeFactory.create(
			name = "ChatStore",
			initialState = stateKeeper.consume(key = "ChatStore") ?: State(),
			bootstrapper = ChatBootstrapper(),
			executorFactory = { chatExecutor },
			reducer = ChatReducer
		) {
		}.also {
			stateKeeper.register(key = "ChatStore") {
				it.state.copy()
			}
		}

	private class ChatBootstrapper : CoroutineBootstrapper<BootstrapIntent>() {
		override fun invoke() {
			dispatch(BootstrapIntent.ShowChat)
		}
	}

	private object ChatReducer : Reducer<State, Message> {
		override fun State.reduce(msg: Message): State =
			when (msg) {
				is Message.ShowChat -> copy(chat = msg.chat)
				is Message.AddElements -> copy(chat = chat + msg.newMessages)
				is Message.RemoveTailFromLastMessage -> {
					val newList = chat.toMutableList()
					val chatMessage = (newList.removeLast() as ChatElement.ChatMessage).copy(isTailed = false)
					newList.add(chatMessage)
					copy(chat = newList)
				}
				is Message.ShowError -> copy(error = msg.throwable)
			}
	}

}

sealed class Message {
	data class ShowChat(val chat: List<ChatElement>) : Message()
	data class AddElements(val newMessages: List<ChatElement>) : Message()
	data object RemoveTailFromLastMessage : Message()
	data class ShowError(val throwable: Throwable) : Message()
}

sealed class BootstrapIntent {
	data object ShowChat : BootstrapIntent()
}
