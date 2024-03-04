package com.elmaref.ui.quran.paged.tfseer.servies

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class PlayAyahBroadcastReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val data = intent?.getStringExtra("data")
        val action = intent?.action
        val intentService = Intent(context, PlayAyahService::class.java)
        intentService.putExtra("data", data)
        intentService.action = action
        context?.startService(intentService)

    }

}