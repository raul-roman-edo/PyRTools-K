package com.pyrapps.pyrtools.core.execution.network


class ApiResponse<Payload>(var code: Int = 0, var errorCode: String = "") {
    var payload: Payload? = null
}