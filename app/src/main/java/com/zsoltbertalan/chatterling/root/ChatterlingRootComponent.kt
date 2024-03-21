package com.zsoltbertalan.chatterling.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.zsoltbertalan.chatterling.di.IoDispatcher
import com.zsoltbertalan.chatterling.di.MainDispatcher
import com.zsoltbertalan.chatterling.domain.api.ChatterlingRepository
import com.zsoltbertalan.chatterling.ui.chat.ChatComp
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.FlowCollector

typealias CreateChatComp = (ComponentContext, () -> Unit, FlowCollector<ChatComp.Output>) -> ChatComp
interface ChatterlingRootComp {
	val childStackValue: Value<ChildStack<*, ChatterlingChild>>
}

class ChatterlingRootComponent internal constructor(
	componentContext: ComponentContext,
	private val finishHandler: () -> Unit,
	private val createChatComp: CreateChatComp,
) : ChatterlingRootComp,
	ComponentContext by componentContext {

	constructor(
		componentContext: ComponentContext,
		@MainDispatcher mainContext: CoroutineDispatcher,
		@IoDispatcher ioContext: CoroutineDispatcher,
		chatterlingRepository: ChatterlingRepository,
		finishHandler: () -> Unit,
	) : this(
		componentContext = componentContext,
		finishHandler,
		createChatComp = createChatFactory(chatterlingRepository, mainContext, ioContext),
	)

	private val stack = childStack(
		source = navigation,
		initialStack = { listOf(Configuration.Chat) },
		handleBackButton = true,
		childFactory = ::createChild
	)

	override val childStackValue = stack

	private fun createChild(configuration: Configuration, componentContext: ComponentContext): ChatterlingChild =
		when (configuration) {

			is Configuration.Chat -> ChatterlingChild.Chat(
				createChatComp(
					componentContext.childContext(key = "ChatComponent"),
					finishHandler,
					FlowCollector(::onChatOutput)
				)
			)
		}

}
