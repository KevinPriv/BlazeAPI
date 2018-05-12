package me.kbrewster.blazeapi.api.font

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.texture.TextureUtil
import org.lwjgl.opengl.GL11.*
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage

class CustomFontRenderer {

    val font: Font

    private var texID: Int = 0
    private val chars = arrayOfNulls<IntObject>(2048)

    private var antiAlias: Boolean = false
    private var fontHeight = -1
    private var charOffset = 8

    var IMAGE_WIDTH = 1024
    var IMAGE_HEIGHT = 1024

    val height: Int
        get() = (fontHeight - charOffset) / 2

    var isAntiAlias: Boolean
        get() = antiAlias
        set(antiAlias) {
            if (this.antiAlias != antiAlias) {
                this.antiAlias = antiAlias
                setupTexture(antiAlias)
            }
        }

    constructor(font: Font, antiAlias: Boolean, charOffset: Int) {
        this.font = font
        this.antiAlias = antiAlias
        this.charOffset = charOffset
        setupTexture(antiAlias)
    }

    @Suppress("UNUSED")
    constructor(font: Font, antiAlias: Boolean) {
        this.font = font
        this.antiAlias = antiAlias
        charOffset = 8
        setupTexture(antiAlias)
    }

    private fun setupTexture(antiAlias: Boolean) {
        if (font.size <= 15) {
            IMAGE_WIDTH = 256
            IMAGE_HEIGHT = 256
        }
        when {
            font.size <= 43 -> {
                IMAGE_WIDTH = 512
                IMAGE_HEIGHT = 512
            }
            font.size <= 91 -> {
                IMAGE_WIDTH = 1024
                IMAGE_HEIGHT = 1024
            }
            else -> {
                IMAGE_WIDTH = 2048
                IMAGE_HEIGHT = 2048
            }
        }

        val img = BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB)
        val g = img.graphics as Graphics2D
        g.font = font

        g.color = Color(255, 255, 255, 0)
        g.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT)
        g.color = Color.white

        var rowHeight = 0
        var positionX = 0
        var positionY = 0
        for (i in 0..2047) {
            val ch = i.toChar()
            val fontImage = getFontImage(ch, antiAlias)

            val newIntObject = IntObject()

            newIntObject.width = fontImage.width
            newIntObject.height = fontImage.height

            if (positionX + newIntObject.width >= IMAGE_WIDTH) {
                positionX = 0
                positionY += rowHeight
                rowHeight = 0
            }

            newIntObject.storedX = positionX
            newIntObject.storedY = positionY

            if (newIntObject.height > fontHeight) {
                fontHeight = newIntObject.height
            }

            if (newIntObject.height > rowHeight) {
                rowHeight = newIntObject.height
            }
            chars[i] = newIntObject
            g.drawImage(fontImage, positionX, positionY, null)

            positionX += newIntObject.width
        }

        try {
            texID = TextureUtil.uploadTextureImageAllocate(TextureUtil.glGenTextures(), img, true, true)
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

    }

    private fun getFontImage(ch: Char, antiAlias: Boolean): BufferedImage {
        val tempfontImage = BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)
        val g = tempfontImage.graphics as Graphics2D
        if (antiAlias) {
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
        } else {
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF)
        }
        g.font = font
        val fontMetrics = g.fontMetrics
        var charwidth = fontMetrics.charWidth(ch) + 8

        if (charwidth <= 0) {
            charwidth = 7
        }
        var charheight = fontMetrics.height + 3
        if (charheight <= 0) {
            charheight = font.getSize()
        }
        val fontImage = BufferedImage(charwidth, charheight, BufferedImage.TYPE_INT_ARGB)
        val gt = fontImage.graphics as Graphics2D
        if (antiAlias) {
            gt.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
        } else {
            gt.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF)
        }
        gt.font = font
        gt.color = Color.WHITE
        val charX = 3
        val charY = 1
        gt.drawString(ch.toString(), charX, charY + fontMetrics.ascent)

        return fontImage

    }

    @Throws(ArrayIndexOutOfBoundsException::class)
    fun drawChar(c: Char, x: Float, y: Float) {
        try {
            drawQuad(x, y,
                    chars[c.toInt()]!!.width.toFloat(),
                    chars[c.toInt()]!!.height.toFloat(),
                    chars[c.toInt()]!!.storedX.toFloat(),
                    chars[c.toInt()]!!.storedY.toFloat(),
                    chars[c.toInt()]!!.width.toFloat(),
                    chars[c.toInt()]!!.height.toFloat())
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun drawQuad(x: Float, y: Float, width: Float, height: Float, srcX: Float,
                         srcY: Float, srcWidth: Float, srcHeight: Float) {
        val renderSRCX = srcX / IMAGE_WIDTH
        val renderSRCY = srcY / IMAGE_HEIGHT
        val renderSRCWidth = srcWidth / IMAGE_WIDTH
        val renderSRCHeight = srcHeight / IMAGE_HEIGHT
        glBegin(GL_TRIANGLES)
        glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY)
        glVertex2d((x + width).toDouble(), y.toDouble())
        glTexCoord2f(renderSRCX, renderSRCY)
        glVertex2d(x.toDouble(), y.toDouble())
        glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight)
        glVertex2d(x.toDouble(), (y + height).toDouble())
        glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight)
        glVertex2d(x.toDouble(), (y + height).toDouble())
        glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY + renderSRCHeight)
        glVertex2d((x + width).toDouble(), (y + height).toDouble())
        glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY)
        glVertex2d((x + width).toDouble(), y.toDouble())
        glEnd()
    }

    fun drawString(text: String, x: Double, y: Double, color: Color, shadow: Boolean) {
        var xCoord = x
        var yCoord = y
        xCoord *= 2.0
        yCoord = yCoord * 2 - 2
        glPushMatrix()
        // glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_NICEST);
        glScaled(0.25, 0.25, 0.25)
        // glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.bindTexture(texID)
        glColor(if (shadow) Color(0.05f, 0.05f, 0.05f, color.getAlpha() / 255f) else color)
        val size = text.length
        for (indexInString in 0 until size) {
            val character = text[indexInString]
            if (character.toInt() < chars.size && character.toInt() >= 0) {
                drawChar(character, xCoord.toFloat(), yCoord.toFloat())
                xCoord += (chars[character.toInt()]!!.width - charOffset).toDouble()
            }
        }
        glPopMatrix()
    }

    private fun glColor(color: Color) {
        val red = color.red / 255f
        val green = color.green / 255f
        val blue = color.blue / 255f
        val alpha = color.alpha / 255f
        glColor4f(red, green, blue, alpha)
    }

    fun getStringHeight(text: String): Int {
        var lines = 1
        for (c in text.toCharArray()) {
            if (c == '\n') {
                lines++
            }
        }
        return (fontHeight - charOffset) / 2 * lines
    }

    fun getStringWidth(text: String): Int {
        var width = 0
        for (c in text.toCharArray()) {
            if (c.toInt() < chars.size && c.toInt() >= 0) {
                width += chars[c.toInt()]!!.width - charOffset
            }
        }
        return width / 2
    }

    private inner class IntObject {

        var width: Int = 0
        var height: Int = 0
        var storedX: Int = 0
        var storedY: Int = 0
    }
}