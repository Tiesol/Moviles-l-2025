package com.example.tetrizzzz.observer

interface Observable {
    fun addObserver(observer: Observer)
    fun notifyObservers()
}