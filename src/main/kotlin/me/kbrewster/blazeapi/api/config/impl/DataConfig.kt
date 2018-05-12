package me.kbrewster.blazeapi.api.config.impl

import com.google.gson.GsonBuilder
import me.kbrewster.blazeapi.api.config.IConfig
import me.kbrewster.blazeapi.api.config.strategy.AnnotationExclusionStrategy

class DataConfig : IConfig {

    private val gsonRules = GsonBuilder()
            .addSerializationExclusionStrategy(AnnotationExclusionStrategy())
            .addDeserializationExclusionStrategy(AnnotationExclusionStrategy())
            .setPrettyPrinting()
            .create()

    override fun load() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun save() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}