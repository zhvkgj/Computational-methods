import kotlin.math.roundToInt

fun powerMethod(matrix: Matrix, eps: Double, ifPrint: Boolean): ResultData{
    val vectorCur = Vector(1.0, matrix.size)
    val vectorPrev = Vector(1.0, matrix.size)
    var lambdaCur = -1.0
    var lambdaPrev = 1.0
    var counter = 0
    while(Math.abs(lambdaCur - lambdaPrev) > eps){
        for(i in 0 until vectorPrev.size)
            vectorPrev[i] = vectorCur[i]
        lambdaPrev = lambdaCur
        val temp = matrix * vectorPrev
        for(i in 0 until vectorCur.size)
            vectorCur[i] = temp[i]
        val coordUp = vectorCur[vectorCur.indexMaxByAbs]
        val coordDown = vectorPrev[vectorCur.indexMaxByAbs]
        lambdaCur = coordUp / coordDown
        counter++
        if (ifPrint) println("Power method iteration number $counter: $lambdaCur ")
    }

    return ResultData(lambdaCur, vectorPrev, counter)
}

fun aitkenMod(matrix: Matrix, eps: Double): Double {
    val vectorCur = Vector(1.0, matrix.size)
    val vectorPrev = Vector(1.0, matrix.size)
    var lambdaCur = -1.0
    var lambdaPrev = 1.0
    var lambdaPrevPrev = 1.0
    var counter = 0
    while(Math.abs(lambdaCur - lambdaPrev) > eps){
        for(i in 0 until vectorPrev.size)
            vectorPrev[i] = vectorCur[i]
        lambdaPrevPrev = lambdaPrev
        lambdaPrev = lambdaCur
        val temp = matrix * vectorPrev
        for(i in 0 until vectorCur.size)
            vectorCur[i] = temp[i]
        val coordUp = vectorCur[vectorCur.indexMaxByAbs]
        val coordDown = vectorPrev[vectorCur.indexMaxByAbs]
        lambdaCur = coordUp / coordDown
        counter++
        if (counter.rem(3) == 0){
            lambdaCur = (lambdaPrevPrev * lambdaCur - (lambdaPrev * lambdaPrev)) /
                    (lambdaPrevPrev - 2 * lambdaPrev + lambdaCur)
        }
        println("Aitken's method iteration number $counter: $lambdaCur ")
    }

    return lambdaCur
}

fun aitken(matrix: Matrix, eps: Double): Double{
    var precision = 1.0
    while(eps * Math.pow(10.0, precision) < 1){
        precision++
    }
    val result= powerMethod(matrix, eps, false)
    val firstVec = matrix * result.vec
    val secondVec = matrix * firstVec
    val thirdVec = matrix * secondVec
    val firstLambda = (result.num * Math.pow(10.0, precision + 1)).roundToInt() / Math.pow(10.0, precision + 1)
    val secondLambda = secondVec[secondVec.indexMaxByAbs] / firstVec[secondVec.indexMaxByAbs]
    val thirdLambda = thirdVec[thirdVec.indexMaxByAbs] / secondVec[thirdVec.indexMaxByAbs]
    return (firstLambda * thirdLambda - secondLambda * secondLambda) / (firstLambda - 2 * secondLambda + thirdLambda)
}

fun scalarMultiply(matrix: Matrix, eps: Double): Double{
    val defaultCurVec = Vector(1.0, matrix.size)
    val defaultPrevVec = Vector(1.0, matrix.size)
    val transposeCurVec = Vector(1.0, matrix.size)
    val transposePrevVec = Vector(1.0, matrix.size)
    var curLambda = 1.0
    var prevLambda = -1.0
    val transposeMatrix = matrix.transpose()
    var counter = 0
    while(Math.abs(curLambda - prevLambda) > eps){
        for(i in 0 until defaultPrevVec.size)
            defaultPrevVec[i] = defaultCurVec[i]
        var temp = matrix * defaultPrevVec
        for(i in 0 until defaultCurVec.size)
            defaultCurVec[i] = temp[i]
        for(i in 0 until transposePrevVec.size)
            transposePrevVec[i] = transposeCurVec[i]
        temp = transposeMatrix * transposePrevVec
        for(i in 0 until transposeCurVec.size)
            transposeCurVec[i] = temp[i]
        prevLambda = curLambda
        curLambda = (transposeCurVec * defaultPrevVec) / (transposePrevVec * defaultPrevVec)
        counter++
        println("Scalar method iteration number $counter: $curLambda")
    }
    return curLambda
}

fun secondOwnNumber(matrix: Matrix, eps: Double): Double{
    val curVec = Vector(1.0, matrix.size)
    val prevVec = Vector(1.0, matrix.size)
    val prevPrevVec = Vector(1.0, matrix.size)
    var curLambda = 0.0
    var prevLambda = -1.0
    val maxAbsLambda = scalarMultiply(matrix, eps)
    var count = 0
    while(Math.abs(curLambda - prevLambda) > eps){
        for(i in 0 until prevPrevVec.size) {
            prevPrevVec[i] = prevVec[i]
            prevVec[i] = curVec[i]
        }
        val temp = matrix * prevVec
        for(i in 0 until curVec.size)
            curVec[i] = temp[i]
        prevLambda = curLambda
        curLambda =
            (curVec[0] - maxAbsLambda * prevVec[0]) /
                    (prevVec[0] - maxAbsLambda * prevPrevVec[0])
        count++

    }
    println("count: $count")
    return curLambda
}

fun reverseOwnNumber(matrix: Matrix, eps: Double): Double{
    val lambda = powerMethod(matrix, eps, false).num
    val modifyMatrix = matrix - Matrix(arrayOf(
        arrayOf(lambda, 0.0, 0.0),
        arrayOf(0.0, lambda, 0.0),
        arrayOf(0.0, 0.0, lambda)
    ))
    return lambda + powerMethod(modifyMatrix, eps, false).num
}

