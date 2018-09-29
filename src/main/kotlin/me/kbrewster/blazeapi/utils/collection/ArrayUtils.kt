package me.kbrewster.blazeapi.utils.collection

object ArrayUtils {

    @JvmStatic
    fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
        val tmp = this[index1] // 'this' corresponds to the list
        this[index1] = this[index2]
        this[index2] = tmp
    }

    @JvmStatic
    fun <T> Iterator<T>.toList(): List<T> =
            ArrayList<T>().apply {
                while (hasNext())
                    this += next()
            }

    @JvmStatic
    infix fun <T> List<T>.insideOf(obj: T): Boolean {
        return this.firstOrNull { it == obj } != null
    }

}