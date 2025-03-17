package com.example.tuner


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import com.example.tuner.ui.TunerScreen
import com.example.tuner.ui.theme.TunerTheme
import com.musicg.dsp.FastFourierTransform
import com.musicg.processor.TopManyPointsProcessorChain
import com.musicg.properties.FingerprintProperties
import com.musicg.wave.Wave
import com.musicg.wave.extension.Spectrogram


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var am = this.getAssets().open("E.wav")
        Log.d("YO", am.available().toString())
        var audioE1 = Wave(this.getAssets().open("A.wav"))
        var aud = Wave(this.getAssets().open("LowE.wav"))
        var hehe = Wave(this.getAssets().open("LowE.wav")).getSpectrogram(4096, 0)
        var audioE2 = Wave(this.getAssets().open("LowE.wav")).getSpectrogram(4096, 0).normalizedSpectrogramData
        Log.d("YAAA", aud.waveHeader.toString())
        //Log.d("HI", audioE1.getFingerprintSimilarity(audioE2).similarity.toString())
        //Log.d("HI2", audioE2.toString())
        //var ftt = FastFourierTransform()
        //var arr = ftt.getMagnitudes(audioE1)
        //Log.d("MAX", audioE1.spectrogram.normalizedSpectrogramData[3][110].toString())
        //Log.d("MAX", audioE2.indexOfFirst { it == audioE1.max() }.toString())
       // for(i in audioE2[0].indices) {
         //   Log.d("HIHIHI", i.toString())
       // }
        //Log.d("HOHOHO", aud.unitFrequency.toString())
        var counter = 0
        var prev = true
        var max = 0.0
       for(i in audioE2) {
            //var max = 0.0
            //Log.d("FIR", i.get(81).toString())
            //Log.d("FIR", i.get(82).toString())
            //Log.d("FIR", i.get(83).toString())
            //for(j in 0..(312 * hehe.unitFrequency / aud.waveHeader.sampleRate).toInt()) {
              for(j in i.indices) {
                if(i[j] > max) {
                    max = i[j]
                    prev = true
                    counter = j
                }
            }
            if(prev)
                Log.d(counter.toString() + "HI", max.toString())
            prev = false
        }
        //Log.d("AUD", aud.getSpectrogram(4096, 0).unitFrequency.toString())
        //Log.d(counter.toString() + "HI", max.toString())
//        var buf = CircularBuffer()
//        buf.push(Wave(this.getAssets().open("A.wav")))
//        buf.push(Wave(this.getAssets().open("A.wav")))
//        buf.push(Wave(this.getAssets().open("LowE.wav")))
//        buf.push(Wave(this.getAssets().open("A.wav")))
//        buf.push(Wave(this.getAssets().open("A.wav")))
//        buf.push(Wave(this.getAssets().open("E.wav")))

//        Log.d("BUF", buf.pop().toString())
//        Log.d("BUF", buf.pop().toString())
//        Log.d("BUF", buf.pop().toString())
//        Log.d("BUF", buf.pop().toString())
//        buf.push(Wave(this.getAssets().open("E.wav")))
//        Log.d("BUF", buf.pop().toString())
//        Log.d("BUF", buf.pop().toString())
//        Log.d("BUF", buf.pop().toString())



        //for(i in audioE2.indices-1) {
         //   if(audioE2[i][86] > 9000)
         //       Log.d(i.toString(), audioE2[i][86].toString())
       // }

//        // create a wave object
//        val wave: Wave = Wave(this.getAssets().open("LowE.wav"))
//        val spectrogram = Spectrogram(wave)
//
//        val processorChain = TopManyPointsProcessorChain(spectrogram.normalizedSpectrogramData, 1)
//        val processedIntensities = processorChain.intensities
//
//        for (i in processedIntensities.indices) {
//            for (j in processedIntensities[i].indices) {
//                if (processedIntensities[i][j] > 0) {
//                    Log.d(i.toString(), processedIntensities[i][j].toString())
//                }
//            }
//        }

        setContent {
            TunerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TunerScreen(
                        context = applicationContext
                    )
                }
            }
        }
    }
}

