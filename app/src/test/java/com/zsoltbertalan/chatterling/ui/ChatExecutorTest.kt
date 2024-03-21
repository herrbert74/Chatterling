package com.zsoltbertalan.chatterling.ui

import com.arkivanov.essenty.statekeeper.StateKeeperDispatcher
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.base.kotlin.ext.test
import com.zsoltbertalan.chatterling.domain.api.ChatterlingRepository
import com.zsoltbertalan.chatterling.domain.model.ChatElement
import com.zsoltbertalan.chatterling.ext.CurrentTime
import com.zsoltbertalan.chatterling.testhelper.ChatMessageMother
import com.zsoltbertalan.chatterling.ui.chat.ChatExecutor
import com.zsoltbertalan.chatterling.ui.chat.ChatStore
import com.zsoltbertalan.chatterling.ui.chat.ChatStoreFactory
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ChatExecutorTest {

	private val chatterlingRepository = mockk<ChatterlingRepository>(relaxed = true)

	private lateinit var chatExecutor: ChatExecutor

	private lateinit var chatStore: ChatStore

	private val testCoroutineDispatcher = Dispatchers.Unconfined

	private val currentTime: CurrentTime = mockk()

	@Before
	fun setUp() {
		coEvery { chatterlingRepository.getChatMessageFlow() } answers
			{ flowOf(ChatMessageMother.createChatMessageList(1711041850594L)) }

		every { currentTime.get() } answers { 1711041850594L }

		chatExecutor = ChatExecutor(
			chatterlingRepository,
			testCoroutineDispatcher,
			testCoroutineDispatcher,
			currentTime
		)

		chatStore =
			ChatStoreFactory(DefaultStoreFactory(), chatExecutor).create(stateKeeper = StateKeeperDispatcher())
	}

	@Test
	fun `when initial message flow received then timestamp is added to the start`() = runTest {
		val states = chatStore.states.test()

		states.first().chat.first() shouldBe ChatElement.ChatTimestamp("THURSDAY", "17:24")
	}

}


