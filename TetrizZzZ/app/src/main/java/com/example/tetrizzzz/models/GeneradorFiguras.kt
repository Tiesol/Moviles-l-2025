package com.example.tetrizzzz.models
import kotlin.random.Random

object GeneradorFiguras {

    fun crearAleatoria(): Figura {
        val figura: Figura = when (Random.nextInt(7)) {
            0 -> FiguraCuadrado()
            1 -> FiguraRectangulo()
            2 -> FiguraMontana()
            3 -> FiguraL()
            4 -> FiguraLInvertida()
            5 -> FiguraEscalera()
            else -> FiguraEscaleraInvertida()
        }
        figura.rotarAleatoria()
        figura.colorAleatorio()
        return figura
    }
}
