package me.kbrewster.blazeapi.api.command

import java.lang.reflect.Method
import java.security.InvalidParameterException

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class Command(val name: String, val alias: Array<String> = [])

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class DefaultCommand

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class SubCommand

class AnnotationNotFoundException(msg: String) : Exception(msg)

object CommandManager {

    private val commands = HashMap<Any, ArrayList<Method>>()

    @JvmStatic
    fun register(obj: Any) {
        val clazz = obj.javaClass
        getCommandAnnotation(clazz) ?: throw AnnotationNotFoundException("Could not find @Command annotation inside of class")
        val methods = ArrayList<Method>()
        clazz.declaredMethods
                .filter { it.isAnnotationPresent(DefaultCommand::class.java) || it.isAnnotationPresent(SubCommand::class.java) }
                .forEach { method -> methods.add(method) }
        commands[obj] = methods
    }

    @JvmStatic
    operator fun invoke(command: String) {
        if (command.startsWith("/")) {
            var args = command.split(" ")
            val prefix = args[0].removePrefix("/")
            args = args.drop(1)
            val argumentParser = CommandArgumentParserFactory()
            val typedArguments = argumentParser.parse(args)
            commands
                    .filterKeys { getCommandAnnotation(it.javaClass)!!.name == prefix }
                    .forEach { classObj, commands ->
                        val subCommands = commands.filter {
                            it.isAnnotationPresent(SubCommand::class.java) &&
                                    it.parameters.size == typedArguments.size
                        }
                        for (method in subCommands) {
                            val parameters = method.parameters
                            val correctParamters = typedArguments.filterIndexed { i, arg ->
                                val paramType = parameters[i].type
                                when (arg) {
                                    is Int -> paramType.isAssignableFrom(Int::class.java)
                                    is Double -> paramType.isAssignableFrom(Double::class.java)
                                    is Boolean -> paramType.isAssignableFrom(Boolean::class.java)
                                    is String -> paramType.isAssignableFrom(String::class.java)
                                    else -> false
                                }
                            }.size == parameters.size
                            if (correctParamters) {
                                invokeCommand(classObj, method, typedArguments)
                                return@forEach
                            }
                        }
                        val defaultCommand = commands.firstOrNull { it.isAnnotationPresent(DefaultCommand::class.java) }
                                ?: throw AnnotationNotFoundException("Could not find default command?\nPlease annotate with @DefaultCommand")
                        when {
                            defaultCommand.parameters[0].type.isAssignableFrom(List::class.java) -> defaultCommand.invoke(classObj, typedArguments)
                            defaultCommand.parameters[0].type.isAssignableFrom(Array<Any>::class.java) -> defaultCommand.invoke(classObj, typedArguments.toTypedArray())

                            else -> {
                                throw InvalidParameterException("Default Command must contain an object array or list as the first parameter!")
                            }
                        }
                    }
        }
    }

    private fun invokeCommand(obj: Any, command: Method, arguments: List<Any>) {
        command.invoke(obj, *arguments.toTypedArray())
    }

    private fun getCommandAnnotation(clazz: Class<Any>): Command? {
        return clazz.getAnnotation(Command::class.java)
    }

}