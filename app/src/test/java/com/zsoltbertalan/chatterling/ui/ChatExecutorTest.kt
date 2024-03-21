package com.zsoltbertalan.chatterling.ui

import com.arkivanov.essenty.statekeeper.StateKeeperDispatcher
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.base.kotlin.ext.test
import com.zsoltbertalan.chatterling.domain.api.ChatterlingRepository
import com.zsoltbertalan.chatterling.testhelper.ChatMessageMother
import com.zsoltbertalan.chatterling.ui.chat.ChatExecutor
import com.zsoltbertalan.chatterling.ui.chat.ChatStore
import com.zsoltbertalan.chatterling.ui.chat.ChatStoreFactory
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Test

class ChatExecutorTest {

	private val chatterlingRepository = mockk<ChatterlingRepository>(relaxed = true)

	private lateinit var chatExecutor: ChatExecutor

	private lateinit var chatStore: ChatStore

	private val testCoroutineDispatcher = Dispatchers.Unconfined

	@Before
	fun setUp() {
		coEvery { chatterlingRepository.getAllChat() } answers { ChatMessageMother.createChatMessageList() }

		chatExecutor = ChatExecutor(
			chatterlingRepository,
			testCoroutineDispatcher,
			testCoroutineDispatcher
		)

		chatStore =
			ChatStoreFactory(DefaultStoreFactory(), chatExecutor).create(stateKeeper = StateKeeperDispatcher())
	}

	@Test
	fun `when started then getChat is called and returns correct list`() {
		val states = chatStore.states.test()

		coVerify(exactly = 1) { chatterlingRepository.getAllChat() }
		states.first().chat shouldBe ChatMessageMother.createChatMessageList()
	}

	@Test
	fun `when sort button is pressed then getChat returned in reverse order`() {
		val states = chatStore.states.test()

		chatStore.accept(ChatStore.Intent.SendMessageClicked)

		states.last().chat shouldBe ChatMessageMother.createChatMessageList().reversed()
	}

}


