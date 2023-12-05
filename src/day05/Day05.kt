package day05

import readInput
import kotlin.math.min

class MapType {
    private val ranges = mutableListOf<MapRange>()

    fun addRange(mapRange: MapRange) = ranges.add(mapRange)

    fun translate(sourceIndex: Long) : Long {
        var targetIndex: Long? = sourceIndex
        for (r in ranges) {
            val aux = r.computeIndex(targetIndex!!)
            if (aux != null) {
                targetIndex = aux
                break
            }
        }
        return targetIndex ?: sourceIndex
    }
}

data class MapRange(val targetStart: Long, val sourceStart: Long, val length: Long) {
    fun computeIndex(sourceIndex: Long): Long? {
        var  targetIndex: Long? = null
        // Check source index is mappable
        if (sourceIndex in sourceStart until sourceStart+length) {
            targetIndex = sourceIndex - (sourceStart - targetStart)
        }
        return targetIndex
    }
}

private fun readMapTypes(input: List<String>): List<MapType> {
    val mapTypes = mutableListOf<MapType>()
    var currentMapType : MapType? = null

    input.drop(1).forEach { s ->
        if (s.contains(":")) {
            if (currentMapType != null) { mapTypes.add(currentMapType!!) }
            currentMapType = MapType()
        } else if (s.isNotBlank()) {
            val (targetStart, sourceStart, length) = s.split("\\s+".toRegex()).map(String::toLong)
            currentMapType!!.addRange(MapRange(targetStart, sourceStart, length))
        }
    }

    if (currentMapType != null) { mapTypes.add(currentMapType!!) }

    return mapTypes
}

private fun locationForSeed(seed: Long, mapTypes: List<MapType>): Long {
    var location = seed

    mapTypes.forEach { m ->
       location = m.translate(location)
    }

    return location
}

fun part1(input: List<String>): Long {
    val mapTypes = readMapTypes(input)
    val seeds = input.first().split(":")[1].trim().split("\\s+".toRegex()).map(String::toLong)
    return seeds.minOf { locationForSeed(it, mapTypes) }
}

fun part2(input: List<String>): Long {
    val mapTypes = readMapTypes(input)
    val seeds = input.first().split(":")[1].trim().split("\\s+".toRegex()).map(String::toLong)

    var location: Long = Long.MAX_VALUE
    seeds.chunked(2).forEachIndexed { i, l ->
        val (seedStart, length) = l
        println("Chunk [${i+1}/${seeds.size/2}] $seedStart with length $length")
        for (seed in seedStart until seedStart + length) {
            location = min(location, locationForSeed(seed, mapTypes))
        }
    }

    return location
}

fun main() {
    val testInput = readInput("input/day05_min")
    println(part1(testInput))
    println(part2(testInput))
}