import java.io.File
import java.util.*

data class Edge(val u: Int, val v: Int, val weight: Int, val resource: Int)
data class Node(val x: Double, val y: Double)

data class Problem(val nodes: List<Node>, val edges: List<Edge>, val source: Int, val dest: Int, val capacity: Int) {

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
                Node(x, y)
            }

            val edges = 0.rangeTo(m).map { i ->
                val u = scanner.nextInt()
                val v = scanner.nextInt()
                val w = scanner.nextInt()
                val r = scanner.nextInt()
                Edge(u, v, w, r)
            }

            return Problem(nodes, edges, s, t, capacity)
        }
    }
}