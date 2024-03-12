package com.elmaref.data.model.salat

import androidx.lifecycle.MutableLiveData

data class SalatTime(
    val prayTimeName: String?=null,
    val prayTime: String?=null,
    val prayIcon: Int?=null,
    val prayPassedSalat: String?=null,
    var ringType: Int ?=null
)
