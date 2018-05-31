@file:JvmName("Wrapper")
package me.kbrewster.blazeapi.client

import net.minecraft.client.Minecraft

val mc = Minecraft.getMinecraft()

val thePlayer = mc.thePlayer

val fontRenderer = mc.fontRendererObj
