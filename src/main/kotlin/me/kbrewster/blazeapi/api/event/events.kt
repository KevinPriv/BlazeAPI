package me.kbrewster.blazeapi.api.event

import net.minecraft.util.text.ITextComponent
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
 * Invoked once before the client has started
 */
class PreInitializationEvent

/**
 * Invoked once the client has started
 */
class PostInitializationEvent


class ClientTickEvent


class RenderEvent(val partialTicks: Float)

class GuiScreenOpenEvent

class GuiScreenCloseEvent

/**
 * Fired when a chat packet is about to be sent to the server
 */
class ChatSentEvent(var message: String) : CancellableEvent()

/**
 * Fired when a chat message is about to be displayed on the client
 */
class ChatReceivedEvent(var chat: ITextComponent) : CancellableEvent()

class ServerJoinEvent(val ip: String, val port: Int)

class ServerDisconnectEvent(val reason: ITextComponent, val reasonLocalizationKey: String)
/**
 * Fired when a chat packet is about to be sent to the server
 */
class RespawnPlayerEvent : CancellableEvent()

/**
 * Fired once the client has shutdown
 */
class ShutdownEvent