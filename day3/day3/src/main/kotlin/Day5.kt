import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class Point(val x: Int, val y: Int) {
    companion object {
        fun from(string: String): Point {
            val s = string.split(",").mapNotNull { it.trim().takeUnless { it.isEmpty() } }
            require(s.size == 2)
            return Point(s[0].toInt(), s[1].toInt())
        }
    }
}

data class Line(val a: Point, val b: Point) {
    companion object {
        fun from(string: String): Line {
            val s = string.split("->").mapNotNull { it.trim().takeUnless { it.isEmpty() } }
            require(s.size == 2)
            return Line(Point.from(s[0]), Point.from(s[1]))
        }
    }
}

data class Field(
    val numbers: MutableList<Int>,
    val span: Int
) {
    fun get(x: Int, y: Int): Int = numbers[y * span + x]
    fun inc(x: Int, y: Int) = set(x, y, get(x ,y) + 1)
    fun set(x: Int, y: Int, value: Int) { numbers[y * span + x] = value }

    fun incLine(line: Line) {
        val minx = min(line.a.x, line.b.x)
        val maxx = max(line.a.x, line.b.x)
        val miny = min(line.a.y, line.b.y)
        val maxy = max(line.a.y, line.b.y)
        if (line.a.x == line.b.x) {
            for (y in min(line.a.y, line.b.y)..max(line.a.y, line.b.y)) {
                inc(line.a.x, y)
            }
        } else if (line.a.y == line.b.y) {
            for (x in min(line.a.x, line.b.x)..max(line.a.x, line.b.x)) {
                inc(x, line.a.y)
            }
        } else if (maxx - minx == maxy - miny) {
            var x = line.a.x
            var y = line.a.y
            val dx = if (line.b.x > line.a.x) 1 else -1
            val dy = if (line.b.y > line.a.y) 1 else -1
            do {
                inc(x, y)
                x += dx
                y += dy
            } while (x != line.b.x)
            inc(x, y)
        } else {
            assert(false) { "Invalid line $line" }
        }
    }
}

fun main(args: Array<String>) {
    val values = generateSequence(::readLine)
        .mapNotNull { line -> line.trim().takeUnless { trimmed -> trimmed.isBlank() } }
        .toList()

    val lines = values.map { Line.from(it) }
    val width = lines.maxOf { max(it.a.x, it.b.x) } + 1
    val height = lines.maxOf { max(it.a.y, it.b.y) } + 1

    val field = Field(MutableList(width * height) { 0 }, width)
    for (line in lines) {
        field.incLine(line)
    }

    val count = field.numbers.count { it >= 2 }
    println(count)
}
