import kotlin.math.abs

fun main() {
    val values = generateSequence(::readLine)
        .mapNotNull { line -> line.trim().takeUnless { trimmed -> trimmed.isBlank() } }
        .toList()

    val crabs = values.first().split(",").map { it.toInt() }

    val costsForPositions = (0..crabs.maxOrNull()!!).map {
        var cost = 0
        for (c in crabs) {
            cost += abs(c - it).cost
        }
        cost
    }

    println(costsForPositions.mapIndexed { index, value -> index to value }.minByOrNull { it.second })
}

val Int.cost: Int get() = this * (this + 1) / 2
//    get() {
//        return if (this <= 1) 1 else {
//            (this - 1).cost + this - 1
//        }
//    }