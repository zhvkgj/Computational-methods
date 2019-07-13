import java.util.*

val scan = Scanner(System.`in`)

fun evalByLeftRectangle(leftSide: Double,
                        rightSide: Double,
                        countOfParts: Int,
                        func : (Double) -> Double,
                        funcDev : (Double) -> Double)
        : ReturnType {
    val step = (rightSide - leftSide) / countOfParts
    var result = 0.0
    for (i in 1..countOfParts){
        val currentValue = leftSide + step * (i - 1)
        result += func(currentValue)
    }
    result *= step
    val correctResult = funcDev(rightSide) - funcDev(leftSide)
    val error = Math.abs(result - correctResult)
    return ReturnType("Evaluation by method of the left rectangles...", result, error)
}

fun evalByRightRectangle(leftSide: Double,
                         rightSide: Double,
                         countOfParts: Int,
                         func : (Double) -> Double,
                         funcDev : (Double) -> Double)
        : ReturnType {
    val step = (rightSide - leftSide) / countOfParts
    var result = 0.0
    for (i in 1..countOfParts){
        val currentValue = leftSide + step + step * (i - 1)
        result += func(currentValue)
    }
    result *= step
    val correctResult = funcDev(rightSide) - funcDev(leftSide)
    val error = Math.abs(result - correctResult)
    return ReturnType("Evaluation by method of the right rectangles...", result, error)
}

fun evalByAverageRectangle(leftSide: Double,
                           rightSide: Double,
                           countOfParts: Int,
                           func : (Double) -> Double,
                           funcDev : (Double) -> Double)
        : ReturnType {
    val step = (rightSide - leftSide) / countOfParts
    var result = 0.0
    for (i in 1..countOfParts){
        val currentValue = leftSide + step / 2 + step * (i - 1)
        result += func(currentValue)
    }
    result *= step
    val correctResult = funcDev(rightSide) - funcDev(leftSide)
    val error = Math.abs(result - correctResult)
    return ReturnType("Evaluation by method of the average rectangles...", result, error)
}

fun evalByTrapezium(leftSide: Double,
                    rightSide: Double,
                    countOfParts: Int,
                    func : (Double) -> Double,
                    funcDev : (Double) -> Double)
        : ReturnType {
    val step = (rightSide - leftSide) / countOfParts
    var result = 0.0
    for (i in 1 until countOfParts){
        val currentValue = leftSide + step * i
        result += func(currentValue)
    }
    result *= 2
    result += func(leftSide) + func(rightSide)
    result *= step / 2
    val correctResult = funcDev(rightSide) - funcDev(leftSide)
    val error = Math.abs(result - correctResult)
    return ReturnType("Evaluation by method of the trapeze...", result, error)
}

fun evalBySimpson(leftSide: Double,
                  rightSide: Double,
                  countOfParts: Int,
                  func : (Double) -> Double,
                  funcDev : (Double) -> Double)
        : ReturnType {
    val step = (rightSide - leftSide) / (2 * countOfParts)
    var result = 0.0
    for (i in 1 until 2 * countOfParts step 2){
        val currentValue = leftSide + step * i
        result += 4 * func(currentValue)
    }
    for (i in 2 until 2 * countOfParts step 2){
        val currentValue = leftSide + step * i
        result += 2 * func(currentValue)
    }
    result += func(leftSide) + func(rightSide)
    result *= step / 3
    val correctResult = funcDev(rightSide) - funcDev(leftSide)
    val error = Math.abs(result - correctResult)
    return ReturnType("Evaluation by Simpson's method...", result, error)
}

fun main(args: Array<String>){
    println("Enter a left point: ")
    val a = scan.nextDouble()
    println("Enter a right point: ")
    val b = scan.nextDouble()
    println("Enter a count of parts: ")
    val m = scan.nextInt()
    val f : (Double) -> Double = {Math.sin(it) + 2 * it}
    val fDev : (Double) -> Double = {(-1) * Math.cos(it) + it * it}
    val correctResult = fDev(b) - fDev(a)
    println("Correct result: $correctResult\n")
    listOf(
        evalByLeftRectangle(a, b, m, f, fDev),
        evalByRightRectangle(a, b, m, f, fDev),
        evalByAverageRectangle(a, b, m, f, fDev),
        evalByTrapezium(a, b, m, f, fDev),
        evalBySimpson(a, b, m, f, fDev)
    )
        .forEach { println("${it.component1()}\nResult: ${it.component2()}\nError: ${it.component3()}\n") }
}