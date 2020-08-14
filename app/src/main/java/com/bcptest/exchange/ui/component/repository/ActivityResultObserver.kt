package com.bcptest.exchange.ui.component.repository

import android.content.Intent

interface ActivityResultObserver {
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}