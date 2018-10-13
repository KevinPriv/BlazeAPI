package me.kbrewster.blazeapi.utils

object OperatingSystem {

    fun getPlatform(): Platform {
        val osName = System.getProperty("os.name").toLowerCase()
        return when {
            osName.contains("win") -> Platform.WINDOWS
            osName.contains("mac") -> Platform.MACOS
            osName.contains("solaris") || osName.contains("sunos") -> Platform.SOLARIS
            osName.contains("linux") || osName.contains("unix") -> Platform.LINUX
            else -> Platform.UNKNOWN
        }
    }

    enum class Platform {
        LINUX, SOLARIS, WINDOWS, MACOS, UNKNOWN
    }
}
