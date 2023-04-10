package com.glopez.driverassignments.testutils

import com.glopez.driverassignments.domain.model.DriverAssignment
import com.glopez.driverassignments.domain.repository.ShipmentsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeShipmentsErrorRepository : ShipmentsRepository {

    override fun getDriverAssignments(): Flow<List<DriverAssignment>> {
        return flow {
            throw Exception("There was a problem retrieving assignments.")
        }
    }
}