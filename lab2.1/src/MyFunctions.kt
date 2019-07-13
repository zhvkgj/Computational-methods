// MyFunctions.kt
// all functions for 2x2 matrix
//
fun reverse(matrix: Matrix2d): Matrix2d{
    val det = matrix.determinant
    return Matrix2d(
        matrix.a22 / det,
        -matrix.a12 / det,
        -matrix.a21 / det,
        matrix.a11 / det
    )
}

fun conditionNumber(matrix: Matrix2d): Double =
    matrix.norma * reverse(matrix).norma

fun kramer(matrix: Matrix2d, vec: Vector2d): Vector2d{
    val det = matrix.determinant
    val det1 = vec.a1 * matrix.a22 - vec.a2 * matrix.a12
    val det2 = vec.a2 * matrix.a11 - vec.a1 * matrix.a21
    return Vector2d(det1 / det, det2 / det)
}

fun gauss(matrix: Matrix2d, vec: Vector2d): Vector2d{
    val root2 = (vec.a2 - matrix.a21 * (vec.a1 / matrix.a11)) /
            (matrix.a22 - matrix.a21 * (matrix.a12 / matrix.a11))
    val root1 = vec.a1 / matrix.a11 - (matrix.a12 / matrix.a11) * root2
    return Vector2d(root1, root2)
}