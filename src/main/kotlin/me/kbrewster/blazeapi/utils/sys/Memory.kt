package me.kbrewster.blazeapi.utils.sys

object Memory {

    /**
     * Gets the total memory allocated to the program
     */
    fun getTotalMemory() = Runtime.getRuntime().totalMemory()

    /**
     * Gets the amount of free memory
     */
    fun getFreeMemory() = Runtime.getRuntime().freeMemory()

    /**
     * Gets total memory used
     */
    fun getMemory() = Runtime.getRuntime().run { totalMemory() - freeMemory() }

    /**
     * Runs garbage collector
     */
    fun runGC() = System.gc()
}