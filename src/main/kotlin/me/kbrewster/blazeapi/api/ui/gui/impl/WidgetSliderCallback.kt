package me.kbrewster.blazeapi.api.ui.gui.impl

import me.kbrewster.blazeapi.api.ui.gui.widget.WidgetButton

@FunctionalInterface
interface WidgetSliderCallback {

    fun onChange(widget: WidgetButton, value: Float)

}