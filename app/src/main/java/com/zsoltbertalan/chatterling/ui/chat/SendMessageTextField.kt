package com.zsoltbertalan.chatterling.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.zsoltbertalan.chatterling.R
import com.zsoltbertalan.chatterling.design.Colors
import com.zsoltbertalan.chatterling.design.smallDimensions
import com.zsoltbertalan.chatterling.testhelper.ChatMessageMother
import com.zsoltbertalan.chatterling.ui.defaultChatExecutor
import kotlinx.collections.immutable.toImmutableList

@Composable
fun SendMessageTextField(
	modifier: Modifier = Modifier,
	comp: ChatComp,
	model: ChatStore.State
) {
	var message by rememberSaveable { mutableStateOf("") }

	val keyboardController = LocalSoftwareKeyboardController.current
	Row(
		modifier = modifier.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.Center
	) {
		OutlinedTextField(
			value = message,
			onValueChange = { message = it },
			keyboardOptions = KeyboardOptions(
				keyboardType = KeyboardType.Text,
				imeAction = ImeAction.Done
			),
			keyboardActions = KeyboardActions(
				onDone = {
					keyboardController?.hide()
					comp.onSendMessageClicked(message)
					message = ""
				},
			),
			modifier = Modifier
				.padding(horizontal = smallDimensions.marginLarge, vertical = smallDimensions.marginNormal)
				.weight(0.9f),
			shape = RoundedCornerShape(32.dp),
			isError = model.error != null,
			maxLines = 5
		)
		IconButton(
			onClick = {
				keyboardController?.hide()
				comp.onSendMessageClicked(message)
				message = ""
			},
			enabled = message.isNotBlank(),
			modifier = Modifier
				.padding(end = smallDimensions.marginLarge, top = smallDimensions.marginNormal, bottom = smallDimensions.marginNormal)
				.weight(0.1f)
				.then(
					if (message.isNotBlank()) Modifier else Modifier.alpha(0.5F)
				)
				.background(
					Colors.primary,
					CircleShape
				)
		) {
			Icon(
				imageVector = Icons.AutoMirrored.Filled.Send,
				contentDescription = stringResource(id = R.string.chat_send_message),
				tint = Colors.onPrimary
			)
		}
	}
}

@Preview()
@Composable
fun SendMessageTextFieldPreview() {
	val componentContext = DefaultComponentContext(lifecycle = LifecycleRegistry())
	SendMessageTextField(
		Modifier,
		ChatComponent(componentContext, defaultChatExecutor(), {}, {}),
		ChatStore.State(chat = ChatMessageMother.createChatMessageList(1711041850594L).toImmutableList()),
	)
}