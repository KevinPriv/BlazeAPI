package me.kbrewster.blazeapi.api.event

import java.util.concurrent.CopyOnWriteArrayList

/**
 * Used to register/post events so they can be invoked from any class
 *
 * @since 1.0
 * @author Kevin Brewster
 */
class EventBus {

    private val subscriptions = HashMap<Class<*>, CopyOnWriteArrayList<EventSubscriber>>()

    /**
     * Registers all methods of a class into the event system with
     * the {@link package me.kbrewster.blazeapi.api.event.InvokeEvent} annotation
     *
     * @param obj An instance of the class which you would like to register as an event
     */
    fun register(obj: Any) {
        val clazz = obj.javaClass
        // iterates though all the methods in the class
        for (method in clazz.declaredMethods) {
            // all the informaton and error checking before the method is added such
            // as if it even is an event before the element even touches the hashmap
            method.getAnnotation(InvokeEvent::class.java) ?: continue
            val event = method.parameters.first().type ?: throw
            IllegalArgumentException("Couldn't find parameter inside of ${method.name}!")
            val priority = method.getAnnotation(InvokeEvent::class.java).value
            method.isAccessible = true

            // where the method gets added to the event key inside of the subscription hashmap
            // the arraylist is either sorted or created before the element is added
            this.subscriptions.let { subs ->
                if (subs.containsKey(event)) {
                    // sorts array on insertion
                    subs[event]?.add(EventSubscriber(obj, method, priority))
                    subs[event]?.sortByDescending { it.priority.value }
                } else {
                    // event hasn't been added before so it creates a new instance
                    // sorting does not matter here since there is no other elements to compete against
                    subs[event] = CopyOnWriteArrayList()
                    subs[event]?.add(EventSubscriber(obj, method, priority))
                }
            }
        }
    }

    /**
     * Registers all methods of each class in the array into the event system with
     * the {@link package me.kbrewster.blazeapi.api.event.InvokeEvent} annotation
     *
     * @param obj An instance of the class which you would like to register as an event
     */
    fun register(vararg obj: Any) =
            obj.forEach(this::register)

    /**
     * Unregisters all methods of the class instance from the event system
     * inside of {@link #subscriptions}
     *
     * @param obj An instance of the class which you would like to register as an event
     */
    fun unregister(obj: Any) =
            this.subscriptions.values.forEach { map ->
                map.removeIf { it.instance == obj }
            }

    /**
     * Unregisters all methods of the class from the event system
     * inside of {@link #subscriptions}
     *
     * @param clazz An instance of the class which you would like to register as an event
     */
    fun unregister(clazz: Class<*>) =
            this.subscriptions.values.forEach { map ->
                map.removeIf { it.instance.javaClass == clazz }
            }

    /**
     * Invokes all of the methods which are inside of the classes
     * registered to the event
     *
     * @param event Event that is being posted
     */
    fun post(event: Any) {
        this.subscriptions[event.javaClass]
                ?.forEach { sub -> sub.method.invoke(sub.instance, event) }
    }
}