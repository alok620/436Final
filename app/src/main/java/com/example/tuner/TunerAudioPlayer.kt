package com.example.tuner

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.core.net.toUri
import java.io.File

class TunerAudioPlayer(
    private val context: Context
): AudioPlayer {
    private var player: MediaPlayer? = null

    override fun playFile(file: File?) {
        if(file != null) {
            Log.d("HI", "MADEIT")
            MediaPlayer.create(context, file.toUri()).apply {
                player = this
                start()
            }
        }
    }

    override fun stop() {
        player?.stop()
        player?.release()
        player = null
    }
}