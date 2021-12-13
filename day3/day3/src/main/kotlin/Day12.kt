
data class CaveLayout(
    val connections: Map<String, List<String>>
)

data class NavState(
    val layout: CaveLayout,
    val current: String,
    val visited: List<String>,
    val visitedSmallCave: String? = null
) {
    private fun getValidTargets(): List<String> {
        //println("getValidTargets, current=$current, visited=$visited, layout=$layout")
        return layout.connections[current]!!.filter {
            it !in visited || it == it.uppercase()
        }
    }

    private fun getValidRevisitTargets(): List<String> {
        return layout.connections[current]!!.filter {
            it == it.lowercase() && (it !in visited || visitedSmallCave == null) && it != "start" && it != "end"
        }.filter { it !in getValidTargets() }
    }

    fun navigateAll(): List<List<String>> {
        val routes = mutableListOf<List<String>>()
        for (target in getValidTargets().withEndLast()) {
            if (target == "end") {
                println("route: ${visited + target}")
                return routes.toList() + listOf(visited + target)
            } else {
                val newState = this.copy(current = target, visited = visited + target)
                routes += newState.navigateAll()
            }
        }
        return routes
    }

    private fun getValidTargetsPart2(): List<String> {
        //println("getValidTargets, current=$current, visited=$visited, layout=$layout")
        return layout.connections[current]!!.filter {
            it !in visited || it == it.uppercase() || visitedSmallCave == null
        }
    }

    fun navigateAllPart2(): List<List<String>> {
        val routes = mutableListOf<List<String>>()

        var dbg = false
        if (visited == listOf("start", "b", "A")) {
            println("@@@ this=$this")
            println("getValidTargets=${getValidTargets().withEndLast()}")
            println("getValidRevisitTargets=${getValidRevisitTargets()}")
            dbg = true
        }

        if (dbg) println("--------------")
        for (target in getValidTargets().withEndLast()) {
            if (target == "end") {
                println("route: ${visited + target}")
                routes += listOf(visited + target)
            } else {
                val newState = this.copy(current = target, visited = visited + target)
                if (dbg) println("recursing (1): newState=$newState")
                routes += newState.navigateAllPart2()
            }
        }

        if (dbg) println("--------------")
        for (target in getValidRevisitTargets()) {
            val newState = this.copy(current = target, visited = visited + target, visitedSmallCave = target)
            if (dbg) println("recursing (revisit): newState=$newState")
            routes += newState.navigateAllPart2()
        }
        return routes
    }
}

fun List<String>.withEndLast(): List<String> = if ("end" in this) {
    this.filter { it != "end" } + "end"
} else {
    this
}

fun main(args: Array<String>) {
    val values = generateSequence(::readLine)
        .mapNotNull { line -> line.trim().takeUnless { trimmed -> trimmed.isBlank() } }
        .toList()

    val connections = mutableMapOf<String, List<String>>()
    for (line in values) {
        val (a, b) = line.split("-")
        println("$a <--> $b")
        if (a !in connections) {
            connections[a] = listOf(b)
        } else {
            connections[a] = connections[a]!! + listOf(b)
        }

        if (b !in connections) {
            connections[b] = listOf(a)
        } else {
            connections[b] = connections[b]!! + listOf(a)
        }
    }

    val layout = CaveLayout(connections)
    val s = NavState(layout, "start", listOf("start"))
    val routes = s.navigateAll()
    println("count=${routes.joinToString("\n")}")
    println("count=${routes.size}")

    val s2 = NavState(layout, "start", listOf("start"))
    val routes2 = s2.navigateAllPart2()
    println("count2=${routes2.joinToString("\n") { it.joinToString(",") }}")
    println("count2=${routes2.size}")
}
