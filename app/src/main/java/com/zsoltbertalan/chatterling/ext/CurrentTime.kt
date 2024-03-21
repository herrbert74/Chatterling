package com.zsoltbertalan.chatterling.ext

interface CurrentTime {

    fun get(): Long

    class Impl : CurrentTime {
        override fun get() = System.currentTimeMillis()

    }
}
