package me.kbrewster.blazeapi.internal.addons.translate

import me.kbrewster.blazeapi.internal.addons.AddonManifest
import me.kbrewster.blazeapi.internal.addons.annotations.Instance

class InstanceTranslator : ITranslator {

    override fun translate(manifest: AddonManifest) {
        val instance = Class.forName(manifest.mainClass).newInstance()
        val clazz = instance.javaClass
        val fields = clazz.fields
        for (field in fields) {
            val annotation = field.getAnnotation(Instance::class.java)
            if (annotation != null) {
                field.isAccessible = true
                field.set(instance, instance)
            }
        }
    }

}