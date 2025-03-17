package com.example.tuner

import java.io.File

interface AudioRecorder {
    fun start(output: File)
    fun stop()
}