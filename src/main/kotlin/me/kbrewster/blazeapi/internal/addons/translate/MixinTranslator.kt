package me.kbrewster.blazeapi.internal.addons.translate

import me.kbrewster.blazeapi.internal.addons.AddonManifest
import me.kbrewster.blazeapi.internal.addons.translate.impl.AbstractTranslator
import org.spongepowered.asm.mixin.Mixins

class MixinTranslator : AbstractTranslator() {

    override fun translate(manifest: AddonManifest) {
        manifest.mixinConfigs?.forEach(Mixins::addConfiguration)
    }

}