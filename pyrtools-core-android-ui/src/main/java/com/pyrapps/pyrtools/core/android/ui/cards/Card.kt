package com.pyrapps.pyrtools.core.android.ui.cards

class Card<Model> @JvmOverloads constructor(
  val type: Enum<*>,
  val id: String = type.toString(),
  val model: Model? = null,
  val isSwipeable: Boolean = false
) {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || other !is Card<*>) return false
    return id == other.id
  }

  override fun hashCode() = id.hashCode()
}