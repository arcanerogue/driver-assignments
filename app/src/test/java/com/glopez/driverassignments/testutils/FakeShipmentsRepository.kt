package com.glopez.driverassignments.testutils

import com.glopez.driverassignments.domain.model.DriverAssignment
import com.glopez.driverassignments.domain.repository.ShipmentsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeShipmentsRepository : ShipmentsRepository {
    private val fakeAssignments: ArrayList<DriverAssignment> = ArrayList()

    fun populateAssignments(assignments: List<DriverAssignment>) {
        fakeAssignments.addAll(assignments)
    }

    override fun getDriverAssignments(): Flow<List<DriverAssignment>> {
        return flow {
            delay(500)
            emit(fakeAssignments)
        }
    }
}