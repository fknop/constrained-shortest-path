fun main(args: Array<String>) {

    val problem = Problem.fromFile(args[0])
    val solver = SubgradientSolver(problem)

    val node = solver.solve()

    printForInginious(node!!)
}

fun printForInginious (node: SearchNode) {
    val path = node.path()

    println(node.weight)
    println(path.map { it.v }.joinToString(" "))
}

