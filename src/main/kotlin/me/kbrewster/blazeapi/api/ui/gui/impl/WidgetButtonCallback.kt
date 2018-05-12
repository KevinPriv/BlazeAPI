package me.kbrewster.blazeapi.api.ui.gui.impl

import me.kbrewster.blazeapi.api.ui.gui.widget.WidgetButton

@FunctionalInterface
interface WidgetButtonCallback {

    fun onClick(widget: WidgetButton)

}