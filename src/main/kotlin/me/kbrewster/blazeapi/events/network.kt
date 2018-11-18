package me.kbrewster.blazeapi.events

import me.kbrewster.eventbus.AbstractEvent
import me.kbrewster.eventbus.CancellableEvent
import net.minecraft.util.IChatComponent

/**
 * Fired once the player joins a server
 */
class ServerJoinEvent(val ip: String, val port: Int) : AbstractEvent()

/**
 * Fired once the player disconnects from the server
 */
class ServerDisconnectEvent : AbstractEvent()

/**
 * Fired when a chat packet is about to be sent to the server
 */
class ChatSentEvent(var message: String) : CancellableEvent()

/**
 * Fired when a chat message is about to be displayed on the client
 */
class ChatReceivedEvent(var chat: IChatComponent) : CancellableEvent()

