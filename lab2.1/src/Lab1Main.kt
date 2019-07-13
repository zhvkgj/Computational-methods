// Lab1Main.kt
//
fun main() {
    // task 1
    val A = Matrix2d(
        a11 = -403.15,
        a12 = 200.95,
        a21 = 1205.70,
        a22 = -604.10
    )
    val b = Vector2d(
        a1 = 200.0,
        a2 = -600.0
    )
    val deltaB = Vector2d(
        a1 = -1.0,
        a2 = -1.0
    )
    val x1 = kramer(A, b)
    val mA = conditionNumber(A)
    val x2 = kramer(A, b + deltaB)
    val factErrorTask1 = (x2 - x1).norma / x1.norma
    val theoryCondError = mA * deltaB.norma / b.norma
    val theoryError = deltaB.norma / x1.norma * reverse(A).norma
    println("Task 1\nRoot Kramer:\n${x1.a1}\n${x1.a2}\nRoot Kramer with disturbance:\n" +
            "${x2.a1}\n" +
            "${x2.a2}\nCondition number: $mA\n\n" +
            "Actual relative error: $factErrorTask1\n" +
            "Theoretical relative error: $theoryCondError\n" +
            "Other theoretical relative error: $theoryError")
    println()
    // task 2
    val y1 = kramer(A, b + deltaB)
    val y2 = gauss(A, b + deltaB)
    val z1 = kramer(A - Matrix2d(500.0), b + deltaB)
    val z2 = gauss(A - Matrix2d(500.0), b + deltaB)
    println("Task 2\nRoot Kramer:\n${y1.a1}\n${y1.a2}\nRoot Gauss:\n" +
            "${y2.a1}\n" +
            "${y2.a2}\n" +
            "Root Kramer with other matrix:\n${z1.a1}\n${z1.a2}\nRoot Gauss with other matrix:\n" +
            "${z2.a1}\n" +
            "${z2.a2}")
    println()
    // task 3
    val c = Vector2d(
        a1 = 200.0,
        a2 = 420.0
    )
    val B = Matrix2d(
        a11 = -400.6,
        a12 = 199.8,
        a21 = 1198.8,
        a22 = -600.4
    )
    val w1 = gauss(A, c)
    val w2 = gauss(B, c)
    val errorMatrix = (B - A).norma / A.norma
    val factErrorTask3 = (w2 - w1).norma / w1.norma
    println("Task 3\nRoot Gauss:\n${w1.a1}\n${w1.a2}\nRoot Gauss with other matrix:\n" +
            "${w2.a1}\n" +
            "${w2.a2}\nMatrix actual relative error: $errorMatrix\n" +
            "Root actual relative error: $factErrorTask3")
    println()
}