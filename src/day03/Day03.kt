package day03

import readInput


class EngineSchematic {
    val rows: MutableList<List<Char>> = mutableListOf()
}

private fun readMatrix(input: List<String>) : EngineSchematic {
    val engineSchematic = EngineSchematic()
    input.forEach { s ->
        engineSchematic.rows.add(s.toList())
    }

    return engineSchematic
}

private fun hasElementNearby(engineSchematic: EngineSchematic, i: Int, j: Int, checker: (row: Int, col: Int) -> Boolean): Boolean {
    var hasSymbol = false
    for (row in i-1..i+1) {
        for (col in j-1..j+1) {
            if (row in 0 until engineSchematic.rows.size && col in 0 until engineSchematic.rows[row].size) {
                hasSymbol = hasSymbol || checker(row, col)
            }
        }
    }
    return hasSymbol
}

private fun hasSymbolNearby(engineSchematic: EngineSchematic, i: Int, j: Int): Boolean {
    return hasElementNearby(engineSchematic, i, j) { r, c -> !engineSchematic.rows[r][c].isDigit()  && engineSchematic.rows[r][c] != '.' }
}

private fun hasGearNearby(engineSchematic: EngineSchematic, i: Int, j: Int): Pair<Boolean, String> {
    var gearPos = ""
    return hasElementNearby(engineSchematic, i, j) { r, c ->
        if (engineSchematic.rows[r][c] == '*') {
            gearPos = "${r}_${c}"
        }; engineSchematic.rows[r][c] == '*'
    } to gearPos
}

private fun saveGear(
    gearMap: MutableMap<String, MutableList<Long>>,
    gearPos: String,
    currentNumber: String
) {
    if (!gearMap.containsKey(gearPos)) {
        gearMap[gearPos] = mutableListOf()
    }
    gearMap[gearPos]!!.add(currentNumber.toLong())
}

fun part1(input: List<String>): Long {
    val engineSchematic = readMatrix(input)
    var total = 0L
    for (i in 0 until  engineSchematic.rows.size) {
        var currentNumber = ""
        var isValid = false
        for (j in 0 until engineSchematic.rows[i].size) {
            if (engineSchematic.rows[i][j].isDigit()) {
                currentNumber += engineSchematic.rows[i][j]
                // Check symbol presence in all directions
                isValid = isValid || hasSymbolNearby(engineSchematic, i, j)
            } else {
                if (currentNumber.isNotBlank() && isValid) {
                    total += currentNumber.toLong()
                }
                currentNumber = ""
                isValid = false
            }
            if (j == engineSchematic.rows[i].size-1 && currentNumber.isNotBlank() && isValid) {
                total += currentNumber.toLong()
            }
        }
    }
    return total
}

fun part2(input: List<String>): Long {
    val gearMap = linkedMapOf<String, MutableList<Long>>()
    val engineSchematic = readMatrix(input)
    for (i in 0 until  engineSchematic.rows.size) {
        var currentNumber = ""
        var isValid = false
        var gearPos = ""
        for (j in 0 until engineSchematic.rows[i].size) {
            if (engineSchematic.rows[i][j].isDigit()) {
                currentNumber += engineSchematic.rows[i][j]
                // Check symbol presence in all directions
                val (hasGear, gearPosAux) = hasGearNearby(engineSchematic, i, j)
                isValid = isValid || hasGear
                if (hasGear) {
                    gearPos = gearPosAux
                }
            } else {
                if (currentNumber.isNotBlank() && isValid) {
                    saveGear(gearMap, gearPos, currentNumber)
                }
                currentNumber = ""
                isValid = false
            }
            if (j == engineSchematic.rows[i].size-1 && currentNumber.isNotBlank() && isValid) {
                saveGear(gearMap, gearPos, currentNumber)
            }
        }
    }
    return gearMap.filterValues { v -> v.size == 2 }.map { (_,v) -> v.reduce { a, b -> a * b } }.sum()
}

fun main() {
    val testInput = readInput("input/day03")
    println(part1(testInput))
    println(part2(testInput))
}