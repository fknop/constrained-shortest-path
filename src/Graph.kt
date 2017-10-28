import java.util.*

/**
 * Edge of a graph
 */
data class Edge(val source: Int, val goal: Int, val weight: Int, val time: Int) {

    /**
     * Return the other node
     */
    fun other(s: Int) = if (s == source) goal else source
}

/**
 * A graph nodes (x and y are not used)
 */
data class GraphNode(val x: Double, val y: Double)

/**
 * Graph class, takes nodes and edges
 */
class Graph(val nodes: List<GraphNode>, private val edges: List<Edge>) {

    /**
     * Adjacency map
     */
    private val neighbors = mutableMapOf<Int, MutableList<Edge>>()

    /**
     * Initialize the adjacency map
     */
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

    fun edges (u: Int) = neighbors[u]
}

/**
 * Node used in the search
 */
data class SearchNode(val v: Int, val cost: Double, val weight: Int = 0, val time: Int = 0, var parent: SearchNode? = null): Comparable<SearchNode> {

    override fun compareTo(other: SearchNode) = cost.compareTo(other.cost)

    /**
     * Return the path that the node has taken
     */
    fun path (): List<SearchNode> {
        var node = this
        val path = mutableListOf(this)

        while (node.parent != null) {
            node = node.parent!!
            path.add(node)
        }

        return path.reversed()
    }

    /**
     * Return the neighbors of this node
     * cost is the cost function.
     */
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

/**
 * Uniform search to find a shortest path between a source and a goal
 */
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
        node.neighbors(graph, cost)
            .filterNot { it.v in explored }
            .forEach { frontier.add(it) }
    }

    return null
}

