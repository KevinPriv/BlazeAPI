package me.kbrewster.blazeapi.api.event

import java.lang.reflect.Method


/**
 * Used to store information about events and index so they can be easily accessed by ASM
 */
data class EventSubscriber(val instance: Any, val method: Method, val priority: Priority)

/**
 * Assign to a method to invoke an event
 * The first parameter of the method should be the event it is calling
 */
annotation class InvokeEvent(val priority: Priority = Priority.NORMAL)

/**
 * Invoked once the client has started
 */
class PreInitializationEvent

/**
 * Invoked once the client has started
 */
class PostInitializationEvent

/**
 * Invoked once the client has shutdown
 */
class ShutdownEvent