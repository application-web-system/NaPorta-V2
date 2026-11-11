package com.example.naporta

object CartStore {
    private val mutableItems = mutableListOf<CartItem>()
    private val observers = mutableSetOf<(List<CartItem>) -> Unit>()
    val items: List<CartItem> get() = mutableItems.toList()

    fun observe(observer: (List<CartItem>) -> Unit) {
        observers += observer
        observer(items)
    }

    fun removeObserver(observer: (List<CartItem>) -> Unit) {
        observers -= observer
    }

    fun add(item: CartItem) {
        val index = mutableItems.indexOfFirst { it.productId == item.productId }
        if (index >= 0) mutableItems[index] = mutableItems[index].copy(quantity = mutableItems[index].quantity + 1)
        else mutableItems += item
        notifyChanges()
    }

    fun increase(productId: Long) = changeQuantity(productId, 1)
    fun decrease(productId: Long) = changeQuantity(productId, -1)

    fun remove(productId: Long) {
        mutableItems.removeAll { it.productId == productId }
        notifyChanges()
    }

    fun clear() {
        mutableItems.clear()
        notifyChanges()
    }

    private fun changeQuantity(productId: Long, delta: Int) {
        val index = mutableItems.indexOfFirst { it.productId == productId }
        if (index < 0) return
        val updated = mutableItems[index].copy(quantity = mutableItems[index].quantity + delta)
        if (updated.quantity > 0) mutableItems[index] = updated else mutableItems.removeAt(index)
        notifyChanges()
    }

    private fun notifyChanges() {
        val snapshot = items
        observers.toList().forEach { it(snapshot) }
    }
}
