import java.util.*

val scan = Scanner(System.`in`)

fun main(){
    println("Enter the count of parts: ")
    val n = scan.nextInt()
    //task2
    val gridExplicit = explicitMethod(
        n,
        1,
        0.005,
        1.0,
        {x -> Math.sin(Math.PI * x)},
        {_ -> 0.0},
        {_ -> 0.0},
        {_, _ -> 0.0}
    )
    val gridImplicit = implicitMethod(
        n,
        1,
        0.005,
        1.0,
        {x -> Math.sin(Math.PI * x)},
        {_ -> 0.0},
        {_ -> 0.0},
        {_, _ -> 0.0}
    )
    val testExplicit = testResultExplicit(n, 1, 0.005, 1.0)
    val testImplicit = testResultImplicit(n, 1, 0.005, 1.0)
    println("The grid for explicit result:\n")
    printMatrix(gridExplicit, testExplicit)
    println("\nThe grid for implicit result:\n")
    printMatrix(gridImplicit, testImplicit)

    //task3
    /*val gridExplicit = explicitMethod(
        n,
        1,
        0.005,
        1.0,
        {x -> x * x * x},
        {_ -> 0.0},
        {_ -> 1.0},
        {x, _ -> 1.0 + x}
    )
    val gridImplicit = implicitMethod(
        n,
        1,
        0.005,
        1.0,
        {x -> x * x * x},
        {_ -> 0.0},
        {_ -> 1.0},
        {x, _ -> 1.0 + x}
    )
    val arrOfPoints = Array(n + 1){it * (1.0 / n)}
    val correctResult = Array(gridExplicit.size){Array(gridExplicit[0].size){x -> Math.pow(arrOfPoints[x], 3.0)}}
    println("The grid for explicit result:\n")
    printMatrix(gridExplicit, correctResult)
    println("\nThe grid for implicit result:\n")
    printMatrix(gridImplicit, correctResult)*/

    //task4
    /*val gridExplicit = explicitMethod(
        n,
        1,
        0.005,
        1.0,
        {x -> Math.sin(0.5 * x) + 1.0 - x},
        {t -> Math.exp(-0.25 * t)},
        {t -> Math.exp(-0.25 * t) * Math.sin(0.5)},
        {x, t -> -0.25 * Math.exp(-0.25 * t) * (1.0 - x)}
    )
    val gridImplicit = implicitMethod(
        n,
        1,
        0.005,
        1.0,
        {x -> Math.sin(0.5 * x) + 1.0 - x},
        {t -> Math.exp(-0.25 * t)},
        {t -> Math.exp(-0.25 * t) * Math.sin(0.5)},
        {x, t -> -0.25 * Math.exp(-0.25 * t) * (1.0 - x)}
    )
    val arrOfPoints = Array(n + 1){it * (1.0 / n)}
    val arrOfMoments = Array(gridExplicit.size){it * 0.005}
    val correctResult =
        Array(gridExplicit.size)
        {t -> Array(gridExplicit[0].size)
        {x -> Math.exp(-0.25 * arrOfMoments[t]) * (Math.sin(0.5 * arrOfPoints[x])+ 1.0 - arrOfPoints[x])}}
    println("The grid for explicit result:\n")
    printMatrix(gridExplicit, correctResult)
    println("\nThe grid for implicit result:\n")
    printMatrix(gridImplicit, correctResult)*/
}