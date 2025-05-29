package com.example.tetrizzzz.ui.components

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.tetrizzzz.models.Figura
import com.example.tetrizzzz.models.GeneradorFiguras
import com.example.tetrizzzz.models.Tablero
import com.example.tetrizzzz.observer.Observer
import com.example.tetrizzzz.ui.activities.ScoreActivity

class TableroView(context: Context?, attrs: AttributeSet?) : View(context, attrs), Observer {

    private var model = Tablero()
    private var figuraActual: Figura = GeneradorFiguras.crearAleatoria().apply {
        addObserver(this@TableroView)
        moverAutomaticamente(model)
    }

    var onScoreUpdated: ((Int) -> Unit)? = null

    private val paint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = 2f
        isAntiAlias = true
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val ancho = width / model.columnas
        val alto = height / model.filas

        for (i in 0 until model.filas) {
            for (j in 0 until model.columnas) {
                val colorCelda = model.matriz[i][j]
                paint.color = if (colorCelda == 0) Color.TRANSPARENT else model.colores[i][j]
                canvas.drawRect(
                    (j * ancho).toFloat(),
                    (i * alto).toFloat(),
                    ((j + 1) * ancho).toFloat(),
                    ((i + 1) * alto).toFloat(),
                    paint
                )
                paint.color = Color.BLACK
                paint.style = Paint.Style.STROKE
                canvas.drawRect(
                    (j * ancho).toFloat(),
                    (i * alto).toFloat(),
                    ((j + 1) * ancho).toFloat(),
                    ((i + 1) * alto).toFloat(),
                    paint
                )
                paint.style = Paint.Style.FILL_AND_STROKE
            }
        }

        figuraActual.draw(canvas, paint, ancho, alto)
    }

    override fun update() {
        post {
            invalidate()
            if (!figuraActualPuedeSeguir() && !figuraActual.estaMoviendo()) {
                figuraActual.desbloquear()
                val nivel = if (model.puntaje >= 100) 5 else 0
                val nuevaVelocidad = (1000L - (nivel * 100L)).coerceAtLeast(100L)
                val nuevaFigura = GeneradorFiguras.crearAleatoria().apply {
                    colorAleatorio()
                    addObserver(this@TableroView)
                    velocidadActual = nuevaVelocidad
                }
                if (esGameOver(nuevaFigura)) {
                    val intent = Intent(context, ScoreActivity::class.java)
                    intent.putExtra("FINAL_SCORE", model.puntaje)
                    context.startActivity(intent)
                    return@post
                }
                figuraActual = nuevaFigura
                figuraActual.moverAutomaticamente(model)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val mitadPantalla = width / 2
            if (event.x < mitadPantalla) {
                figuraActual.moverIzquierda(model)
            } else {
                figuraActual.moverDerecha(model)
            }
            return true
        }
        return super.onTouchEvent(event)
    }


    fun rotarFigura() {
        if (figuraActual.estaBloqueado()) return
        figuraActual.rotar(model)
    }

    fun moverFiguraHastaFondo() {
        figuraActual.bajarHastaFondo(model) {
            val nivel = if (model.puntaje >= 100) 5 else 0
            val nuevaVelocidad = (1000L - (nivel * 100L)).coerceAtLeast(100L)
            val nuevaFigura = GeneradorFiguras.crearAleatoria().apply {
                colorAleatorio()
                addObserver(this@TableroView)
                velocidadActual = nuevaVelocidad
            }
            if (esGameOver(nuevaFigura)) {
                val intent = Intent(context, ScoreActivity::class.java)
                intent.putExtra("FINAL_SCORE", model.puntaje)
                context.startActivity(intent)
            }
            figuraActual = nuevaFigura
            figuraActual.moverAutomaticamente(model)
        }
        invalidate()
    }

    private fun figuraActualPuedeSeguir(): Boolean {
        val forma = figuraActual.getFormaActual()
        val filaActual = figuraActual.fila
        val columnaActual = figuraActual.columna
        for (i in forma.indices) {
            for (j in forma[i].indices) {
                if (forma[i][j] != 0) {
                    val nuevaFila = filaActual + i + 1
                    val nuevaCol = columnaActual + j
                    if (nuevaFila >= model.filas || model.matriz[nuevaFila][nuevaCol] != 0) {
                        return false
                    }
                }
            }
        }
        return true
    }

    private fun esGameOver(figura: Figura): Boolean {
        val forma = figura.getFormaActual()
        val filaActual = figura.fila
        val columnaActual = figura.columna

        for (i in forma.indices) {
            for (j in forma[i].indices) {
                if (forma[i][j] != 0) {
                    val filaTab = filaActual + i
                    val colTab = columnaActual + j
                    if (filaTab in 0 until model.filas && colTab in 0 until model.columnas) {
                        if (model.matriz[filaTab][colTab] != 0) {
                            return true
                        }
                    }
                }
            }
        }
        return false
    }
}
