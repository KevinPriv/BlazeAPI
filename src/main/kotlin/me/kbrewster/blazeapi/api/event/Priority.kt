package me.kbrewster.blazeapi.api.event

enum class Priority(val value: Int) {
    HIGHEST(2), // Called first
    HIGH(1),
    NORMAL(0),
    LOW(-1),
    LOWEST(-2) // Called last
}