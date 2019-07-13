// Lab1Main.kt
// k = 1
//
fun main() {
    // task 1
    val defaultMatrix = Matrix(arrayOf(
        arrayOf(-1.478867, -0.093574, 0.912588),
        arrayOf(-0.093574, 1.106642, 0.032985),
        arrayOf(0.912588, 0.032985, -1.482249)
    ))
    defaultMatrix.print()

    var result = powerMethod(defaultMatrix, 0.00001, true)
    val num = result.num
    println("Power method: $num")
    val vec = result.vec
    println("Vector:")
    vec.print()
    val count = result.count
    println("Count of iterations: $count")
    println()
    val correctlyNum = aitkenMod(defaultMatrix, 0.00001)
    println("Aitken's method result: $correctlyNum")
    println()
    val scalarNum = scalarMultiply(defaultMatrix, 0.00001)
    println("Scalar method result: $scalarNum")
    val secondOwnNum = reverseOwnNumber(defaultMatrix, 0.00001)
    println("Reverse own number: $secondOwnNum")
}