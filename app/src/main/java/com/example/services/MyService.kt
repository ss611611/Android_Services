package com.example.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.provider.Settings

class MyService : Service() {
    private lateinit var player: MediaPlayer

    override fun onStartCommand(init: Intent, flag: Int, startId: Int): Int {
        playLoopMusic()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopPlayMusic()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private fun playLoopMusic() {
        player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI)
        player.isLooping = true
        player.start()
    }

    private fun stopPlayMusic() {
        player.stop()
    }

}