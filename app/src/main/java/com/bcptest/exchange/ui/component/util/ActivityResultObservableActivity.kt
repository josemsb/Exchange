package com.bcptest.exchange.ui.component.util

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.bcptest.exchange.ui.component.repository.ActivityResultObservable
import com.bcptest.exchange.ui.component.repository.ActivityResultObserver

abstract class ActivityResultObservableActivity : AppCompatActivity(), ActivityResultObservable {
    private val activityObserverList = mutableListOf<ActivityResultObserver>()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        activityObserverList.forEach { it.onActivityResult(requestCode, resultCode, data) }
    }

    override fun addObserver(activityResultObserver: ActivityResultObserver) {
        activityObserverList.add(activityResultObserver)
    }

    override fun removeObserver(activityResultObserver: ActivityResultObserver) {
        activityObserverList.remove(activityResultObserver)
    }

}