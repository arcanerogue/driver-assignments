package com.glopez.driverassignments.domain.repository

import com.glopez.driverassignments.domain.model.DriverAssignment
import kotlinx.coroutines.flow.Flow

interface ShipmentsRepository {
    fun getDriverAssignments(): Flow<List<DriverAssignment>>
}