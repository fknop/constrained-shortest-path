import java.io.File
import java.util.*

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