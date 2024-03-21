package com.zsoltbertalan.chatterling.ext

import io.realm.kotlin.types.RealmInstant

const val MILLIS_IN_SECOND = 1000
const val NANOS_IN_MILLIS = 1000_000

fun RealmInstant.Companion.fromMillis(milliSeconds: Long): RealmInstant =
	from(milliSeconds / MILLIS_IN_SECOND, milliSeconds.rem(MILLIS_IN_SECOND).toInt() * NANOS_IN_MILLIS)

fun RealmInstant.toEpochMillis(): Long =
	this.epochSeconds * MILLIS_IN_SECOND + this.nanosecondsOfSecond / NANOS_IN_MILLIS
