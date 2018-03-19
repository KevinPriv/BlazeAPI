package me.kbrewster.blazeapi.internal.addons.translate

import me.kbrewster.blazeapi.internal.addons.AddonManifest


interface ITranslator {

    fun translate(manifest: AddonManifest)

}