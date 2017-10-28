fun main(args: Array<String>) {

    val problem = Problem.fromFile(args[0])
    val solver = SubgradientSolver(problem)

    val node = solver.solve()

    if (node != null) {
        val (graph, source, goal, capacity) = problem
        val bab = BranchAndBoundSolver(graph, source, goal, capacity, node.weight)
        val solution = bab.solve()

        if (solution != null) {
            printForInginious(solution)
        }
        else {
            printForInginious(node)
        }
    }
    else {
        println("Problem infeasible")
    }
}

fun printForInginious (node: SearchNode) {
    val path = node.path()

    println(node.weight)
    println(path.map { it.v }.joinToString(" "))
}

