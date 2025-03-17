package com.example.tuner

import java.io.File

interface AudioPlayer {
    fun playFile(file: File?)
    fun stop()
}