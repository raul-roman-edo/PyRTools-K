package com.pyrapps.pyrtools.core.android.broadcastevents

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v4.content.LocalBroadcastManager
import com.pyrapps.pyrtools.core.broadcastevents.BroadcastEventReceiver


class BroadcastEventReceiverImpl<T>(private val context: Context, private val action: String)
    : BroadcastEventReceiver<T>, BroadcastReceiver() {
    companion object {
        val KEY = "Broadcast.Result"
    }

    private val intentFilter by lazy { IntentFilter(action) }
    private var callback: ((T) -> Unit)? = null

    override fun register(callback: (T) -> Unit) {
        this.callback = callback
        LocalBroadcastManager.getInstance(context).registerReceiver(this, intentFilter)
    }

    override fun unregister() {
        callback = null
        LocalBroadcastManager.getInstance(context).unregisterReceiver(this)
    }

    override fun onReceive(p0: Context?, intent: Intent?) {
        val parameter = intent?.getSerializableExtra(KEY) as T
        callback?.invoke(parameter)
    }
}