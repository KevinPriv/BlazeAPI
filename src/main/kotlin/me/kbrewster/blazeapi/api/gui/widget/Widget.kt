package me.kbrewster.blazeapi.api.gui.widget

import me.kbrewster.blazeapi.api.gui.WidgetButtonCallback
import net.minecraft.client.gui.Gui

abstract class Widget: Gui() {

    abstract var x: Int
    abstract var y: Int

    abstract var width: Int
    abstract var height: Int

    abstract val callback: WidgetButtonCallback

    fun drawRelativeRect(x: Int, y: Int, width: Int, length: Int, colour: Int) {
        drawRect(x, y, x + width, y + length, colour)
    }

}