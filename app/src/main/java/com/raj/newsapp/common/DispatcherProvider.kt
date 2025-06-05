package com.raj.newsapp.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface DispatcherProvider {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
}

class DefaultDispatcherProvider(
    override val main: CoroutineDispatcher = Dispatchers.Main,//Single threaded
    override val io: CoroutineDispatcher = Dispatchers.IO,//64 thread or # CPU core(which ever is larger)
    override val default: CoroutineDispatcher= Dispatchers.Default//#thread = # of CPU core (At least 2)
) : DispatcherProvider