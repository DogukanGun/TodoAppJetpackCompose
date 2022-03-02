package com.dag.todoappjetpack.util

sealed class RequestState<out T> {
    object Loading : RequestState<Nothing>()
    class Success<T>(val data:T) : RequestState<T>()
    class Error(val Error:Throwable) : RequestState<Nothing>()
}