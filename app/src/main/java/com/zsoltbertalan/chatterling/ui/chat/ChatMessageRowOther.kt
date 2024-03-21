package com.zsoltbertalan.chatterling.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.babestudios.base.compose.design.smallDimensions
import com.zsoltbertalan.chatterling.design.ChatterlingTypography
import com.zsoltbertalan.chatterling.design.Colors
import com.zsoltbertalan.chatterling.domain.model.ChatElement.ChatMessage

@Composable
fun ChatMessageRowOther(
	modifier: Modifier = Modifier,
	chatMessage: ChatMessage,
	onItemClicked: (ChatMessage) -> Unit,
	isTailed: Boolean = false
) {

	val tailedRoundedCornerShape = RoundedCornerShape(16.dp, 16.dp, 2.dp, 16.dp)

	Row(modifier = modifier
		.padding(vertical = 8.dp, horizontal = 16.dp)
		.fillMaxWidth(1f)){
		Spacer(modifier.weight(0.2f))
		Box(
			modifier = modifier
				.align(Alignment.Top)
				.weight(0.8f)
		) {
			Row(modifier = Modifier
				.clickable { onItemClicked(chatMessage) }
				.width(IntrinsicSize.Max)
				.align(Alignment.CenterEnd)
				.background(
					color = Colors.primary,
					shape = if (isTailed) tailedRoundedCornerShape else RoundedCornerShape(16.dp)
				)
				.padding(vertical = smallDimensions.marginNormal, horizontal = smallDimensions.marginLarge)
				.testTag("ChatRow")
			) {

				Text(
					text = chatMessage.text,

					style = ChatterlingTypography.bodyMedium.merge(color = Colors.onPrimary),
					modifier = Modifier
						.weight(1.0f)
						.padding(vertical = smallDimensions.marginNormal, horizontal = smallDimensions.marginNormal)
					//.recomposeHighlighter()
				)

			}
		}
	}
}

@Preview()
@Composable
fun ChatMessageRowOtherPreview() {
	Column {
		ChatMessageRowOther(
			Modifier.align(Alignment.End),
			ChatMessage(text = "Not very long text"), {},
		)
	}
}

@Preview()
@Composable
fun ChatMessageRowOtherLongPreview() {
	Column {
		ChatMessageRowOther(
			Modifier.align(Alignment.End),
			ChatMessage(text = "Very long text, that will be multiline in preview, so we can test multiline previews."),
			{},
		)
	}
}

@Preview()
@Composable
fun TailedChatMessageRowOtherPreview() {
	Column {
		ChatMessageRowOther(
			Modifier.align(Alignment.End),
			ChatMessage(text = "Not long text"), {}, true,
		)
	}
}
