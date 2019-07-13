data class Args(
    val f: (Double, Double) -> Double,
    val g: (Double) -> Double,
    val defaultPoint: Double,
    val defaultValue: Double,
    val step: Double,
    val lastIndex: Int
)