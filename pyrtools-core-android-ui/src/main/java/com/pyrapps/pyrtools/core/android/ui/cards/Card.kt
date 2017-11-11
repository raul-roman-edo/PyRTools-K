package com.pyrapps.pyrtools.core.android.ui.cards

data class Card<out Model>(val id: String,
                           val type: Int,
                           val model: Model? = null,
                           var isSwipeable: Boolean = false)