package com.zsoltbertalan.chatterling.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.zsoltbertalan.chatterling.design.ChatterlingTheme
import com.zsoltbertalan.chatterling.ui.chat.ChatScreen

@Composable
fun ChatterlingRootContent(component: ChatterlingRootComp) {

	val stack = component.childStackValue

	ChatterlingTheme {
		Children(stack = stack, animation = stackAnimation(scale())) {
			when (val child = it.instance) {
				is ChatterlingChild.Chat -> ChatScreen(child.component)
			}
		}
	}

}
