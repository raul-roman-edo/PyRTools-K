package com.pyrapps.pyrtools.core.broadcastevents


interface BroadcastEventSender<Param> {
    fun send(param: Param)
}