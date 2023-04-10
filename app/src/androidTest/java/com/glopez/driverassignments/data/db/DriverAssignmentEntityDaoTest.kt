package com.glopez.driverassignments.data.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.glopez.driverassignments.data.model.DriverAssignmentEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class DriverAssignmentEntityDaoTest {
    private lateinit var driverAssignmentDb: DriverAssignmentDatabase
    private lateinit var driverAssignmentDao: DriverAssignmentDao

    @Before
    fun initializeDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        driverAssignmentDb = Room.inMemoryDatabaseBuilder(
            context,
            DriverAssignmentDatabase::class.java
        ).build()

        driverAssignmentDao = driverAssignmentDb.driverAssignmentDao()
    }

    @Test
    fun verifyGetDriverAssignmentsWithEmptyTable() = runTest {
        driverAssignmentDao.getDriverAssignments().test {
            val assignments = awaitItem()
            assertTrue(assignments.isEmpty())
        }
    }

    @Test
    fun verifyGetDriverAssignments() = runTest {
        val driverAssignment1 = DriverAssignmentEntity("driver", "shipment", 0)
        val driverAssignment2 = DriverAssignmentEntity("driver2", "shipment2", 1)

        driverAssignmentDao.insertDriverAssignment(driverAssignment1)
        driverAssignmentDao.insertDriverAssignment(driverAssignment2)

        driverAssignmentDao.getDriverAssignments().test {
            val assignments = awaitItem()
            assertEquals(2, assignments.size)
            assertEquals(driverAssignment1.driverName, assignments[0].driverName)
            assertEquals(driverAssignment1.shipmentName, assignments[0].shipmentName)
            assertEquals(driverAssignment2.driverName, assignments[1].driverName)
            assertEquals(driverAssignment2.shipmentName, assignments[1].shipmentName)
        }
    }

    @Test
    fun verifyGetDriverAssignmentById() = runTest {
        val driverAssignment1 = DriverAssignmentEntity("driver", "shipment", 0)
        val driverAssignment2 = DriverAssignmentEntity("driver2", "shipment2", 1)

        driverAssignmentDao.insertDriverAssignment(driverAssignment1)
        driverAssignmentDao.insertDriverAssignment(driverAssignment2)

        val assignment = driverAssignmentDao.getDriverAssignmentById(1)
        assertEquals(assignment.driverName, driverAssignment2.driverName)
        assertEquals(assignment.shipmentName, driverAssignment2.shipmentName)
    }

    @Test
    fun verifyGetDriverAssignmentByIdNotFound() = runTest {
        val assignment = driverAssignmentDao.getDriverAssignmentById(-1)
        assertNull(assignment)
    }

    @Test
    fun verifyGetDriverAssignmentsGetCountWhenEmpty() = runTest {
        val count = driverAssignmentDao.getCount()
        assertEquals(0, count)
    }

    @Test
    fun verifyGetDriverAssignmentsGetCount() = runTest {
        val driverAssignment1 = DriverAssignmentEntity("driver", "shipment", 0)
        val driverAssignment2 = DriverAssignmentEntity("driver2", "shipment2", 1)

        driverAssignmentDao.insertDriverAssignment(driverAssignment1)
        driverAssignmentDao.insertDriverAssignment(driverAssignment2)

        val count = driverAssignmentDao.getCount()
        assertEquals(2, count)
    }

    @After
    fun closeDb() {
        driverAssignmentDb.close()
    }
}