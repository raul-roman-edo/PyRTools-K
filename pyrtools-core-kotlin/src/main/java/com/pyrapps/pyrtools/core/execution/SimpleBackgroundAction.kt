package com.pyrapps.pyrtools.core.execution

import com.pyrapps.pyrtools.core.execution.AsyncAction.BackgroundAction
import kotlinx.coroutines.experimental.async
import kotlin.coroutines.experimental.CoroutineContext

class SimpleBackgroundAction<in Param, out Result>(private val bgContext: CoroutineContext,
                                                   private val action: (Param?) -> Result)
    : BackgroundAction<Param, Result> {
    suspend override fun invoke(param: Param?): Result {
        return async(bgContext) { action(param) }.await()
    }
}