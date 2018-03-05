package me.kbrewster.blazeapi.api.event

import java.util.concurrent.CopyOnWriteArraySet

object EventBus {

    private val subscriptions = HashMap<Class<*>, CopyOnWriteArraySet<EventSubscriber>>()

    @JvmStatic fun register(obj: Any) {
        val clazz = obj.javaClass
        for (method in clazz.declaredMethods) {
            method.getAnnotation(InvokeEvent::class.java) ?: continue
            val event = method.parameters.first().type ?: throw
            IllegalArgumentException("Couldn't find parameter inside of ${method.name}!")
            val priority = method.getAnnotation(InvokeEvent::class.java).priority
            method.isAccessible = true
            subscriptions.putIfAbsent(event, CopyOnWriteArraySet())
            subscriptions[event]!!.add(EventSubscriber(obj, method, priority))
        }
    }

    @JvmStatic fun register(vararg obj: Any) =
            obj.forEach(this::register)

    @JvmStatic fun unregister(obj: Any) =
            subscriptions.values.forEach { map ->
                map.removeIf { it.instance == obj }
            }

    @JvmStatic fun unregister(clazz: Class<*>) =
            subscriptions.values.forEach { map ->
                map.removeIf { it.instance.javaClass == clazz }
            }

    @JvmStatic fun post(event: Any) {
        subscriptions[event.javaClass]
                ?.sortedByDescending { it.priority.value }
                ?.forEach { sub -> sub.method.invoke(sub.instance, event) }

    }
}