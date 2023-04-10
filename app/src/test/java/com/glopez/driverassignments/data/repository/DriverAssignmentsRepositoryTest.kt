package com.glopez.driverassignments.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.glopez.driverassignments.data.model.DriverAssignmentEntity
import com.glopez.driverassignments.data.model.SeedDataModel
import com.glopez.driverassignments.domain.repository.SeedDataSource
import com.glopez.driverassignments.testutils.FakeDriverAssignmentDao
import com.glopez.driverassignments.testutils.FakeSeedDataSource
import com.glopez.driverassignments.testutils.TestDispatcherProvider
import com.glopez.driverassignments.testutils.TestDispatcherRule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DriverAssignmentsRepositoryTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineTestRule = TestDispatcherRule()

    private val driverAssignmentDao = FakeDriverAssignmentDao()
    private lateinit var driverAssignmentRepository: DriverAssignmentsRepository

    private val drivers = listOf("Everardo Welch", "Orval Mayert", "Howard Emmerich")
    private val shipments = listOf("215 Osinski Manors", "9856 Marvin Stravenue", "7127 Kathlyn Ferry")
    private val seedDataSource = FakeSeedDataSource(drivers, shipments)

    private fun getDriverAssignmentRepo(
        seedDataSource: SeedDataSource,
        scope: CoroutineScope
    ) = DriverAssignmentsRepository(
        seedDataSource,
        driverAssignmentDao,
        scope,
        TestDispatcherProvider()
    )

    @Test
    fun `verify getDriverAssignments calculates and emits assignments after seeding data`() = runTest {
        advanceUntilIdle()
        driverAssignmentRepository = getDriverAssignmentRepo(seedDataSource, this)

        driverAssignmentRepository.getDriverAssignments().test {
            val assignments = awaitItem()
            cancelAndConsumeRemainingEvents()
            assertEquals(3, assignments.size)
        }
    }

    @Test
    fun `verify getDriverAssignments emits when data present`() = runTest {
        advanceUntilIdle()
        driverAssignmentDao.insertDriverAssignment(
            DriverAssignmentEntity("Izaiah Lowe", "987 Champlin Lake", 0)
        )
        driverAssignmentDao.insertDriverAssignment(
            DriverAssignmentEntity("Monica Hermann", "63187 Volkman Garden Suite 447", 1)
        )

        driverAssignmentRepository = getDriverAssignmentRepo(seedDataSource, this)

        driverAssignmentRepository.getDriverAssignments().test {
            val assignments = awaitItem()
            cancelAndConsumeRemainingEvents()
            assertEquals(2, assignments.size)
        }
    }

    @Test
    fun `verify getDriverAssignments emits when data empty`() = runTest {
        advanceUntilIdle()
        val seedEmptyDataSource = FakeSeedDataSource(emptyList(), emptyList())

        driverAssignmentRepository = getDriverAssignmentRepo(seedEmptyDataSource, this)

        driverAssignmentRepository.getDriverAssignments().test {
            val assignments = awaitItem()
            cancelAndConsumeRemainingEvents()
            assertEquals(0, assignments.size)
        }
    }

    @Test
    fun `verify getDriverAssignments emits when seed drivers and shipments null`() = runTest {
        advanceUntilIdle()
        val seedEmptyDataSource = FakeSeedDataSource(null, null)

        driverAssignmentRepository = getDriverAssignmentRepo(seedEmptyDataSource, this)

        driverAssignmentRepository.getDriverAssignments().test {
            val assignments = awaitItem()
            cancelAndConsumeRemainingEvents()
            assertEquals(0, assignments.size)
        }
    }

    @Test
    fun `verify getDriverAssignments emits when seed drivers null`() = runTest {
        advanceUntilIdle()
        val seedEmptyDataSource = FakeSeedDataSource(null, shipments)

        driverAssignmentRepository = getDriverAssignmentRepo(seedEmptyDataSource, this)

        driverAssignmentRepository.getDriverAssignments().test {
            val assignments = awaitItem()
            cancelAndConsumeRemainingEvents()
            assertEquals(0, assignments.size)
        }
    }

    @Test
    fun `verify getDriverAssignments emits when seed shipments null`() = runTest {
        advanceUntilIdle()
        val seedEmptyDataSource = FakeSeedDataSource(drivers, null)

        driverAssignmentRepository = getDriverAssignmentRepo(seedEmptyDataSource, this)

        driverAssignmentRepository.getDriverAssignments().test {
            val assignments = awaitItem()
            cancelAndConsumeRemainingEvents()
            assertEquals(0, assignments.size)
        }
    }

    @Test
    fun `verify getDriverAssignments emits when seed parsing returns null`() = runTest {
        advanceUntilIdle()
        val seedNullDataSource = object : SeedDataSource {
            override fun parseSeedData(): SeedDataModel? {
                return null
            }
        }

        driverAssignmentRepository = getDriverAssignmentRepo(seedNullDataSource, this)

        driverAssignmentRepository.getDriverAssignments().test {
            val assignments = awaitItem()
            cancelAndConsumeRemainingEvents()
            assertEquals(0, assignments.size)
        }
    }
}