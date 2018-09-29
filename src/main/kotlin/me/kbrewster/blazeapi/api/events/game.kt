package me.kbrewster.blazeapi.api.events

import me.kbrewster.eventbus.AbstractEvent

/**
 * Fired once before the client has started
 */
class PreInitializationEvent : AbstractEvent()

/**
 * Fired once the client has started
 */
class InitializationEvent : AbstractEvent()

/**
 * Fired every tick (50 milliseconds)
 */
class ClientTickEvent : AbstractEvent()

/**
 * Fired once the client has shutdown
 */
class ShutdownEvent : AbstractEvent()

/**
 * Input Events e.g Keybinds
 */
class InputEvents {

    /**
     * Fired once the player left clicks
     */
    class LeftClick : AbstractEvent()

    /**
     * Fired once the player right clicks
     */
    class RightClick : AbstractEvent()

    /**
     * Fired once a key is pressed
     */
    class Keypress(val key: Int, val isRepeated: Boolean, val isPressed: Boolean)

}