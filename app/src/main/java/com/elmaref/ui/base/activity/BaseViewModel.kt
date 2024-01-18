package com.example.muslim.ui.base.activity

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


abstract class BaseViewModel<N>(private val application: Application?=null): ViewModel(){
    var navigator:N?=null
    val showLoading = MutableLiveData<Boolean>()
    val messageLiveData = MutableLiveData<String>()
    var context: Context?=null
    open fun <T : Application> getApplication(): T {
        return application as T
    }
}