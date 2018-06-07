package me.kbrewster.blazeapi.api.ui.gui

import net.minecraft.util.EnumChatFormatting
import java.awt.Color

data class ColourScheme(val primary: Color = Color.GRAY,
                        val secondary: Color = Color.WHITE,
                        val tertiary: Color = Color.DARK_GRAY)

data class MinecraftColourScheme(val primary: EnumChatFormatting = EnumChatFormatting.GRAY,
                                 val secondary: EnumChatFormatting = EnumChatFormatting.WHITE,
                                 val tertiary: EnumChatFormatting = EnumChatFormatting.DARK_GRAY)