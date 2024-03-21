package com.zsoltbertalan.chatterling.testhelper

import com.zsoltbertalan.chatterling.domain.model.ChatElement.ChatMessage

/**
 * This is an example of an ObjectMother that can be used in both Unit and Android UI tests.
 * As such it would go into its own module in a normal project.
 */
object ChatMessageMother {

	fun createChatMessageList(timestamp: Long): List<ChatMessage> = listOf(
		createDefaultChatMessage(id = "0", text = "message1", timestamp),
		createDefaultChatMessage(id = "1", text = "message2", timestamp),
		createDefaultChatMessage(id = "2", text = "message3", timestamp, isReceivedMessage = true),
		createDefaultChatMessage(id = "3", text = "message4", timestamp, isReceivedMessage = true),
		createDefaultChatMessage(id = "4", text = "message5", timestamp),
		createDefaultChatMessage(id = "5", text = "message6", timestamp),
	)

}

private fun createDefaultChatMessage(
	id: String = "",
	text: String = "Hello",
	timestamp: Long,
	isReceivedMessage: Boolean = false,
): ChatMessage = ChatMessage(
	id = id,
	text = text,
	timestamp = timestamp,
	isReceivedMessage = isReceivedMessage,
)
