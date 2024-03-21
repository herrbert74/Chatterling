package com.zsoltbertalan.chatterling.ui.chat

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.mvikotlin.core.store.Store
import com.zsoltbertalan.chatterling.domain.model.ChatElement
import com.zsoltbertalan.chatterling.ui.chat.ChatStore.Intent
import com.zsoltbertalan.chatterling.ui.chat.ChatStore.SideEffect
import com.zsoltbertalan.chatterling.ui.chat.ChatStore.State
import kotlinx.parcelize.Parcelize

interface ChatStore : Store<Intent, State, SideEffect> {

	sealed class Intent {
		data class SendMessageClicked(val message: String) : Intent()
	}

	@Parcelize
	data class State(val chat: List<ChatElement> = emptyList(), val error: Throwable? = null) : Parcelable

	sealed class SideEffect {
		data object Initial : SideEffect()
		data object ShowToast : SideEffect()
	}
}