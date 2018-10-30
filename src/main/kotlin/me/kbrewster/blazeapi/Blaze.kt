@file:JvmName("BlazeAPI")
package me.kbrewster.blazeapi

import me.kbrewster.blazeapi.internal.addons.Addon
import me.kbrewster.eventbus.DefaultEventBus
import org.apache.logging.log4j.LogManager
import org.spongepowered.asm.service.ITransformer

/**
 * Minecraft version the API is currently running
 */
val MC_VERSION = "1.8.9"
    @JvmName("getMCVersion") get

/**
 * BlazeAPI Logger
 */
val LOGGER = LogManager.getLogger("BlazeAPI")
    @JvmName("getLogger") get

/**
 * Creates a default instance for the eventbus where the
 * Minecraft events are registered and posted
 * {@link me.kbrewster.blazeapi.api.event.EventBus}
 */
val EVENT_BUS = DefaultEventBus()
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