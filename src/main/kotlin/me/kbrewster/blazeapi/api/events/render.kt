package me.kbrewster.blazeapi.api.events

import me.kbrewster.eventbus.AbstractEvent
import net.minecraft.client.gui.GuiScreen

/**
 * Fired while GameOverlay is being rendered
 */
class RenderEvent(val partialTicks: Float) : AbstractEvent()

/**
 * Fired once a {@link net.minecraft.client.gui.GuiScreen} is opened
 */
class GuiScreenOpenEvent(val screen: GuiScreen) : AbstractEvent()

/**
 * Fired once a {@link net.minecraft.client.gui.GuiScreen} is closed
 */
class GuiScreenCloseEvent : AbstractEvent()