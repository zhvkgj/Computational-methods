data class ArgsForMethods(
    val point: Double,
    val prevPoint: Double,
    val prevValue: Double,
    val step: Double,
    val f: (x: Double, y: Double) -> Double,
    val g: (x: Double) -> Double
)