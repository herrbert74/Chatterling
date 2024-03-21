package com.zsoltbertalan.chatterling.data.db

import com.zsoltbertalan.chatterling.domain.model.ChatElement
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.notifications.InitialResults
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.notifications.UpdatedResults
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatMessageDao @Inject constructor(private val realm: Realm) : ChatMessageDataSource {

	override suspend fun purgeDatabase() {
		realm.write {
			val messages = this.query(ChatMessageDbo::class).find()
			delete(messages)
		}
	}

	override suspend fun insertMessages(chatMessages: List<ChatElement.ChatMessage>) {
		chatMessages.forEach { chatMessage ->
			realm.write {
				chatMessages.map { copyToRealm(chatMessage.toDbo(), UpdatePolicy.ALL) }
			}
		}
	}

	override fun getMessages(): List<ChatElement.ChatMessage> {
		return realm.query(ChatMessageDbo::class).find().toList().map { dbo -> dbo.toChatMessage() }
	}

	override fun getMessageFlow(): Flow<List<ChatMessageDbo>> {
		return realm.query(ChatMessageDbo::class)
			.asFlow()
			.map { changes -> getInsertions(changes) }
	}

	private fun getInsertions(changes: ResultsChange<ChatMessageDbo>): List<ChatMessageDbo> =
		when (changes) {
			is UpdatedResults -> changes.list.filterIndexed { index, _ -> changes.insertions.contains(index) }
			is InitialResults -> changes.list
			else -> emptyList()
		}

}
