package com.pyrapps.pyrtools.core.android.ui.cards

import android.graphics.Rect

interface CardSpacingProvider {
  fun getSpacing(positionInList: Int): Rect
}