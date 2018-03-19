@file:JvmName("BlazeAPI")
package me.kbrewster.blazeapi

import me.kbrewster.blazeapi.api.event.EventBus
import me.kbrewster.blazeapi.internal.addons.IAddon
import org.apache.logging.log4j.LogManager

/**
 * Name of the API
 */
val ID = "Blaze"
    @JvmName("getID") get

/**
 * Version the API is currently in
 */
val VERSION = "1.0.0"
    @JvmName("getVersion") get

/**
 * Minecraft version the API is currently running
 */
val MC_VERSION = "1.12"
    @JvmName("getMCVersion") get

/**
 * Logger
 */
val LOGGER = LogManager.getLogger(ID)
    @JvmName("getLogger") get

/**
 * Creates a default instance for the eventbus where the
 * Minecraft events are registered and posted
 * {@link me.kbrewster.blazeapi.api.event.EventBus}
 */
val EVENTBUS = EventBus()
    @JvmName("getEventBus") get

/**
 * List of loaded addons
 * This will be populated at the end of {@link me.kbrewster.blazeapi.internal.addons.AddonBootstrap.Phase#INIT}
 */
val LOADED_ADDONS = ArrayList<IAddon>()
    @JvmName("getLoadedAddons") get


