@file:JvmName("MemoryUtils")

package me.kbrewster.blazeapi.utils

/**
 * Gets the total memory allocated to the program
 */
val totalMemory get() = Runtime.getRuntime().totalMemory()

/**
 * Gets the amount of free memory
 */
val freeMemory get() = Runtime.getRuntime().freeMemory()

/**
 * Gets total memory used
 */
val memory get() = Runtime.getRuntime().run { totalMemory() - freeMemory() }

/**
 * Runs garbage collector
 */
fun runGC() = System.gc()