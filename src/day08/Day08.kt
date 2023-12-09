package day08

import readInput

class Node(val name: String, var left: Node? = null, var right: Node? = null)

private fun readNetwork(input: List<String>): Map<String, Node> {
    val nodeMap = mutableMapOf<String, Node>()

    input.drop(2).forEach { s ->
        val (currentName, siblings) = s.split("[\\s+]=[\\s+]".toRegex())
        val (leftNode, rightNode) = siblings.replace("(","").replace(")","").split(", ").map { nodeMap.merge(it, Node(name = it)) { n, _ -> n } }
        val currentNode = nodeMap.merge(currentName, Node(name = currentName)) { n, _ -> n }
        //if (!currentNode?.isTerminal()!!) {
            currentNode?.left = leftNode
            currentNode?.right = rightNode
        //}
    }

    return nodeMap
}

fun traverse(movements: String, startNode: Node, stopCondition: (n: Node) -> Boolean): Long {
    var steps = 0L
    var movementIterator = movements.iterator()
    var currentNode : Node? = startNode

    val visitedNodes = mutableSetOf<String>()

    visitedNodes.add(startNode.name)

    while (stopCondition(currentNode!!)) {
        if (!movementIterator.hasNext()) {
            movementIterator = movements.iterator()
        }
        val movement = movementIterator.next()
        currentNode = if (movement == 'L') { currentNode.left } else { currentNode.right }
        steps++
    }

    return steps
}

// Recursive method to return gcd of a and b
fun gcd(a: Long, b: Long): Long {
    if (a == 0L) return b
    return gcd(b % a, a)
}

// method to return LCM of two numbers
fun lcm(a: Long, b: Long): Long {
    return (a / gcd(a, b)) * b
}

fun part1(input: List<String>): Long {
    val movements = input.first()
    val nodeMap = readNetwork(input)

    return traverse(movements, nodeMap["AAA"]!!) { n -> n.name != "ZZZ" }
}

fun part2(input: List<String>): Long {
    val movements = input.first()
    val nodeMap = readNetwork(input)
    var steps = 1L

    // Find all starting nodes
    val startNodes = nodeMap.filterKeys { s ->  s.endsWith('A') }.map { e -> e.value }
    for (node in startNodes) {
         steps = lcm(steps, traverse(movements, node) { n -> !n.name.endsWith("Z") })
    }

    return steps
}

fun main() {
    val testInput = readInput("input/day08")
    //println(part1(testInput))
    println(part2(testInput))
}
