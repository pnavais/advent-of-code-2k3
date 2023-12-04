package day02

import readInput

const val MAX_RED   = 12L
const val MAX_GREEN = 13L
const val MAX_BLUE  = 14L

enum class Color(val maxNum: Long) {
    RED(MAX_RED), GREEN(MAX_GREEN), BLUE(MAX_BLUE)
}

data class BallInfo(val number: Long, val color: Color) {
    fun isValid() = number <= color.maxNum
}

fun getId(input: String): Long {
    val (gameInfo, subsets) = input.split(":")
    val gameId = gameInfo.split(" ")[1].trim().toLong()
    val isValid = subsets.split(";").none { s ->
        val ballsInfo = s.split(",").any {
            val (n, c) = it.trim().split(" ")
            !BallInfo(n.toLong(), Color.valueOf(c.uppercase())).isValid()
        }
        ballsInfo
    }
    return if (isValid) gameId else 0L
}

fun getPower(input: String): Long {
    val (_, subsets) = input.split(":")
    val totalBalls = mutableListOf<BallInfo>()
    subsets.split(";").forEach { s ->
        totalBalls.addAll(s.split(",").map {
            val (n, c) = it.trim().split(" ")
            BallInfo(n.toLong(), Color.valueOf(c.uppercase()))
        })
    }
    val maxGreen = totalBalls.filter { it.color == Color.GREEN }.maxBy { ballInfo -> ballInfo.number }.number
    val maxRed = totalBalls.filter { it.color == Color.RED }.maxBy { ballInfo -> ballInfo.number }.number
    val maxBlue = totalBalls.filter { it.color == Color.BLUE }.maxBy { ballInfo -> ballInfo.number }.number
    return maxGreen * maxRed * maxBlue
}

fun part1(input: List<String>): Long {
    return input.sumOf(::getId)
}

fun part2(input: List<String>): Long {
    return input.sumOf(::getPower)
}

fun main() {
    val testInput = readInput("input/day02")
    println(part1(testInput))
    println(part2(testInput))
}