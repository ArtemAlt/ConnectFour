package connectfour

import java.util.*
import kotlin.collections.ArrayList

const val FIRST_PLAYER_CH = "o"
const val SECOND_PLAYER_CH = "*"
const val NUMBERS_OF_WIN = 4


class Cell(s: String, posX: Int, posY: Int) {
    private var value: String = s
    private var posX: Int = posX
    private var posY: Int = posY

    fun getPosX(): Int {
        return this.posX
    }

    fun getValue(): String {
        return this.value
    }

    fun isEmpty(): Boolean {
        return this.value == "║ "
    }

    fun setNewChar(s: String) {
        this.value = "║$s"
    }

    fun getPosY(): Int {
        return this.posY
    }

}

class Board {
    private var rows = 0;
    private var columns = 0;
    private lateinit var body: ArrayList<ArrayList<Cell>>

    fun initBodyOfBoard(rows: Int, columns: Int): ArrayList<ArrayList<Cell>> {
        this.rows = rows;
        this.columns = columns
        this.body = arrayListOf()
        this.body.add(zeroLine())
        for (i in 1..rows) {
            body.add(generateRow(i))
        }
        body.add(lastLine())
        return body
    }

    private fun generateRow(num: Int): ArrayList<Cell> {
        val rows = ArrayList<Cell>()
        for (i in 1..columns + 1) rows.add(Cell("║ ", num, i))
        return rows
    }

    fun printBoard() {
        for (i in 0..this.rows + 1) {
            for (j in 0..this.columns) {
                print(body[i][j].getValue())
            }
            println()
        }
    }


    private fun lastLine(): ArrayList<Cell> {
        val col = arrayListOf<Cell>()
        col.add(Cell("╚", rows + 1, 1))
        for (i in 2..columns) col.add(Cell("═╩", rows + 1, i))
        col.add(Cell("═╝", rows + 1, columns - 1))
        return col
    }

    private fun zeroLine(): ArrayList<Cell> {
        val zeroLine = arrayListOf<Cell>()
        zeroLine.add(Cell(" ", 0, 0))
        for (i in 1..columns) zeroLine.add(Cell("$i ", 0, i))
        return zeroLine
    }


    fun setCharInColumn(col: Int, s: String) {
        val row = countEmptyRowIndexInColumn(col)
        if (row != 0) {
            setCharInCell(row, col - 1, s)
        } else {
            throw RuntimeException("$col")
        }
    }

    fun getColumn(index: Int): List<Cell> {
        val column = arrayListOf<Cell>()
        for (i in 0..rows) {
            column.add(body[i][index - 1])
        }
        return column
    }

    fun getRow(index: Int): List<Cell> {
        return body[index]
    }


    private fun countEmptyRowIndexInColumn(col: Int): Int {
        var index = 0
        val column = arrayListOf<Cell>()
        for (i in 0..rows) {
            column.add(body[i][col - 1])
        }
        column.reverse()
        for (i in 0 until column.size) {
            if (column[i].isEmpty()) {
                index = column[i].getPosX()
                break
            }
        }
        return index
    }

    private fun setCharInCell(posX: Int, posY: Int, s: String) {
        body[posX][posY].setNewChar(s)
    }

    fun isBoardFull(): Boolean {
        var result = true
        run breaker@{
            for (i in 1..this.rows) {
                for (j in 0 until this.columns) {
                    if (body[i][j].isEmpty()) {
                        result = false
                        return@breaker
                    }
                }
            }
        }
        return result
    }

    fun clear() {
        this.initBodyOfBoard(this.rows, this.columns)
    }
}

class Player(private var name: String, private var char: String, private var score: Int = 0) {

    fun getName(): String {
        return this.name
    }

    fun getChar(): String {
        return this.char
    }

    fun getScore(): Int {
        return this.score
    }

    fun setScore(score: Int) {
        this.score = score
    }

    fun incrementScore() {
        this.score++
    }

    fun scoreForWin() {
        this.score = this.score + 2
    }

}

class Game(p1: Player, p2: Player, b: Board) {
    private var currentPlayer: Player = p1
    private var firstPlayer: Player = p1
    private var secondPlayer: Player = p2
    private var board: Board = b
    var columns: Int = 0
    var rows: Int = 0
    private var isBoardFull = false
    private var isWinner = false
    private var isManualExit = false
    var isSingleGame = false
    private var totalGames: Int = 0
    var counter = 1;

    private fun setDrawScore() {
        firstPlayer.incrementScore()
        secondPlayer.incrementScore()
    }

    fun setTotalGames(turns: Int) {
        this.totalGames = turns
    }

    fun getTotalGames(): Int {
        return this.totalGames
    }

    private fun isWinner(p: Player): Boolean {
        val char = p.getChar()
        val currentSymbol = "║$char"
        var index = 0
        //проверка по горизонтали
        breakerRow@ for (i in 1..this.rows + 1) {
            val row = board.getRow(i)
            for (j in 0..this.columns) {
                val cellValue = row[j].getValue()
                if (cellValue == currentSymbol) {
                    index++
                    if (index == NUMBERS_OF_WIN) {
                        isWinner = true
                        break@breakerRow
                    }
                } else {
                    index = 0
                }
            }
        }
        // проверка по вертикали
        if (!isWinner) breakerCol@ for (i in 1..this.columns) {
            val col = board.getColumn(i)
            for (j in 1..this.rows) {
                val cellValue = col[j].getValue()
                if (cellValue == currentSymbol) {
                    index++
                    if (index == NUMBERS_OF_WIN) {
                        isWinner = true
                        break@breakerCol
                    }
                } else {
                    index = 0
                }
            }
        }
        //проверка диагоналей
        if (!isWinner) breakerDiog@ for (i in 1..this.rows - NUMBERS_OF_WIN + 1) {
            for (j in 0..this.columns - NUMBERS_OF_WIN) {
                for (k in 0 until NUMBERS_OF_WIN) {
//                    println("Diog right check - ${i + k} : ${j + k}")
                    if (this.board.getRow(i + k)[j + k].getValue() == currentSymbol) {
                        index++
                        if (index == NUMBERS_OF_WIN) {
                            isWinner = true
//                            println("Right diag")
                            break@breakerDiog
                        }
                    } else index = 0
                }
                index = 0
                for (k in 0 until NUMBERS_OF_WIN) {
                    if (this.board.getRow(i + k)[j + NUMBERS_OF_WIN - k - 1].getValue() == currentSymbol) {
                        index++
                        if (index == NUMBERS_OF_WIN) {
                            isWinner = true
                            break@breakerDiog
                        }
                    } else index = 0
                }
            }
        }

        return isWinner
    }

    private fun changeCurrentPlayer() {
        currentPlayer = if (currentPlayer == firstPlayer) secondPlayer else firstPlayer
    }

    fun initBoard() {
        var repeat: Boolean
        var rows = 6
        var columns = 7
        val reg = Regex("[1-9]?[0-9] *?\t?[xX] *?\t?[1-9]?[0-9]")
        do {
            println(
                "Set the board dimensions (Rows x Columns)\n" +
                        "Press Enter for default (6 x 7)"
            )
            val rAndC = readln().trim()
            if (rAndC.isEmpty()) {
                repeat = false
            } else if (reg.matches(rAndC)) {
                repeat = false
                val rac = rAndC.lowercase(Locale.getDefault()).split("x")
                rows = rac[0].trim().toInt()
                columns = rac[1].trim().toInt()
                if (rows !in 5..9) {
                    repeat = true
                    println("Board rows should be from 5 to 9")
                } else if (columns !in 5..9) {
                    repeat = true
                    println("Board columns should be from 5 to 9")
                }
            } else {
                println("Invalid input")
                repeat = true
            }
        } while (repeat)
        this.board.initBodyOfBoard(rows, columns)
        this.columns = columns
        this.rows = rows
    }

    fun printBoard() {
        board.printBoard()
    }

    fun play() {
        println("${currentPlayer.getName()}'s turn: ")
        val playerTurn = readln().trim()
        if (playerTurn == "end") {
            isManualExit = true
            totalGames = 0
        } else {
            val turn: Int
            try {
                turn = playerTurn.toInt()
                if (turn in 1..columns) {
                    try {
                        board.setCharInColumn(turn, currentPlayer.getChar())
                        board.printBoard()
                        if (board.isBoardFull()) {
                            isBoardFull = true
                            println("It is a draw")
                            setDrawScore()
                            changeCurrentPlayer()
                            if (!isSingleGame) {
                                println(
                                    "Score\n" +
                                            "${firstPlayer.getName()}: ${firstPlayer.getScore()} ${secondPlayer.getName()}: ${secondPlayer.getScore()}"
                                )
                            }

                        } else if (isWinner(currentPlayer)) {
                            println("Player ${currentPlayer.getName()} won")
                            currentPlayer.scoreForWin()
                            changeCurrentPlayer()
                            if (!isSingleGame) {
                                println(
                                    "Score\n" +
                                            "${firstPlayer.getName()}: ${firstPlayer.getScore()} ${secondPlayer.getName()}: ${secondPlayer.getScore()}"
                                )
                            }
                        } else {
                            changeCurrentPlayer()
                        }
                    } catch (e: RuntimeException) {
                        println("Column ${e.message} is full")
                    }

                } else {
                    println("The column number is out of range (1 - $columns)")
                }
            } catch (e: NumberFormatException) {
                println("Incorrect column number")
            }
        }
    }

    fun end(): Boolean {
        if (isWinner) {
            totalGames--
            counter++
            this.board.clear()
        }
        if (isBoardFull) {
            totalGames--
            counter++
            this.board.clear()
        }
        return isBoardFull || isWinner || isManualExit
    }

    fun printIntro() {
        when (totalGames) {
            1 -> {
                println("Single game")
                isSingleGame = true
            }

            else -> {
               if(!isSingleGame) println(
                    "Total $totalGames games\n"
                )
            }

        }
    }

    fun drop() {
        this.isBoardFull = false
        this.isWinner = false
        this.isManualExit = false
    }
}

fun main() {
    println("Connect Four")
    println("First player's name:")
    val firstPlayerName = readln()
    val p1 = Player(firstPlayerName, FIRST_PLAYER_CH)
    println("Second player's name:")
    val secondPlayerName = readln()
    val p2 = Player(secondPlayerName, SECOND_PLAYER_CH)
    val game = Game(p1, p2, Board())
    game.initBoard()
    var totalGames = 0;
    do {
        println(
            "Do you want to play single or multiple games?\n" +
                    "For a single game, input 1 or press Enter\n" +
                    "Input a number of games:"
        )
        try {
            val tmp = readln().trim()
            if (tmp.isEmpty()) {
                totalGames = 1
            } else {
                totalGames = tmp.toInt()
                if (totalGames == 0) throw Exception()
        }

        } catch (e: Exception) {
            println("Invalid input")
        }

    } while (totalGames <= 0)
    game.setTotalGames(totalGames)
    println("$firstPlayerName VS $secondPlayerName\n${game.rows} X ${game.columns} board")
    game.printIntro()
    do {
        game.drop()
        if (!game.isSingleGame) println("Game #${game.counter}")
        game.printBoard()
        do {
            game.play()
        } while (!game.end())

    } while (game.getTotalGames() > 0)
    println("Game over!")
}




