package day07

import readInput

val cardValueMapping = mapOf('2' to 2,  '3' to 3,  '4' to 4, '5' to 5,  '6' to 6,  '7' to 7, '8' to 8,
                             '9' to 9, 'T' to 10, 'J' to 11, 'Q' to 12, 'K' to 13, 'A' to 14)

val cardValueMappingJoker = cardValueMapping + mapOf('J' to 1)

enum class Type(val value: Int) {
    FIVE_OF_A_KIND(7), FOUR_OF_A_KIND(6), FULL_HOUSE(5), THREE_OF_A_KIND(4), TWO_PAIR(3), ONE_PAIR(2), HIGH_CARD(1), WRONG_CARDS(0);

    companion object {
        fun fromCards(cards: String, useJoker: Boolean = false) : Type {
            val cardsProcessed = if (useJoker) { translateJokers(cards) } else cards
            val cardGroups = cardsProcessed.groupingBy { it }.eachCount()
            return when (cardGroups.size) {
                5 -> HIGH_CARD
                4 -> ONE_PAIR
                3 -> if (cardGroups.maxBy { it.value }.value == 3) THREE_OF_A_KIND else TWO_PAIR // 3, 1, 1 or 2, 2, 1
                2 -> if (cardGroups.maxBy { it.value }.value == 4) FOUR_OF_A_KIND else FULL_HOUSE // 4, 1 or 3, 2
                1 -> FIVE_OF_A_KIND
                else -> WRONG_CARDS
            }
        }

        private fun translateJokers(cards: String): String {
            return if (cards != "JJJJJ") {
                // Transform jokers into the highest grouped card
                val maxCard = cards.replace("J", "").groupingBy { it }.eachCount()
                    .map { it.value to it.key }.groupBy { it.first }.maxBy { it.key }
                    .value
                    .map { it.second }
                    .sortedWith(Comparator { o1, o2 ->
                        cardValueMappingJoker[o2]?.compareTo(
                            cardValueMappingJoker[o1] ?: -1
                        )!!
                    })[0]
                cards.replace('J', maxCard)
            } else { cards }
        }
    }
}

data class Hand(val cards: String, val bid: Long, val useJokers: Boolean = false) : Comparable<Hand> {
    private val type : Type = Type.fromCards(cards, useJokers)

    override fun compareTo(other: Hand): Int {
        var res = type.compareTo(other.type)

        // Compare equal hands by pos value
        if (res == 0) {
            val cardMapper = if (useJokers) { cardValueMappingJoker } else { cardValueMapping }
            for (i in 0..other.cards.length) {
                val aux = cardMapper[other.cards[i]]?.compareTo(cardMapper[cards[i]]?:-1)!!
                if (aux != 0) {
                    res = aux
                    break
                }
            }
        }

        return res
    }
}

fun part1(input: List<String>): Long {
    return input.map { s -> val (cards, bid) = s.split(" "); Hand(cards, bid.toLong()) }
        .sortedDescending()
        .mapIndexed { index,hand -> hand.bid.times(index+1) }
        .sum()
}

fun part2(input: List<String>): Long {
    return input.map { s -> val (cards, bid) = s.split(" "); Hand(cards, bid.toLong(), useJokers = true) }
        .sortedDescending()
        .mapIndexed { index,hand -> hand.bid.times(index+1) }
        .sum()
}

fun main() {
    val testInput = readInput("input/day07")
    println(part1(testInput))
    println(part2(testInput))
}