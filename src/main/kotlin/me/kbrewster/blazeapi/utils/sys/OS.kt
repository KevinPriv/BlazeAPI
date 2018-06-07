package me.kbrewster.blazeapi.utils.sys

object OS {

    val platform: Platform
        get() {
            val osName = System.getProperty("os.name").toLowerCase()
            if (osName.contains("win")) {
                return Platform.WINDOWS
            }
            if (osName.contains("mac")) {
                return Platform.MACOS
            }
            if (osName.contains("solaris")) {
                return Platform.SOLARIS
            }
            if (osName.contains("sunos")) {
                return Platform.SOLARIS
            }
            if (osName.contains("linux")) {
                return Platform.LINUX
            }
            return if (osName.contains("unix")) {
                Platform.LINUX
            } else Platform.UNKNOWN
        }

    enum class Platform {
        LINUX, SOLARIS, WINDOWS, MACOS, UNKNOWN
    }
}
