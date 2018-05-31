package me.kbrewster.blazeapi.api.ui

import me.kbrewster.blazeapi.client.fontRenderer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats


interface Drawer {


    companion object {

        /**
         * Simple method to draw a rectangle on the Client's overlay.<br></br>
         *
         * @param color the color
         * @param x the x position
         * @param y the y position
         * @param width the width
         * @param height the height
         */
        fun drawRect(color: Int, x: Float, y: Float, width: Float, height: Float) {
            var x = x
            var y = y
            var x2 = x + width
            var y2 = y + height

            if (x > x2) {
                val k = x
                x = x2
                x2 = k
            }
            if (y > y2) {
                val k = y
                y = y2
                y2 = k
            }

            val a = (color shr 24 and 255).toFloat() / 255.0f
            val r = (color shr 16 and 255).toFloat() / 255.0f
            val g = (color shr 8 and 255).toFloat() / 255.0f
            val b = (color and 255).toFloat() / 255.0f

            GlStateManager.enableBlend()
            GlStateManager.disableTexture2D()

            val tessellator = Tessellator.getInstance()
            val worldrenderer = tessellator.worldRenderer
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
            GlStateManager.color(r, g, b, a)
            worldrenderer.begin(7, DefaultVertexFormats.POSITION)
            worldrenderer.pos(x.toDouble(), y2.toDouble(), 0.0).endVertex()
            worldrenderer.pos(x2.toDouble(), y2.toDouble(), 0.0).endVertex()
            worldrenderer.pos(x2.toDouble(), y.toDouble(), 0.0).endVertex()
            worldrenderer.pos(x.toDouble(), y.toDouble(), 0.0).endVertex()
            tessellator.draw()
            GlStateManager.color(1f, 1f, 1f, 1f)

            GlStateManager.enableTexture2D()
            GlStateManager.disableBlend()

            GlStateManager.popMatrix()
            GlStateManager.pushMatrix()
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


        fun drawImage(image: Image, x: Double, y: Double, width: Double, height: Double) {
            if (image.texture == null) return

            GlStateManager.color(1f, 1f, 1f, 1f)
            GlStateManager.enableBlend()
            GlStateManager.scale(1f, 1f, 50f)
            GlStateManager.bindTexture(image.texture!!.glTextureId)
            GlStateManager.enableTexture2D()

            val tessellator = Tessellator.getInstance()
            val worldrenderer = tessellator.worldRenderer
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX)

            worldrenderer.pos(x, y + height, 0.0)
                    .tex(0.0, 1.0).endVertex()
            worldrenderer.pos(x + width, y + height, 0.0)
                    .tex(1.0, 1.0).endVertex()
            worldrenderer.pos(x + width, y, 0.0)
                    .tex(1.0, 0.0).endVertex()
            worldrenderer.pos(x, y, 0.0)
                    .tex(0.0, 0.0).endVertex()
            tessellator.draw()

            GlStateManager.popMatrix()
            GlStateManager.pushMatrix()
        }
    }
}