
class BranchAndBoundSolver(val graph: Graph, val source: Int, val goal: Int, val capacity: Int, var bound: Int) {

    // The solution
    var solution: SearchNode? = null

    // The shortest distance from source to a node
    val distances = mutableMapOf<Int, SearchNode>()

    fun solve(): SearchNode? {
        val source = SearchNode(source, 0.0)
        dfs(source, mutableSetOf())
        return solution
    }

    fun dfs(node: SearchNode, previousExplored: MutableSet<Int>) {

        // Copy explored
        val explored = mutableSetOf<Int>()
        explored.addAll(previousExplored)

        // Add current node to explored set
        explored.add(node.v)

        // Filter nodes that have been explored and those that are worse than the current solution
        node.neighbors(graph)
            .filter { it.v !in explored }
            .filter { isBetter(it) }
            .forEach {
                if (it.v == goal) {
                    solution = it
                    this.bound = it.weight
                }
                else {
                    dfs(it, explored)
                }
            }
    }

    fun isBetter(node: SearchNode): Boolean {

        // Lowerbound violated
        if (node.weight >= this.bound) {
            return false
        }

        // Capacity violated
        if (node.time > this.capacity) {
            return false
        }

        if (node.v in distances) {
            val n = distances[node.v]!!

            // We can't say that the node is better or not if just the weight is smaller. The time might be considerably larger

            // There is a better path than this one at this node
            if (n.weight < node.weight && n.time < node.time) {
                return false
            }
            // There is a better path with this node
            else if (n.weight > node.weight && n.time > node.time) {
                distances[node.v] = node
            }
        }
        else {
            // Not in shortest distances: insert it
            distances[node.v] = node
        }

        return true
    }
}