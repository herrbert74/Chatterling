package com.zsoltbertalan.chatterling.domain.api

import com.zsoltbertalan.chatterling.domain.model.ChatElement.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatterlingRepository {

	suspend fun sendMessage(message: ChatMessage)

	suspend fun getAllChat(): List<ChatMessage>

	suspend fun getChatMessageFlow(): Flow<List<ChatMessage>>

}
