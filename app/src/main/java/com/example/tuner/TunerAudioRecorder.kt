package com.example.tuner

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

//media recorder no longer used but kept in for posterity
//AudioRecord used instead
//helpful resources I would never have been able to figure out writing the header without
//https://stackoverflow.com/questions/5245497/how-to-record-wav-format-file-in-android
//https://stackoverflow.com/questions/9179536/writing-pcm-recorded-data-into-a-wav-file-java-android
class TunerAudioRecorder(
    private val context: Context,
    private val file: File
): AudioRecorder {
    private var rec: AudioRecord? = null
    private var isRecording = false
    private var recordingThread: Thread? = null
    private val RECORDER_SAMPLE_RATE = 44100
    private val BUFFER_SIZE = 2048
    private val BITS_PER_SAMPLE: Short = 16
    private val NUMBER_CHANNELS: Short = 2
    private val BYTE_RATE = (RECORDER_SAMPLE_RATE * NUMBER_CHANNELS * BITS_PER_SAMPLE) / 8



    @SuppressLint("MissingPermission")
    fun startRecording() {
        rec = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            RECORDER_SAMPLE_RATE, AudioFormat.CHANNEL_IN_STEREO,
            AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE
        )

        rec?.startRecording()
        isRecording = true
        recordingThread = Thread({ writeAudioDataToFile() }, "AudioRecorder Thread")
        recordingThread?.start()
    }

    private fun writeAudioDataToFile() {
        val data = arrayListOf<Byte>()
        val bData = ByteArray(BUFFER_SIZE/2)


        var os: FileOutputStream? = null
        try {
            os = FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        for (byte in wavFileHeader()) {
            data.add(byte)
        }


        while (isRecording) {
            // gets the voice output from microphone to byte format
            rec?.read(bData, 0, BUFFER_SIZE/2)
            try {
                for(byte in bData) {
                    data.add(byte)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        updateHeaderInformation(data)
        os?.write(data.toByteArray())
        try {
            os!!.flush()
            os!!.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun stopRecording() {
        // stops the recording activity
        if (rec != null) {
            isRecording = false
            rec?.stop()
            rec?.release()
            rec = null
            recordingThread?.join()
            recordingThread = null
        }
    }

    private var recorder: MediaRecorder? = null

    private fun createRecorder(): MediaRecorder {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else MediaRecorder()
    }

    override  fun start(output: File) {
        createRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setAudioEncodingBitRate(16*44100)
            setAudioSamplingRate(44100)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(FileOutputStream(output).fd)

            prepare()
            start()
            recorder = this
        }
    }

    override fun stop() {
        recorder?.stop()
        recorder?.reset()
        recorder?.release()
        recorder = null
    }

    private fun wavFileHeader(): ByteArray {
        val headerSize = 44
        val header = ByteArray(headerSize)

        header[0] = 'R'.code.toByte() // RIFF/WAVE header
        header[1] = 'I'.code.toByte()
        header[2] = 'F'.code.toByte()
        header[3] = 'F'.code.toByte()

        header[4] = (0 and 0xff).toByte() // Size of the overall file, 0 because unknown
        header[5] = (0 shr 8 and 0xff).toByte()
        header[6] = (0 shr 16 and 0xff).toByte()
        header[7] = (0 shr 24 and 0xff).toByte()

        header[8] = 'W'.code.toByte()
        header[9] = 'A'.code.toByte()
        header[10] = 'V'.code.toByte()
        header[11] = 'E'.code.toByte()

        header[12] = 'f'.code.toByte() // 'fmt ' chunk
        header[13] = 'm'.code.toByte()
        header[14] = 't'.code.toByte()
        header[15] = ' '.code.toByte()

        header[16] = 16 // Length of format data
        header[17] = 0
        header[18] = 0
        header[19] = 0

        header[20] = 1 // Type of format (1 is PCM)
        header[21] = 0

        header[22] = NUMBER_CHANNELS.toByte()
        header[23] = 0

        header[24] = (RECORDER_SAMPLE_RATE and 0xff).toByte() // Sampling rate
        header[25] = (RECORDER_SAMPLE_RATE shr 8 and 0xff).toByte()
        header[26] = (RECORDER_SAMPLE_RATE shr 16 and 0xff).toByte()
        header[27] = (RECORDER_SAMPLE_RATE shr 24 and 0xff).toByte()

        header[28] = (BYTE_RATE and 0xff).toByte() // Byte rate = (Sample Rate * BitsPerSample * Channels) / 8
        header[29] = (BYTE_RATE shr 8 and 0xff).toByte()
        header[30] = (BYTE_RATE shr 16 and 0xff).toByte()
        header[31] = (BYTE_RATE shr 24 and 0xff).toByte()

        header[32] = (NUMBER_CHANNELS * BITS_PER_SAMPLE / 8).toByte() //  16 Bits stereo
        header[33] = 0

        header[34] = BITS_PER_SAMPLE.toByte() // Bits per sample
        header[35] = 0

        header[36] = 'L'.code.toByte()
        header[37] = 'I'.code.toByte()
        header[38] = 'S'.code.toByte()
        header[39] = 'T'.code.toByte()

        header[40] = (0 and 0xff).toByte() // Size of the data section.
        header[41] = (0 shr 8 and 0xff).toByte()
        header[42] = (0 shr 16 and 0xff).toByte()
        header[43] = (0 shr 24 and 0xff).toByte()

        return header
    }

    private fun updateHeaderInformation(data:ArrayList<Byte>) {
        val fileSize = data.size
        val contentSize = fileSize - 44

        data[4] = (fileSize and 0xff).toByte() // Size of the overall file
        data[5] = (fileSize shr 8 and 0xff).toByte()
        data[6] = (fileSize shr 16 and 0xff).toByte()
        data[7] = (fileSize shr 24 and 0xff).toByte()

        data[40] = (contentSize and 0xff).toByte() // Size of the data section.
        data[41] = (contentSize shr 8 and 0xff).toByte()
        data[42] = (contentSize shr 16 and 0xff).toByte()
        data[43] = (contentSize shr 24 and 0xff).toByte()
    }
}