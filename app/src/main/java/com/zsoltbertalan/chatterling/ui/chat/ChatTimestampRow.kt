package com.zsoltbertalan.chatterling.ui.chat

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.babestudios.base.compose.design.smallDimensions
import com.zsoltbertalan.chatterling.design.ChatterlingTypography
import com.zsoltbertalan.chatterling.design.Colors
import com.zsoltbertalan.chatterling.design.bodySmallBold
import com.zsoltbertalan.chatterling.domain.model.ChatElement

@Composable
fun ChatTimestampRow(chatTimestamp: ChatElement.ChatTimestamp) {

	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = smallDimensions.marginNormal, horizontal = smallDimensions.marginLarge)
			.testTag("ChatTimestampRow")
	) {

		Text(
			text = chatTimestamp.day,
			style = ChatterlingTypography.bodySmallBold.merge(color = Colors.onSurface),
			textAlign = TextAlign.End,
			modifier = Modifier
				.weight(1.0f)
				.padding(end = 4.dp)
		)
		Text(
			text = chatTimestamp.timestamp,
			style = ChatterlingTypography.bodySmall.merge(color = Colors.onSurface),
			modifier = Modifier
				.weight(1.0f)
		)

	}
}
