// MyFunction.kt
//
fun fixedPointIteration(matrix: Matrix, vector: Vector, eps: Double): Pair<Vector,Int>{
    val prev = Vector(0.0, vector.size)
    val cur = matrix * prev + vector
    var counter = 1
    while((cur - prev).norma > eps){
        for(i in 0 until prev.size)
            prev[i] = cur[i]
        val temp = matrix * prev + vector
        for(i in 0 until cur.size)
            cur[i] = temp[i]
        counter++
    }

    return Pair(cur, counter)
}

fun fixedPointIteration(matrix: Matrix, vector: Vector, times: Int): ResultData{
    val prev = Vector(0.0, vector.size)
    val cur = matrix * prev + vector
    var i = times - 1
    val prevPrev = Vector(0.0, vector.size)

    while(i > 0){
        if(i == 1){
            for(j in 0 until prevPrev.size)
                prevPrev[j] = prev[j]
        }
        for(j in 0 until prev.size)
            prev[j] = cur[j]
        val temp = matrix * prev + vector
        for(j in 0 until cur.size)
            cur[j] = temp[j]
        i--
    }

    return ResultData(cur, prev, prevPrev)
}

fun errorPriory(matrix: Matrix, vector: Vector, times: Int) =
    Math.pow(matrix.norma, times.toDouble()) / (1 - matrix.norma) * vector.norma

fun lusternik(root: Vector, prev: Vector, addVec: Vector): Vector{
    val y1 = root - prev
    val y2 = prev - addVec
    val lambdaMax = y1[y1.indexMaxByAbs]/ y2[y1.indexMaxByAbs]
    return root + (root - prev) * (lambdaMax / (1 - lambdaMax))
}

fun seidel(matrix: Matrix, vector: Vector, eps: Double): Pair<Vector, Int>{
    val prev = Vector(0.0, vector.size)
    val cur = Vector(0.0, vector.size)
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
    var counter = 1
    while((cur - prev).norma > eps){
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
        counter++
    }

    return Pair(cur, counter)
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

fun optimumParam(defaultMatrix: Matrix, defaultVector: Vector, times: Int): Vector{
    val param = 2.0 / (-0.878 + 1.217)
    val modifyMatrix = Matrix(arrayOf(
        arrayOf(1.0, 0.0, 0.0),
        arrayOf(0.0, 1.0, 0.0),
        arrayOf(0.0, 0.0, 1.0)
    )) - defaultMatrix * param
    val modifyVector = defaultVector * param
    return fixedPointIteration(modifyMatrix, modifyVector, times).root
}