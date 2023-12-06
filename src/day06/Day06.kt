package day06

import readInput

enum class Direction { UP, DOWN }

fun traverse(time: Long, refTime: Long, distance: Long, direction: Direction): Long {
    var isValid = true
    var numWays = 0L
    var startTime = time
    while (isValid) {
        if (startTime.times(refTime.minus(startTime)) > distance) {
            numWays++
            if (direction == Direction.DOWN) { startTime-- } else { startTime++ }
        } else {
            isValid = false
        }
    }
    return numWays
}

fun computeWaysForRace(time : Long, distance: Long): Long {
    val startTime = time.div(2)
    var numWays = traverse(startTime, time, distance, Direction.DOWN)
    numWays += traverse(startTime + 1, time, distance, Direction.UP)
    return numWays
}


private fun computeAllWays(
    times: List<Long>,
    distances: List<Long>
): Long {
    var ways = 1L
    repeat(times.size) {
        ways *= computeWaysForRace(times[it], distances[it])
    }

    return ways
}

fun part1(input: List<String>): Long {
    val times = input.first().split(":")[1].trim().split("\\s+".toRegex()).map(String::toLong)
    val distances = input.last().split(":")[1].trim().split("\\s+".toRegex()).map(String::toLong)

    return computeAllWays(times, distances)
}


fun part2(input: List<String>): Long {
    val times = listOf(input.first().split(":")[1].trim().replace("\\s+".toRegex(), "").toLong())
    val distances = listOf(input.last().split(":")[1].trim().replace("\\s+".toRegex(), "").toLong())

    return computeAllWays(times, distances)
}

fun main() {
    val testInput = readInput("input/day06")
    println(part1(testInput))
    println(part2(testInput))
}