package me.kbrewster.blazeapi.api.config.impl

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.kbrewster.blazeapi.api.config.*
import java.io.File
import java.lang.reflect.Field

class GsonConfig(private val file: File) : Config {

    private val savableObjects = ArrayList<Any>()

    private val gsonRules = GsonBuilder()
            .setPrettyPrinting()
            .create()

    fun register(obj: Any) {
        savableObjects.add(obj)
    }

    fun unregister(clazz: Class<*>) {
        val removeObj = savableObjects.firstOrNull { it.javaClass == clazz } ?: return
        savableObjects.remove(removeObj)
    }


    fun unregister(obj: Any) {
        val removeObj = savableObjects.firstOrNull { obj == obj } ?: return
        savableObjects.remove(removeObj)
    }

    override fun load() {
        if(!file.exists()) {
            return
        }

        val parser = JsonParser()
        val json = parser.parse(file.readText()).asJsonObject
        savableObjects.forEach { obj ->
            val clazzKey = obj.javaClass.canonicalName
            if (json.has(clazzKey)) {
                loadToClass(json, obj)
            }
        }
    }

    private fun loadToClass(json: JsonObject, obj: Any) {
        for(field in obj.javaClass.declaredFields) {
            field.isAccessible = true
            field.annotations.forEach { annotation ->
                val clazzObject = json[obj.javaClass.canonicalName].asJsonObject

                when (annotation) {
                    is SaveableString -> {
                        val fieldObject = clazzObject[annotation.value]
                        if(fieldObject.isJsonNull) {
                            return
                        }
                        setString(obj, field, fieldObject.asString, annotation)
                    }
                    is SaveableBoolean -> {
                        val fieldObject = clazzObject[annotation.value]
                        if(fieldObject.isJsonNull) {
                            return
                        }
                        setBoolean(obj, field, fieldObject.asBoolean, annotation)
                    }
                    is SaveableNumber -> TODO("not implemented")
                    is SaveableObject -> TODO("not implemented")
                }
            }
        }
    }

    override fun save() {
        val json = JsonObject()
        val jsonObject = JsonObject()
        savableObjects.forEach { obj ->
            obj.javaClass.declaredFields.forEach { field ->
                field.isAccessible = true
                field.annotations.forEach { annotation ->
                    when (annotation) {
                        is SaveableString -> jsonObject.addProperty(annotation.value, getString(obj, field, annotation))
                        is SaveableBoolean -> jsonObject.addProperty(annotation.value, getBoolean(obj, field, annotation))
                        is SaveableNumber -> TODO("not implemented")
                        is SaveableObject -> TODO("not implemented")
                    }
                }
            }
            json.add(obj.javaClass.canonicalName, jsonObject)
        }
        file.writeText(gsonRules.toJson(json))
    }

    private fun getString(obj: Any, field: Field, saveable: SaveableString): String {
        return field.get(obj).toString()
    }

    private fun setString(obj: Any, field: Field, value: String, saveable: SaveableString) {
        field.set(obj, value)
    }

    private fun getBoolean(obj: Any, field: Field, saveable: SaveableBoolean): Boolean {
        return field.getBoolean(obj)
    }

    private fun setBoolean(obj: Any, field: Field, value: Boolean, saveable: SaveableBoolean) {
        field.set(obj, value)
    }
}