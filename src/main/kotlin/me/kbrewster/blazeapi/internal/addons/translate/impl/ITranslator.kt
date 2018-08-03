package me.kbrewster.blazeapi.internal.addons.translate.impl

import me.kbrewster.blazeapi.internal.addons.AddonManifest


interface ITranslator {

    fun translate(manifest: AddonManifest)

}