import java.util.*

val scan = Scanner(System.`in`)

fun main() {
    val A = arrayOf(
        arrayOf(-6.0, 8.0, 10.0, -1.0, 4.0),
        arrayOf(1.0, -3.0, 10.0, 10.0, -13.0),
        arrayOf(0.0, 6.0, 12.0, -8.0, 1.0),
        arrayOf(0.0, 0.0, 2.0, 0.0, -5.0)
    )
    printMatrix(A)
    printVector(gaussWithMainElement(A))
}