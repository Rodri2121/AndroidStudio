package com.example.logintry.ui.theme.util

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val message: String, val cause: Exception? = null) : Resource<Nothing>()

    object Loading : Resource<Nothing>() {
        override fun toString() = "Loading"
    }

    object Idle : Resource<Nothing>() {
        override fun toString() = "Idle"
    }
}