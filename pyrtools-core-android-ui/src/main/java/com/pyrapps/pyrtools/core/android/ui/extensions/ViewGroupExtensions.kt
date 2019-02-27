package com.pyrapps.pyrtools.core.android.ui.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun ViewGroup.inflateView(layoutId: Int, attachToRoot: Boolean = false): View
        = LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
