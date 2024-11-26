package org.example

import io.retable.Retable
import io.retable.RetableRecord
import java.io.File

val table1 = "Table1.xlsx"
val table2 = "Table2.xlsx"
val table3 = "Table3.xlsx"
val table4 = "Table4.xlsx"
val table5 = "Table5.xlsx"

fun main() {
    println("Table 1:")
    println(getVelocityErrorArray(table1).toList())
    println(getVelocityArray(table1).toList())
    println()
    println("Table 2:")
    println(getVelocityErrorArray(table2).toList())
    println(getVelocityArray(table2).toList())
    println()
    println("Table 3:")
    println(getVelocityErrorArray(table3).toList())
    println(getVelocityArray(table3).toList())
    println()
    println("Table 4:")
    println(getVelocityErrorArray(table4).toList())
    println(getVelocityArray(table4).toList())
    println()
    println("Table 5:")
    println(getVelocityErrorArray(table5).toList())
    println(getVelocityArray(table5).toList())
}

fun getPosition(measurement: Int, path: String): Double {
    File(path).inputStream().use {
        val retable = Retable.excel().read(it)
        val rows = retable.records.toList()
        return rows[measurement-1].rawData[2].toDouble()
    }
}

fun getTime(measurement: Int, path: String): Double {
    File(path).inputStream().use {
        val retable = Retable.excel().read(it)
        val rows = retable.records.toList()
        return rows[measurement-1].rawData[1].toDouble()
    }
}

fun calcVelocity(measurement: Int, path: String): Double {
    val deltaTime = getTime(measurement+1, path) - getTime(measurement-1, path)
    val deltaPos = getPosition(measurement+1, path) - getPosition(measurement-1, path)
    return deltaPos / deltaTime
}

fun getVelocityArray(path: String): DoubleArray {
    val array = DoubleArray(9)
    for (i in 2..8) {
        array[i-1] = calcVelocity(i, path)
    }
    return array
}

const val deltaTimeErrorMultiplier = 0.15
const val deltaXError = 0.001

fun calcVelocityError(measurement: Int, path: String): Double {
    val deltaTime = getTime(measurement+1, path) - getTime(measurement-1, path)
    val deltaPos = getPosition(measurement+1, path) - getPosition(measurement-1, path)
    val velocity = calcVelocity(measurement, path)
    val deltaTimeError = deltaTimeErrorMultiplier * deltaTime/2.0

    return velocity * (deltaXError/deltaPos + deltaTimeError/(deltaTime/2.0))
}

fun getVelocityErrorArray(path: String): DoubleArray {
    val array = DoubleArray(9)
    for (i in 2..8) {
        array[i-1] = calcVelocityError(i, path)
    }
    return array
}