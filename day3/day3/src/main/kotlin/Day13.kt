data class PointI(val x: Int, val y: Int)

fun main(args: Array<String>) {
    val values = generateSequence(::readLine)
        .mapNotNull { line -> line.trim().takeUnless { trimmed -> trimmed.isBlank() } }
        .toList()

    val coords = values
        .filter { it.contains(",") }
        .map {
            val (x, y) = it.split(",")
            PointI(x.toInt(), y.toInt())
        }

    val folds = mutableListOf<(PointI) -> PointI>()
    values
        .filter { it.contains("=") }
        .forEach {
            val (a, b) = it.split("=")
            val axis = a.last()
            val coord = b.toInt()
            if (axis == 'x') {
                folds += { point -> point.copy(x = if (point.x > coord) 2 * coord - point.x else point.x) }
            } else if (axis == 'y') {
                folds += { point -> point.copy(y = if (point.y > coord) 2 * coord - point.y else point.y) }
            }
        }

    val foldedCoords1 = coords.map { point -> folds.take(1).fold(point) { i, p -> p(i) } }.toSet()
    println("foldedCoords1.size=${foldedCoords1.size}")

    val foldedCoords2 = coords.map { point -> folds.fold(point) { i, p -> p(i) } }.toSet()
    println("foldedCoords2.size=${foldedCoords2.size}")

    for (y in 0..foldedCoords2.maxOf { it.y }) {
        println(buildString {
            for (x in 0..foldedCoords2.maxOf { it.x }) {
                if (PointI(x, y) in foldedCoords2) append("#") else append(".")
            }
        })
    }
}
