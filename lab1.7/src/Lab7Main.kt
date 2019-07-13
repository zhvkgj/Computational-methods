import java.util.*

val scan = Scanner(System.`in`)

fun evalCorrectValue(
    point: Double,
    g: (x: Double) -> Double
) =
    ReturnType(
        message = "Correct value",
        point = point,
        value = g(point),
        error = 0.0
    )

fun evalValuesByTailor(
    data: ArgsForMethods,
    derivative: (Double, Double) -> Double
) : ReturnType {
    val result = data.prevValue + data.step * data.f(
        data.prevPoint,
        data.prevValue
    ) + data.step * data.step / 2 * derivative(
        data.prevPoint,
        data.prevValue
    )
    val correctResult = evalCorrectValue(
        data.point,
        data.g
    ).value
    return ReturnType(
        message = "Tailor's method value",
        point = data.point,
        value = result,
        error = Math.abs(result - correctResult))
}

fun evalValuesByAdamsFourthOrder(
    point: Double,
    prevValue: Double,
    coefficients: Array<Double>,
    g: (Double) -> Double
) : ReturnType {
    val result = prevValue +
            coefficients[0] +
            1.0 / 2 * coefficients[1] +
            5.0 / 12 * coefficients[2] +
            3.0 / 8 * coefficients[3] +
            251.0 / 720 * coefficients[4]
    val correctResult = evalCorrectValue(
        point,
        g
    ).value
    return ReturnType(
        message = "The fourth order Adams's method value",
        point = point,
        value = result,
        error = Math.abs(result - correctResult))
}

fun evalValuesByRungeKuttaFourthOrder(data: ArgsForMethods) : ReturnType {
    val k1 = data.step * data.f(
        data.prevPoint,
        data.prevValue
    )
    val k2 = data.step * data.f(
        data.prevPoint + data.step / 2,
        data.prevValue + k1 / 2
    )
    val k3 = data.step * data.f(
        data.prevPoint + data.step / 2,
        data.prevValue + k2 / 2
    )
    val k4 = data.step * data.f(
        data.prevPoint + data.step,
        data.prevValue + k3
    )
    val result = data.prevValue + 1.0 / 6 * (k1 + 2 * k2 + 2 * k3 + k4)
    val correctResult = evalCorrectValue(
        data.point,
        data.g
    ).value
    return ReturnType(
        message = "The fourth order Runge-Kutta's method value",
        point = data.point,
        value = result,
        error = Math.abs(result - correctResult))
}

fun evalValuesByEuler(data: ArgsForMethods) : ReturnType {
    val result = data.prevValue + data.step * data.f(
        data.prevPoint,
        data.prevValue
    )
    val correctResult = evalCorrectValue(
        data.point,
        data.g
    ).value
    return ReturnType(
        message = "Euler's method value",
        point = data.point,
        value = result,
        error = Math.abs(result - correctResult))
}

fun evalValuesByAdvancedEuler(data: ArgsForMethods) : ReturnType {
    val result = data.prevValue + data.step *
            data.f(
                data.prevPoint + data.step / 2,
                data.prevValue + data.step / 2 *
                        data.f(
                            data.prevPoint,
                            data.prevValue
                        )
            )
    val correctResult = evalCorrectValue(
        data.point,
        data.g
    ).value
    return ReturnType(
        message = "Advanced Euler's method value",
        point = data.point,
        value = result,
        error = Math.abs(result - correctResult)
    )
}

fun evalValuesByEulerKoshi(data: ArgsForMethods) : ReturnType {
    val preResult = data.prevValue + data.step * data.f(
        data.prevPoint,
        data.prevValue
    )
    val result = data.prevValue + data.step / 2 * (data.f(
        data.prevPoint,
        data.prevValue
    ) + data.f(
        data.point,
        preResult
    ))
    val correctResult = evalCorrectValue(
        data.point,
        data.g
    ).value
    return ReturnType(
        message = "Euler-Koshi's method value",
        point = data.point,
        value = result,
        error = Math.abs(result - correctResult)
    )
}

fun printCorrectResults(data: Args){
    println("Correct values")
    for (i in -2..data.lastIndex) {
        val curPoint = data.defaultPoint + i * data.step
        println("%.14f".format(curPoint) +
                " | " +
                "%.14f".format(evalCorrectValue(
                    curPoint,
                    data.g
                ).value))
    }
    println()
}

fun printEulerAndRungeKuttaResults(
    data: Args,
    func: (ArgsForMethods) -> ReturnType
){
    val points = Array(data.lastIndex + 1) {0.0}
    val values = Array(data.lastIndex + 1) {0.0}
    val errors = Array(data.lastIndex + 1) {0.0}
    var info = "Error!"
    points[0] = data.defaultPoint
    for (i in 1 until points.size)
        points[i] = points[0] + i * data.step
    values[0] = data.defaultValue
    for(i in 1 until values.size){
        val arguments = ArgsForMethods(
            point = points[i],
            prevPoint = points[i - 1],
            prevValue = values[i - 1],
            step = data.step,
            f = data.f,
            g = data.g
        )
        val res = func(arguments)
        values[i] = res.value
        errors[i] = res.error
        if (i == 1) info = res.message
    }
    println(info)
    for(i in 1 until values.size)
        println("%.14f".format(points[i]) +
                " | " +
                "%.14f".format(values[i]) +
                " | " +
                "%.14f".format(errors[i]))
    println()
}

fun printTailorResults(
    data: Args,
    dev: (Double, Double) -> Double
){
    val points = Array(6) {0.0}
    val values = Array(6) {0.0}
    val errors = Array(6) {0.0}
    var info = "Error!"
    points[0] = data.defaultPoint - 3 * data.step
    for (i in 1 until points.size)
        points[i] = data.defaultPoint + (i - 3) * data.step
    values[0] = evalCorrectValue(
        points[0],
        data.g
    ).value
    for(i in 1 until values.size){
        val arguments = ArgsForMethods(
            point = points[i],
            prevPoint = points[i - 1],
            prevValue = values[i - 1],
            step = data.step,
            f = data.f,
            g = data.g
        )
        val res = evalValuesByTailor(
            arguments,
            dev
        )
        values[i] = res.value
        errors[i] = res.error
        if (i == 1) info = res.message
        values.fill
    }
    println(info)
    for(i in 1 until values.size)
        println("%.14f".format(points[i]) +
                " | " +
                "%.14f".format(values[i]) +
                " | " +
                "%.14f".format(errors[i]))
    println()
}

fun printAdamsResult(
    data: Args,
    dev: (Double, Double) -> Double
){
    val table = Array(data.lastIndex + 3)
    {Array(8)
    {0.0}}

    for (i in 0..data.lastIndex + 2)
        table[i][0] = data.defaultPoint + (i - 2) * data.step
    val backfieldTableValue = evalValuesByTailor(
        ArgsForMethods(
            point = table[0][0],
            prevPoint = data.defaultPoint - 3 * data.step,
            prevValue = evalCorrectValue(
                data.defaultPoint - 3 * data.step,
                data.g
            ).value,
            step = data.step,
            f = data.f,
            g = data.g
        ),
        dev
    )
    table[0][1] = backfieldTableValue.value
    table[0][2] = backfieldTableValue.error

    for (i in 1..4) {
        val arguments = ArgsForMethods(
            point = table[i][0],
            prevPoint = table[i - 1][0],
            prevValue = table[i - 1][1],
            step = data.step,
            f = data.f,
            g = data.g
        )
        val res = evalValuesByTailor(
            arguments,
            dev
        )
        table[i][1] = res.value
        table[i][2] = res.error
    }
    for (i in 0..4) {
        table[i][3] = data.step * data.f(
            table[i][0],
            table[i][1]
        )
    }
    var k = 4
    for (j in 4..7) {
        k--
        for (i in 0..k) {
            table[i][4] = table[i + 1][j - 1] - table[i][j - 1]
        }
    }
    var info = "Error!"
    for (i in 5..data.lastIndex + 2){
        val coefficients = arrayOf(
            table[i - 1][3],
            table[i - 2][4],
            table[i - 3][5],
            table[i - 4][6],
            table[i - 5][7]
        )
        val res = evalValuesByAdamsFourthOrder(
            point = table[i][0],
            prevValue = table[i - 1][1],
            coefficients = coefficients,
            g = data.g
        )
        table[i][1] = res.value
        table[i][2] = res.error
        table[i][3] = data.step * data.f(
            table[i][0],
            table[i][1]
        )
        if (i == 5) info = res.message
    }
    println(info)
    for (i in 0..data.lastIndex + 2)
        println("%.14f".format(table[i][0]) +
                " | " +
                "%.14f".format(table[i][1]) +
                " | " +
                "%.14f".format(table[i][2]) +
                " | " +
                "%.14f".format(table[i][3]))
    println()
}

fun printTables(
    data: Args,
    derivative: (Double, Double) -> Double
){
    printCorrectResults(data)
    val f1: (ArgsForMethods) -> ReturnType =
        {evalValuesByEuler(it)}
    val f2: (ArgsForMethods) -> ReturnType =
        {evalValuesByAdvancedEuler(it)}
    val f3: (ArgsForMethods) -> ReturnType =
        {evalValuesByEulerKoshi(it)}
    val f4: (ArgsForMethods) -> ReturnType =
        {evalValuesByRungeKuttaFourthOrder(it)}
    listOf(f1, f2, f3, f4)
        .forEach {printEulerAndRungeKuttaResults(data, it)}
    printTailorResults(data, derivative)
    printAdamsResult(data, derivative)
}

fun main(args: Array<String>){
    val f: (x: Double, y: Double) -> Double = {x, y -> -y + Math.sin(x)}
    val g: (Double) -> Double = {1.5 * Math.exp(-it) + Math.sin(it) / 2 - Math.cos(it) / 2}
    val dev: (Double, Double) -> Double = {x, y -> y - Math.sin(x) + Math.cos(x)}
    println("Enter an evaluation step: ")
    val h = scan.nextDouble()
    println("Enter the last index: ")
    val n = scan.nextInt()
    println()
    val data = Args(
        defaultPoint = 0.0,
        defaultValue = 1.0,
        f = f,
        g = g,
        lastIndex = n,
        step = h
    )
    printTables(data, dev)
}