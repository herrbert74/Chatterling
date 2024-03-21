package com.zsoltbertalan.chatterling.root

import com.zsoltbertalan.chatterling.domain.api.ChatterlingRepository
import com.zsoltbertalan.chatterling.ext.CurrentTime
import com.zsoltbertalan.chatterling.ui.chat.ChatComponent
import com.zsoltbertalan.chatterling.ui.chat.ChatExecutor
import kotlinx.coroutines.CoroutineDispatcher

/**
* These are higher order functions with common parameters used in the RootComponent,
* that return functions, that create the Decompose feature components from feature specific parameters.
*/

internal fun createChatFactory(
	chatterlingRepository: ChatterlingRepository,
	mainContext: CoroutineDispatcher,
	ioContext: CoroutineDispatcher,
): CreateChatComp = { childContext, finishHandler, output ->
	ChatComponent(
		componentContext = childContext,
		chatExecutor = ChatExecutor(chatterlingRepository, mainContext, ioContext, CurrentTime.Impl()),
		output = output,
		finishHandler = finishHandler,
	)
}
