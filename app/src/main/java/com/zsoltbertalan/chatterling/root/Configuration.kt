package com.zsoltbertalan.chatterling.root

import kotlinx.serialization.Serializable

@Serializable
sealed class Configuration {

	@Serializable
	data object Chat : Configuration()
}
