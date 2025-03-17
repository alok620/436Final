package com.example.tuner

import com.musicg.wave.Wave
import java.io.File

class CircularBuffer {
    private var next = 0
    private var oldest = 0
    private var elements = 0
    private var buffer = arrayOf<Wave?>(null, null, null, null, null)

    fun pop() : Wave? {
        if(elements != 0) {
            val temp = buffer[oldest]
            buffer[oldest] = null
            --elements
            oldest = getNextInd(oldest)
            return temp
        }
        return null
    }

    fun push(wave: Wave) {
        if(next == oldest) {
            oldest = getNextInd(oldest)
        }
        buffer[next] = wave
        next = getNextInd(next)
        ++elements
    }

    private fun getNextInd(ptr: Int): Int {
        if(ptr == 4) {
            return 0
        } else {
            return ptr + 1
        }
    }

    private fun getPrevInd(ptr: Int): Int {
        if(ptr == 0) {
            return 4
        } else {
            return ptr - 1
        }
    }

}