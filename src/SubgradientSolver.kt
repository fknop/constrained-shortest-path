import kotlin.Double.Companion.NEGATIVE_INFINITY

class SubgradientSolver(problem: Problem) {
    val graph: DirectedGraph = problem.graph
    val source: Int = problem.source
    val goal: Int = problem.goal
    val capacity: Int = problem.capacity

    private fun muk (k : Int): Double {
        return 1.0 / k.toDouble()
    }

    fun solve(): SearchNode? {
        val epsilon = 0.00000001 // Let's define epsilon as this number for now
        var LStar = NEGATIVE_INFINITY
        var k = 0
        var mu = 1.0
        var lambda = .0

        var PStar: SearchNode? = uniform(graph, source, goal, { cost, edge ->
            cost + edge.time
        })

        if (PStar == null || PStar.cost > capacity) {
            // PROBLEM INFEASIBLE
        }


        while (mu > epsilon) {
            val Pk = uniform(graph, source, goal, { cost, edge ->
                cost + (edge.weight + (lambda * edge.time))
            })
            val Lk = Pk!!.weight + (lambda * (Pk.time - capacity))
            if (Lk > LStar) {
                LStar = Lk
                if (Pk.cost <= capacity) {
                    PStar = Pk
                }
            }

            lambda = maxOf(.0, lambda + (mu * (Pk.time - capacity)))
            mu = muk(k)
            k++
        }

        return PStar
    }
}