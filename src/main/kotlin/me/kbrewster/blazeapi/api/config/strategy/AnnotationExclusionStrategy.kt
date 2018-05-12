package me.kbrewster.blazeapi.api.config.strategy

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import me.kbrewster.blazeapi.api.config.Ignore


class AnnotationExclusionStrategy : ExclusionStrategy {

    override fun shouldSkipField(f: FieldAttributes): Boolean {
        return f.getAnnotation(Ignore::class.java) != null
    }

    override fun shouldSkipClass(clazz: Class<*>): Boolean {
        return false
    }

}