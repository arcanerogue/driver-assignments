package com.glopez.driverassignments.testutils

import com.glopez.driverassignments.data.db.DriverAssignmentDao
import com.glopez.driverassignments.data.model.DriverAssignmentEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeDriverAssignmentDao: DriverAssignmentDao {
    private val fakeAssignments: ArrayList<DriverAssignmentEntity> = ArrayList()

    override fun getDriverAssignments(): Flow<List<DriverAssignmentEntity>> {
        return flow {
            delay(500)
            emit(fakeAssignments)
        }
    }

    override suspend fun getDriverAssignmentById(id: Int): DriverAssignmentEntity =
        fakeAssignments.find { assignment -> assignment.id == id }
            ?: throw NoSuchElementException("No element found with that id")

    override suspend fun insertDriverAssignment(assignment: DriverAssignmentEntity) {
        fakeAssignments.add(assignment)
    }

    override fun getCount(): Int = fakeAssignments.size
}