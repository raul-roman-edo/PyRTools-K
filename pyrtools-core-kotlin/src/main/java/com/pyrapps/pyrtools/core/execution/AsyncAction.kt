package com.pyrapps.pyrtools.core.execution

import kotlinx.coroutines.experimental.launch
import kotlin.coroutines.experimental.CoroutineContext


class AsyncAction<in Param, out Result>(val uiContext: CoroutineContext,
                                        val executedInParallel
                                        : BackgroundAction<Param, Result>) {
    inline fun execute(param: Param? = null, crossinline dispatch: (Result) -> Unit) {
        launch(uiContext) {
            val result = executedInParallel(param)
            dispatch(result)
        }
    }

    interface BackgroundAction<in Param, out Result> {
        suspend operator fun invoke(param: Param? = null): Result
    }
}