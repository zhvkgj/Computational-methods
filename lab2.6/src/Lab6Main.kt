// Lab6Main.kt
//
import java.util.*

val scan = Scanner(System.`in`)

fun main() {
    println("Enter a count of coordinate functions: ")
    /*val n = scan.nextInt()

    val arrOfPoints = Array(9){-1 + it * 0.25}
    val arrOfCoeffs = getCoefficients(n)

    println("Coefficients of coordinate functions: ")
    for (i in arrOfCoeffs)
        print("$i ")
    println("\n")

    println("Values at points with step 0.25: ")
    for(i in arrOfPoints)
        print("${leastSquareMethod(n, i)} ")
    println("\n")*/

    val arrOfPoints = Array(9){-1 + it * 0.25}
    println("Values at points with step 0.25: ")
    for(i in arrOfPoints)
        print("${leastSquareMethod(3, i, true)} ")
    println()
    for(i in arrOfPoints)
        print("${leastSquareMethod(6, i)} ")
    println()
}