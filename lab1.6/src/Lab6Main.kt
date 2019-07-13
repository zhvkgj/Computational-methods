import java.util.*

val scan = Scanner(System.`in`)

fun evalByAverageRectangle(leftSide: Double,
                           rightSide: Double,
                           countOfParts: Int,
                           func: (Double) -> Double)
        : Double {
    val step = (rightSide - leftSide) / countOfParts
    var result = 0.0
    for (i in 1..countOfParts){
        val currentValue = leftSide + step / 2 + step * (i - 1)
        result += func(currentValue)
    }
    result *= step
    return result
}

fun evalByQuadratureGaussFormulaWithTwoNodes(
    leftSide: Double,
    rightSide: Double,
    countOfParts: Int,
    f: (Double) -> Double,
    w: (Double) -> Double) : Double
{
    println("Evaluating by quadrature Gauss's formula with N = 2...")
    val moments = Array(4) {0.0}
    for(i in 0..3)
        moments[i] =
                evalByAverageRectangle(leftSide, rightSide, countOfParts)
                {it -> w(it) * Math.pow(it, i.toDouble())}
    println("Moments of weight function: ${moments.joinToString()}")
    val a1 =
        (moments[0] * moments[3] - moments[2] * moments[1]) /
                (moments[1] * moments[1] - moments[2] * moments[0])
    val a2 =
        (moments[2] * moments[2] - moments[3] * moments[1]) /
                (moments[1] * moments[1] - moments[2] * moments[0])
    println("Orthogonal polynomial: x^2 + $a1 * x + $a2")
    val firstRoot = (-a1 + Math.sqrt(a1 * a1 - 4 * a2)) / 2
    val secondRoot = (-a1 - Math.sqrt(a1 * a1 - 4 * a2)) / 2
    val firstQuadratureNum = (moments[1] - secondRoot * moments[0]) / (firstRoot - secondRoot)
    val secondQuadratureNum = (moments[1] - firstRoot * moments[0]) / (secondRoot - firstRoot)
    println("Roots: $firstRoot, $secondRoot")
    println("Coefficient: $firstQuadratureNum, $secondQuadratureNum")
    val result = firstQuadratureNum * f(firstRoot) + secondQuadratureNum * f(secondRoot)
    val checkVal =
        Math.abs(moments[3] -
            (firstQuadratureNum * Math.pow(firstRoot, 3.0) +
                    secondQuadratureNum * Math.pow(secondRoot, 3.0)))
    println("Check value: $checkVal")
    return result
}

fun evalByGaussFormulaWithTwoNodes(leftSide: Double,
                       rightSide: Double,
                       countOfParts: Int,
                       f: (Double) -> Double)
        : Double {
    println("Evaluating by Gauss's formula with N = 2...")
    val firstRoot = 1 / Math.sqrt(3.0)
    val secondRoot = (-1) / Math.sqrt(3.0)
    val firstQuadratureNum = 1.0
    val secondQuadratureNum = 1.0
    println("Roots: $firstRoot, $secondRoot")
    println("Coefficient: $firstQuadratureNum, $secondQuadratureNum")
    val step = (rightSide - leftSide) / countOfParts
    var result = 0.0
    for(i in 0 until countOfParts)
        result += f(firstRoot * step / 2 + leftSide + step * i + step / 2) +
                    f(secondRoot * step / 2 + leftSide + step * i + step / 2)
    result *= step / 2
    return result
}

fun main(args: Array<String>){
    println("Enter a left point: ")
    val a = scan.nextDouble()
    println("Enter a right point: ")
    val b = scan.nextDouble()
    println("Enter a count of parts: ")
    val m = scan.nextInt()
    println()
    val f : (Double) -> Double = { Math.sin(it) }
    val w : (Double) -> Double = { Math.log(it) * (-1)}
    val g : (Double) -> Double = {Math.log(it) * Math.sin(it) * (-1)}
    val fstResult = evalByQuadratureGaussFormulaWithTwoNodes(a, b, m, f, w)
    println("Result: $fstResult\n")
    val sndResult = evalByGaussFormulaWithTwoNodes(a, b, m, g)
    println("Result: $sndResult\n")
    val moduleSub = Math.abs(fstResult - sndResult)
    println("Module difference of numbers: $moduleSub\n")
}