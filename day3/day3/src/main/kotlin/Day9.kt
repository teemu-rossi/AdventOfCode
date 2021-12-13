data class SmokeField(
    val numbers: MutableList<Int>,
    val span: Int,
    val height: Int
) {
    fun get(x: Int, y: Int): Int = numbers[y * span + x]
//    fun inc(x: Int, y: Int) = set(x, y, get(x ,y) + 1)
    fun set(x: Int, y: Int, value: Int) { numbers[y * span + x] = value }

    fun isLowPoint(x: Int, y: Int): Boolean {
        if (x > 0 && get(x - 1, y) <= get(x, y)) return false
        if (x < span - 1 && get(x + 1, y) <= get(x, y)) return false
        if (y > 0 && get(x, y - 1) <= get(x, y)) return false
        if (y < height - 1 && get(x, y + 1) <= get(x, y)) return false
        return true
    }

    fun countLows(): Int {
        var res = 0
        for (x in 0 until span) {
            for (y in 0 until height) {
                if (isLowPoint(x, y)) {
                    res += 1
                }
            }
        }

        return res
    }

    fun risk(): Int {
        var res = 0
        for (x in 0 until span) {
            for (y in 0 until height) {
                if (isLowPoint(x, y)) {
                    res += 1 + get(x, y)
                }
            }
        }

        return res
    }

    val basinSizes = mutableListOf<Int>()
    fun findBasins() {
        for (x in 0 until span) {
            for (y in 0 until height) {
                if (get(x, y) < 10) {
                    basinSizes += fillBasin(x, y)
                }
            }
        }
    }

    fun fillBasin(x: Int, y: Int): Int {
        if (x !in 0 until span || y !in 0 until height) return 0

        var count = 0
        if (get(x, y) < 9) {
            set(x, y, 10)
            count++
        } else {
            return 0
        }

        count += fillBasin(x - 1, y)
        count += fillBasin(x + 1, y)
        count += fillBasin(x, y - 1)
        count += fillBasin(x, y + 1)

        return count
    }

    fun print() {
        for (y in 0 until height) {
            val s = buildString {
                for (x in 0 until span) {
                    val v = get(x, y)
                    if (v == 10) {
                        append("-")
                    } else {
                        append(v)
                    }
                }
            }
            println(s)
        }
    }
}

@OptIn(ExperimentalStdlibApi::class)
fun main(args: Array<String>) {
    val values = generateSequence(::readLine)
        .mapNotNull { line -> line.trim().takeUnless { trimmed -> trimmed.isBlank() } }
        .toList()

    var span = 0
    var height = 0
    val numbers = buildList {
        for (line in values) {
            val nums = line.toList().map { it.toString().toInt() }
            if (nums.isNotEmpty()) {
                span = nums.size
                height++
            }
            for (v in nums) {
                add(v)
            }
        }
    }

    val field = SmokeField(numbers.toMutableList(), span, height)
    println("lows=${field.countLows()} risk=${field.risk()}")

    field.findBasins()
    field.print()
    println("found ${field.basinSizes.size} basins, sizes=${field.basinSizes.sortedDescending()}")
    val res = field.basinSizes.sortedDescending().take(3).fold(1) { a, b -> a * b }
    println("result $res")
}
