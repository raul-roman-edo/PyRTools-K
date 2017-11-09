package com.pyrapps.pyrtools.core.execution

import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


class ThreadExecutor private constructor() {
    private object Holder {
        val INSTANCE = ThreadExecutor()
    }

    companion object {
        val instance: ThreadExecutor by lazy { Holder.INSTANCE }
    }

    val threadPool: ThreadPoolExecutor by lazy {
        val poolSize = Runtime.getRuntime().availableProcessors()
        val maxPoolSize = poolSize
        val keepAliveTime = 10L
        val queue = ArrayBlockingQueue<Runnable>(5)
        ThreadPoolExecutor(poolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS, queue)
    }

    operator fun invoke(task: Runnable) {
        threadPool.execute(task)
    }
}