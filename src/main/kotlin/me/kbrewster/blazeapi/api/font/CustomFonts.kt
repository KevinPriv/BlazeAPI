package me.kbrewster.blazeapi.api.font

import java.awt.Font

enum class CustomFonts(val fontRenderer: MinecraftFontRenderer) {
    ARIAL(MinecraftFontRenderer(Font("Arial", Font.PLAIN, 30), true, 8)),
    TIMES_NEW_ROMAN(MinecraftFontRenderer(Font("Times New Roman", Font.PLAIN, 30), true, 8)),
    IMPACT(MinecraftFontRenderer(Font("Impact", Font.PLAIN, 30), true, 8)),
    COMIC_SANS_MS(MinecraftFontRenderer(Font("Comic Sans MS", Font.PLAIN, 30), true, 8));
}