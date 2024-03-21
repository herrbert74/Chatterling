package com.zsoltbertalan.chatterling.domain.model

import com.arkivanov.essenty.parcelable.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class ChatElement : Parcelable {

	@Parcelize
	data class ChatMessage(
		val id: String = "",
		val text: String = "",
		val timestamp: Long = 0L,
		val isReceivedMessage: Boolean = false,
	) : ChatElement(), Parcelable

	@Parcelize
	data class ChatTimestamp(
		val day: String = "",
		val timestamp: String = "",
	) : ChatElement(), Parcelable

}
