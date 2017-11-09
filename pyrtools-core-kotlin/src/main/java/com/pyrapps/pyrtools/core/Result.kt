package com.pyrapps.pyrtools.core

open class Result<Payload>(var isValid: Boolean = false, var payload: Payload? = null)