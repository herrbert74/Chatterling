package com.zsoltbertalan.chatterling.root

import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.zsoltbertalan.chatterling.ui.chat.ChatComp

val navigation = StackNavigation<Configuration>()

internal fun onChatOutput(output: ChatComp.Output): Unit = when (output) {

	is ChatComp.Output.Selected ->
	{}

}
