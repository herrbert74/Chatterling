package com.zsoltbertalan.chatterling.di

import android.content.Context
import android.net.ConnectivityManager
import com.zsoltbertalan.chatterling.data.ChatterlingAccessor
import com.zsoltbertalan.chatterling.data.db.ChatMessageDataSource
import com.zsoltbertalan.chatterling.domain.api.ChatterlingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@Suppress("unused")
@InstallIn(SingletonComponent::class)
class SingletonModule {

	@Provides
	@Singleton
	fun provideChatterlingRepository(
		chatMessageDataSource: ChatMessageDataSource,
	): ChatterlingRepository {
		return ChatterlingAccessor(chatMessageDataSource)
	}

	@Provides
	@Singleton
	internal fun provideConnectivityManager(
		@ApplicationContext context: Context
	): ConnectivityManager {
		return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
	}

	@DefaultDispatcher
	@Provides
	fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

	@IoDispatcher
	@Provides
	fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

	@MainDispatcher
	@Provides
	fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDispatcher
