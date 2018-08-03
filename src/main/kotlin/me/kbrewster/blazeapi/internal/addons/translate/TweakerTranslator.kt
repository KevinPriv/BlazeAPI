package me.kbrewster.blazeapi.internal.addons.translate

import me.kbrewster.blazeapi.internal.addons.AddonManifest
import me.kbrewster.blazeapi.internal.addons.translate.impl.AbstractTranslator
import net.minecraft.launchwrapper.ITweaker
import net.minecraft.launchwrapper.Launch


class TweakerTranslator : AbstractTranslator() {

    override fun translate(manifest: AddonManifest) {
        val tweakers = Launch.blackboard["Tweaks"] as MutableList<ITweaker>
        tweakers.add(Class.forName(manifest.tweakerClass).newInstance() as ITweaker)
    }

}