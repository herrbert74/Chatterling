package com.zsoltbertalan.chatterling.data.db

import com.zsoltbertalan.chatterling.domain.model.ChatElement
import com.zsoltbertalan.chatterling.ext.fromMillis
import com.zsoltbertalan.chatterling.ext.toEpochMillis
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class ChatMessageDbo() : RealmObject {

	constructor(
		text: String,
		timestamp: Long, //milliSeconds, but it will be stored as RealmInstant (seconds + nanos)
		isReceivedMessage: Boolean,
	) : this() {
		this.text = text
		this.realmInstant = RealmInstant.fromMillis(timestamp)
		this.isReceivedMessage = isReceivedMessage
	}

	@PrimaryKey
	var id: ObjectId = ObjectId()
	var text: String = ""
	var realmInstant: RealmInstant = RealmInstant.fromMillis(System.currentTimeMillis())
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
	timestamp = this.realmInstant.toEpochMillis(),
	isReceivedMessage = this.isReceivedMessage,
)
