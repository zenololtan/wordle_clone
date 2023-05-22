package com.ztan.worlde_clone.models

import kotlin.random.Random

class RandomWords {
    private val words: Words = Words()

    fun newWord(): String {
        return words.array[Random.nextInt(0, words.array.size - 1)]
    }
}