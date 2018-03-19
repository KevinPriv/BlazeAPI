package me.kbrewster.blazeapi.api.gui

import me.kbrewster.blazeapi.api.gui.widget.WidgetButton

@FunctionalInterface
interface WidgetButtonCallback {

    fun onClick(widget: WidgetButton)

}