data class OctopusField(
    val numbers: MutableList<Int>,
    val span: Int,
    val height: Int
) {
    fun get(x: Int, y: Int): Int = numbers[y * span + x]
    fun inc(x: Int, y: Int) = set(x, y, get(x ,y) + 1)
    fun set(x: Int, y: Int, value: Int) { numbers[y * span + x] = value }

    val flashed = numbers.map { false }.toMutableList()
    fun setFlashed(x: Int, y: Int, value: Boolean) { flashed[y * span + x] = value }
    fun getFlashed(x: Int, y: Int): Boolean = flashed[y * span + x]

    fun stepSingle(x: Int, y: Int) {
        if (x !in 0 until span || y !in 0 until height) return

        inc(x, y)
        if (get(x, y) > 9) {
            if (!getFlashed(x, y)) {
                setFlashed(x, y, true)

                for (dx in -1..1) {
                    for (dy in -1..1) {
                        if (dx == 0 && dy == 0) continue
                        stepSingle(x + dx, y + dy)
                    }
                }
            }
        }
    }

    fun step(): Int {
        for (y in 0 until height) {
            for (x in 0 until span) {
                stepSingle(x, y)
            }
        }
        val count = flashed.count { it }
        for (y in 0 until height) {
            for (x in 0 until span) {
                if (getFlashed(x, y)) {
                    set(x, y, 0)
                }
            }
        }
        flashed.replaceAll { false }
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

    val field = OctopusField(numbers.toMutableList(), span, height)
    field.print()
    var count = 0
    for(i in 0 until 1000) {
        val flashCount = field.step()
        count += flashCount
        println("Step ${i + 1}, count=$count")
        field.print()
        if (flashCount == field.height*field.span) {
            println("All flashed at step ${i + 1}")
            break
        }
    }
    println("result $count")
}
