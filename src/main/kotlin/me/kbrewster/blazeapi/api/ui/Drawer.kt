package me.kbrewster.blazeapi.api.ui

import me.kbrewster.blazeapi.client.fontRenderer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats

interface Drawer {


    companion object {
        /**
         * Draws a solid color rectangle with the specified coordinates and color.
         */
        fun drawRect(left: Int, top: Int, right: Int, bottom: Int, color: Int) {
            var left = left
            var top = top
            var right = right
            var bottom = bottom
            if (left < right) {
                val i = left
                left = right
                right = i
            }

            if (top < bottom) {
                val j = top
                top = bottom
                bottom = j
            }

            val f3 = (color shr 24 and 255).toFloat() / 255.0f
            val f = (color shr 16 and 255).toFloat() / 255.0f
            val f1 = (color shr 8 and 255).toFloat() / 255.0f
            val f2 = (color and 255).toFloat() / 255.0f
            val tessellator = Tessellator.getInstance()
            val bufferbuilder = tessellator.buffer
            GlStateManager.enableBlend()
            GlStateManager.disableTexture2D()
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO)
            GlStateManager.color(f, f1, f2, f3)
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION)
            bufferbuilder.pos(left.toDouble(), bottom.toDouble(), 0.0).endVertex()
            bufferbuilder.pos(right.toDouble(), bottom.toDouble(), 0.0).endVertex()
            bufferbuilder.pos(right.toDouble(), top.toDouble(), 0.0).endVertex()
            bufferbuilder.pos(left.toDouble(), top.toDouble(), 0.0).endVertex()
            tessellator.draw()
            GlStateManager.enableTexture2D()
            GlStateManager.disableBlend()
        }

        /**
         * Simple method to draw a string to the Client's overlay.<br></br>
         * For more text options, use [Text]
         *
         * @param text the string
         * @param x the x position
         * @param y the y position
         */
        fun drawString(text: String, x: Float, y: Float) {
            fontRenderer.drawString(text, x.toInt(), y.toInt(), 0xffffff)
        }

        /**
         * Simple method to draw a string with a drop shadow to the Client's overlay.<br></br>
         * For more text options, use [Text]
         *
         * @param text the string
         * @param x the x position
         * @param y the y position
         */
        fun drawStringWithShadow(text: String, x: Float, y: Float) {
            fontRenderer.drawString(text, x.toInt(), y.toInt(), 0xffffff)
        }


        fun drawImage(image: Image, x: Int, y: Int, size: Int) {
            if (image.texture == null) {
                return
            }

            val scale = size.toFloat() / 256.toFloat()
            val f = 256 * 0.00390625f
            val modX = x / scale
            val modY = y / scale

            GlStateManager.color(1f, 1f, 1f, 1f)
            GlStateManager.enableBlend()
            GlStateManager.scale(scale, scale, 50f)
            GlStateManager.bindTexture(image.texture!!.glTextureId)
            GlStateManager.enableTexture2D()

            val tessellator = Tessellator.getInstance()
            val bufferbuilder = tessellator.buffer
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION)
            bufferbuilder.pos(modX.toDouble(), modY.toDouble() + 256, 0.0).endVertex()
            bufferbuilder.pos(modX.toDouble() + 256, modY.toDouble() + 256, 0.0).endVertex()
            bufferbuilder.pos(modX.toDouble() + 256, modY.toDouble(), 0.0).endVertex()
            bufferbuilder.pos(modX.toDouble(), modY.toDouble(), 0.0).endVertex()
            tessellator.draw()

            GlStateManager.popMatrix()
            GlStateManager.pushMatrix()
        }
    }

}