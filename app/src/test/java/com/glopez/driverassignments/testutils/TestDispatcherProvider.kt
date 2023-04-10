package com.glopez.driverassignments.testutils

import com.glopez.driverassignments.utils.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class TestDispatcherProvider : DispatcherProvider {
    val testDispatcher = UnconfinedTestDispatcher()

    override fun main(): CoroutineDispatcher = testDispatcher

    override fun io(): CoroutineDispatcher = testDispatcher

    override fun default(): CoroutineDispatcher  = testDispatcher

    override fun unconfined(): CoroutineDispatcher  = testDispatcher
}