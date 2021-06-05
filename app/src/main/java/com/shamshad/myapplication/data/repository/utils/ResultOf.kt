package com.autosmarts.gondor.data.repository.utils

import java.lang.Exception

sealed class ResultOf<out T> {
    data class Success<out T>(val value: T) : ResultOf<T>()
    data class Error(val exception: Exception) : ResultOf<Nothing>()
    object Loading : ResultOf<Nothing>()
}