package com.pyrapps.pyrtools.core.broadcastevents


interface BroadcastEventReceiver<Param> {
    fun register(callback: (Param) -> Unit)

    fun unregister()
}