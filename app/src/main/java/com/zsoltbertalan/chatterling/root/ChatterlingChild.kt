package com.zsoltbertalan.chatterling.root

import com.zsoltbertalan.chatterling.ui.chat.ChatComp

sealed class ChatterlingChild {
	data class Chat(val component: ChatComp) : ChatterlingChild()
}
