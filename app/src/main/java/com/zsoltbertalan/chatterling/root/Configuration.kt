package com.zsoltbertalan.chatterling.root

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

sealed class Configuration : Parcelable {

    @Parcelize
    data object Chat : Configuration()
}
