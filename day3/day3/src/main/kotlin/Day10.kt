@OptIn(ExperimentalStdlibApi::class)
fun main(args: Array<String>) {
    val values = generateSequence(::readLine)
        .mapNotNull { line -> line.trim().takeUnless { trimmed -> trimmed.isBlank() } }
        .toList()

    val errors = mutableListOf<Int>()
    for ((lineNum, line) in values.withIndex()) {
        val stack = mutableListOf<Char>()

        val ess = errors.size
        for (c in line) {
            when (c) {
                '(' -> stack.add('(')
                '{' -> stack.add('{')
                '[' -> stack.add('[')
                '<' -> stack.add('<')
                ')' -> if (stack.last() == '(') stack.removeLast() else {
                    errors.add(3); println(String.format("line  %d  invalid  %c   %d", lineNum + 1, c, 3)); break
                }
                ']' -> if (stack.last() == '[') stack.removeLast() else {
                    errors.add(57); println(String.format("line  %d  invalid  %c   %d", lineNum + 1, c, 57)); break
                }
                '}' -> if (stack.last() == '{') stack.removeLast() else {
                    errors.add(1197); println(String.format("line  %d  invalid  %c   %d", lineNum + 1, c, 1197)); break
                }
                '>' -> if (stack.last() == '<') stack.removeLast() else {
                    errors.add(25137); println(
                        String.format(
                            "line  %d  invalid  %c   %d",
                            lineNum + 1,
                            c,
                            25137
                        )
                    ); break
                }
                else -> throw RuntimeException("Unexpected $c")
            }
        }
        if (ess == errors.size) {
            errors.add(0)
        }
    }

    println("errors=$errors")
    println("errors.size=${errors.size}")
    println("sum=${errors.sum()}")

    // autocomplete pass, part 2
    val scores = mutableListOf<Long>()
    for ((lineNum, line) in values.withIndex()) {
        val stack = mutableListOf<Char>()

        var errored = false
        for (c in line) {
            when (c) {
                '(' -> stack.add('(')
                '{' -> stack.add('{')
                '[' -> stack.add('[')
                '<' -> stack.add('<')
                ')' -> if (stack.last() == '(') stack.removeLast() else {
                    errored = true; break
                }
                ']' -> if (stack.last() == '[') stack.removeLast() else {
                    errored = true; break
                }
                '}' -> if (stack.last() == '{') stack.removeLast() else {
                    errored = true; break
                }
                '>' -> if (stack.last() == '<') stack.removeLast() else {
                    errored = true; break
                }
                else -> throw RuntimeException("Unexpected $c")
            }
        }
        if (!errored) {
            var score = 0L
            for (c in stack.reversed()) {
                score *= 5L
                score += when (c) {
                    '(' -> 1L
                    '[' -> 2L
                    '{' -> 3L
                    '<' -> 4L
                    else -> throw RuntimeException("Unexpected $c")
                }
            }
            scores.add(score)
        }
    }

    println("scores=${scores.sorted()}")
    println("autocomplete: ${scores.sorted()[scores.size / 2]}")
}
