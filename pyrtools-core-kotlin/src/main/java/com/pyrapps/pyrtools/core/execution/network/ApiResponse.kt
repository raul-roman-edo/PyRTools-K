package com.pyrapps.pyrtools.core.execution.network

import com.pyrapps.pyrtools.core.Result


open class ApiResponse<Payload>(var code: Int = 0, var errorCode: String = "") : Result<Payload>()