package com.midcores.silapan.presentation.viewmodel.state

sealed class ResultState<out T> {
    data class Success<T>(val data: T) : ResultState<T>()
    data class Failure(val message: String) : ResultState<Nothing>()
}