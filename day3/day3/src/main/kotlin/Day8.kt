val digits = mapOf(
    0 to "abcefg",  // 6
    1 to "cf",      // 2
    2 to "acdeg",   // 5
    3 to "acdfg",   // 5
    4 to "bcdf",    // 4
    5 to "abdfg",   // 5
    6 to "abdefg",  // 6
    7 to "acf",     // 3
    8 to "abcdefg", // 7
    9 to "abcdfg"   // 6
)

fun main() {
    val values = generateSequence(::readLine)
        .mapNotNull { line -> line.trim().takeUnless { trimmed -> trimmed.isBlank() } }
        .toList()

    var uniqCount = 0
    var totalSum = 0
    for (line in values) {
        val split = line.split(" ")
        val possibleSignals = split.take(10)
        val valueShown = split.takeLast(4)

        uniqCount += valueShown.count { it.length == 2 || it.length == 7 || it.length == 3 || it.length == 4 }

        val mapping = mutableMapOf(
            "a" to mutableListOf("a", "b", "c", "d", "e", "f", "g"),
            "b" to mutableListOf("a", "b", "c", "d", "e", "f", "g"),
            "c" to mutableListOf("a", "b", "c", "d", "e", "f", "g"),
            "d" to mutableListOf("a", "b", "c", "d", "e", "f", "g"),
            "e" to mutableListOf("a", "b", "c", "d", "e", "f", "g"),
            "f" to mutableListOf("a", "b", "c", "d", "e", "f", "g"),
            "g" to mutableListOf("a", "b", "c", "d", "e", "f", "g")
        )

        val digitMapping = mutableMapOf<String, Int>()

        fun mustBeDigit(sig: String, digit: Int) {
            for (c in sig) {
                mapping[c.toString()]!!.removeIf { it !in digits[digit]!! }
            }
            digitMapping[sig] = digit
        }

        for (sig in possibleSignals) {
            when (sig.length) {
                2 -> mustBeDigit(sig, 1)
                4 -> mustBeDigit(sig, 4)
                3 -> mustBeDigit(sig, 7)
                7 -> mustBeDigit(sig, 8)
            }
        }

        val seven = possibleSignals.first { it.length == 3 }.toList().map { it.toString() }
        val four = possibleSignals.first { it.length == 4 }.toList().map { it.toString() }
        val one = possibleSignals.first { it.length == 2 }.toList().map { it.toString() }

        // seven has 'aaaa' in addition to one
//        mapping["a"] = (seven - one).toMutableList()

        // four has 'bb' and 'dddd' in addition to one
//        val bOrD = four - one
//        mapping["b"]!!.removeIf { it !in bOrD }

        fun mustBeSegment(input: String, real: String) {
            mapping[input]!!.removeAll { it != real }
            mapping.keys.forEach { key ->
                if (key != input) {
                    mapping[key]!!.removeAll { it == real }
                }
            }
        }

        // count instances in digits
        for (segment in "abcdefg".toList().map { it.toString() }) {
            val count = possibleSignals.count { segment in it }
            when (count) {
                6 -> mustBeSegment(segment, "b")
//                8 -> mustBeSegment(segment, "c")
//                7 -> "d" tai "g"
                4 -> mustBeSegment(segment, "e")
                9 -> mustBeSegment(segment, "f")
            }
        }

        repeat(10) {
            for ((k, v) in mapping.toMap()) {
                if (v.size == 1) {
                    for ((k2, v2) in mapping.toMap()) {
                        if (k2 != k) {
                            mapping[k2]!!.removeAll { it == v.first() }
                        }
                    }
                }
            }
        }

        val mappedDigits = buildString {
            for (v in valueShown) {
                val mapped = v.toList().mapNotNull { mapping[it.toString()]?.first() }.sorted().joinToString("")
                val digit = digits.filter { (k, v) -> v == mapped }.keys.first()
                append(digit)
            }
        }
        totalSum += mappedDigits.toInt()

        println("$line --> $mappedDigits")
        for ((k, v) in mapping) {
            println("$k ${v.joinToString("")}")
        }
    }
    println("uniqCount = $uniqCount, totalSum=$totalSum")
}

// 0:      1:      2:      3:      4:
//  aaaa    ....    aaaa    aaaa    ....
// b    c  .    c  .    c  .    c  b    c
// b    c  .    c  .    c  .    c  b    c
//  ....    ....    dddd    dddd    dddd
// e    f  .    f  e    .  .    f  .    f
// e    f  .    f  e    .  .    f  .    f
//  gggg    ....    gggg    gggg    ....
//
// 5:      6:      7:      8:      9:
//  aaaa    aaaa    aaaa    aaaa    aaaa
// b    .  b    .  .    c  b    c  b    c
// b    .  b    .  .    c  b    c  b    c
//  dddd    dddd    ....    dddd    dddd
// .    f  e    f  .    f  e    f  .    f
// .    f  e    f  .    f  e    f  .    f
//  gggg    gggg    ....    gggg    gggg
//


//println("a 8: ${digits.values.count { it.contains("a") }}")
//println("b 6: ${digits.values.count { it.contains("b") }}")
//println("c 8: ${digits.values.count { it.contains("c") }}")
//println("d 7: ${digits.values.count { it.contains("d") }}")
//println("e 4: ${digits.values.count { it.contains("e") }}")
//println("f 9: ${digits.values.count { it.contains("f") }}")
//println("g 7: ${digits.values.count { it.contains("g") }}")
//
