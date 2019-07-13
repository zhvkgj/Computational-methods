import java.util.*

fun findMaxAbsElement(matrix: Array<Array<Double>>, visitedRows: Stack<Int>, visitedCols: Stack<Int>): Pair<Int,Int>{
    var maxElem = 0.0
    var xPos = 0
    var yPos = 0
    for (i in 0 until matrix.size){
        if (visitedRows.contains(i))
            continue
        for (j in 0 until matrix[0].size - 1){
            if (visitedCols.contains(j))
                continue
            val curMaxElem = Math.abs(matrix[i][j])
            if (maxElem < curMaxElem) {
                maxElem = curMaxElem
                xPos = i
                yPos = j
            }
        }
    }
    return Pair(xPos, yPos)
}

fun cutRowAndCol(matrix: Array<Array<Double>>,
                    row: Int,
                    col: Int)
        : Array<Array<Double>> {
    val resultMatrix =
        Array(matrix.size - 1){Array(matrix[0].size - 1){0.0}}
    for (i in 0 until row) {
        for (j in 0 until col)
            resultMatrix[i][j] = matrix[i][j]
        for (j in col until resultMatrix[0].size)
            resultMatrix[i][j] = matrix[i][j + 1]
    }
    for (i in row until resultMatrix.size) {
        for (j in 0 until col)
            resultMatrix[i][j] = matrix[i + 1][j]
        for (j in col until resultMatrix[0].size)
            resultMatrix[i][j] = matrix[i + 1][j + 1]
    }
    return resultMatrix
}

fun printMatrix(matrix: Array<Array<Double>>){
    for (i in 0 until matrix.size)
        matrix[i].joinToString(" ").also{println(it)}
    println()
}

fun printVector(vec: Array<Double>){
    for (i in 0 until vec.size)
        print("${vec[i]} ")
    println("\n")
}

fun gaussWithMainElement(matrix: Array<Array<Double>>): Array<Double> {
    // direct move
    val curMatrix =
        Array(matrix.size){j ->
            Array(matrix[0].size)
            {matrix[j][it]}}
    val stackOfMainRows = Stack<Array<Double>>()
    val visitedRows = Stack<Int>()
    val visitedCols = Stack<Int>()
    for (k in 0 until matrix.size){
        val mainRow = findMaxAbsElement(curMatrix, visitedRows, visitedCols).first
        val mainCol = findMaxAbsElement(curMatrix, visitedRows, visitedCols).second
        val mainElem = curMatrix[mainRow][mainCol]
        val mArr = Array(matrix.size){curMatrix[it][mainCol] / mainElem}
        stackOfMainRows.add(curMatrix[mainRow])
        for (i in 0 until curMatrix.size) {
            if (i == mainRow || visitedRows.contains(i))
                continue
            for (j in 0 until curMatrix[0].size) {
                if (visitedCols.contains(j))
                    continue
                curMatrix[i][j] = curMatrix[i][j] - curMatrix[mainRow][j] * mArr[i]
            }
        }
        visitedRows.add(mainRow)
        visitedCols.add(mainCol)
        printMatrix(curMatrix)
    }
    // reverse
    val resultVec = Array(matrix.size){0.0}
    for (i in 0 until resultVec.size){
        val curRow = stackOfMainRows.pop()
        val curRoot = visitedCols.pop()
        resultVec[curRoot] = curRow[curRow.size - 1]
        for (j in 0 until resultVec.size) {
            if (j == curRoot)
                continue
            resultVec[curRoot] -= curRow[j] * resultVec[j]
        }
        resultVec[curRoot] /= curRow[curRoot]
    }
    return resultVec
}
