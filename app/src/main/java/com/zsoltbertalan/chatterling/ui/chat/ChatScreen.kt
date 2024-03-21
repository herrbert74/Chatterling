package com.zsoltbertalan.chatterling.ui.chat

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.zsoltbertalan.chatterling.R
import com.zsoltbertalan.chatterling.design.ChatterlingTypography
import com.zsoltbertalan.chatterling.design.Colors
import com.zsoltbertalan.chatterling.domain.model.ChatElement
import com.zsoltbertalan.chatterling.testhelper.ChatMessageMother
import com.zsoltbertalan.chatterling.ui.chat.ChatStore.SideEffect.Initial
import com.zsoltbertalan.chatterling.ui.chat.ChatStore.SideEffect.ShowToast
import com.zsoltbertalan.chatterling.ui.defaultChatExecutor

@Composable
fun ChatScreen(component: ChatComp) {

	val model by component.state.subscribeAsState()

	val sideEffect by component.sideEffects.collectAsState(Initial)

	BackHandler(onBack = { component.onBackClicked() })

	ChatScaffold(component, model, sideEffect)

}

@Composable
private fun ChatScaffold(
	component: ChatComp,
	model: ChatStore.State,
	sideEffect: ChatStore.SideEffect
) {
	Scaffold(
		topBar = {
			TopAppBar(
				modifier = Modifier
					.padding(bottom = 10.dp)
					.shadow(
						elevation = 6.dp,
						spotColor = Color.DarkGray
					),
				colors = topAppBarColors(
					containerColor = Colors.surface,
					titleContentColor = Colors.onSurface,
				),
				navigationIcon = {
					IconButton(onClick = { component.onBackClicked() }) {
						Icon(
							imageVector = Icons.AutoMirrored.Filled.ArrowBack,
							contentDescription = "Back",
							tint = MaterialTheme.colorScheme.onPrimaryContainer
						)
					}
				},
				title = {
					Row {
						Box(
							modifier = Modifier
								.padding(end = 4.dp)
								.background(Colors.primary, CircleShape)
						) {
							Icon(
								modifier = Modifier.padding(4.dp),
								imageVector = Icons.Default.Face,
								contentDescription = stringResource(id = R.string.avatar),
								tint = Colors.onPrimary
							)
						}
						Text("Sarah")
					}
				},
				actions = {
					IconButton(onClick = { }) {
						Icon(
							imageVector = Icons.Default.MoreVert,
							contentDescription = "More icon",
						)
					}
				}
			)
		}
	) { innerPadding ->
		if (model.error == null) {
			Column {
				LazyColumn(
					modifier = Modifier
						.weight(1.0f)
						.fillMaxWidth(1f)
						.padding(innerPadding),
					content = {
						items(
							model.chat.size,
							{ index -> model.chat[index] }
						) { index ->
							val chatElement = model.chat[index]
							if (chatElement is ChatElement.ChatMessage) {
								if (chatElement.isReceivedMessage) {
									ChatMessageRowOther(
										Modifier.align(Alignment.End),
										chatMessage = chatElement,
										onItemClicked = component::onItemClicked,
									)
								} else {
									ChatMessageRow(
										chatMessage = chatElement,
										onItemClicked = component::onItemClicked,
									)
								}
								Spacer(modifier = Modifier.padding(vertical = 8.dp))
							} else {
								ChatTimestampRow(chatTimestamp = chatElement as ChatElement.ChatTimestamp)
							}
						}
					}
				)
				SendMessageTextField(comp = component, model = component.state.value)
			}
		} else {
			ErrorView(innerPadding)
		}
		if (sideEffect == ShowToast) {
			//show toast or something else
		}
	}
}

@Composable
private fun ErrorView(innerPadding: PaddingValues) {
	Column(
		Modifier
			.fillMaxSize(1f)
			.padding(innerPadding),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		Image(
			painter = painterResource(com.babestudios.base.android.R.drawable.ic_business_error),
			contentDescription = null
		)
		Text(
			"Something went wrong",
			style = ChatterlingTypography.titleLarge,
			modifier = Modifier
				.align(Alignment.CenterHorizontally),
		)
	}
}

@Preview("Chat Screen Preview")
@Composable
fun ChatScreenPreview() {
	val componentContext = DefaultComponentContext(lifecycle = LifecycleRegistry())
	ChatScaffold(
		ChatComponent(componentContext, defaultChatExecutor(), {}, {}),
		ChatStore.State(chat = ChatMessageMother.createChatMessageList(1711041850594L)),
		Initial
	)
}

@Preview("Chat Screen Error Preview")
@Composable
fun ChatScreenErrorPreview() {
	val componentContext = DefaultComponentContext(lifecycle = LifecycleRegistry())
	ChatScaffold(
		ChatComponent(componentContext, defaultChatExecutor(), {}, {}),
		ChatStore.State(
			chat = ChatMessageMother.createChatMessageList(1711041850594L),
			error = RuntimeException("Something went wrong")
		),
		Initial
	)
}
