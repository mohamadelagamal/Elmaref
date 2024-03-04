package com.elmaref.ui.quran.paged.tfseer.servies

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.viewModelScope
import com.elmaref.R
import com.elmaref.data.api.ApiManager
import com.elmaref.data.model.enums.ServiceAction
import com.elmaref.ui.quran.paged.tfseer.QuranMenuTfser
import com.quranscreen.constants.LocaleConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class PlayAyahService:Service() {

    private var mediaPlayer: MediaPlayer? = null
    // jop to cancel all coroutines when service is destroyed
    private val job = SupervisorJob()
    // Io dispatcher to perform network operations in the background thread
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // get data from intent
        val ayahId = intent?.getStringExtra(LocaleConstants.AYAH_ID_SERVICES)
        Log.e("Ayahs", "AudioUrl: $ayahId")


        when(intent?.action) {
            ServiceAction.PLAY_MUSIC.toString() -> playMusic(ayahId?.toInt() ?: 1)
            ServiceAction.STOP_MUSIC.toString() -> stopMusic()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun stopMusic() {
        //Stop the music
        stopAndReleaseMediaPlayer()

        //Stop the foreground service
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun stopAndReleaseMediaPlayer() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        job.cancel() }

    @SuppressLint("ForegroundServiceType")
    private fun playMusic(id:Int) {
        scope.launch {
            val urlSource = getAyahAudio(id)
            val audioUrl = "https://verses.quran.com/${urlSource}"

            // Create a notification
            val notification = createNotification()

            // Start foreground service
            startForeground(1, notification)

            // Perform the service task here (e.g., play music)
            // Initialize MediaPlayer and start playing music
            mediaPlayer = MediaPlayer().apply {
                setDataSource(audioUrl)
                prepare()
                start()
            }
        }


    }

    private fun createNotification(): Notification? {
//        val builder = NotificationCompat.Builder(this, "MY_SERVICE_CHANNEL")
//            .setContentTitle("سورة البقرة")
//            .setContentText("الشيخ مشاري العفاسى ")
//            .setSmallIcon(R.drawable.icon_application)
//            .setSilent(true) // This is important, or the service will make a sound when it starts
//
//        return builder.build()

        // Create an intent that will be broadcasted when the exit action is clicked
        val stopIntent = Intent(this, PlayAyahService::class.java).apply {
            action = ServiceAction.STOP_MUSIC.toString()
        }
        val stopPendingIntent = PendingIntent.getService(this, 0, stopIntent,
            PendingIntent.FLAG_MUTABLE)

        // Create the notification
        val builder = NotificationCompat.Builder(this, "MY_SERVICE_CHANNEL")
            .setContentTitle("سورة البقرة")
            .setContentText("الشيخ مشاري العفاسى ")
            .setSmallIcon(R.drawable.icon_application)
            .setSilent(true) // This is important, or the service will make a sound when it starts
            .addAction(R.drawable.baseline_stop_24, "Exit", stopPendingIntent) // Add the exit action

        return builder.build()
    }
   suspend fun getAyahAudio(number: Int):String? {

            var audioUrl:String? = null
            try {
                val api = ApiManager.getApi()
                val response = api.getSpecificAya(7, "$number")
                Log.e("Ayahs", "Response: ${response.audioFiles?.get(0)?.url}")
                 audioUrl = response.audioFiles?.get(0)?.url


            } catch (e: Exception) {
                Log.e("Ayahs", "Error: ${e.message}")
            }
            return audioUrl

    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        job.cancel()
    }
}