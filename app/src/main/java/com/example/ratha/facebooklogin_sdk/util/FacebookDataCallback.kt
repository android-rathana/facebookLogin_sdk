package com.example.ratha.facebooklogin_sdk.util

interface FacebookDataCallback<T> {
    fun onSuccess(user: T)
    fun onFail(error: String)
}