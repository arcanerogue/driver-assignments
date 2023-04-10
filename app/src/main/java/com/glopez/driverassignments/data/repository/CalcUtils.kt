package com.glopez.driverassignments.data.repository

import java.util.*
import kotlin.math.abs

fun isEven(input: Int): Boolean {
    if (input < 0) return false
    return input % 2 == 0
}

fun numberOfVowels(input: String): Int {
    val inputWithNoDigits = input.filter { it.isLetter() }
    var count = 0
    for (char in inputWithNoDigits.lowercase(Locale.US).toCharArray()) {
        when (char) {
            'a', 'e', 'i', 'o', 'u' -> count++
        }
    }
    return count
}

fun numberOfConsonants(input: String): Int {
    val inputWithNoDigits = input.filter { it.isLetter() }
    val numVowels = numberOfVowels(inputWithNoDigits)
    return inputWithNoDigits.length - numVowels
}

fun doubleEquals(value1: Double, value2: Double): Boolean {
    val diff = abs(value1 - value2)
    return diff < 0.01
}

fun calculateScoreForShipment(shipmentName: String, driverNames: List<String>): DoubleArray {
    val scores: MutableList<Double> = mutableListOf()
    driverNames.forEach { driverName ->
        val score = getSuitabilityScore(driverName, shipmentName)
        scores.add(score)
    }
    return scores.toDoubleArray()
}

fun getSuitabilityScore(driverName: String, shipmentAddress: String): Double {
    val length = shipmentAddress.length
    var baseSS = when {
        length == 0 -> 0.0
        isEven(length) -> numberOfVowels(driverName) * 1.5
        else -> numberOfConsonants(driverName) * 1.0
    }

    val factors = hasCommonFactors(
        getFactors(driverName.length),
        getFactors(shipmentAddress.length)
    )
    if (factors > 1) {
        baseSS += (baseSS * 0.5)
    }
    return baseSS
}

private fun hasCommonFactors(
    driverNameFactors: MutableList<Int>,
    shipmentAddressFactors: MutableList<Int>
): Int {
    var numberOfFactors = 0
    val commonFactorsCount = HashMap<Int, Int>()
    driverNameFactors.addAll(shipmentAddressFactors)
    driverNameFactors.sort()

    for (value: Int in driverNameFactors) {
        if (commonFactorsCount.containsKey(value)) {
            val count = commonFactorsCount[value]?.plus(1) ?: 1
            commonFactorsCount[value] = count
        } else {
            commonFactorsCount[value] = 1
        }
    }

    commonFactorsCount.values.forEach { count ->
        if (count > 1)
            numberOfFactors++
    }

    return numberOfFactors
}

private fun getFactors(input: Int): MutableList<Int> {
    val factors = mutableListOf<Int>()
    if (input < 0)
        return factors

    for (value in 1..input) {
        if (input % value == 0) {
            factors.add(value)
        }
    }
    return factors
}

