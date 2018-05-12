package me.kbrewster.blazeapi.api.font

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.resources.IResourceManager
import net.minecraft.util.ChatAllowedCharacters
import net.minecraft.util.ResourceLocation
import net.minecraft.util.StringUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.*
import java.awt.Color
import java.awt.Font
import java.util.*

@Suppress("UNUSED")
class MinecraftFontRenderer(font: Font, antiAlias: Boolean, charOffset: Int) : FontRenderer(Minecraft.getMinecraft().gameSettings, ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().textureManager, false) {

    private val customColorCodes = arrayOfNulls<Color>(256)
    private val colorCode = IntArray(32)
    var font: CustomFontRenderer? = null
        private set
    private var boldFont: CustomFontRenderer? = null
    private var italicFont: CustomFontRenderer? = null
    private var boldItalicFont: CustomFontRenderer? = null
    private var colorcodeIdentifiers = "0123456789abcdefklmnor"
    private var bidi: Boolean = false

    val height: Int
        get() = font!!.height / 2


    val fontName: String
        get() = font!!.font.fontName


    val size: Int
        get() = font!!.font.size


    var isAntiAliasing: Boolean
        get() = font!!.isAntiAlias && boldFont!!.isAntiAlias && italicFont!!.isAntiAlias && boldItalicFont!!.isAntiAlias
        set(antiAlias) {
            font!!.isAntiAlias = antiAlias
            boldFont!!.isAntiAlias = antiAlias
            italicFont!!.isAntiAlias = antiAlias
            boldItalicFont!!.isAntiAlias = antiAlias
        }


    init {
        setFont(font, antiAlias, charOffset)
        customColorCodes['q'.toInt()] = Color(0, 90, 163)
        colorcodeIdentifiers = setupColorcodeIdentifier()
        setupMinecraftColorcodes()
        FONT_HEIGHT = height
    }

    fun drawString(s: String, x: Float, y: Float, color: Int): Int {
        return drawString(s, x, y, color, false)
    }

    override fun drawStringWithShadow(s: String, x: Float, y: Float, color: Int): Int {
        drawString(StringUtils.stripControlCodes(s), x + 0.8f, y + 0.8f, 0x000000, false)
        return drawString(s, x, y, color, false)
    }

    fun drawCenteredString(s: String, x: Int, y: Int, color: Int, shadow: Boolean) {
        if (shadow) {
            drawStringWithShadow(s, (x - getStringWidth(s) / 2).toFloat(), y.toFloat(), color)
        } else {
            drawString(s, x - getStringWidth(s) / 2, y, color)
        }
    }

    fun drawCenteredStringXY(s: String, x: Int, y: Int, color: Int, shadow: Boolean) {
        drawCenteredString(s, x, y - height / 2, color, shadow)
    }

    fun drawCenteredString(s: String, x: Int, y: Int, color: Int) {
        drawStringWithShadow(s, (x - getStringWidth(s) / 2).toFloat(), y.toFloat(), color)
    }

    override fun drawString(text: String?, x: Float, y: Float, color: Int, shadow: Boolean): Int {
        var result = 0

        val lines = text!!.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (i in lines.indices) {
            result = drawLine(lines[i], x, y + i * height, color, shadow)
        }

        return result
    }

    private fun drawLine(text: String?, x: Float, y: Float, color: Int, shadow: Boolean): Int {
        var color = color
        if (text == null) {
            return 0
        }
        glPushMatrix()
        glTranslated(x - 1.5, y + 0.5, 0.0)
        val wasBlend = glGetBoolean(GL_BLEND)
        GlStateManager.enableAlpha()
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        glEnable(GL_TEXTURE_2D)
        if (color and -67108864 == 0) {
            color = color or -16777216
        }

        if (shadow) {
            color = color and 16579836 shr 2 or (color and -16777216)
        }

        val red = (color shr 16 and 0xff) / 255f
        val green = (color shr 8 and 0xff) / 255f
        val blue = (color and 0xff) / 255f
        val alpha = (color shr 24 and 0xff) / 255f
        val c = Color(red, green, blue, alpha)
        if (text.contains("\u00a7")) {
            val parts = text.split("\u00a7".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            var currentColor = c
            var currentFont = font
            var width = 0
            var randomCase = false
            var bold = false
            var italic = false
            var strikethrough = false
            var underline = false

            for (index in parts.indices) {
                if (parts[index].length <= 0) {
                    continue
                }
                if (index == 0) {

                    currentFont!!.drawString(parts[index], width.toDouble(), 0.toDouble(), currentColor, shadow)
                    width += currentFont.getStringWidth(parts[index])

                } else {
                    val words = parts[index].substring(1)
                    val type = parts[index][0]
                    val colorIndex = colorcodeIdentifiers.indexOf(type)
                    if (colorIndex != -1) {
                        when {
                            colorIndex < 16 -> { // coloring
                                val colorcode = colorCode[colorIndex]
                                currentColor = getColor(colorcode, alpha)
                                bold = false
                                italic = false
                                randomCase = false
                                underline = false
                                strikethrough = false
                            }
                            colorIndex == 16 -> randomCase = true
                            colorIndex == 17 -> bold = true
                            colorIndex == 18 -> strikethrough = true
                            colorIndex == 19 -> underline = true
                            colorIndex == 20 -> italic = true
                            colorIndex == 21 -> { // reset
                                bold = false
                                italic = false
                                randomCase = false
                                underline = false
                                strikethrough = false
                                currentColor = c
                            }
                            colorIndex > 21 -> { // custom mang
                                val customColor = customColorCodes[type.toInt()]!!
                                currentColor = Color(customColor.red / 255f, customColor.green / 255f,
                                        customColor.blue / 255f, alpha)
                            }
                        }
                    }

                    if (bold && italic) {
                        boldItalicFont!!.drawString(if (randomCase) toRandom(currentFont, words) else words, width.toDouble(), 0.toDouble(),
                                currentColor, shadow)
                        currentFont = boldItalicFont
                    } else if (bold) {
                        boldFont!!.drawString(if (randomCase) toRandom(currentFont, words) else words, width.toDouble(), 0.toDouble(), currentColor,
                                shadow)
                        currentFont = boldFont
                    } else if (italic) {
                        italicFont!!.drawString(if (randomCase) toRandom(currentFont, words) else words, width.toDouble(), 0.toDouble(), currentColor,
                                shadow)
                        currentFont = italicFont
                    } else {
                        font!!.drawString(if (randomCase) toRandom(currentFont, words) else words, width.toDouble(), 0.toDouble(), currentColor,
                                shadow)
                        currentFont = font
                    }
                    val u = font!!.height / 16f
                    val h = currentFont!!.getStringHeight(words)
                    if (strikethrough) {
                        drawLine(width / 2.0 + 1, h.toDouble() / 3, (width + currentFont.getStringWidth(words)) / 2.0 + 1, h.toDouble() / 3, u)
                    }
                    if (underline) {
                        drawLine(width / 2.0 + 1, h.toDouble() / 2, (width + currentFont.getStringWidth(words)).toDouble() / 2.0 + 1, h.toDouble() / 2, u)
                    }
                    width += currentFont.getStringWidth(words)
                }
            }
        } else {
            font!!.drawString(text, 0.toDouble(), 0.toDouble(), c, shadow)
        }
        if (!wasBlend) {
            glDisable(GL_BLEND)
        }
        glPopMatrix()
        GL11.glColor4f(1f, 1f, 1f, 1f)

        return (x + getStringWidth(text)).toInt()
    }


    private fun toRandom(font: CustomFontRenderer?, text: String): String {
        var newText = ""
        val allowedCharacters = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000"
        for (c in text.toCharArray()) {
            if (ChatAllowedCharacters.isAllowedCharacter(c)) {
                val index = fontRandom.nextInt(allowedCharacters.length)
                newText += allowedCharacters.toCharArray()[index]
            }
        }
        return newText
    }

    fun getStringHeight(text: String?): Int {
        return if (text == null) {
            0
        } else font!!.getStringHeight(text) / 2
    }

    override fun getColorCode(p_175064_1_: Char): Int {
        return colorCode["0123456789abcdef".indexOf(p_175064_1_)]
    }

    override fun setBidiFlag(state: Boolean) {
        bidi = state
    }

    override fun getBidiFlag(): Boolean {
        return bidi
    }

    private fun sizeStringToWidth(str: String, wrapWidth: Int): Int {
        val var3 = str.length
        var var4 = 0
        var var5 = 0
        var var6 = -1

        var var7 = false
        while (var5 < var3) {
            val var8 = str[var5]
            when (var8.toInt()) {
                10 -> --var5
                167 -> if (var5 < var3 - 1) {
                    ++var5
                    val var9 = str[var5]
                    if (var9.toInt() != 108 && var9.toInt() != 76) {
                        if (var9.toInt() == 114 || var9.toInt() == 82 || isFormatColor(var9)) {
                            var7 = false
                        }
                    } else {
                        var7 = true
                    }
                }
                32 -> {
                    var6 = var5
                    var4 += getStringWidth(Character.toString(var8))
                    if (var7) {
                        ++var4
                    }
                }
                else -> {
                    var4 += getStringWidth(Character.toString(var8))
                    if (var7) {
                        ++var4
                    }
                }
            }

            if (var8.toInt() == 10) {
                ++var5
                var6 = var5
                break
            }

            if (var4 > wrapWidth) {
                break
            }
            ++var5
        }

        return if (var5 != var3 && var6 != -1 && var6 < var5) var6 else var5
    }

    override fun getCharWidth(c: Char): Int {
        return getStringWidth(Character.toString(c))
    }

    override fun getStringWidth(text: String?): Int {
        if (text == null) {
            return 0
        }
        if (text.contains("\u00a7")) {
            val parts = text.split("\u00a7".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            var currentFont = font
            var width = 0
            var bold = false
            var italic = false

            for (index in parts.indices) {
                if (parts[index].length <= 0) {
                    continue
                }
                if (index == 0) {
                    width += currentFont!!.getStringWidth(parts[index])
                } else {
                    val words = parts[index].substring(1)
                    val type = parts[index][0]
                    val colorIndex = colorcodeIdentifiers.indexOf(type)
                    if (colorIndex != -1) {
                        when {
                            colorIndex < 16 -> { // coloring
                                bold = false
                                italic = false
                            }
                            colorIndex == 16 -> {
                            }
                            colorIndex == 17 -> bold = true
                            colorIndex == 18 -> {
                            }
                            colorIndex == 19 -> {
                            }
                            colorIndex == 20 -> italic = true
                            colorIndex == 21 -> { // reset
                                bold = false
                                italic = false
                            }
                        }
                    }
                    if (bold && italic) {
                        currentFont = boldItalicFont
                    } else if (bold) {
                        currentFont = boldFont
                    } else if (italic) {
                        currentFont = italicFont
                    } else {
                        currentFont = font
                    }

                    width += currentFont!!.getStringWidth(words)
                }
            }
            return width / 2
        } else {
            return font!!.getStringWidth(text) / 2
        }
    }


    fun setFont(font: Font, antiAlias: Boolean, charOffset: Int) {
        synchronized(this) {
            this.font = CustomFontRenderer(font, antiAlias, charOffset)
            boldFont = CustomFontRenderer(font.deriveFont(Font.BOLD), antiAlias, charOffset)
            italicFont = CustomFontRenderer(font.deriveFont(Font.ITALIC), antiAlias, charOffset)
            boldItalicFont = CustomFontRenderer(font.deriveFont(Font.BOLD or Font.ITALIC), antiAlias, charOffset)
            FONT_HEIGHT = height
        }
    }

    fun wrapWords(text: String, width: Double): List<String> {
        val finalWords = ArrayList<String>()
        if (getStringWidth(text) > width) {
            val words = text.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            var currentWord = ""
            var lastColorCode = (-1).toChar()

            for (word in words) {
                for (i in 0 until word.toCharArray().size) {
                    val c = word.toCharArray()[i]

                    if (c == '\u00a7' && i < word.toCharArray().size - 1) {
                        lastColorCode = word.toCharArray()[i + 1]
                    }
                }
                if (getStringWidth("$currentWord$word ") < width) {
                    currentWord += "$word "
                } else {
                    finalWords.add(currentWord)
                    currentWord = if (lastColorCode.toInt() == -1) "$word " else "\u00a7$lastColorCode$word "
                }
            }
            if (currentWord != "") {
                if (getStringWidth(currentWord) < width) {
                    finalWords
                            .add(if (lastColorCode.toInt() == -1) "$currentWord " else "\u00a7$lastColorCode$currentWord ")
                    currentWord = ""
                } else {
                    for (s in formatString(currentWord, width)) {
                        finalWords.add(s)
                    }
                }
            }
        } else {
            finalWords.add(text)
        }
        return finalWords
    }

    fun formatString(s: String, width: Double): List<String> {
        val finalWords = ArrayList<String>()
        var currentWord = ""
        var lastColorCode = (-1).toChar()
        for (i in 0 until s.toCharArray().size) {
            val c = s.toCharArray()[i]

            if (c == '\u00a7' && i < s.toCharArray().size - 1) {
                lastColorCode = s.toCharArray()[i + 1]
            }

            if (getStringWidth(currentWord + c) < width) {
                currentWord += c
            } else {
                finalWords.add(currentWord)
                currentWord = if (lastColorCode.toInt() == -1) c.toString() else "\u00a7" + lastColorCode + c.toString()
            }
        }

        if (currentWord != "") {
            finalWords.add(currentWord)
        }

        return finalWords
    }


    private fun drawLine(x: Double, y: Double, x1: Double, y1: Double, width: Float) {
        glDisable(GL_TEXTURE_2D)
        glLineWidth(width)
        glBegin(GL_LINES)
        glVertex2d(x, y)
        glVertex2d(x1, y1)
        glEnd()
        glEnable(GL_TEXTURE_2D)
    }


    private fun setupMinecraftColorcodes() {
        for (index in 0..31) {
            val var6 = (index shr 3 and 1) * 85
            var var7 = (index shr 2 and 1) * 170 + var6
            var var8 = (index shr 1 and 1) * 170 + var6
            var var9 = (index shr 0 and 1) * 170 + var6

            if (index == 6) {
                var7 += 85
            }

            if (index >= 16) {
                var7 /= 4
                var8 /= 4
                var9 /= 4
            }

            colorCode[index] = var7 and 255 shl 16 or (var8 and 255 shl 8) or (var9 and 255)
        }
    }

    override fun trimStringToWidth(p_78269_1_: String, p_78269_2_: Int): String {
        return trimStringToWidth(p_78269_1_, p_78269_2_, false)
    }

    override fun trimStringToWidth(p_78262_1_: String, p_78262_2_: Int, p_78262_3_: Boolean): String {
        val var4 = StringBuilder()
        var var5 = 0
        val var6 = if (p_78262_3_) p_78262_1_.length - 1 else 0
        val var7 = if (p_78262_3_) -1 else 1
        var var8 = false
        var var9 = false

        var var10 = var6
        while (var10 >= 0 && var10 < p_78262_1_.length && var5 < p_78262_2_) {
            val var11 = p_78262_1_[var10]
            val var12 = getStringWidth(Character.toString(var11))
            if (var8) {
                var8 = false
                if (var11.toInt() != 108 && var11.toInt() != 76) {
                    if (var11.toInt() == 114 || var11.toInt() == 82) {
                        var9 = false
                    }
                } else {
                    var9 = true
                }
            } else if (var12 < 0) {
                var8 = true
            } else {
                var5 += var12
                if (var9) {
                    ++var5
                }
            }

            if (var5 > p_78262_2_) {
                break
            }

            if (p_78262_3_) {
                var4.insert(0, var11)
            } else {
                var4.append(var11)
            }
            var10 += var7
        }

        return var4.toString()
    }

    override fun listFormattedStringToWidth(str: String, wrapWidth: Int): List<String> {
        return wrapFormattedStringToWidth(str, wrapWidth).split("\n".toRegex()).dropLastWhile { it.isEmpty() }
    }

    fun wrapFormattedStringToWidth(str: String, wrapWidth: Int): String {
        val var3 = sizeStringToWidth(str, wrapWidth)
        if (str.length <= var3) {
            return str
        } else {
            val var4 = str.substring(0, var3)
            val var5 = str[var3]
            val var6 = var5.toInt() == 32 || var5.toInt() == 10
            val var7 = getFormatFromString(var4) + str.substring(var3 + if (var6) 1 else 0)
            return var4 + "\n" + wrapFormattedStringToWidth(var7, wrapWidth)
        }
    }

    fun getColor(colorCode: Int, alpha: Float): Color {
        return Color((colorCode shr 16) / 255f, (colorCode shr 8 and 0xff) / 255f, (colorCode and 0xff) / 255f, alpha)
    }


    private fun setupColorcodeIdentifier(): String {
        var minecraftColorCodes = "0123456789abcdefklmnor"
        for (i in customColorCodes.indices) {
            if (customColorCodes[i] != null) {
                minecraftColorCodes += i.toChar()
            }
        }
        return minecraftColorCodes
    }

    override fun onResourceManagerReload(p_110549_1_: IResourceManager?) {}

    companion object {

        fun getFormatFromString(p_78282_0_: String): String {
            var var1 = ""
            var var2 = -1
            val var3 = p_78282_0_.length

            var2 = p_78282_0_.indexOf(167.toChar(), var2 + 1)
            while (var2 != -1) {
                var2 = p_78282_0_.indexOf(167.toChar(), var2 + 1)
                if (var2 < var3 - 1) {
                    val var4 = p_78282_0_[var2 + 1]
                    if (isFormatColor(var4)) {
                        var1 = "\u00a7" + var4
                    } else if (isFormatSpecial(var4)) {
                        var1 = var1 + "\u00a7" + var4
                    }
                }
            }

            return var1
        }

        private fun isFormatSpecial(formatChar: Char): Boolean {
            return (formatChar.toInt() in 107..111 || formatChar.toInt() in 75..79 || formatChar.toInt() == 114
                    || formatChar.toInt() == 82)
        }

        private fun isFormatColor(colorChar: Char): Boolean {
            return (colorChar.toInt() in 48..57 || colorChar.toInt() in 97..102
                    || colorChar.toInt() in 65..70)
        }
    }
}