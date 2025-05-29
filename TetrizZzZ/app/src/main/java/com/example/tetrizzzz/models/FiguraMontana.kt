package com.example.tetrizzzz.models

class FiguraMontana : Figura() {
    override val formas = listOf(
        arrayOf(
            arrayOf(0, 1, 0),
            arrayOf(1, 1, 1)
        ),
        arrayOf(
            arrayOf(0, 1, 0),
            arrayOf(0, 1, 1),
            arrayOf(0, 1, 0)
        ),
        arrayOf(
            arrayOf(1, 1, 1),
            arrayOf(0, 1, 0)
        ),
        arrayOf(
            arrayOf(0, 1, 0),
            arrayOf(1, 1, 0),
            arrayOf(0, 1, 0)
        )
    )
}
