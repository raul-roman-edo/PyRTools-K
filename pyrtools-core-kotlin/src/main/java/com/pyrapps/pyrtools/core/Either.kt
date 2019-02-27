package com.pyrapps.pyrtools.core

import com.pyrapps.pyrtools.core.Either.Left
import com.pyrapps.pyrtools.core.Either.Right

sealed class Either<out E, out V> {
  data class Left<out E>(val value: E) : Either<E, Nothing>()
  data class Right<out V>(val value: V) : Either<Nothing, V>()

  inline fun <V2> map(f: (V) -> V2): Either<E, V2> = when (this) {
    is Left -> this
    is Right -> Right(f(value))
  }

  inline fun <E2> mapLeft(f: (E) -> E2): Either<E2, V> = when (this) {
    is Left -> Left(f(value))
    is Right -> this
  }

  inline fun <A> fold(
    e: (E) -> A,
    v: (V) -> A
  ): A = when (this) {
    is Left -> e(value)
    is Right -> v(value)
  }
}

fun <E> E.toLeft() = Either.Left(this)

fun <V> V.toRight() = Either.Right(this)

inline infix fun <E, V, V2> Either<E, V>.flatMap(f: (V) -> Either<E, V2>) = when (this) {
  is Left -> this
  is Right -> f(value)
}

inline infix fun <E, E2, V> Either<E, V>.flatMapLeft(f: (E) -> Either<E2, V>) = when (this) {
  is Left -> f(value)
  is Right -> this
}
