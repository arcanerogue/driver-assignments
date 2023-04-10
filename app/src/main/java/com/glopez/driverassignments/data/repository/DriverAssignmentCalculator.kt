package com.glopez.driverassignments.data.repository

import java.util.*
import kotlin.collections.ArrayList

private const val UNSET_VALUE = -1
/**
 * To solve for the maximum suitability score over the set of drivers, I've referenced the
 * Hungarian algorithm which can be explained here: (https://en.wikipedia.org/wiki/Hungarian_algorithm)
 *
 * Another explanation I referenced can be viewed here:
 * https://www.youtube.com/watch?v=cQ5MsiGaDY8&t=450s&ab_channel=CompSci
 *
 * @param scoreMatrix nxn matrix of suitability score values
 */
class DriverAssignmentCalculator(private val scoreMatrix: CalcMatrix) {
    /**
     * Tracks the row elements containing a zero value and stars them. The index corresponds to the
     * row in the scoreMatrix. The value at that index corresponds to the column in the scoreMatrix.
     */
    private val starredRows = IntArray(scoreMatrix.rows) { UNSET_VALUE }

    /**
     * Tracks the column elements containing a zero value and stars them. The index corresponds to the
     * column in the scoreMatrix. The value at that index corresponds to the row in the scoreMatrix.
     */
    private val starredColumns = IntArray(scoreMatrix.columns) { UNSET_VALUE }

    /**
     * Covers a row in the scoreMatrix when determining an optimal solution. The index corresponds to the
     * row in the scoreMatrix. The value at that index corresponds to whether or not that row is covered.
     */
    private val coveredRows = BooleanArray(scoreMatrix.rows) { false }

    /**
     * Covers a column in the scoreMatrix when determining an optimal solution. The index corresponds to the
     * column in the scoreMatrix. The value at that index corresponds to whether or not that column is covered.
     */
    private val coveredColumns = BooleanArray(scoreMatrix.columns) { false }

    /**
     * During column covering and priming of a zero, tracks the row elements containing a zero value and stars
     * them. The index corresponds to the row in the scoreMatrix. The value at that index corresponds to the
     * column in the scoreMatrix.
     */
    private val starredZerosInRow = IntArray(scoreMatrix.rows) { UNSET_VALUE }

    init {
        if (scoreMatrix.rows != scoreMatrix.columns) {
            throw IllegalArgumentException("Rows and columns not equal.")
        }
    }

    /**
     * @return calculates a list of Shipment-to-Driver assignments that maximizes
     * the suitability score over the set of Drivers.
     */
    fun solveSuitabilityScoreMatrix(): List<Coordinates> {
        generateMaximumCostMatrix()
        reduceMatrixRows()
        reduceMatrixColumns()
        coverRowsForOptimalSolutions()
        coverColumnsForOptimalSolutions()

        while (!allColumnsCovered()) {
            var zeroLocation = starZero()
            while (zeroLocation == null) {
                applyLowestValue()
                zeroLocation = starZero()
            }
            if (starredRows[zeroLocation.shipment] == -1) {
                findZeroesNotCoveredAndPrime(zeroLocation)
                coverColumnsForOptimalSolutions()
            } else {
                coveredRows[zeroLocation.shipment] = true
                coveredColumns[starredRows[zeroLocation.shipment]] = false
                applyLowestValue()
            }
        }
        val assignments = ArrayList<Coordinates>()
        starredColumns.forEachIndexed { index, value ->
            assignments.add(Coordinates(index, value))
        }
        return assignments
    }

    private fun allColumnsCovered(): Boolean {
        coveredColumns.forEach { column ->
            if (!column) return false
        }
        return true
    }

    /**
     * Step 1
     * Since it is desired to achieve the maximum cost when applying the Hungarian algorithm,
     * the score matrix will need to be negated by multiplying each value in the matrix by -1.
     * Following that, the maximum absolute value in the matrix is added to every element in the matrix.
     */
    private fun generateMaximumCostMatrix() {
        var maxScore = Double.MIN_VALUE
        for (row in 0 until scoreMatrix.rows) {
            for (column in 0 until scoreMatrix.columns) {
                val currentValue = scoreMatrix.getValueAtCoords(row, column)
                if (currentValue > maxScore) {
                    maxScore = currentValue
                }
                scoreMatrix.setValueAtCoords(row, column, currentValue * -1)
            }
        }

        for (row in 0 until scoreMatrix.rows) {
            for (column in 0 until scoreMatrix.columns) {
                val updatedValue = scoreMatrix.getValueAtCoords(row, column) + maxScore
                scoreMatrix.setValueAtCoords(row, column, updatedValue)
            }
        }
    }


    /**
     * Step 2
     * For each row in the matrix, the minimum element is subtracted from each value in the row.This produces
     * non-negative values and results in some elements containing a zero which will be used to evaluated the
     * problem solution.
     */
    private fun reduceMatrixRows() {
        for (row in 0 until scoreMatrix.rows) {
            var rowMinScore = Double.MAX_VALUE

            for (column in 0 until scoreMatrix.columns) {
                if (scoreMatrix.getValueAtCoords(row, column) < rowMinScore) {
                    rowMinScore = scoreMatrix.getValueAtCoords(row, column)
                }
            }

            for (index in 0 until scoreMatrix.columns) {
                val reducedScore = scoreMatrix.getValueAtCoords(row, index) - rowMinScore
                scoreMatrix.setValueAtCoords(row, index, reducedScore)
            }
        }
    }

    /**
     * Step 3
    * For each column in the matrix, the minimum element is subtracted from each value in the column. This produces
     * non-negative values and results in some elements containing a zero which will be used to evaluated the
     * problem solution.
    */
    private fun reduceMatrixColumns() {
        for (column in 0 until scoreMatrix.columns) {
            var columnMinScore = Double.MAX_VALUE

            for (row in 0 until scoreMatrix.rows) {
                if (scoreMatrix.getValueAtCoords(row, column) < columnMinScore) {
                    columnMinScore = scoreMatrix.getValueAtCoords(row, column)
                }
            }
            for (index in 0 until scoreMatrix.rows) {
                val reducedColumnScore = scoreMatrix.getValueAtCoords(index, column) - columnMinScore
                scoreMatrix.setValueAtCoords(index, column, reducedColumnScore)
            }
        }
    }

    /**
     * Step 4
     * At this step, the Hungarian algorithm requires covering rows and columns containing a zero value. It is
     * desired to mark as few rows and columns as possible. Here the rows are covered.
     */
    private fun coverRowsForOptimalSolutions() {
        val rowContainsStar = BooleanArray(scoreMatrix.rows)
        val columnContainsStar = BooleanArray(scoreMatrix.columns)

        for (row in 0 until scoreMatrix.rows) {
            for (column in 0 until scoreMatrix.columns) {
                if (doubleEquals(scoreMatrix.getValueAtCoords(row, column), 0.0) &&
                    !rowContainsStar[row] &&
                    !columnContainsStar[column]) {

                    rowContainsStar[row] = true
                    columnContainsStar[column] = true
                    starredRows[row] = column
                    starredColumns[column] = row
                    continue
                }
            }
        }
    }

    /**
     * Step 5
     * Covers columns containing a zero in the matrix values.
     */
    private fun coverColumnsForOptimalSolutions() {
        for (i in starredColumns.indices) {
            coveredColumns[i] = starredColumns[i] != -1
        }
    }

    /**
     * Step 6
     * The first zero encountered in the covered rows are now starred with a zero. Only one
     * zero in the row or column can be starred.
     */
    private fun starZero(): Coordinates? {
        for (row in 0 until scoreMatrix.rows) {
            if (!coveredRows[row]) {
                for (column in 0 until scoreMatrix.columns) {
                    if (doubleEquals(scoreMatrix.getValueAtCoords(row, column),0.0) &&
                        !coveredColumns[column]) {
                        starredZerosInRow[row] = column
                        return Coordinates(row, column)
                    }
                }
            }
        }
        return null
    }

    /**
     * Step 7
     * Cover column that contains a starred zero. Once a zero that is not covered is encountered,
     * that element is marked as a prime
     */
    private fun findZeroesNotCoveredAndPrime(zeroLocation: Coordinates) {
        var isZeroFound: Boolean
        var driverCoordinate = zeroLocation.driver

        // Tracks the locations of found zeroes
        val locations = ArrayList<Coordinates>().apply {
            add(zeroLocation)
        }

        do {
            isZeroFound = if (starredColumns[driverCoordinate] != UNSET_VALUE) {
                locations.add(Coordinates(shipment = starredColumns[driverCoordinate], driver = driverCoordinate))
                true
            } else {
                false
            }
            if (!isZeroFound) break

            val shipmentCoordinate = starredColumns[driverCoordinate]
            driverCoordinate = starredZerosInRow[shipmentCoordinate]

            isZeroFound = if (driverCoordinate != -1) {
                locations.add(Coordinates(shipment = shipmentCoordinate, driver = driverCoordinate))
                true
            } else {
                false
            }
        } while (isZeroFound)

        for (coordinate in locations) {
            // remove stars from path
            if (starredColumns[coordinate.driver] == coordinate.shipment) {
                starredRows[coordinate.shipment] = UNSET_VALUE
                starredColumns[coordinate.driver] = UNSET_VALUE
            }
            // replace primes with stars
            if (starredZerosInRow[coordinate.shipment] == coordinate.driver) {
                starredRows[coordinate.shipment] = coordinate.driver
                starredColumns[coordinate.driver] = coordinate.shipment
            }
        }

        // clear rows, columns, and stars
        Arrays.fill(coveredRows, false)
        Arrays.fill(coveredColumns, false)
        Arrays.fill(starredZerosInRow, UNSET_VALUE)
    }

    // Step 8
    /**
     * Find lowest uncovered value and subtract from every unmarked element. Add this value to
     * every element covered by two lines
     */
    private fun applyLowestValue() {
        var lowestUncoveredValue = Double.MAX_VALUE
        for (row in 0 until scoreMatrix.rows) {
            if (coveredRows[row]) {
                continue
            }
            for (column in 0 until scoreMatrix.columns) {
                if (!coveredColumns[column] &&
                    scoreMatrix.getValueAtCoords(row, column) < lowestUncoveredValue) {
                    lowestUncoveredValue = scoreMatrix.getValueAtCoords(row, column)
                }
            }
        }
        if (lowestUncoveredValue > 0) {
            for (row in 0 until scoreMatrix.rows) {
                for (column in 0 until scoreMatrix.columns) {
                    if (coveredRows[row] && coveredColumns[column]) {
                        val increasedScore = scoreMatrix.getValueAtCoords(row, column) + lowestUncoveredValue
                        scoreMatrix.setValueAtCoords(row, column, increasedScore)
                    } else if (!coveredRows[row] && !coveredColumns[column]) {
                        val reducedScore = scoreMatrix.getValueAtCoords(row, column) - lowestUncoveredValue
                        scoreMatrix.setValueAtCoords(row, column, reducedScore)
                    }
                }
            }
        }
    }
}