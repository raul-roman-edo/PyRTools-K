package com.pyrapps.pyrtools.core.android.ui.extensions

import android.widget.ImageView
import com.squareup.picasso.Picasso


fun ImageView.load(url: String) {
    Picasso.with(context).load(url).into(this)
}