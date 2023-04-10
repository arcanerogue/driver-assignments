package com.glopez.driverassignments.data.repository

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class DriverAssignmentCalculatorTest {

    @Test
    fun `verify driver assignments with 4x4 matrix`() {
        val expected = listOf(
            Coordinates(0,2),
            Coordinates(1,0),
            Coordinates(2,1),
            Coordinates(3,3),
        )
        val drivers = listOf(
            "Everardo Welch",
            "Orval Mayert",
            "Howard Emmerich",
            "987 Champlin Lake",)

        val shipments = listOf(
            "215 Osinski Manors",
            "9856 Marvin Stravenue",
            "7127 Kathlyn Ferry",
            "Izaiah Lowe")

        val calcMatrix = CalcMatrix(shipments.size, drivers.size)
        shipments.forEachIndexed { index, shipmentName ->
            calcMatrix.fillRowValues(index, calculateScoreForShipment(shipmentName, drivers))
        }
        val calculator = DriverAssignmentCalculator(calcMatrix)
        val solution = calculator.solveSuitabilityScoreMatrix()

        assertEquals(expected[0], solution[0])
        assertEquals(expected[1], solution[1])
        assertEquals(expected[2], solution[2])
        assertEquals(expected[3], solution[3])
    }

    @Test
    fun `verify driver assignment calculator throws when supplied with non-square matrix`() {
        val drivers = listOf(
            "Everardo Welch",
            "Orval Mayert",
            "Howard Emmerich",
            "Izaiah Lowe")

        val shipments = listOf(
            "215 Osinski Manors",
            "9856 Marvin Stravenue",
            "7127 Kathlyn Ferry")

        val calcMatrix = CalcMatrix(shipments.size, drivers.size)
        shipments.forEachIndexed { index, shipmentName ->
            calcMatrix.fillRowValues(index, calculateScoreForShipment(shipmentName, drivers))
        }

        assertThrows(IllegalArgumentException::class.java) { DriverAssignmentCalculator(calcMatrix) }
    }
}