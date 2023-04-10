package com.glopez.driverassignments.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.glopez.driverassignments.testutils.FakeShipmentsRepository
import com.glopez.driverassignments.testutils.LiveDataTestObserver
import com.glopez.driverassignments.testutils.TestDispatcherRule
import com.glopez.driverassignments.domain.model.DriverAssignment
import com.glopez.driverassignments.testutils.FakeShipmentsErrorRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsInstanceOf
import org.junit.After
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DriversViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineTestRule = TestDispatcherRule()

    private val driverAssignmentObserver = LiveDataTestObserver<UiState<List<DriverAssignment>>>()
    private val shipmentsRepository = FakeShipmentsRepository()
    private lateinit var driversViewModel: DriversViewModel

    @Test
    fun `verify UiState Success when fetching assignments`() = runTest {
        val driverName1 = "Joel Miller"
        val driverName2 = "Ellie Williams"
        val shipmentName1 = "1234 Austin"
        val shipmentName2 = "5678 Jackson"
        val assignments = listOf(
            DriverAssignment(driverName1, shipmentName1),
            DriverAssignment(driverName2, shipmentName2))
        driversViewModel = DriversViewModel(shipmentsRepository)
        driversViewModel.driverAssignments.observeForever(driverAssignmentObserver)
        shipmentsRepository.populateAssignments(assignments)
        advanceUntilIdle()

        val uiState = driverAssignmentObserver.getData()
        val data = uiState?.data

        assertNotNull(data)
        assertThat(uiState, IsInstanceOf(UiState.Success::class.java))
        assertEquals(assignments.size, data?.size)
        assertEquals(driverName1, data?.get(0)?.driverName)
        assertEquals(shipmentName1, data?.get(0)?.shipmentName)
        assertEquals(driverName2, data?.get(1)?.driverName)
        assertEquals(shipmentName2, data?.get(1)?.shipmentName)
    }

    @Test
    fun `verify UiState Loading when empty assignments received`() = runTest {
        driversViewModel = DriversViewModel(shipmentsRepository)
        driversViewModel.driverAssignments.observeForever(driverAssignmentObserver)

        val uiState = driverAssignmentObserver.getData()

        assertNotNull(uiState)
        assertThat(uiState, IsInstanceOf(UiState.Loading::class.java))
    }

    @Test
    fun `verify UiState Error when error encountered during assignment fetch`() = runTest {
        driversViewModel = DriversViewModel(FakeShipmentsErrorRepository())
        driversViewModel.driverAssignments.observeForever(driverAssignmentObserver)
        advanceUntilIdle()

        val data = driverAssignmentObserver.getData()

        assertThat(data, IsInstanceOf(UiState.Error::class.java))
    }

    @After
    fun cleanup() {
        driversViewModel.driverAssignments.removeObserver(driverAssignmentObserver)
    }
}