package com.bcptest.exchange.ui.component.repository

interface ActivityResultObservable {
    fun addObserver(activityResultObserver: ActivityResultObserver)
    fun removeObserver(activityResultObserver: ActivityResultObserver)
}