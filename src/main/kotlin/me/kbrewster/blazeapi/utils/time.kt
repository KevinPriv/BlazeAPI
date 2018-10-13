@file:JvmName("TimeUtils")

package me.kbrewster.blazeapi.utils

import java.time.OffsetDateTime

inline val currentTimeMs inline get() = System.currentTimeMillis()

val currentOffsetDateTime: OffsetDateTime get() = OffsetDateTime.now()
