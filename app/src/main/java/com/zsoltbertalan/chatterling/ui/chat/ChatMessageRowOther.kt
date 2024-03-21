package com.zsoltbertalan.chatterling.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.babestudios.base.compose.design.smallDimensions
import com.zsoltbertalan.chatterling.design.ChatterlingTypography
import com.zsoltbertalan.chatterling.design.Colors
import com.zsoltbertalan.chatterling.domain.model.ChatElement.ChatMessage

@Composable
fun ChatMessageRowOther(chatMessage: ChatMessage, onItemClicked: (ChatMessage) -> Unit) {

	val tailedRoundedCornerShape = RoundedCornerShape(16.dp, 16.dp, 16.dp, 2.dp)
	Box(
		modifier = Modifier
			.padding(vertical = 8.dp, horizontal = 16.dp)
			.fillMaxWidth(0.8f)
	) {
		Row(modifier = Modifier
			.clickable { onItemClicked(chatMessage) }
			.width(IntrinsicSize.Max)
			.background(
				color = Colors.surfaceContainer,
				shape = if (chatMessage.isTailed) tailedRoundedCornerShape else RoundedCornerShape(16.dp)
			)
			.padding(vertical = smallDimensions.marginNormal, horizontal = smallDimensions.marginLarge)
			.testTag("ChatRow")
		) {

			Text(
				text = chatMessage.text,

				style = ChatterlingTypography.bodyMedium.merge(color = Colors.onSurface),
				modifier = Modifier
					.weight(1.0f)
					.padding(vertical = smallDimensions.marginNormal, horizontal = smallDimensions.marginNormal)
			)

		}
	}
}

@Preview
@Composable
fun ChatMessageRowOtherPreview() {
	ChatMessageRowOther(
		ChatMessage(text = "Not very long text"),
	) {}
}

@Preview
@Composable
fun ChatMessageRowOtherLongPreview() {
	ChatMessageRowOther(
		ChatMessage(text = "Very long text, that will be multiline in preview, so we can test multiline previews."),
	) {}
}

@Preview
@Composable
fun TailedChatMessageRowOtherPreview() {
	ChatMessageRowOther(
		ChatMessage(text = "Not long text"),
	) {}
}
