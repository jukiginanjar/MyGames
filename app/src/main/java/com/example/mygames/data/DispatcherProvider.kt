package com.example.mygames.data

import kotlinx.coroutines.Dispatchers

class DispatcherProvider {

    val io get() = Dispatchers.IO

    val default get() = Dispatchers.Default

    val main get() = Dispatchers.Main
}