fun main(args: Array<String>) {
    println("Hello World!")

    val values = generateSequence(::readLine)
        .mapNotNull { line -> line.trim().takeUnless { trimmed -> trimmed.isBlank() } }
        .toList()
    values.forEach { println(it) }

    val bits = values.maxOf { it.length }

    val gamma = buildString {
        for (i in 0 until bits) {
            val mostCommon = if (values.countOnes(i) >= (values.size + 1) / 2) "1" else "0"
            append(mostCommon)
        }
    }

    println("gamma=$gamma")

    val epsilon = gamma.map { if (it == '0') '1' else '0' }.joinToString(separator = "")
    println("epsilon=$epsilon")

    println("power=${gamma.toInt(2) * epsilon.toInt(2)}")

    // oxygen generator rating
    val list1 = values.toMutableList()
    var oxygenGen: Int? = null
    for (i in 0 until bits) {
        val mostCommon = if (list1.countOnes(i) >= (list1.size + 1) / 2) "1" else "0"
        println("mostCommon[$i]=$mostCommon (ones=${list1.countOnes(i)} list size=${list1.size}}")
        list1.removeIf { it[i] != mostCommon[0] }
        if (list1.size <= 1) {
            println("oxygen generator: ${list1.firstOrNull()}")
            oxygenGen = list1.firstOrNull()?.toInt(2)
            break
        }
    }

    // CO2 scrubber rating
    val list2 = values.toMutableList()
    var scrubber: Int? = null
    for (i in 0 until bits) {
        val leastCommon = if (list2.countOnes(i) < (list2.size + 1) / 2) "1" else "0"
        list2.removeIf { it[i] != leastCommon[0] }
        if (list2.size <= 1) {
            println("scrubber first: ${list2.firstOrNull()}")
            scrubber = list2.firstOrNull()?.toInt(2)
            break
        }
    }

    println("oxygenGen=$oxygenGen co2scrubber=$scrubber multi=${oxygenGen?.times(scrubber ?: 0)}")
}

fun List<String>.countOnes(index: Int): Int = count { it[index] == "1"[0] }
