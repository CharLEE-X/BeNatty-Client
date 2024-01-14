package data.utils

infix fun <T> List<T>.update(item: T): List<T> {
    val mutableList = this.toMutableList()
    mutableList.firstOrNull { it == item }
        ?.let { mutableList.remove(it) }
        ?: mutableList.add(item)
    return mutableList
}
