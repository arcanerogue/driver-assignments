package com.glopez.driverassignments.data.repository

/**
 * Represents the suitability score matrix used in DriverAssignmentsCalculator
 * @param rows number of rows in the matrix
 * @param columns number of columns in the matrix
 */
class CalcMatrix(val rows: Int, val columns: Int) {
    private val matrix = Array(rows) { DoubleArray(columns)}

    fun fillRowValues(row: Int, values: DoubleArray) {
        matrix[row] = values
    }

    fun getRow(row: Int): DoubleArray {
        return matrix[row]
    }

    fun getValueAtCoords(row: Int, column: Int): Double {
        return matrix[row][column]
    }

    fun setValueAtCoords(row: Int, column: Int, value: Double) {
        matrix[row][column] = value
    }
}