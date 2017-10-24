import java.io.File
import java.util.*

data class DirectedEdge(val source: Int, val goal: Int, val weight: Int, val time: Int)
data class GraphNode(val x: Double, val y: Double)

class DirectedGraph(private val nodes: List<GraphNode>, private val edges: List<DirectedEdge>) {
    private val neighbors = mutableMapOf<Int, MutableList<DirectedEdge>>()
    init {
        edges.forEach { edge ->
            if (edge.source in neighbors) {
                neighbors[edge.source]!!.add(edge)
            }
            else {
                neighbors[edge.source] = mutableListOf(edge)
            }
        }
    }

    fun node (u: Int) = nodes[u]
    fun edges (u: Int) = neighbors[u]
}

data class SearchNode(val u: Int, val cost: Double, val weight: Int = 0, val time: Int = 0, val parent: SearchNode? = null): Comparable<SearchNode> {

    override fun compareTo(other: SearchNode): Int {
        return when {
            cost - other.cost < 0 -> -1
            other.cost - cost < 0 -> 1
            else -> 0
        }
    }

    fun path (): List<SearchNode> {
        var node = this
        val path = mutableListOf(this)

        while (node.parent != null) {
            path.add(node)
            node = node.parent!!
        }

        return path.reversed()
    }

    fun neighbors(graph: DirectedGraph, cost: (Double, DirectedEdge) -> Double): MutableList<SearchNode> {
        val nodes = mutableListOf<SearchNode>()

        val edges = graph.edges(u)
        if (edges != null) {
            nodes.addAll(edges.map { edge -> SearchNode(edge.goal, cost(this.cost, edge), weight + edge.weight, time + edge.time, this) })
        }

        return nodes
    }
}


fun uniform (graph: DirectedGraph, source: Int, goal: Int, cost: (Double, DirectedEdge) -> Double): SearchNode? {

    val goal = graph.node(goal)
    val frontier = PriorityQueue<SearchNode>()
    val explored = mutableSetOf<Int>()

    frontier.offer(SearchNode(source, .0))

    while (frontier.isNotEmpty()) {
        val node = frontier.poll()
        if (graph.node(node.u) == goal) {
            return node
        }

        if (node.u !in explored) {
            explored.add(node.u)
            node.neighbors(graph, cost).forEach { n -> frontier.add(n) }
        }
    }

    return null
}



data class Problem(val graph: DirectedGraph, val source: Int, val goal: Int, val capacity: Int) {

    companion object {
        fun fromFile (path: String): Problem {
            val file = File(path)
            val scanner = Scanner(file)
            val n = scanner.nextInt()
            val m = scanner.nextInt()
            val s = scanner.nextInt()
            val t = scanner.nextInt()
            val capacity = scanner.nextInt()

            val nodes = 0.rangeTo(n).map { i ->
                val x = scanner.nextDouble()
                val y = scanner.nextDouble()
                GraphNode(x, y)
            }

            val edges = 0.rangeTo(m).map { i ->
                val u = scanner.nextInt()
                val v = scanner.nextInt()
                val w = scanner.nextInt()
                val r = scanner.nextInt()
                DirectedEdge(u, v, w, r)
            }

            val graph = DirectedGraph(nodes, edges)

            return Problem(graph, s, t, capacity)
        }
    }
}