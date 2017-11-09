package com.pyrapps.pyrtools.core.android.broadcastevents

import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import com.pyrapps.pyrtools.core.broadcastevents.BroadcastEventSender
import java.io.Serializable


class BroadcastEventSenderImpl<T>(private val context: Context,
                                  private val action: String) : BroadcastEventSender<T> {
    override fun send(param: T) {
        val intent = Intent(action)
        intent.putExtra(BroadcastEventReceiverImpl.KEY, param as Serializable)
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }
}