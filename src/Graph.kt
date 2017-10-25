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

class Graph(val nodes: List<GraphNode>, private val edges: List<Edge>) {
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

data class SearchNode(val v: Int, val cost: Double, val weight: Int = 0, val time: Int = 0, var parent: SearchNode? = null): Comparable<SearchNode> {

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

    fun neighbors(graph: Graph, cost: (SearchNode, Edge) -> Double = { n, e -> n.cost + e.weight }): MutableList<SearchNode> {
        val nodes = mutableListOf<SearchNode>()

        val edges = graph.edges(v)
        edges?.forEach { edge ->
            val c = cost(this, edge)
            if (c < Double.POSITIVE_INFINITY) {
                nodes.add(
                        SearchNode(edge.other(v), c, weight + edge.weight, time + edge.time, this)
                )
            }
        }

        return nodes
    }
}

fun uniform (graph: Graph, source: Int, goal: Int, cost: (SearchNode, Edge) -> Double): SearchNode? {

    val frontier = PriorityQueue<SearchNode>()
    val explored = mutableSetOf<Int>()

    frontier.offer(SearchNode(source, .0))

    while (frontier.isNotEmpty()) {
        val node = frontier.poll()

        if (node.v == goal) {
            return node
        }

        explored.add(node.v)
        node.neighbors(graph, cost).forEach { n ->
            if (n.v !in explored) {
                frontier.add(n)
            }
        }
    }

    return null
}

