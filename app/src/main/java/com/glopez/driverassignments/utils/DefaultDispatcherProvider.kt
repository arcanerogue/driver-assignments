package com.glopez.driverassignments.utils

import kotlinx.coroutines.Dispatchers

class DefaultDispatcherProvider : DispatcherProvider {
    override fun main() = Dispatchers.Main

    override fun io() = Dispatchers.IO

    override fun default() = Dispatchers.Default

    override fun unconfined() = Dispatchers.Unconfined
}