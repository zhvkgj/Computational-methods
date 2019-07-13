import java.util.*

val scan = Scanner(System.`in`)
const val a = 0.0
const val b = 1.0

fun main(){
    println("Enter the count of parts: ")
    val n = scan.nextInt()
    val step = (b - a) / n
    val arrOfPoints = Array(n + 1){it * step}
    val resultWithErrorFirstOrder = sweepMethod(
        arrOfPoints,
        step,
        1.0,
        -2.0,
        1.0,
        0.0,
        1.0,
        0.5,
        1
    )
    val resultWithErrorSecondOrder = sweepMethod(
        arrOfPoints,
        step,
        1.0,
        -2.0,
        1.0,
        0.0,
        1.0,
        0.5,
        2
    )
    val correctResult = Array(n + 1){1.0 / (arrOfPoints[it] * arrOfPoints[it] + 1.0)}
    //for(i in result)
        //print("$i ")
    print("Error with first order:\n")
    for (i in 0..n)
        println("${Math.abs(resultWithErrorFirstOrder[i] - correctResult[i])} ")
    println("\n")
    print("Error with second order:\n")
    for (i in 0..n)
        println("${Math.abs(resultWithErrorSecondOrder[i] - correctResult[i])} ")
    println("\n")
}