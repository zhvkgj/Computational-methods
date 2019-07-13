// Lab5Main.kt
// k = 0
//
import java.util.*

val scan = Scanner(System.`in`)

fun main() {
    val k = scan.nextInt()
    val step = 1.0 / (k - 1)
    val arrOfPoints = Array(k){it * step}
    for(i in arrOfPoints){
        val value4 = replacementMethod(i, 4, true)
        val value3 = replacementMethod(i, 3, true)
        val residual =
            Math.abs(value4 - value3)
        println("Value for 4 elements at point $i: $value4")
        println("Value for 3 elements at point $i: $value3")
        println("Residual at point $i: $residual")
    }
    println()
    println("Error for 3 elements: ${errorEval(3, arrOfPoints)}")
    println("Error for 4 elements: ${errorEval(4, arrOfPoints)}")
}