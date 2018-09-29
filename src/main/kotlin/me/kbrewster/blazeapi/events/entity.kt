package me.kbrewster.blazeapi.events

import me.kbrewster.eventbus.AbstractEvent
import me.kbrewster.eventbus.CancellableEvent
import net.minecraft.util.BlockPos

/**
 * Fired player is about to respawn
 */
class RespawnPlayerEvent : CancellableEvent()


/**
 * Fired once the players spawnpoint as been changed
 */
class SpawnpointChangeEvent(val blockPos: BlockPos) : AbstractEvent()