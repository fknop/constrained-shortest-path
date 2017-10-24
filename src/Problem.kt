import java.io.File
import java.util.*

data class Edge(val source: Int, val goal: Int, val weight: Int, val time: Int) {

    fun other(s: Int): Int {
        return if (s == source) {
            goal
        }
        else {
            source
        }
    }
}
data class GraphNode(val x: Double, val y: Double)

class Graph(private val nodes: List<GraphNode>, private val edges: List<Edge>) {
    private val neighbors = mutableMapOf<Int, MutableList<Edge>>()
    init {
        edges.forEach { edge ->

            if (edge.goal in neighbors) {
                neighbors[edge.goal]!!.add(edge)
            }
            else {
                neighbors[edge.goal] = mutableListOf(edge)
            }

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

data class SearchNode(val v: Int, val cost: Double, val weight: Int = 0, val time: Int = 0, val parent: SearchNode? = null): Comparable<SearchNode> {

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
            node = node.parent!!
            path.add(node)
        }

        return path.reversed()
    }

    fun neighbors(graph: Graph, cost: (Double, Edge) -> Double): MutableList<SearchNode> {
        val nodes = mutableListOf<SearchNode>()

        val edges = graph.edges(v)
        if (edges != null) {
            nodes.addAll(edges.map { edge -> SearchNode(edge.other(v), cost(this.cost, edge), weight + edge.weight, time + edge.time, this) })
        }

        return nodes
    }
}


fun uniform (graph: Graph, source: Int, goal: Int, cost: (Double, Edge) -> Double): SearchNode? {

    val frontier = PriorityQueue<SearchNode>()
    val explored = mutableSetOf<Int>()

    frontier.offer(SearchNode(source, .0))

    while (frontier.isNotEmpty()) {
        val node = frontier.poll()

        if (node.v !in explored) {

            if (node.v == goal) {
                return node
            }

            explored.add(node.v)
            node.neighbors(graph, cost).forEach { n -> frontier.add(n) }
        }
    }

    return null
}



data class Problem(val graph: Graph, val source: Int, val goal: Int, val capacity: Int) {

    companion object {
        fun fromFile (path: String): Problem {
            val file = File(path)
            val scanner = Scanner(file)
            val n = scanner.nextInt()
            val m = scanner.nextInt()
            val s = scanner.nextInt()
            val t = scanner.nextInt()
            val capacity = scanner.nextInt()

            val nodes = (0 until n).map { i ->
                val x = scanner.next().toDouble()
                val y = scanner.next().toDouble()
                GraphNode(x, y)
            }

            val edges = (0 until m).map { i ->
                val u = scanner.nextInt()
                val v = scanner.nextInt()
                val w = scanner.nextInt()
                val r = scanner.nextInt()
                Edge(u, v, w, r)
            }

            val graph = Graph(nodes, edges)

            return Problem(graph, s, t, capacity)
        }
    }
}