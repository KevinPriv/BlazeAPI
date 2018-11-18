package me.kbrewster.blazeapi.events

import me.kbrewster.eventbus.AbstractEvent
import net.minecraft.client.multiplayer.WorldClient

/**
 * Fired once the world has finished loading
 */
class WorldLoadEvent(val worldClient: WorldClient, val loadingMessage: String) : AbstractEvent()

class WorldUnloadEvent : AbstractEvent()