package com.bcptest.exchange.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

object ViewModelUtil {
    @Suppress("UNCHECKED_CAST")
    inline fun <VM : ViewModel> viewModelFactory(crossinline f: () -> VM) =
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T = f() as T
        }
}