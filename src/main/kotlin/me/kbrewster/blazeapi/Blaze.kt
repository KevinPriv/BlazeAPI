@file:JvmName("BlazeAPI")
package me.kbrewster.blazeapi

import me.kbrewster.blazeapi.internal.addons.Addon
import me.kbrewster.blazeapi.internal.launch.transformers.impl.ITransformer
import me.kbrewster.eventbus.DefaultEventBus
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
val MC_VERSION = "1.8.9"
    @JvmName("getMCVersion") get

/**
 * BlazeAPI Logger
 */
val LOGGER = LogManager.getLogger(ID)
    @JvmName("getLogger") get

/**
 * Creates a default instance for the eventbus where the
 * Minecraft events are registered and posted
 * {@link me.kbrewster.blazeapi.api.event.EventBus}
 */
val EVENTBUS = DefaultEventBus()
    @JvmName("getEventBus") get

/**
 * List of loaded addons
 * This will be populated at the end of {@link me.kbrewster.blazeapi.internal.addons.AddonBootstrap.Phase#INIT}
 */
val LOADED_ADDONS = ArrayList<Addon>()
    @JvmName("getLoadedAddons") get

/**
 * Transformers that will be ran at the end of the BlazeTweaker
 */
val TRANSFORMERS: Set<ITransformer> = LinkedHashSet()
    @JvmName("getTransformers") get