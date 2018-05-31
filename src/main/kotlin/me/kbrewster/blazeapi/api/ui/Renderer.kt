package me.kbrewster.blazeapi.api.ui

import me.kbrewster.blazeapi.client.fontRenderer
import me.kbrewster.blazeapi.client.mc
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.MathHelper

interface Renderer {

    companion object {

        /**
         * Gets a strings width when rendered.
         *
         * @param text the text to get the width of
         * @param removeFormatting if formatting should be removed
         * @return the width of the text
         */
        fun getStringWidth(text: String, removeFormatting: Boolean): Int {
            return fontRenderer.getStringWidth(text)
        }

        /**
         * Gets a strings width when rendered with formatting removed.
         *
         * @param text the text to get the width of
         * @return the width of the text
         */
        fun getStringWidth(text: String): Int {
            return getStringWidth(text, true)
        }

        /**
         * Gets a color int based on 0-255 rgba values.
         *
         * @param red   red value
         * @param green green value
         * @param blue  blue value
         * @param alpha alpha value
         * @return integer color
         */
        fun color(red: Int, green: Int, blue: Int, alpha: Int): Int {
            return (MathHelper.clamp_int(alpha, 0, 255) * 0x1000000
                    + MathHelper.clamp_int(red, 0, 255) * 0x10000
                    + MathHelper.clamp_int(green, 0, 255) * 0x100
                    + MathHelper.clamp_int(blue, 0, 255))
        }

        /**
         * Gets a color int based on 0-255 rgb values.
         *
         * @param red   red value
         * @param green green value
         * @param blue  blue value
         * @return integer color
         */
        fun color(red: Int, green: Int, blue: Int): Int {
            return color(red, green, blue, 255)
        }

        /**
         * Gets a determined rainbow color based on step and speed.
         *
         * @param step  time elapsed
         * @param speed speed of time
         * @return integer color
         */
        fun getRainbow(step: Float, speed: Float): Int {
            val red = ((Math.sin((step / speed).toDouble()) + 0.75) * 170).toInt()
            val green = ((Math.sin(step / speed + 2 * Math.PI / 3) + 0.75) * 170).toInt()
            val blue = ((Math.sin(step / speed + 4 * Math.PI / 3) + 0.75) * 170).toInt()
            return (-0x1000000
                    + MathHelper.clamp_int(red, 0, 255) * 0x10000
                    + MathHelper.clamp_int(green, 0, 255) * 0x100
                    + MathHelper.clamp_int(blue, 0, 255))
        }

        /**
         * Gets a determined rainbow color based on step with a default speed of 1.
         *
         * @param step time elapsed
         * @return integer color
         */
        fun getRainbow(step: Float): Int {
            return getRainbow(step, 1f)
        }

        /**
         * Gets a determined rainbow color array based on step and speed.
         *
         * @param step  time elapsed
         * @param speed speed of time
         * @return the array of colors {red,green,blue}
         */
        fun getRainbowColors(step: Float, speed: Float): IntArray {
            val red = ((Math.sin((step / speed).toDouble()) + 0.75) * 170).toInt()
            val green = ((Math.sin(step / speed + 2 * Math.PI / 3) + 0.75) * 170).toInt()
            val blue = ((Math.sin(step / speed + 4 * Math.PI / 3) + 0.75) * 170).toInt()
            return intArrayOf(red, green, blue)
        }

        /**
         * Offset by x and y.
         *
         * @param x the x offset
         * @param y the y offset
         */
        fun translate(x: Float, y: Float) {
            GlStateManager.translate(x, y, 0f)
        }

        /**
         * Scales a drawn overlay object using GlStateManager.
         *
         * @param scale the scale
         */
        fun scale(scale: Float) {
            scale(scale, scale)
        }

        /**
         * Scales a drawn overlay object disproportionally using GlStateManager.
         *
         * @param scaleX the x scale
         * @param scaleY the y scale
         */
        fun scale(scaleX: Float, scaleY: Float) {
            GlStateManager.scale(scaleX, scaleY, 1f)
        }

        /**
         * Rotate a drawn overlay object using GlStateManager.
         *
         * @param angle the angle
         */
        fun rotate(angle: Float) {
            GlStateManager.rotate(angle, 0f, 0f, 1f)
        }

        /**
         * Shifts the color of a drawn overlay object using GlStateManager.
         *
         * @param red the red value
         * @param green the green value
         * @param blue the blue value
         * @param alpha the alpha value
         */
        fun colorize(red: Int, green: Int, blue: Int, alpha: Int) {
            GlStateManager.color(
                    MathHelper.clamp_int(red, 0, 255).toFloat(),
                    MathHelper.clamp_int(green, 0, 255).toFloat(),
                    MathHelper.clamp_int(blue, 0, 255).toFloat(),
                    MathHelper.clamp_int(alpha, 0, 255).toFloat()
            )
        }

        /**
         * Shifts the color of a drawn overlay object using GlStateManager.<br></br>
         * This method defaults alpha to 255.
         *
         * @param red the red value
         * @param green the green value
         * @param blue the blue value
         */
        fun colorize(red: Int, green: Int, blue: Int) {
            colorize(red, green, blue, 255)
        }

        /**
         * Creates a new [Image] object. If the file with the specified name is found,
         * then it will be loaded, otherwise the file will be downloaded from the url and
         * then saved to a file with said name to be loaded from in the future.
         *
         *
         * **This instances a new object and should be treated as such.**<br></br>
         * This should not be used to instance and draw an image every frame, instead use
         * [Renderer.drawImage] or [Image.draw]
         *
         *
         * @param name the name of the file to save/load from
         * @param url the url of the file to download
         * @return a new [Image] object
         */
        fun image(name: String, url: String): Image? {
            return Image.load(name, url)
        }
    }

    object screen {
        val width: Int
            get() = ScaledResolution(mc).scaledWidth

        val height: Int
            get() = ScaledResolution(mc).scaledHeight

        val scale: Int
            get() = ScaledResolution(mc).scaleFactor
    }
}