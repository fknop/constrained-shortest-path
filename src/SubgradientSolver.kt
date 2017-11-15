import kotlin.Double.Companion.NEGATIVE_INFINITY

const val EPSILON = 0.1 // Let's define epsilon as this number for now

class SubgradientSolver(problem: Problem) {

    val graph: Graph = problem.graph
    val source: Int = problem.source
    val goal: Int = problem.goal
    val capacity: Int = problem.capacity

    /**
     * Returns the value of mu at iteration k
     * Simple function that tends to 0
     */
    private fun muk (k : Int): Double {
        return 1.0 / k.toDouble()
    }

    fun solve(): SearchNode? {
        var lStar = NEGATIVE_INFINITY
        var k = 0
        var mu = 1.0
        var lambda = .0

        // Shortest path with time as weights
        // It is the first bound to the constrained shortest path problem
        var pStar: SearchNode? = uniform(graph, source, goal, { node, edge -> node.cost + edge.time })

        // No path found or the cost is greater than the capacity: the problem is infeasible
        if (pStar == null || pStar.cost > capacity) {
            return null
        }

        fun cost (node: SearchNode, edge: Edge) = node.cost + (edge.weight + (lambda * edge.time))

        while (mu > EPSILON) {
            val pK = uniform(graph, source, goal, ::cost) ?: return null

            val lK = pK.weight + (lambda * (pK.time - capacity))
            if (lK >= lStar) {
                lStar = lK
                if (pK.time <= capacity) {
                    pStar = pK
                }
            }

            lambda = maxOf(.0, lambda + (mu * (pK.time - capacity)))
            k++
            mu = muk(k)
        }

        return pStar
    }
}
