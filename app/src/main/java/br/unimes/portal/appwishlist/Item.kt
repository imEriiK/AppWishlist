package br.unimes.portal.appwishlist

data class Item(
    val id: String = "",
    val nome: String = "",
    val preco: Double? = null,
    val lugar: String = ""
)