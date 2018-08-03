package me.kbrewster.blazeapi.api.ui

import net.minecraft.util.MathHelper

enum class Colours(val red: Int, val green: Int, val blue: Int, val alpha: Int = 255) {

    BLACK(0, 0, 0, 255),
    DARK_BLUE(0, 0, 190, 255),
    DARK_GREEN(0, 190, 0, 255),
    DARK_AQUA(0, 190, 190, 255),
    DARK_RED(190, 0, 0, 255),
    DARK_PURPLE(190, 0, 190, 255),
    GOLD(217, 163, 52, 255),
    GRAY(190, 190, 190, 255),
    DARK_GRAY(63, 63, 63, 255),
    BLUE(63, 63, 254, 255),
    GREEN(63, 254, 63, 255),
    AQUA(63, 254, 254, 255),
    RED(254, 63, 63, 255),
    LIGHT_PURPLE(254, 63, 254, 255),
    YELLOW(254, 254, 63, 255),
    WHITE(255, 255, 255, 255);


    /**
     * Gets a color int based on 0-255 rgba values.
     *
     * @param red   red value
     * @param green green value
     * @param blue  blue value
     * @param alpha alpha value
     * @return integer color
     */
    fun getRGB(): Int {
        return (MathHelper.clamp_int(alpha, 0, 255) * 0x1000000
                + MathHelper.clamp_int(red, 0, 255) * 0x10000
                + MathHelper.clamp_int(green, 0, 255) * 0x100
                + MathHelper.clamp_int(blue, 0, 255))
    }

    companion object {

        /**
         * Gets a color int based on 0-255 rgba values.
         *
         * @param red   red value
         * @param green green value
         * @param blue  blue value
         * @param alpha alpha value
         * @return integer color
         */
        fun getColour(red: Int, green: Int, blue: Int, alpha: Int): Int {
            return (MathHelper.clamp_int(alpha, 0, 255) * 0x1000000
                    + MathHelper.clamp_int(red, 0, 255) * 0x10000
                    + MathHelper.clamp_int(green, 0, 255) * 0x100
                    + MathHelper.clamp_int(blue, 0, 255))
        }

        /**
         * Gets a color based off of a hex integer input
         *
         * @param color the hex integer
         * @return the color
         */
        fun getColour(color: Int): Colours = when (color) {
            0 -> BLACK
            1 -> DARK_BLUE
            2 -> DARK_GREEN
            3 -> DARK_AQUA
            4 -> DARK_RED
            5 -> DARK_PURPLE
            6 -> GOLD
            7 -> GRAY
            8 -> DARK_GRAY
            9 -> BLUE
            10 -> GREEN
            11 -> AQUA
            12 -> RED
            13 -> LIGHT_PURPLE
            14 -> YELLOW
            else -> WHITE
        }
    }

}