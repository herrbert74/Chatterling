package com.zsoltbertalan.chatterling.di

import com.zsoltbertalan.chatterling.data.db.ChatMessageDao
import com.zsoltbertalan.chatterling.data.db.ChatMessageDataSource
import com.zsoltbertalan.chatterling.data.db.ChatMessageDbo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

@Module
@Suppress("unused")
@InstallIn(SingletonComponent::class)
class DataModule {

	@Provides
	@Singleton
	fun provideRealmConfiguration() = RealmConfiguration.Builder(
		schema = setOf(ChatMessageDbo::class)
	).build()

	@Provides
	@Singleton
	fun provideRealm(realmConfiguration: RealmConfiguration) = Realm.open(realmConfiguration)

	@Provides
	@Singleton
	internal fun provideChatMessageDataSource(realm: Realm): ChatMessageDataSource {
		return ChatMessageDao(realm)
	}

}
