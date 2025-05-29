package com.example.tetrizzzz.models

class Tablero {
    var filas = 20
    var columnas = 10
    var puntaje: Int = 0
    var matriz: Array<Array<Int>> = Array(filas) { Array(columnas) { 0 } }
    val colores = Array(filas) { Array(columnas) { 0 } }

    fun eliminarFilasCompletas(): Int {
        var filasEliminadas = 0
        val nuevasFilas = mutableListOf<Array<Int>>()
        val nuevosColores = mutableListOf<Array<Int>>()

        for (i in filas - 1 downTo 0) {
            if (matriz[i].all { it != 0 }) {
                filasEliminadas++
                continue
            }
            nuevasFilas.add(0, matriz[i])
            nuevosColores.add(0, colores[i])
        }
        while (nuevasFilas.size < filas) {
            nuevasFilas.add(0, Array(columnas) { 0 })
            nuevosColores.add(0, Array(columnas) { 0 })
        }
        for (i in 0 until filas) {
            matriz[i] = nuevasFilas[i]
            colores[i] = nuevosColores[i]
        }
        if (filasEliminadas > 0) {
            val puntosGanados = if (filasEliminadas == 1) {
                10
            } else {
                filasEliminadas * filasEliminadas * 10
            }
            puntaje += puntosGanados
        }
        return filasEliminadas
    }
}