package com.zsoltbertalan.chatterling.data.db

import com.zsoltbertalan.chatterling.domain.model.ChatElement
import kotlinx.coroutines.flow.Flow

interface ChatMessageDataSource {

	suspend fun purgeDatabase()

	suspend fun insertMessages(chatMessages: List<ChatElement.ChatMessage>)

	fun getMessages(): List<ChatElement.ChatMessage>

	fun getMessageFlow(): Flow<List<ChatMessageDbo>>

}
