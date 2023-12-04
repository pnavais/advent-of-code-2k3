package day04

import readInput
import java.util.Stack
import kotlin.math.pow

private fun getMatches(s: String): Int {
    val (n, w) = s.split(":")[1].split("|").map { i -> i.trim().split("\\s+".toRegex()).map(String::toInt) }
    val winning = w.toSet()
    return n.map { i -> i to winning.contains(i) }.count { p -> p.second }
}

fun part1(input: List<String>): Int {
    return input.sumOf { s ->
        val matches = getMatches(s)
        if (matches > 0) {
            2.0.pow(matches.minus(1)).toInt()
        } else {
            0
        }
    }
}

fun part2(input: List<String>): Int {
    val connectionMap = input.associate { s ->
        val cardId = s.split(":")[0].split("\\s+".toRegex())[1].toInt()
        val matches = getMatches(s)
        val connections = mutableListOf<Int>()
        for (i in cardId + 1 until cardId + 1 + matches) {
            connections.add(i)
        }
        cardId to connections
    }

    var totalSratchCards = 0
    val stack = Stack<Int>()

    connectionMap.keys.forEach { stack.push(it); }

    while (stack.isNotEmpty()) {
        val currentCardId = stack.pop()
        totalSratchCards++
        connectionMap[currentCardId]?.forEach { stack.push(it) }
    }

    return totalSratchCards
}

fun main() {
    val testInput = readInput("input/day04")
    println(part1(testInput))
    println(part2(testInput))
}