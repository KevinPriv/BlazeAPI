package me.kbrewster.blazeapi.api.event

import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.multiplayer.WorldClient
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.ITextComponent
import java.lang.reflect.Method


/**
 * Used to store information about a subscriber
 * This are linked to an event vai a HashMap {@see me.kbrewster.blazeapi.api.event.EventBus#subscribers}
 *
 * @param instance the instance of the event class
 * @param method the method of the registered instance
 * @param priority when the event should be executed over the rest (lowest = last to be executed)
 */
data class EventSubscriber(val instance: Any, val method: Method, val priority: Priority)

/**
 * Assign to a method to invoke an event
 * The first parameter of the method should be the event it is calling
 */
annotation class InvokeEvent(val value: Priority = Priority.NORMAL)

/**
 * Fired once before the client has started
 */
class PreInitializationEvent

/**
 * Fired once the client has started
 */
class InitializationEvent

/**
 * Fired every tick (50 milliseconds)
 */
class ClientTickEvent

/**
 * Fired while GameOverlay is being rendered
 */
class RenderEvent(val partialTicks: Float)

/**
 * Fired once a {@link net.minecraft.client.gui.GuiScreen} is opened
 */
class GuiScreenOpenEvent(val screen: GuiScreen)

/**
 * Fired once a {@link net.minecraft.client.gui.GuiScreen} is closed
 */
class GuiScreenCloseEvent

/**
 * Fired when a chat packet is about to be sent to the server
 */
class ChatSentEvent(var message: String) : CancellableEvent()

/**
 * Fired when a chat message is about to be displayed on the client
 */
class ChatReceivedEvent(var chat: ITextComponent) : CancellableEvent()

/**
 * Fired once the player joins a server
 */
class ServerJoinEvent(val ip: String, val port: Int)

/**
 * Fired once the player disconnects from the server
 */
class ServerDisconnectEvent(val reason: ITextComponent, val reasonLocalizationKey: String)

/**
 * Fired once the players spawnpoint as been changed
 */
class SpawnpointChangeEvent(val blockPos: BlockPos)

/**
 * Fired when a chat packet is about to be sent to the server
 */
class RespawnPlayerEvent : CancellableEvent()

/**
 * Fired once the world has been loaded
 */
class WorldLoadEvent(val worldClient: WorldClient, val loadingMessage: String)

/**
 * Fired once the client has shutdown
 */
class ShutdownEvent