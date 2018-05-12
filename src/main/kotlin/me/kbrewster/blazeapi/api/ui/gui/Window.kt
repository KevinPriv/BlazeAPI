package me.kbrewster.blazeapi.api.ui.gui

import net.minecraft.client.gui.Gui
import java.awt.Color

class Window(val title: String = "Empty", val scheme: ColourScheme = ColourScheme()) : Screen() {

    val windowX = 0
    val windowY = 0
    val windowWidth = this.width
    val windowHeight = this.height

    val isMovable = true

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.drawScreen(mouseX, mouseY, partialTicks)

        // draws moveable bar
        Gui.drawRect(this.windowX, this.windowY,
                this.windowWidth, this.windowY + 11,
                scheme.tertiary.rgb)
        this.drawString(fontRenderer, title, windowX, windowY, Color.BLACK.rgb)

        // draws body of the window
        Gui.drawRect(this.windowX, this.windowY + 11,
                this.windowWidth, this.windowHeight,
                scheme.tertiary.rgb)

    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        super.mouseClicked(mouseX, mouseY, mouseButton)
        // todo: make window movable
    }

}