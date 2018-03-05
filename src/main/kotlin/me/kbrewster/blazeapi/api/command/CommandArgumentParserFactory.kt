package me.kbrewster.blazeapi.api.command

import java.lang.Double.parseDouble
import java.lang.Integer.parseInt

internal class CommandArgumentParserFactory {

    private fun toInteger(x: String): Int? {
        return try {
            parseInt(x)
        } catch(e: Exception) {
            null
        }
    }

    private fun toDouble(x: String): Double? {
        return try {
            parseDouble(x)
        } catch(e: Exception) {
            null
        }
    }

    private fun toBoolean(x: String): Boolean? {
        return try {
            if(x.equals("true", ignoreCase = true) || x.equals("false", ignoreCase = true)) {
                return x.equals("true", ignoreCase = true)
            }
            null
        } catch(e: Exception) {
            null
        }
    }

    fun parse(args: List<String>): List<Any> {
        val types = ArrayList<Any>()
        args.forEach {arg ->
            when {
                toInteger(arg) != null -> types.add(toInteger(arg) as Any)
                toDouble(arg) != null -> types.add(toDouble(arg) as Any)
                toBoolean(arg) != null -> types.add(toBoolean(arg) as Any)
                else -> types.add(arg)
            }
        }
        return types
    }
}