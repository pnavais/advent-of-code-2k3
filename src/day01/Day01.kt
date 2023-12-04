package day01

import readInput

val NUMBERS = mapOf("one" to "o1e", "two" to "t2o", "three" to "t3e",
    "four" to "f4r", "five" to "f5e", "six" to "s6x",
    "seven" to "s7n", "eight" to "e8t", "nine" to "n9e")

fun part1(input: List<String>): Long  = input.sumOf { s -> "${s.first(Char::isDigit)}${s.last(Char::isDigit)}".toLong() }

fun part2(input: List<String>): Long {

    val processed = input.map {
        s ->
        var m = s
        NUMBERS.forEach { (k,v) -> m = m.replace(k, v) }
        m
    }

    return part1(processed)
}

fun main() {
    val testInput = readInput("day01/day_complete")
    println(part1(testInput))
    println(part2(testInput))
}