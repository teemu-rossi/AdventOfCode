
val CARD_WIDTH = 5
val CARD_HEIGHT = 5

fun List<Int>.isBingo(inputs: List<Int>) = this.all { inputs.contains(it) }

data class BingoCard(
    val numbers: List<Int>
) {
    fun get(row: Int, column: Int): Int = numbers[row * CARD_WIDTH + column]
    fun getRow(row: Int) = (0 until CARD_WIDTH).map { get(row, it) }
    fun getColumn(column: Int) = (0 until CARD_HEIGHT).map { get(it, column) }

    fun isBingo(inputs: List<Int>): Boolean {
        for (row in 0 until CARD_HEIGHT) {
            if (getRow(row).isBingo(inputs)) {
                return true
            }
        }

        for (col in 0 until CARD_WIDTH) {
            if (getColumn(col).isBingo(inputs)) {
                return true
            }
        }

        return false
    }

    fun getScore(inputs: List<Int>): Int = numbers.filter { it !in inputs }.sum()
}

fun main(args: Array<String>) {
    println("Hello World!")

    val values = generateSequence(::readLine)
        .mapNotNull { line -> line.trim().takeUnless { trimmed -> trimmed.isBlank() } }
        .toList()

    val bingoInput = values.first().split(",").map { it.toInt() }

    val boards = values
        .drop(1)
        .windowed(CARD_HEIGHT, step = CARD_HEIGHT)
        .map { list -> BingoCard(list.joinToString(" ").split(" ").filter { it.isNotBlank() }.map { num -> num.toInt() }) }

    for (i in bingoInput.indices) {
        val inputs = bingoInput.take(i)
        val winner = boards.firstOrNull { it.isBingo(inputs) }
        if (winner != null) {
            println("score: ${winner.getScore(inputs)}")
            println("result: ${winner.getScore(inputs) * bingoInput[i - 1]}")
            break
        }
    }

    val boards2 = boards.toMutableList()
    for (i in 1 until bingoInput.size) {
        val inputs = bingoInput.take(i)
        println("i=$i inputs=$inputs")
        val winners = boards2.filter { it.isBingo(inputs) }
        if (winners.size == 1 && boards2.size == 1) {
            println("2nd score: ${winners.first().getScore(inputs)}")
            println("2nd result: ${winners.first().getScore(inputs) * inputs.last()}")
            break
        }
        println("removed $winners, remaining ${boards2.size}")
        boards2.removeAll(winners)
    }
}