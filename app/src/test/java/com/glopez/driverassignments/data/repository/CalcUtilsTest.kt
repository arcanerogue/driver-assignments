package com.glopez.driverassignments.data.repository

import org.junit.Assert.*
import org.junit.Test

class CalcUtilsTest {
    @Test
    fun `verify isEven is true with even input`() {
        var input = 2
        var result = isEven(input)
        assertTrue(result)

        input = 102
        result = isEven(input)
        assertTrue(result)

        input = 0
        result = isEven(input)
        assertTrue(result)

        input = 1234532
        result = isEven(input)
        assertTrue(result)
    }

    @Test
    fun `verify isEven is false with odd input`() {
        var input = 1
        var result = isEven(input)
        assertFalse(result)

        input = 503
        result = isEven(input)
        assertFalse(result)

        input = 2235
        result = isEven(input)
        assertFalse(result)
    }

    @Test
    fun `verify isEven with negative value input`() {
        var input = -2
        var result = isEven(input)
        assertFalse(result)

        input = -1
        result = isEven(input)
        assertFalse(result)
    }

    @Test
    fun `verify numberOfValues result`() {
        var input = "Testing for vowels" // 5
        var count = numberOfVowels(input)
        assertEquals(5, count)

        input = "AAEEIIOOUU" // 10
        count = numberOfVowels(input)
        assertEquals(10, count)

        input = "Joel Miller" // 4
        count = numberOfVowels(input)
        assertEquals(4, count)
    }

    @Test
    fun `verify numberOfVowels ignores special characters`() {
        var input = "n0 v0w3ls" // 0
        var count = numberOfVowels(input)
        assertEquals(0, count)

        input = "!!!***$$$" // 0
        count = numberOfVowels(input)
        assertEquals(0, count)
    }

    @Test
    fun `verify numberOfVowels ignores white space`() {
        var input = "    " // 0
        var count = numberOfVowels(input)
        assertEquals(0, count)

        input = "" // 0
        count = numberOfVowels(input)
        assertEquals(0, count)
    }

    @Test
    fun `verify numberOfVowels ignores digits`() {
        var input = "n0 v0w3ls" // 0
        var count = numberOfVowels(input)
        assertEquals(0, count)

        input = "1234567890" // 0
        count = numberOfVowels(input)
        assertEquals(0, count)

        input = "-1" // 0
        count = numberOfVowels(input)
        assertEquals(0, count)
    }

    @Test
    fun `verify numberOfConsonants returns count`() {
        var input = "cdfgh" // 5
        var count = numberOfConsonants(input)
        assertEquals(5, count)

        input = "AAEEIIOOUU" // 0
        count = numberOfConsonants(input)
        assertEquals(0, count)

        input = "Ellie Williams" // 7
        count = numberOfConsonants(input)
        assertEquals(7, count)
    }

    @Test
    fun `verify numberOfConsonants ignores special characters`() {
        var input = "C0n#on@nts" // 6
        var count = numberOfConsonants(input)
        assertEquals(6, count)

        input = "!!!***$$$" // 0
        count = numberOfConsonants(input)
        assertEquals(0, count)
    }

    @Test
    fun `verify numberOfConsonants ignores white space`() {
        var input = "    " // 0
        var count = numberOfConsonants(input)
        assertEquals(0, count)

        input = "" // 0
        count = numberOfConsonants(input)
        assertEquals(0, count)
    }

    @Test
    fun `verify numberOfConsonants ignores digits`() {
        var input = "n0 v0w3ls" // 5
        var count = numberOfConsonants(input)
        assertEquals(5, count)

        input = "1234567890" // 0
        count = numberOfConsonants(input)
        assertEquals(0, count)

        input = "-1" // 0
        count = numberOfConsonants(input)
        assertEquals(0, count)
    }

    @Test
    fun `verify suitability score`() {
        val driverName = "Everardo Welch"
        val shipments = listOf(
            "215 Osinski Manors",
            "9856 Marvin Stravenue",
            "7127 Kathlyn Ferry",
            "")

        val result1 = getSuitabilityScore(driverName, shipments[0])
        val result2 = getSuitabilityScore(driverName, shipments[1])
        val result3 = getSuitabilityScore(driverName, shipments[2])
        val result4 = getSuitabilityScore(driverName, shipments[3])

        assertEquals(11.25, result1, 0.01)
        assertEquals(12.0, result2, 0.01)
        assertEquals(11.25, result3, 0.01)
        assertEquals(0.0, result4, 0.01)
    }

    @Test
    fun `verify suitability score with empty driver returns zero`() {
        val driverName = ""
        val shipments = listOf(
            "215 Osinski Manors",
            "9856 Marvin Stravenue",
            "7127 Kathlyn Ferry",
            "")

        val result1 = getSuitabilityScore(driverName, shipments[0])
        val result2 = getSuitabilityScore(driverName, shipments[1])
        val result3 = getSuitabilityScore(driverName, shipments[2])
        val result4 = getSuitabilityScore(driverName, shipments[3])

        assertEquals(0.0, result1, 0.01)
        assertEquals(0.0, result2, 0.01)
        assertEquals(0.0, result3, 0.01)
        assertEquals(0.0, result4, 0.01)
    }

    @Test
    fun `verify shipment scores for drivers are calculated`() {
        val shipment = "7127 Kathlyn Ferry"
        val drivers = listOf(
            "Everardo Welch",
            "Orval Mayert",
            "Howard Emmerich")

        val result = calculateScoreForShipment(shipment, drivers)

        assertEquals(11.25, result[0], 0.01)
        assertEquals(9.0, result[1], 0.01)
        assertEquals(11.25, result[2], 0.01)
    }

    @Test
    fun `verify calculations for empty drivers return empty list`() {
        val shipment = "7127 Kathlyn Ferry"

        val result = calculateScoreForShipment(shipment, emptyList())

        assertTrue(result.isEmpty())
    }
}