// MyFunction.kt
//
// 0 < x < 1, 0 < t <= T, a > 0
fun explicitMethod(
    countOfParts: Int,
    timeLimit: Int,
    timeStep: Double,
    a: Double,
    alpha: (Double) -> Double,
    beta1: (Double) -> Double,
    beta2: (Double) -> Double,
    f: (Double, Double) -> Double
): Array<Array<Double>>
{
    val countOfMoments = Math.round(timeLimit / timeStep).toInt() + 1
    val arrOfPoints = Array(countOfParts + 1){it * (1.0 / countOfParts)}
    val arrOfMoments = Array(countOfMoments){it * timeStep}
    val grid = Array(countOfMoments){Array(countOfParts + 1){0.0}}
    val s = a * timeStep * countOfParts * countOfParts
    for (i in 0..countOfParts)
        grid[0][i] = alpha(arrOfPoints[i])
    for (j in 1 until countOfMoments){
        grid[j][0] = beta1(arrOfMoments[j])
        grid[j][countOfParts] = beta2(arrOfMoments[j])
        for(i in 1 until countOfParts)
            grid[j][i] = s * grid[j - 1][i + 1] + (1.0 - 2.0 * s) * grid[j - 1][i] + s * grid[j - 1][i - 1] +
                    timeStep * f(arrOfPoints[i], arrOfMoments[j - 1])
    }
    return grid
}

fun implicitMethod(
    countOfParts: Int,
    timeLimit: Int,
    timeStep: Double,
    a: Double,
    alpha: (Double) -> Double,
    beta1: (Double) -> Double,
    beta2: (Double) -> Double,
    f: (Double, Double) -> Double
): Array<Array<Double>>
{
    val countOfMoments = Math.round(timeLimit / timeStep).toInt() + 1
    val arrOfPoints = Array(countOfParts + 1){it * (1.0 / countOfParts)}
    val arrOfMoments = Array(countOfMoments){it * timeStep}
    val grid = Array(countOfMoments){Array(countOfParts + 1){0.0}}
    val s = a * timeStep * countOfParts * countOfParts

    for (i in 0..countOfParts)
        grid[0][i] = alpha(arrOfPoints[i])
    for (j in 1 until countOfMoments){
        val fValue = Array(countOfParts + 1){f(arrOfPoints[it], arrOfMoments[j])}
        val temp = sweepMethod(
            arrOfPoints,
            beta1(arrOfMoments[j]),
            beta2(arrOfMoments[j]),
            s,
            timeStep,
            fValue,
            grid[j - 1]
            )
        for(i in 0..countOfParts)
            grid[j][i] = temp[i]
    }
    return grid
}

fun sweepMethod(
    arrOfPoints: Array<Double>,
    beta1: Double,
    beta2: Double,
    s: Double,
    timeStep: Double,
    fValue: Array<Double>,
    uValue: Array<Double>
): Array<Double>{
    val countOfNodes = arrOfPoints.size
    val resultVec = Array(countOfNodes){0.0}
    val slau = Array(4){Array(countOfNodes){0.0}}
    slau[1][0] = 1.0
    slau[3][0] = beta1
    slau[1][countOfNodes - 1] = 1.0
    slau[3][countOfNodes - 1] = beta2
    for(j in 1..countOfNodes - 2){
        slau[0][j] = s
        slau[1][j] = -1.0 - 2.0 * s
        slau[2][j] = s
        slau[3][j] = -timeStep * fValue[j] - uValue[j]
    }
    val arrM = Array(countOfNodes){0.0}
    val arrK = Array(countOfNodes){0.0}
    arrM[1] = -slau[2][0] / slau[1][0]
    arrK[1] = slau[3][0] / slau[1][0]
    for(i in 2 until countOfNodes){
        arrM[i] = -slau[2][i - 1] / (slau[1][i - 1] + slau[0][i - 1] * arrM[i - 1])
        arrK[i] = (-slau[0][i - 1] * arrK[i - 1] + slau[3][i - 1]) / (slau[1][i - 1] + slau[0][i - 1] * arrM[i - 1])
    }
    resultVec[countOfNodes - 1] = (-slau[0][countOfNodes - 1] * arrK[countOfNodes - 1] + slau[3][countOfNodes - 1]) /
            (slau[1][countOfNodes - 1] + slau[0][countOfNodes - 1] * arrM[countOfNodes - 1])
    for(i in countOfNodes - 2 downTo 0)
        resultVec[i] = arrM[i + 1] * resultVec[i + 1] + arrK[i + 1]
    return resultVec
}

fun testResultExplicit(countOfParts: Int, timeLimit: Int, timeStep: Double, a: Double): Array<Array<Double>>{
    val countOfMoments = Math.round(timeLimit / timeStep).toInt() + 1
    val step = 1.0 / countOfParts
    return Array(countOfMoments)
    {j -> Array(countOfParts + 1)
    {i -> Math.pow(1.0 - 4.0 * a * timeStep * Math.sin(Math.PI * step / 2.0) * Math.sin(Math.PI * step / 2.0) /
            (step * step), j.toDouble()) * Math.sin(Math.PI * i * step)}}
}

fun testResultImplicit(countOfParts: Int, timeLimit: Int, timeStep: Double, a: Double): Array<Array<Double>>{
    val countOfMoments = Math.round(timeLimit / timeStep).toInt() + 1
    val step = 1.0 / countOfParts
    return Array(countOfMoments)
    {j -> Array(countOfParts + 1)
    {i -> Math.pow(1.0 + 4.0 * a * timeStep * Math.sin(Math.PI * step / 2.0) * Math.sin(Math.PI * step / 2.0) /
            (step * step), -j.toDouble()) * Math.sin(Math.PI * i * step)}}
}

fun printMatrix(matrix: Array<Array<Double>>){
    for(i in 0 until matrix.size){
        matrix[i].forEach {print("%.10f ".format(it))}
        println()
    }
}

fun printMatrix(matrix: Array<Array<Double>>, otherMatrix: Array<Array<Double>>){
    val temp = Array(matrix.size){Array(matrix[0].size){0.0}}
    for(i in 0 until matrix.size){
        for (j in 0 until matrix[0].size)
            temp[i][j] = Math.abs(matrix[i][j] - otherMatrix[i][j])
    }
    printMatrix(temp)
}