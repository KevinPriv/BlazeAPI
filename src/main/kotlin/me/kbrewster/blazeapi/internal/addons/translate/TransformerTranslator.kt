package me.kbrewster.blazeapi.internal.addons.translate

import me.kbrewster.blazeapi.TRANSFORMERS
import me.kbrewster.blazeapi.internal.addons.AddonManifest
import me.kbrewster.blazeapi.internal.addons.translate.impl.AbstractTranslator
import me.kbrewster.blazeapi.internal.launch.transformers.impl.ITransformer

/**
 * Adds all transformers to a public list
 */
class TransformerTranslator : AbstractTranslator() {

    override fun translate(manifest: AddonManifest) {
        manifest.transformers?.map { Class.forName(it) }
                ?.filter { it is ITransformer }
                ?.forEach { TRANSFORMERS + it }
    }

}