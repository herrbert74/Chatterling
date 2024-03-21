package com.zsoltbertalan.chatterling.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.DefaultComponentContext
import com.zsoltbertalan.chatterling.di.IoDispatcher
import com.zsoltbertalan.chatterling.di.MainDispatcher
import com.zsoltbertalan.chatterling.domain.api.ChatterlingRepository
import com.zsoltbertalan.chatterling.root.ChatterlingRootComponent
import com.zsoltbertalan.chatterling.root.ChatterlingRootContent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@AndroidEntryPoint
class ChatterlingActivity : ComponentActivity() {

    @Inject
    lateinit var chatterlingRepository: ChatterlingRepository

    @Inject
    @MainDispatcher
    lateinit var mainContext: CoroutineDispatcher

    @Inject
    @IoDispatcher
    lateinit var ioContext: CoroutineDispatcher

    private lateinit var chatterlingRootComponent: ChatterlingRootComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chatterlingRootComponent = ChatterlingRootComponent(
            DefaultComponentContext(lifecycle, savedStateRegistry, viewModelStore, null),
            mainContext,
            ioContext,
            chatterlingRepository
        ) { finish() }

        setContent {
            ChatterlingRootContent(chatterlingRootComponent)
        }
    }

}
