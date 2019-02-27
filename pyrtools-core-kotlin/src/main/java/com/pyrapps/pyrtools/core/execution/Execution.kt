package com.pyrapps.pyrtools.core.execution

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface Executor {
  fun <Result> async(f: () -> Result): suspend () -> Result

  fun <Result> sync(
    toExecuteInParallel: suspend () -> Result,
    dispatch: (Result) -> Unit
  )

  fun cancelAll()
}

class AsyncExecutor(
  private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
  private val bgDispatcher: CoroutineDispatcher = Dispatchers.IO
) : Executor {
  private val job = Job()

  override fun <Result> async(f: () -> Result): suspend () -> Result {
    return { withContext(bgDispatcher) { f() } }
  }

  override fun <Result> sync(
    toExecuteInParallel: suspend () -> Result,
    dispatch: (Result) -> Unit
  ) {
    val scope = CoroutineScope(job + mainDispatcher)
    scope.launch {
      val result = toExecuteInParallel()
      if (isActive) {
        dispatch(result)
      }
    }
  }

  override fun cancelAll() {
    job.cancel()
  }
}

fun <Result> (() -> Result).async(executor: Executor) = executor.async { this() }

fun <Param, Result> ((Param) -> Result).async(
  executor: Executor,
  param: Param
) = executor.async { this(param) }

fun <Param0, Param1, Result> ((Param0, Param1) -> Result).async(
  executor: Executor,
  param0: Param0,
  param1: Param1
) = executor.async { this(param0, param1) }

fun <Param0, Param1, Param2, Result> ((Param0, Param1, Param2) -> Result).async(
  executor: Executor,
  param0: Param0,
  param1: Param1,
  param2: Param2
) = executor.async { this(param0, param1, param2) }

inline infix fun <Result, Other> (suspend () -> Result).andThen(
  crossinline transform: (Result) -> Other
): suspend () -> Other = { transform(this()) }

fun <Result> (suspend () -> Result).sync(
  executor: Executor,
  dispatch: (Result) -> Unit
) = executor.sync(this, dispatch)

fun <Param0, Result> (suspend (Param0) -> Result).sync(
  executor: Executor,
  param0: Param0,
  dispatch: (Result) -> Unit
) = executor.sync({ this(param0) }, dispatch)

fun <Param0, Param1, Result> (suspend (Param0, Param1) -> Result).sync(
  executor: Executor,
  param0: Param0,
  param1: Param1,
  dispatch: (Result) -> Unit
) = executor.sync({ this(param0, param1) }, dispatch)

fun <Param0, Param1, Param2, Result> (suspend (Param0, Param1, Param2) -> Result).sync(
  executor: Executor,
  param0: Param0,
  param1: Param1,
  param2: Param2,
  dispatch: (Result) -> Unit
) = executor.sync({ this(param0, param1, param2) }, dispatch)