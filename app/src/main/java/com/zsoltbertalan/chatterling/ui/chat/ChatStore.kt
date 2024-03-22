package com.zsoltbertalan.chatterling.ui.chat

import androidx.compose.runtime.Immutable
import com.arkivanov.mvikotlin.core.store.Store
import com.zsoltbertalan.chatterling.domain.model.ChatElement
import com.zsoltbertalan.chatterling.domain.model.NotSerializable
import com.zsoltbertalan.chatterling.ui.chat.ChatStore.Intent
import com.zsoltbertalan.chatterling.ui.chat.ChatStore.State
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.Serializable

interface ChatStore : Store<Intent, State, Nothing> {

	sealed class Intent {
		data class SendMessageClicked(val message: String) : Intent()
	}

	@Serializable
	@Immutable
	data class State(
		val chat: ImmutableList<ChatElement> = listOf<ChatElement>().toImmutableList(),
		@Serializable( with = NotSerializable::class )
		val error: Throwable? = null
	)

}