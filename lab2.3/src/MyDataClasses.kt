// MyDataClasses.kt
//
data class Matrix(
    val matrix: Array<Array<Double>>
){
    constructor(initNumber: Double, dimension: Int):
            this(Array(dimension){Array(dimension){initNumber}})

    val size = matrix.size

    val norma: Double
        get(){
            val arrOfSums =
                Array(size){ matrix[it].fold(0.0){ acc, d -> acc + Math.abs(d)} }

            var largestElement = arrOfSums[0]

            for (n in arrOfSums)
                if (n > largestElement)
                    largestElement = n

            return largestElement
        }
    fun print(){
        for(i in 0 until size){
            for(j in 0 until size)
                print("${matrix[i][j]} ")
            println()
        }
        println()
    }
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
    operator fun plus(other: Matrix): Matrix{
        return Matrix(Array(size){i -> Array(size){j -> matrix[i][j] + other[i][j]}})
    }
    operator fun times(other: Vector): Vector{
        val resultArr = Array(other.size){0.0}
        var tempSum : Double
        for(i in 0 until matrix.size){
            tempSum = 0.0
            for(j in 0 until matrix[i].size){
                tempSum += matrix[i][j] * other[j]
            }
            resultArr[i] = tempSum
        }
        return Vector(resultArr)
    }
    operator fun times(other: Double): Matrix{
        return Matrix(Array(size){i -> Array(size){j -> matrix[i][j] * other}})
    }
    operator fun minus(other: Matrix): Matrix{
        return Matrix(Array(size){i -> Array(size){j -> matrix[i][j] - other[i][j]}})
    }
    operator fun get(i: Int) : Array<Double>{
        return matrix[i]
    }
    operator fun set(i: Int, arr: Array<Double>){
        for(j in 0 until size)
            matrix[i][j] = arr[j]
    }
}

data class Vector(
    val vector: Array<Double>
){
    constructor(initNumber: Double, dimension: Int):
            this(Array(dimension){initNumber})

    val size = vector.size

    val norma: Double
        get(){
            var maxElement = Math.abs(vector[0])
            val arrAbs = Array(vector.size){Math.abs(vector[it])}
            for (v in arrAbs)
                if (v > maxElement)
                    maxElement = v
            return maxElement
        }
    val indexMaxByAbs: Int
        get(){
            var maxElement = Math.abs(vector[0])
            var maxIndex = 0
            val arrAbs = Array(vector.size){Math.abs(vector[it])}
            for (i in 0 until arrAbs.size)
                if (arrAbs[i] > maxElement){
                    maxElement = arrAbs[i]
                    maxIndex = i
                }
            return maxIndex
        }
    fun print(){
        for(i in 0 until vector.size)
            print("${vector[i]} ")
        println()
    }
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
    operator fun plus(other: Vector): Vector{
        return Vector(Array(size){vector[it] + other[it]})
    }
    operator fun minus(other: Vector): Vector{
        return Vector(Array(size){vector[it] - other[it]})
    }
    operator fun times(other: Double): Vector{
        return Vector(Array(size){vector[it] * other})
    }
    operator fun get(i: Int): Double{
        return vector[i]
    }
    operator fun set(i: Int, num: Double){
        vector[i] = num
    }
}

data class ResultData(val root: Vector, val fst: Vector, val snd: Vector)