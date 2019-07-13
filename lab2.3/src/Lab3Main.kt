// Lab1Main.kt
// k = 1
//
fun main() {
    // task 1
    val defaultMatrix = Matrix(arrayOf(
        arrayOf(1.1, -0.2, 0.1),
        arrayOf(-0.1, 1.1, -0.1),
        arrayOf(-0.3, 0.2, -0.9)
    ))
    val defaultVector = Vector(arrayOf(0.4, 0.8, -0.2))

    val correctRoot = Vector(arrayOf(267.0 / 512.0, 193.0 / 256.0, -117.0 / 512))

    val diagMatrix = Matrix(arrayOf(
        arrayOf(0.0, 0.2 / 1.1, -0.1 / 1.1),
        arrayOf(0.1 / 1.1, 0.0, 0.1 / 1.1),
        arrayOf(-0.3 / 0.9, 0.2 / 0.9, 0.0)
    ))
    val modifyVector = Vector(arrayOf(
        0.4 / 1.1,
        0.8 / 1.1,
        -0.2 / 0.9)
    )

    diagMatrix.print()
    val diagMatrixNorma = diagMatrix.norma
    println("Norma: $diagMatrixNorma")
    println("Correct root:")
    correctRoot.print()

    val result = fixedPointIteration(diagMatrix, modifyVector, 15)
    val x1 = result.root
    println("Root by Fix-Point Iteration:")
    x1.print()

    var prioryError = errorPriory(diagMatrix, modifyVector, 15)
    println("Error priory: $prioryError\n")

    val k = fixedPointIteration(diagMatrix, modifyVector, 0.00001).second
    prioryError = errorPriory(diagMatrix, modifyVector, k)
    println("Count of iteration: $k\nError priory: $prioryError")

    val x2 = lusternik(x1, result.fst, result.snd)
    println("Root by Lusternik:")
    x2.print()

    val x3 = seidel(diagMatrix, modifyVector, 15)
    println("Root by Seidel:")
    x3.print()
    val x4 = optimumParam(defaultMatrix, defaultVector, 150)
    println("Root by OptimumParameter:")
    x4.print()
}