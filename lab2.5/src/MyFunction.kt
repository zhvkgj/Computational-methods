// MyFunction.kt
//
fun evalByGaussFormula(leftSide: Double,
                                   rightSide: Double,
                                   f: (Double) -> Double)
        : Double {
    val roots = arrayOf(
        -0.97390652851717172008,
        -0.86506336668898451073,
        -0.67940956829902440623,
        -0.43339539412924719080,
        -0.148874338981631210885,
        0.148874338981631210885,
        0.43339539412924719080,
        0.67940956829902440623,
        0.86506336668898451073,
        0.97390652851717172008
    )
    val coeffs = arrayOf(
        0.06667134430868905,
        0.1494513491505759,
        0.21908636251598193,
        0.26926671930999824,
        0.2955242247147529,
        0.2955242247147529,
        0.26926671930999824,
        0.21908636251598193,
        0.1494513491505759,
        0.06667134430868905)
    val step = (rightSide - leftSide) / 10
    var result = 0.0
    for(i in 0 until 10) {
        for (j in 0 until 10) {
            result += coeffs[j] * f(roots[j] * step / 2 + leftSide + step * i + step / 2)
        }
    }
    result *= step / 2
    return result
}

fun seidel(matrix: Matrix, vector: Vector, times: Int): Vector{
    val prev = Vector(0.0, vector.size)
    val cur = Vector(0.0, vector.size)
    var counter = times

    while(counter > 0){
        for(i in 0 until prev.size)
            prev[i] = cur[i]
        for(i in 0 until cur.size)
            cur[i] = 0.0
        // i == 0
        for(j in 0 until prev.size)
            cur[0] += matrix[0][j] * prev[j]
        cur[0] += vector[0]
        for(i in 1 until cur.size){
            for(j in 0 until i)
                cur[i] += matrix[i][j] * cur[j]
            for(j in i until prev.size)
                cur[i] += matrix[i][j] * prev[j]
            cur[i] += vector[i]
        }
        counter--
    }

    return cur
}

fun replacementMethod(arg: Double, dimension: Int, isPrint: Boolean): Double{
    var result = Math.exp(-arg)
    val alphaArr: Array<(Double) -> Double> = arrayOf(
        {x -> x},
        {x -> x * x},
        {x -> Math.pow(x, 3.0)},
        {x -> Math.pow(x, 4.0)}
    )
    val betaArr: Array<(Double) -> Double> = arrayOf(
        {x -> -0.2 * x},
        {x -> (-0.02 * x - 0.2) * x},
        {x -> (-0.004 / 3 * x - 0.02) * x * x},
        {x -> (-0.0002 / 3 * x - 0.004 / 3) * Math.pow(x, 3.0)}
    )
    val coeffArr = searchCoefficients(dimension, alphaArr, betaArr, isPrint)
    for(i in 0 until dimension)
        result += coeffArr[i] * alphaArr[i](arg)
    return result
}

fun searchCoefficients(
    dimension: Int,
    alphaArr: Array<(Double) -> Double>,
    betaArr: Array<(Double) -> Double>,
    isPrint: Boolean
): Vector{
    val matrixOmega = Matrix(0.0, dimension)
    val vectorB = Vector(0.0, dimension)
    val matrixA = Matrix(0.0, dimension)
    for(i in 0 until matrixOmega.size) {
        vectorB[i] = evalByGaussFormula(0.0, 1.0){betaArr[i](it) * Math.exp(-it)}
        for (j in 0 until matrixOmega.size)
            matrixOmega[i][j] =
                evalByGaussFormula(0.0, 1.0)
                { betaArr[i](it) * alphaArr[j](it) }
    }
    for(i in 0 until matrixA.size){
        for(j in 0 until matrixA.size){
            if(i == j)
                matrixA[i][j] = 1.0 - matrixOmega[i][j]
            else
                matrixA[i][j] = - matrixOmega[i][j]
        }
    }
    if (isPrint) matrixA.print()
    return seidel(matrixA, vectorB, 15)
}

fun errorEval(dimension: Int, arrOfPoints: Array<Double>): Double{
    val c = evalByGaussFormula(0.0, 1.0) {Math.abs(-2 * (Math.exp(0.2 * it) - 1))}
    //val h3 = evalByGaussFormula(0.0, 1.0) {(0.0002 / 3 * it + 0.004 / 3) * Math.pow(it, 3.0)}
    //val h4 = evalByGaussFormula(0.0, 1.0) {(0.00008 / 3 * it + 0.0002 / 3) * Math.pow(it, 4.0)}
    val h3 = evalByGaussFormula(0.0, 1.0){Math.abs(2.0*(Math.exp(0.2*it)-1) - (0.2*it + (0.02*it+0.2)*it + (0.004/3 * it + 0.02)*it*it))}
    val h4 = evalByGaussFormula(0.0, 1.0){Math.abs(2.0*(Math.exp(0.2*it)-1) - (0.2*it + (0.02*it+0.2)*it + (0.004/3 * it + 0.02)*it*it + (0.0002/3 * it + 0.004/3)*it*it*it))}
    var max = 0.0
    for(i in arrOfPoints){
        val temp = Math.abs(replacementMethod(i, dimension, false))
        if (temp > max) max = temp
    }
    if (dimension == 3)
        return h3 / (1 - c) * max
    else
        return h4 / (1 - c) * max
}