package me.kbrewster.blazeapi.api.ui

import me.kbrewster.blazeapi.EVENTBUS
import me.kbrewster.blazeapi.api.event.ClientTickEvent
import me.kbrewster.blazeapi.api.event.InvokeEvent
import me.kbrewster.blazeapi.api.event.Priority
import net.minecraft.client.renderer.texture.DynamicTexture
import sun.awt.image.ImageAccessException
import java.awt.image.BufferedImage
import java.io.File
import java.net.URL
import javax.imageio.ImageIO

class Image private constructor(private var imageToLoad: BufferedImage?) {

    /**
     * -- GETTER --
     * Gets the image's texture width.
     *
     * @return The image's texture width
     */
    var textureWidth: Int = 0

    /**
     * -- GETTER --
     * Gets the image's texture height.
     *
     * @return The image's texture height
     */
    var textureHeight: Int = 0

    var texture: DynamicTexture? = null

    init {
        EVENTBUS.register(this)
    }

    /**
     * Needs to load texture on the main thread
     * thanks opengl
     */
    @Suppress("UNUSED_PARAMETER")
    @InvokeEvent(Priority.HIGHEST)
    private fun load(event: ClientTickEvent) {
        if (imageToLoad != null) {
            texture = DynamicTexture(imageToLoad!!)
            this.textureWidth = imageToLoad!!.width
            this.textureHeight = imageToLoad!!.height
            imageToLoad = null
            EVENTBUS.unregister(this) // texture has been loaded
        }
    }

    /**
     * Draws the image on screen using the texture width and height.
     *
     * @param x the x position
     * @param y the y position
     * @return The Image object to allow for method chaining
     */
    fun draw(x: Double, y: Double): Image {
        Drawer.drawImage(this, x, y, this.textureWidth.toDouble(), this.textureHeight.toDouble())

        return this
    }

    companion object {

        /**
         * Downloads an image to store at the resource name location.
         *
         * @param url          The url to download the image from
         * @return The Image object to allow for method chaining
         */
        fun load(name: String, url: String): Image? {

            val resourceFile = File(name)

            try {
                if (resourceFile.exists()) {
                    return Image(ImageIO.read(resourceFile))
                }

                val image = ImageIO.read(URL(url))
                ImageIO.write(image, "png", resourceFile)
                return Image(image)
            } catch (e: Exception) {
                throw ImageAccessException("Could not load $name")
            }

        }
    }
}