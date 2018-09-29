package me.kbrewster.blazeapi.api.ui.gui

import me.kbrewster.blazeapi.api.font.CustomFonts
import me.kbrewster.blazeapi.api.ui.Image
import net.minecraft.client.gui.Gui
import java.awt.Color

abstract class Window @JvmOverloads constructor(val title: String = "Empty", val icon: Image? = null, val scheme: ColourScheme = ColourScheme()) : Screen() {

    val bar = Bar(this)

    var windowX = 0
    var windowY = 0
    var windowWidth = this.width
    var windowHeight = this.height

    var focused = false

    init {
        icon?.let {
            val size = 2 shl 4 // 32 x 32 icon
            if (it.textureWidth and it.textureHeight != size) {
                throw Exception("Texture Width and Height are not equal to $size!")
            }
        }
    }

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
        this.bar.onMouseClicked(mouseX, mouseY, mouseButton)
        // todo: make window isMovable
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        super.mouseReleased(mouseX, mouseY, state)
        this.bar.mouseReleased(mouseX, mouseY, state)
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

        var isMovable = true

        var isMoving = false

        fun draw(mouseX: Int, mouseY: Int, partialTicks: Float) {
            with(window) {
                Gui.drawRect(this.windowX, this.windowY,
                        this.windowX + this.windowWidth, this.windowY + 11,
                        scheme.tertiary.rgb)
                CustomFonts.ARIAL.fontRenderer.drawString(title, windowX + 1, windowY + 1,
                        (if (this.focused) Color.BLACK else Color.LIGHT_GRAY).rgb)
                if (isMoving) {
                    this.windowX = mouseX
                    this.windowY = mouseY
                }
            }
        }

        fun onMouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
            if (mouseButton == 0) {
                with(window) {
                    isMoving = mouseX >= windowX && mouseX <= windowX + windowWidth && isMovable &&
                            mouseY >= windowY && mouseY <= windowY + 11
                    focused = true
                }
            }
        }

        fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
            isMoving = false
        }


    }

}