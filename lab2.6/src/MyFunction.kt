import java.util.*

// MyFunction.kt
//
// alpha = 1, beta = 1
fun getJacobiPolynomials(maxDegree: Int, k: Int): Array<(Double) -> Double> {
    if (maxDegree < 0)
        throw Exception("Degree shouldn't be less than zero")
    val arrOfPolynomials =
        Array(maxDegree + 1) {{_: Double -> 1.0}}

    if (maxDegree > 0){
        arrOfPolynomials[1] = {x: Double -> (k + 1.0) * x}

        if (maxDegree > 1)
            for(i in 2..maxDegree)
                arrOfPolynomials[i] = {x: Double ->
                    ((i + k) * (2 * i + 2.0 * k - 1) * x * arrOfPolynomials[i - 1](x) -
                            (i + k) * (i + k - 1.0) * arrOfPolynomials[i - 2](x)) / ((i + 2.0 * k) * i)}
    }

    return arrOfPolynomials
}

// find derivative of the (1+x^2)*JacobiPolynomial(n, k)
fun getDerivativeOfPolynomials(maxDegree: Int, k: Int, order: Int): Array<(Double) -> Double>{
    val arrOfPolynomials = Array(maxDegree + 1){{x: Double -> x}}

    when(order){
        1 ->{
            val arrOfJacobiPolynomialsWithLessExp =
                getJacobiPolynomials(maxDegree + 1, k - 1)
            for(i in 0..maxDegree)
                arrOfPolynomials[i] = {x: Double ->
                    -2.0 * (i + 1) * arrOfJacobiPolynomialsWithLessExp[i + 1](x)
                }
        }
        2 ->{
            val arrOfJacobiPolynomialsWithSameExp =
                getJacobiPolynomials(maxDegree, k)
            for(i in 0..maxDegree)
                arrOfJacobiPolynomialsWithSameExp[i] = {x: Double ->
                    -(i + 1.0) * (i + 2.0) * arrOfJacobiPolynomialsWithSameExp[i](x)
                }
        }
        else -> throw Exception("Invalid order of derivative")
    }
    return arrOfPolynomials
}

fun leastSquareMethod(countOfPolynomials: Int, point: Double, isJacobi: Boolean = true): Double{
    var result = 0.0
    val arrOfCoeffs: Array<Double>
    val arrOfFuncs: Array<Double>

    if(countOfPolynomials == 3 && !isJacobi){
        arrOfCoeffs = getCoefficientsForThreeFunc()
        arrOfFuncs = Array(countOfPolynomials){(1 - point * point) * Math.pow(point, it.toDouble())}
    }
    else{
        val arrOfJacobiPolynomials = getJacobiPolynomials(countOfPolynomials - 1, 1)
        arrOfCoeffs = getCoefficients(countOfPolynomials)
        arrOfFuncs = Array(countOfPolynomials){(1 - point * point) * arrOfJacobiPolynomials[it](point)}
    }

    for(i in 0 until countOfPolynomials){
        result += arrOfCoeffs[i] * arrOfFuncs[i]
    }

    return result
}

fun getCoefficients(countOfPolynomials: Int): Array<Double>{
    if (countOfPolynomials <= 0)
        throw Exception("Count should be more than zero")
    val matrix = Array(countOfPolynomials){Array(countOfPolynomials + 1){0.0}}
    val arrOfFuncs = getJacobiPolynomials(countOfPolynomials - 1, 1)

    val arrOfFirstDerivatives =
        getDerivativeOfPolynomials(countOfPolynomials - 1, 1, 1)
    val arrOfSecondDerivatives =
        getDerivativeOfPolynomials(countOfPolynomials - 1, 1, 2)
    val arrOfCoordFuncs =
        Array(countOfPolynomials) {
            {x: Double -> -(7.0 - x * x * x) /
                    (8.0 + 3.0 * x * x * x) * arrOfSecondDerivatives[it](x) +
                    (1.0 + x / 3.0) * arrOfFirstDerivatives[it](x) +
                    (1.0 / 2.0 - x / 4.0) * arrOfFuncs[it](x)}
        }

    for (i in 0 until countOfPolynomials)
        for (j in 0 until countOfPolynomials)
            matrix[i][j] =
                evalByGaussFormula(-1.0, 1.0) {arrOfCoordFuncs[j](it) * arrOfCoordFuncs[i](it)}
    // for the last column
    for(i in 0 until countOfPolynomials)
        matrix[i][countOfPolynomials] =
            evalByGaussFormula(-1.0, 1.0){(1.0 / 2.0 - it / 3.0) * arrOfCoordFuncs[i](it)}

    return gaussWithMainElement(matrix)
}

fun getCoefficientsForThreeFunc(): Array<Double>{
    val matrix = Array(3){Array(4){0.0}}
    val arrOfFuncs = Array(3){{x: Double -> (1 - x * x) * Math.pow(x, it.toDouble())}}
    val arrOfFirstDerivatives =
        arrayOf(
            {x: Double -> -2.0 * x},
            {x: Double -> 1 - 3.0 * x * x},
            {x: Double -> 2.0 * x - 4.0 * x * x * x}
        )
    val arrOfSecondDerivatives =
        arrayOf(
            {_: Double -> -2.0},
            {x: Double -> -6.0 * x},
            {x: Double -> 2.0 - 12.0 * x * x}
        )
    val arrOfCoordFuncs =
        Array(3) {
            {x: Double -> -(7.0 - x * x * x) /
                    (8.0 + 3.0 * x * x * x) * arrOfSecondDerivatives[it](x) +
                    (1.0 + x / 3.0) * arrOfFirstDerivatives[it](x) +
                    (1.0 / 2.0 - x / 4.0) * arrOfFuncs[it](x)}
        }

    for (i in 0..2)
        for (j in 0..2)
            matrix[i][j] =
                evalByGaussFormula(-1.0, 1.0) {arrOfCoordFuncs[j](it) * arrOfCoordFuncs[i](it)}
    // for the last column
    for(i in 0..2)
        matrix[i][3] =
            evalByGaussFormula(-1.0, 1.0){(1.0 / 2.0 - it / 3.0) * arrOfCoordFuncs[i](it)}

    return gaussWithMainElement(matrix)
}

fun findMaxAbsElement(matrix: Array<Array<Double>>, visitedRows: Stack<Int>, visitedCols: Stack<Int>): Pair<Int,Int>{
    var maxElem = 0.0
    var xPos = 0
    var yPos = 0
    for (i in 0 until matrix.size){
        if (visitedRows.contains(i))
            continue
        for (j in 0 until matrix[0].size - 1){
            if (visitedCols.contains(j))
                continue
            val curMaxElem = Math.abs(matrix[i][j])
            if (maxElem < curMaxElem) {
                maxElem = curMaxElem
                xPos = i
                yPos = j
            }
        }
    }
    return Pair(xPos, yPos)
}

fun gaussWithMainElement(matrix: Array<Array<Double>>): Array<Double> {
    // direct move
    val curMatrix =
        Array(matrix.size){j ->
            Array(matrix[0].size)
            {matrix[j][it]}}
    val stackOfMainRows = Stack<Array<Double>>()
    val visitedRows = Stack<Int>()
    val visitedCols = Stack<Int>()
    for (k in 0 until matrix.size){
        val mainRow = findMaxAbsElement(curMatrix, visitedRows, visitedCols).first
        val mainCol = findMaxAbsElement(curMatrix, visitedRows, visitedCols).second
        val mainElem = curMatrix[mainRow][mainCol]
        val mArr = Array(matrix.size){curMatrix[it][mainCol] / mainElem}
        stackOfMainRows.add(curMatrix[mainRow])
        for (i in 0 until curMatrix.size) {
            if (i == mainRow || visitedRows.contains(i))
                continue
            for (j in 0 until curMatrix[0].size) {
                if (visitedCols.contains(j))
                    continue
                curMatrix[i][j] = curMatrix[i][j] - curMatrix[mainRow][j] * mArr[i]
            }
        }
        visitedRows.add(mainRow)
        visitedCols.add(mainCol)
        //printMatrix(curMatrix)
    }
    // reverse move
    val resultVec = Array(matrix.size){0.0}
    for (i in 0 until resultVec.size){
        val curRow = stackOfMainRows.pop()
        val curRoot = visitedCols.pop()
        resultVec[curRoot] = curRow[curRow.size - 1]
        for (j in 0 until resultVec.size) {
            if (j == curRoot)
                continue
            resultVec[curRoot] -= curRow[j] * resultVec[j]
        }
        resultVec[curRoot] /= curRow[curRoot]
    }
    return resultVec
}

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
            result += coeffs[j] * f(roots[j] * step / 2 +
                    leftSide + step * i + step / 2)
        }
    }
    result *= step / 2
    return result
}