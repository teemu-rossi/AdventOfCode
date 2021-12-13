
fun main() {
    println("Hello World!")

    val values = generateSequence(::readLine)
        .mapNotNull { line -> line.trim().takeUnless { trimmed -> trimmed.isBlank() } }
        .toList()

    var fishies = values.first().split(",").map { it.toInt() }

    repeat(80) {
        fishies = fishies.flatMap { it.go() }
    }

    println(fishies.size)

    val stats = arrayOf(0L, 0L, 0L, 0L,  0L, 0L, 0L, 0L,  0L)
    for (f in values.first().split(",").map { it.toInt() }) {
        stats[f] += 1L
    }

    repeat(256) {
        println(stats.joinToString())
        val new = stats[0]
        val newStats = (stats.drop(1) + 0L).toMutableList()
        newStats[6] += new
        newStats[8] += new

        for (i in newStats.indices) stats[i] = newStats[i]
    }

    println(stats.sum())
}

fun Int.go(): List<Int> = when {
    this > 0 -> listOf(this - 1)
    this == 0 -> listOf(6, 8)
    else -> throw IllegalArgumentException("Bad fishie $this")
}
