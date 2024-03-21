package com.zsoltbertalan.chatterling.data

import com.zsoltbertalan.chatterling.data.db.ChatMessageDataSource
import com.zsoltbertalan.chatterling.data.db.toChatMessage
import com.zsoltbertalan.chatterling.domain.api.ChatterlingRepository
import com.zsoltbertalan.chatterling.domain.model.ChatElement.ChatMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

@Singleton
class ChatterlingAccessor(
	private val chatMessageDataSource: ChatMessageDataSource,
) : ChatterlingRepository {

	override suspend fun sendMessage(message: ChatMessage) {
		chatMessageDataSource.insertMessages(listOf(message))
	}

	override suspend fun getAllChat(): List<ChatMessage> {
		return chatMessageDataSource.getMessages()
	}

	override suspend fun getChatMessageFlow(): Flow<List<ChatMessage>> {
		return chatMessageDataSource.getMessageFlow()
			.map { list ->
				list.map { dbo -> dbo.toChatMessage() }
			}
	}

}
