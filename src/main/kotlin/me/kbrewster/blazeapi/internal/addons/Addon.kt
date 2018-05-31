package me.kbrewster.blazeapi.internal.addons

/**
 * Interface of which the main class
 * of an Addon must implement
 *
 * @since 1.0
 * @author Kevin Brewster
 */
interface Addon {

    /**
     * Invoked once the plugin has successfully loaded or enabled
     * {@see me.kbrewster.blazeapi.internal.addons.AddonMinecraftBootstrap#init}
     */
    fun onEnable()

    /**
     * Invoked once the game has been closed or disabled
     * this is executed at the start of {@link net.minecraft.client.Minecraft#shutdown}
     */
    fun onDisable()

}
