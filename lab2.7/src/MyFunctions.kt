fun slauBuilder(
    arrOfPoints: Array<Double>,
    step: Double,
    alpha0: Double,
    alpha1: Double,
    beta0: Double,
    beta1: Double,
    A: Double,
    B: Double,
    orderForBounds: Int
): Array<Array<Double>> {
    val countOfNodes = arrOfPoints.size
    val slau = Array(4) {Array(countOfNodes){0.0}}

    for (i in 1..countOfNodes - 2) {
        slau[0][i] = 1.0 + step * (arrOfPoints[i] * arrOfPoints[i] + 1.0) / 2.0
        slau[1][i] = -2.0 - step * step * 2.0 * arrOfPoints[i]
        slau[2][i] = 1.0 - step * (arrOfPoints[i] * arrOfPoints[i] + 1.0) / 2.0
        slau[3][i] = step * step * 2.0 * (3.0 * arrOfPoints[i] * arrOfPoints[i] - 1.0) /
                Math.pow((arrOfPoints[i] * arrOfPoints[i] + 1.0), 3.0)
    }

    when (orderForBounds){
        1 -> {
            slau[0][countOfNodes - 1] = -beta1
            slau[1][0] = step * alpha0 - alpha1
            slau[1][countOfNodes - 1] = step * beta0 + beta1
            slau[2][0] = alpha1
            slau[3][0] = step * A
            slau[3][countOfNodes - 1] = step * B
        }
        2 -> {
            slau[0][countOfNodes - 1] = -4.0 * beta1 - beta1 * slau[1][countOfNodes - 2] / slau[0][countOfNodes - 2]
            slau[1][0] = 2.0 * alpha0 * step + alpha1 * slau[0][1] / slau[2][1] - 3.0 * alpha1
            slau[1][countOfNodes - 1] = 2.0 * step * beta0 + 3.0 * beta1 - slau[2][countOfNodes - 2] * beta1 / slau[0][countOfNodes - 2]
            slau[2][0] = alpha1 * slau[1][1] / slau[2][1] + 4.0 * alpha1
            slau[3][0] = A * 2.0 * step + alpha1 * slau[3][1] / slau[2][1]
            slau[3][countOfNodes - 1] = 2.0 * step * B - slau[3][countOfNodes - 2] * beta1 / slau[0][countOfNodes - 2]
        }
        else -> throw Exception("Illegal arguments exception")
    }

    return slau
}

fun sweepMethod(
    arrOfPoints: Array<Double>,
    step: Double,
    alpha0: Double,
    alpha1: Double,
    beta0: Double,
    beta1: Double,
    A: Double,
    B: Double,
    orderForBounds: Int
): Array<Double>{
    val countOfNodes = arrOfPoints.size
    val resultVec = Array(countOfNodes){0.0}
    val slau = slauBuilder(arrOfPoints, step, alpha0, alpha1, beta0, beta1, A, B, orderForBounds)
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