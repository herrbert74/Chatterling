package com.zsoltbertalan.chatterling.data.db

import com.zsoltbertalan.chatterling.domain.model.ChatElement
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class ChatMessageDbo() : RealmObject {

	constructor(
		text: String,
		timestamp: Long,
		isReceivedMessage: Boolean,
	) : this() {
		this.text = text
		this.timestamp = RealmInstant.from(timestamp, 0)
		this.isReceivedMessage = isReceivedMessage
	}

	@PrimaryKey
	var id: ObjectId = ObjectId()
	var text: String = ""
	var timestamp: RealmInstant = RealmInstant.from(System.currentTimeMillis(), 0)
	var isReceivedMessage: Boolean = false

}

fun ChatElement.ChatMessage.toDbo(): ChatMessageDbo = ChatMessageDbo(
	text = this.text,
	timestamp = this.timestamp,
	isReceivedMessage = this.isReceivedMessage,
)

fun ChatMessageDbo.toChatMessage(): ChatElement.ChatMessage = ChatElement.ChatMessage(
	id = id.toHexString(),
	text = this.text,
	timestamp = this.timestamp.epochSeconds,
	isReceivedMessage = this.isReceivedMessage,
)
