package com.elmaref.model.zikir

data class ZikirType(val title:String,val image:Int = -1)
data class Zekr(
    var zekrNum:Int = 0 ,
    var currentCount:Int = 0 ,
    val category: String,
    var count: String,
    val description: String,
    val reference: String,
    val zekr: String
)
data class Parent(
    val section: String,
    val list: List<String>
)