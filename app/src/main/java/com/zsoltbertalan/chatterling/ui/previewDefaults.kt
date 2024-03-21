package com.zsoltbertalan.chatterling.ui

import com.zsoltbertalan.chatterling.data.ChatterlingAccessor
import com.zsoltbertalan.chatterling.data.db.ChatMessageDataSource
import com.zsoltbertalan.chatterling.data.db.ChatMessageDbo
import com.zsoltbertalan.chatterling.domain.model.ChatElement.ChatMessage
import com.zsoltbertalan.chatterling.ext.CurrentTime
import com.zsoltbertalan.chatterling.ui.chat.ChatExecutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

val defaultChatMessageDataSource = object : ChatMessageDataSource {

	override suspend fun purgeDatabase() {
		TODO("Not yet implemented")
	}

	override suspend fun insertMessages(chatMessages: List<ChatMessage>) {
		TODO("Not yet implemented")
	}

	override fun getMessages(): List<ChatMessage> {
		TODO("Not yet implemented")
	}

	override fun getMessageFlow(): Flow<List<ChatMessageDbo>> {
		TODO("Not yet implemented")
	}
}

fun defaultChatExecutor() = ChatExecutor(
	ChatterlingAccessor(defaultChatMessageDataSource),
	Dispatchers.Main,
	Dispatchers.IO,
	CurrentTime.Impl()
)
