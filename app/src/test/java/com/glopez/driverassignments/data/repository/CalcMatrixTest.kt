package com.glopez.driverassignments.data.repository

import org.junit.Assert.assertEquals
import org.junit.Test

class CalcMatrixTest {

    @Test
    fun `verify matrix is created with supplied number of rows and columns`() {
        val rows = 4
        val columns = 4

        val calcMatrix = CalcMatrix(rows, columns)

        assertEquals(rows, calcMatrix.rows)
        assertEquals(columns, calcMatrix.columns)
    }

    @Test
    fun `verify matrix is filled with values`() {
        val rows = 2
        val columns = 2
        val row1 = listOf(1.4, 2.2).toDoubleArray()
        val row2 = listOf(7.3, 10.5).toDoubleArray()

        val calcMatrix = CalcMatrix(rows, columns)
        calcMatrix.fillRowValues(0, row1)
        calcMatrix.fillRowValues(1, row2)

        calcMatrix.getRow(0).forEachIndexed { index, value ->
            assertEquals(row1[index], value, 0.01)
        }
        calcMatrix.getRow(1).forEachIndexed { index, value ->
            assertEquals(row2[index], value, 0.01)
        }
    }

    @Test
    fun `verify retrieval of matrix value with given coordinates`() {
        val rows = 2
        val columns = 2
        val row1 = listOf(1.4, 2.2).toDoubleArray()
        val row2 = listOf(7.3, 10.5).toDoubleArray()

        val calcMatrix = CalcMatrix(rows, columns)
        calcMatrix.fillRowValues(0, row1)
        calcMatrix.fillRowValues(1, row2)

        assertEquals(row1[0], calcMatrix.getValueAtCoords(0, 0), 0.01)
        assertEquals(row1[1], calcMatrix.getValueAtCoords(0, 1), 0.01)
        assertEquals(row2[0], calcMatrix.getValueAtCoords(1, 0), 0.01)
        assertEquals(row2[1], calcMatrix.getValueAtCoords(1, 1), 0.01)
    }

    @Test
    fun `verify set matrix value at given coordinates`() {
        val rows = 2
        val columns = 2
        val row1 = listOf(1.4, 2.2).toDoubleArray()
        val row2 = listOf(7.3, 10.5).toDoubleArray()
        val calcMatrix = CalcMatrix(rows, columns)
        calcMatrix.fillRowValues(0, row1)
        calcMatrix.fillRowValues(1, row2)

        val newValue = 2.5
        calcMatrix.setValueAtCoords(0, 1, newValue)
        calcMatrix.setValueAtCoords(1, 0, newValue)

        assertEquals(newValue, calcMatrix.getValueAtCoords(0, 1), 0.01)
        assertEquals(newValue, calcMatrix.getValueAtCoords(1, 0), 0.01)
    }
}