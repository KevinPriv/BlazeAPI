package me.kbrewster.blazeapi.api.event

open class CancellableEvent {

    var cancelled = false
        @JvmName("isCancelled") get

}