package me.kbrewster.blazeapi.api.ui.gui.widget

import me.kbrewster.blazeapi.api.ui.gui.impl.WidgetButtonCallback
import net.minecraft.client.Minecraft
import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraft.client.audio.SoundHandler
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.ResourceLocation
import net.minecraft.client.gui.GuiButton as MinecraftButton

open class WidgetButton
    @JvmOverloads constructor(
            val buttonId: Int,
            override var x: Int,
            override var y: Int,
            override var width: Int = 200,
            override var height: Int = 20,
            var displayString: String,
            override val callback: WidgetButtonCallback)
    : Widget() {

    /** Location of the image behind the button */
    protected val BUTTON_TEXTURES = ResourceLocation("textures/gui/widgets.png")
    /** True if this control is enabled, false to disable.  */
    var enabled: Boolean = true
    /** Hides the button completely if false.  */
    var visible: Boolean = true
    /** If the button is being hovered over */
    protected var hovered: Boolean = false

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    protected fun getHoverState(mouseOver: Boolean): Int {
        var i = 1

        if (!this.enabled) {
            i = 0
        } else if (mouseOver) {
            i = 2
        }

        return i
    }


    /**
     * Draws this button to the screen.
     */
    fun drawButton(mc: Minecraft, mouseX: Int, mouseY: Int) {
        if (this.visible) {
            val fontrenderer = mc.fontRendererObj
            mc.textureManager.bindTexture(BUTTON_TEXTURES)
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height
            val i = this.getHoverState(this.hovered)
            GlStateManager.enableBlend()
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
            GlStateManager.blendFunc(770, 771)
            this.drawTexturedModalRect(this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height)
            this.drawTexturedModalRect(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height)
            this.mouseDragged(mc, mouseX, mouseY)
            var j = 14737632

            if (!this.enabled) {
                j = 10526880
            } else if (this.hovered) {
                j = 16777120
            }

            this.drawCenteredString(fontrenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, j)
        }
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    protected fun mouseDragged(mc: Minecraft, mouseX: Int, mouseY: Int) {}

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    fun mouseReleased(mouseX: Int, mouseY: Int) {}

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    fun mousePressed(mc: Minecraft, mouseX: Int, mouseY: Int): Boolean {
        return this.enabled && this.visible && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height
    }

    /**
     * Whether the mouse cursor is currently over the button.
     */
    fun isMouseOver(): Boolean {
        return this.hovered
    }

    fun drawButtonForegroundLayer(mouseX: Int, mouseY: Int) {}

    fun playPressSound(soundHandlerIn: SoundHandler) {
        soundHandlerIn.playSound(PositionedSoundRecord.create(ResourceLocation("gui.button.press"), 1.0f))
    }

}