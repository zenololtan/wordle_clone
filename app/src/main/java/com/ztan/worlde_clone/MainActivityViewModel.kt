package com.ztan.worlde_clone

import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.lifecycle.ViewModel
import com.ztan.worlde_clone.models.Cell
import com.ztan.worlde_clone.models.RandomWords
import com.ztan.worlde_clone.models.State

class MainActivityViewModel: ViewModel() {
    private val rm: RandomWords = RandomWords()
    private var secretWord: String = rm.newWord()
    private var state: State = State.PLAYING
    private var turnCount: Int = 0

    private fun cheatCode(): MutableList<Cell> {
        val ret: MutableList<Cell> = mutableListOf<Cell>()

        for (letter in secretWord) {
            ret.add(Cell(letter.toString(), "#787c7f"))
        }
        return ret
    }

    private fun checkWord(guess: String): MutableList<Cell> {
        val remainder: StringBuilder = StringBuilder(secretWord)
        val ret: MutableList<Cell> = mutableListOf<Cell>()
        var check: Int = 0

        if (guess == "cheat")
            return cheatCode()
        for (i: Int in 0 until secretWord.length) {
            var foundAt: Int = remainder.indexOf(guess[i])

            // If the letter is correct and position is correct
            if (i == foundAt) {
                ret.add(Cell(guess[i].toString(), "#6ca965"))
                remainder.setCharAt(foundAt, '0')
                check++
                continue
            }
            // If correct letter and incorrect position
            while (foundAt > -1 && remainder[foundAt] == guess[foundAt])
                foundAt = remainder.indexOf(guess[i], foundAt + 1)
            if (foundAt > -1) {
                ret.add(Cell(guess[i].toString(), "#c8b653"))
                remainder.setCharAt(foundAt, '0')
                continue
            }
            // If incorrect
            ret.add(Cell(guess[i].toString(), "#787c7f"))
        }
        if (check == secretWord.length)
            state = State.WON
        return ret
    }

    fun addLine(row: RelativeLayout, guess: String)  {
        val res: MutableList<Cell> = checkWord(guess)

        row.children.forEachIndexed { index: Int, element: View ->
            val child: TextView = element as TextView

            child.text = res[index].char
            child.setBackgroundColor(Color.parseColor(res[index].color))
        }
        turnCount++
        if (turnCount >= 5)
            state = State.LOST
    }

    fun resetGame(linearLayout: LinearLayout) {
        secretWord = rm.newWord()
        state = State.PLAYING
        turnCount = 0

        while (linearLayout.childCount != 0) {
            linearLayout.removeView(linearLayout.children.first())
        }
    }

    fun getState(): State {
        return state
    }

}