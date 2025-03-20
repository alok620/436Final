package com.example.tuner

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.content.res.Resources
import android.media.MediaRecorder
import android.util.Log
import com.musicg.wave.Wave
import java.io.File

class TunerAudioManager {

    public fun dominantNote(wave: Wave): Double {
        Log.d("HEADER", wave.waveHeader.toString())
        val spec = wave.getSpectrogram(2048, 0)
        var counter = 0
        var prev = true
        var max = 0.0
        for (i in spec.normalizedSpectrogramData) {
            //no frequencies higher than 400 currently supported so clamping for improved
            //accuracy
            for (j in i.indices.filter {it > 0 && it < spec.fftSampleSize * 400 / wave.waveHeader.sampleRate }) {
                if (i[j] > max) {
                    max = i[j]
                    prev = true
                    counter = j
                }
            }
            if (prev)
                Log.d(counter.toString() + "HI", max.toString())
            prev = false
        }
        Log.d( "UNITFREQ", spec.unitFrequency.toString())
        return (counter) * (spec.unitFrequency)
    }
}