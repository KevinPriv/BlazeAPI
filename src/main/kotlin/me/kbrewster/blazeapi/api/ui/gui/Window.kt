package me.kbrewster.blazeapi.api.ui.gui

import me.kbrewster.blazeapi.api.font.CustomFonts
import net.minecraft.client.gui.Gui
import java.awt.Color

abstract class Window @JvmOverloads constructor(val title: String = "Empty", val scheme: ColourScheme = ColourScheme()) : Screen() {

    val bar = Bar(this)

    var windowX = 0
    var windowY = 0
    var windowWidth = this.width
    var windowHeight = this.height

    var focused = true
    var movable = false

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.drawScreen(mouseX, mouseY, partialTicks)

        drawWindow(windowX, windowY, mouseX, mouseY, partialTicks)
    }

    private fun drawBody() {
        Gui.drawRect(this.windowX, this.windowY + 11,
                this.windowX + this.windowWidth, this.windowY + this.windowHeight,
                scheme.primary.rgb)
    }

    fun drawWindow(windowX: Int, windowY: Int, mouseX: Int, mouseY: Int, partialTicks: Float) {
        // draws body of the window
        this.drawBody()
        this.bar.draw(mouseX, mouseY, partialTicks)
    }


    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        super.mouseClicked(mouseX, mouseY, mouseButton)
        // todo: make window movable
    }

    fun centerWindow() {
        this.windowX = (this.width / 2) - (this.windowWidth / 2)
        this.windowY = (this.height / 2) - (this.windowHeight / 2)
    }

    fun setFullscreen(fullscreen: Boolean) {
        if (fullscreen) {
            this.windowX = 0
            this.windowY = 0
            this.windowWidth = this.width
            this.windowHeight = this.height
        } else {
            this.windowHeight = (this.height / 4) * 3
            this.windowWidth = (this.width / 4) * 3
            this.centerWindow()
        }
    }

    class Bar(val window: Window) {

        fun draw(mouseX: Int, mouseY: Int, partialTicks: Float) {
            with(window) {
                Gui.drawRect(this.windowX, this.windowY,
                        this.windowX + this.windowWidth, this.windowY + 11,
                        scheme.tertiary.rgb)
                CustomFonts.ARIAL.fontRenderer.drawString(title, windowX + 1, windowY + 1,
                        (if (this.focused) Color.BLACK else Color.LIGHT_GRAY).rgb)

            }
        }

        fun onMouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {

        }
    }

}