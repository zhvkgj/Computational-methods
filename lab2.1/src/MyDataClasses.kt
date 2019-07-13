// MyDataClasses.kt
//
data class Matrix2d(
    val a11: Double,
    val a12: Double,
    val a21: Double,
    val a22: Double
){
    constructor(initNumber: Double): this(
        initNumber,
        initNumber,
        initNumber,
        initNumber
    )
    val norma: Double
        get(){
            val sum1 = Math.abs(a11) + Math.abs(a12)
            val sum2 = Math.abs(a21) + Math.abs(a22)
            return if (sum1 < sum2) sum2 else sum1
        }
    val determinant: Double
        get() = a11 * a22 - a12 * a21

    operator fun plus(other: Matrix2d): Matrix2d{
        return Matrix2d(
            a11 + other.a11,
            a12 + other.a12,
            a21 + other.a21,
            a22 + other.a22
        )
    }
    operator fun minus(other: Matrix2d): Matrix2d{
        return Matrix2d(
            a11 - other.a11,
            a12 - other.a12,
            a21 - other.a21,
            a22 - other.a22
        )
    }
}

data class Vector2d(
    val a1: Double,
    val a2: Double
){
    val norma: Double
        get() =
            if (Math.abs(a1) < Math.abs(a2))
                Math.abs(a2)
            else Math.abs(a1)

    operator fun plus(other: Vector2d): Vector2d{
        return Vector2d(
            a1 + other.a1,
            a2 + other.a2
        )
    }
    operator fun minus(other: Vector2d): Vector2d{
        return Vector2d(
            a1 - other.a1,
            a2 - other.a2
        )
    }
}
