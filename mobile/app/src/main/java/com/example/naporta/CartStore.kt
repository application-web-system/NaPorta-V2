package com.example.naporta

/*
    # Carrinho de compras em memória
    - Guarda os itens do carrinho na memória RAM
    - Se o app fechar, o carrinho é perdido
    - Ideal para protótipos e testes
    - Tem funções de adicionar, remover, aumentar/diminuir quantidade
 */

/** Carrinho local: vive somente enquanto o processo Android estiver aberto. */
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
        if (index >= 0) mutableItems[index] = mutableItems[index].copy(quantity = mutableItems[index].quantity + item.quantity)
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

    fun loadPreviewDataIfEmpty() {
        if (mutableItems.isNotEmpty()) return
        add(CartItem(1L, "Hambúrguer da Casa", 39.90, imageRes = R.drawable.img_hamburguer))
        add(CartItem(2L, "Smash Duplo", 36.90, imageRes = R.drawable.img_hamburguer))
        add(CartItem(3L, "Hambúrguer Bacon", 44.90, imageRes = R.drawable.img_hamburguer))
    }

    private fun notifyChanges() {
        val snapshot = items
        observers.toList().forEach { observer ->
            observer(snapshot)
        }
    }
}
