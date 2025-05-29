package com.example.tetrizzzz.models

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.example.tetrizzzz.observer.Observable
import com.example.tetrizzzz.observer.Observer
import kotlin.random.Random

abstract class Figura(
    var fila: Int = -1,
    var columna: Int = 4,
) : Observable {

    private var observer: Observer? = null
    private var rotacion = 0
    private var color: Int = 0
    private var moviendo = false
    private var bloqueado = false
    private var hiloMovimiento: Thread? = null

    var velocidadActual: Long = 1000L
    abstract val formas: List<Array<Array<Int>>>

    fun getFormaActual(): Array<Array<Int>> = formas[rotacion]

    fun estaBloqueado(): Boolean = bloqueado
    fun desbloquear() { bloqueado = false }

    fun estaMoviendo(): Boolean = moviendo

    fun moverIzquierda(tablero: Tablero) {
        if (puedeMover(tablero, 0, -1)) {
            columna--
            notifyObservers()
        }
    }

    fun moverDerecha(tablero: Tablero) {
        if (puedeMover(tablero, 0, 1)) {
            columna++
            notifyObservers()
        }
    }

    fun bajarHastaFondo(tablero: Tablero, onTerminar: (() -> Unit)? = null) {
        if (!puedeMoverAbajo(tablero)) return
        hiloMovimiento?.interrupt()
        hiloMovimiento?.join()
        moviendo = false

        while (puedeMoverAbajo(tablero)) {
            fila++
        }

        fijarEnTablero(tablero)
        notifyObservers()
        onTerminar?.invoke()
    }

    fun rotar(tablero: Tablero) {
        val nuevaRotacion = (rotacion + 1) % formas.size
        val formaRotada = formas[nuevaRotacion]

        for (i in formaRotada.indices) {
            for (j in formaRotada[i].indices) {
                if (formaRotada[i][j] != 0) {
                    val nuevaFila = fila + i
                    val nuevaCol = columna + j
                    if (colisiona(tablero, nuevaFila, nuevaCol)) return
                }
            }
        }

        rotacion = nuevaRotacion
        notifyObservers()
    }

    fun rotarAleatoria() {
        rotacion = Random.nextInt(formas.size)
        notifyObservers()
    }

    fun colorAleatorio() {
        color = listOf(
            Color.RED, Color.GREEN, Color.BLUE, Color.CYAN,
            Color.MAGENTA, Color.YELLOW,
            Color.rgb(255, 165, 0),
            Color.rgb(128, 0, 128)
        ).random()
    }

    fun draw(canvas: Canvas, paint: Paint, anchoCelda: Int, altoCelda: Int) {
        val forma = getFormaActual()
        paint.color = color

        for (i in forma.indices) {
            for (j in forma[i].indices) {
                if (forma[i][j] != 0) {
                    val left = ((columna + j) * anchoCelda).toFloat()
                    val top = ((fila + i) * altoCelda).toFloat()
                    val right = left + anchoCelda
                    val bottom = top + altoCelda
                    canvas.drawRect(left, top, right, bottom, paint)
                }
            }
        }
    }

    fun moverAutomaticamente(tablero: Tablero) {
        if (moviendo) return
        moviendo = true

        hiloMovimiento = Thread {
            try {
                while (puedeMoverAbajo(tablero)) {
                    fila++
                    notifyObservers()
                    Thread.sleep(velocidadActual)
                }

                val tiempoGracia = System.currentTimeMillis() + velocidadActual
                while (System.currentTimeMillis() < tiempoGracia) {
                    if (Thread.interrupted()) return@Thread
                    if (puedeMoverAbajo(tablero)) {
                        fila++
                        notifyObservers()
                        Thread.sleep(velocidadActual)
                        return@Thread
                    }
                    Thread.sleep(10)
                }

                fijarEnTablero(tablero)
                notifyObservers()

            } catch (_: InterruptedException) {
            } finally {
                moviendo = false
            }
        }

        hiloMovimiento?.start()
    }

    private fun puedeMover(tablero: Tablero, deltaFila: Int, deltaCol: Int): Boolean {
        val forma = getFormaActual()

        for (i in forma.indices) {
            for (j in forma[i].indices) {
                if (forma[i][j] != 0) {
                    val nuevaFila = fila + i + deltaFila
                    val nuevaCol = columna + j + deltaCol
                    if (colisiona(tablero, nuevaFila, nuevaCol)) return false
                }
            }
        }

        return true
    }

    private fun puedeMoverAbajo(tablero: Tablero): Boolean {
        val forma = getFormaActual()

        for (i in forma.indices) {
            for (j in forma[i].indices) {
                if (forma[i][j] != 0) {
                    val nuevaFila = fila + i + 1
                    val nuevaCol = columna + j
                    if (colisiona(tablero, nuevaFila, nuevaCol)) return false
                }
            }
        }

        return true
    }

    fun colisiona(tablero: Tablero, fila: Int, col: Int): Boolean {
        return fila >= tablero.filas ||
                col < 0 || col >= tablero.columnas ||
                tablero.matriz.getOrNull(fila)?.getOrNull(col) != 0
    }

    private fun fijarEnTablero(tablero: Tablero) {
        val forma = getFormaActual()

        for (i in forma.indices) {
            for (j in forma[i].indices) {
                if (forma[i][j] != 0) {
                    val filaTab = fila + i
                    val colTab = columna + j

                    if (filaTab in 0 until tablero.filas && colTab in 0 until tablero.columnas) {
                        tablero.matriz[filaTab][colTab] = 1
                        tablero.colores[filaTab][colTab] = color
                    }
                }
            }
        }

        val filasEliminadas = tablero.eliminarFilasCompletas()
        if (filasEliminadas > 0) {
            observer?.let {
                if (it is com.example.tetrizzzz.ui.components.TableroView) {
                    it.onScoreUpdated?.invoke(tablero.puntaje)
                }
            }
        }
    }

    override fun addObserver(observer: Observer) {
        this.observer = observer
    }

    override fun notifyObservers() {
        observer?.update()
    }
}
