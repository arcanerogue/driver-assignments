package com.glopez.driverassignments.data.repository

import com.glopez.driverassignments.data.db.DriverAssignmentDao
import com.glopez.driverassignments.data.model.DriverAssignmentEntity
import com.glopez.driverassignments.domain.model.DriverAssignment
import com.glopez.driverassignments.domain.repository.SeedDataSource
import com.glopez.driverassignments.domain.repository.ShipmentsRepository
import com.glopez.driverassignments.utils.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class DriverAssignmentsRepository @Inject constructor(
    private val seedDataSource: SeedDataSource,
    private val driverAssignmentDao: DriverAssignmentDao,
    scope: CoroutineScope,
    dispatcher: DispatcherProvider
) : ShipmentsRepository {

    init {
        scope.launch(dispatcher.io()) {
            val assignmentsCount = driverAssignmentDao.getCount()
            if (assignmentsCount < 1) {
                val seedData = seedDataSource.parseSeedData()
                val drivers = seedData?.drivers.orEmpty()
                val shipments = seedData?.shipments.orEmpty()

                if (drivers.isNotEmpty() && shipments.isNotEmpty()) {
                    calculateShipmentScores(drivers, shipments)
                }
            }
        }
    }

    override fun getDriverAssignments(): Flow<List<DriverAssignment>> {
        return driverAssignmentDao.getDriverAssignments().map { list ->
            list.map { DriverAssignment(it.driverName, it.shipmentName) }
        }
    }

    private suspend fun calculateShipmentScores(drivers: List<String>, shipments: List<String>) {
        val calcMatrix = getCalculationMatrix(drivers, shipments)
        val calculator = DriverAssignmentCalculator(calcMatrix)
        val coordinates = calculator.solveSuitabilityScoreMatrix()
        saveAssignments(coordinates, drivers, shipments)
    }

    private fun getCalculationMatrix(drivers: List<String>, shipments: List<String>): CalcMatrix {
        val calcMatrix = CalcMatrix(shipments.size, drivers.size)
        shipments.forEachIndexed { index, shipmentName ->
            calcMatrix.fillRowValues(index, calculateScoreForShipment(shipmentName, drivers))
        }
        return calcMatrix
    }

    private suspend fun saveAssignments(
        coordinates: List<Coordinates>,
        drivers: List<String>,
        shipments: List<String>
    ) {
        coordinates.forEachIndexed { index, position ->
            val shipmentName = shipments[position.shipment]
            val driverName = drivers[position.driver]
            val assignment = DriverAssignmentEntity(driverName, shipmentName, index)
            driverAssignmentDao.insertDriverAssignment(assignment)
        }
    }
}