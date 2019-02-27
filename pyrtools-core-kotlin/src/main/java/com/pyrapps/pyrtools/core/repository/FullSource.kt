package com.pyrapps.pyrtools.core.repository

import com.pyrapps.pyrtools.core.Either
import com.pyrapps.pyrtools.core.Error

interface FullSource<Params, Data> : Source<Params, Either<Error, Data>>, Updateable<Params, Data>,
    Clearable<Params>