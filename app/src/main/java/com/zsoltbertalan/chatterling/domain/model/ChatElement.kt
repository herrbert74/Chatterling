package com.zsoltbertalan.chatterling.domain.model

import kotlinx.serialization.Serializable

@Serializable
sealed class ChatElement {

	@Serializable
	data class ChatMessage(
		val id: String = "",
		val text: String = "",
		val timestamp: Long = 0L, //milliseconds
		val isReceivedMessage: Boolean = false,
		val isTailed:Boolean = false,
	) : ChatElement()

	@Serializable
	data class ChatTimestamp(
		val day: String = "",
		val timestamp: String = "",
	) : ChatElement()

}
