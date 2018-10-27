@file:JvmName("Wrapper")
package me.kbrewster.blazeapi.client

import net.minecraft.client.Minecraft

val mc
    get() = Minecraft.getMinecraft()

val thePlayer 
    get() = mc.thePlayer

val fontRenderer
    get() = mc.fontRendererObj

inline fun Minecraft.profile(key: String, body: () -> Unit) {
    this.mcProfiler.startSection(key)
    body()
    this.mcProfiler.endSection()
}
