package com.zsoltbertalan.chatterling

import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.zsoltbertalan.chatterling.di.IoDispatcher
import com.zsoltbertalan.chatterling.di.MainDispatcher
import com.zsoltbertalan.chatterling.domain.api.ChatterlingRepository
import com.zsoltbertalan.chatterling.root.ChatterlingRootComponent
import com.zsoltbertalan.chatterling.root.ChatterlingRootContent
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ChatMessageDetailsTest {

	@get:Rule(order = 0)
	val hiltAndroidRule = HiltAndroidRule(this)

	@get:Rule
	val composeTestRule = createComposeRule()

	@Inject
	lateinit var chatterlingRepository: ChatterlingRepository

	@Inject
	@MainDispatcher
	lateinit var mainContext: CoroutineDispatcher

	@Inject
	@IoDispatcher
	lateinit var ioContext: CoroutineDispatcher

	@Before
	fun setUp() {

		hiltAndroidRule.inject()

		CoroutineScope(mainContext).launch {
			val chatterlingRootComponent = ChatterlingRootComponent(
				DefaultComponentContext(lifecycle = LifecycleRegistry()),
				mainContext,
				ioContext,
				chatterlingRepository,
			) {}
			composeTestRule.setContent {
				ChatterlingRootContent(chatterlingRootComponent)
			}
		}

	}

	@Test
	fun showChat() {

		composeTestRule.waitUntilAtLeastOneExists(hasTestTag("ChatHeader"), 1000L)

		composeTestRule.onNodeWithText("Affenpinscher", ignoreCase = true).assertExists()

	}

	@Test
	fun showChatBubbleImages() {

		composeTestRule.waitUntilAtLeastOneExists(hasTestTag("ChatHeader"), 3000L)

		composeTestRule.onNodeWithText("Affenpinscher", ignoreCase = true).performClick()

		composeTestRule.waitUntilAtLeastOneExists(hasTestTag("ChatMessageRow"), 3000L)

		composeTestRule.onAllNodesWithTag("ChatMessageRow").assertAny(hasTestTag("ChatMessageRow"))

	}

}