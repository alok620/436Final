package com.example.tuner.ui

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tuner.TunerAudioManager
import com.example.tuner.TunerAudioPlayer
import com.example.tuner.TunerAudioRecorder
import com.example.tuner.ui.theme.TunerTheme
import com.musicg.wave.Wave
import java.io.File
import java.time.Instant


var audioFile: File? = null

var clicked = false

@Composable
fun TunerScreen(
    modifier: Modifier = Modifier,
    context: Context,
    tunerViewModel: TunerViewModel = viewModel(),
) {
    var lowENote by remember { mutableStateOf(TunerViewModel.Note.LowE) }
    var aNote by remember { mutableStateOf(TunerViewModel.Note.A) }
    var dNote by remember { mutableStateOf(TunerViewModel.Note.D) }
    var gNote by remember { mutableStateOf(TunerViewModel.Note.G) }
    var bNote by remember { mutableStateOf(TunerViewModel.Note.B) }
    var highENote by remember { mutableStateOf(TunerViewModel.Note.HighE) }
    Column(
        modifier = modifier.padding(10.dp)
    ) {
        AppTitle(modifier)
        Box(Modifier.size(1000.dp)) {
            NoteEdit({lowENote = it},tunerViewModel.low,lowENote, context, Modifier.align(Alignment.TopStart))
            NoteEdit({aNote = it},tunerViewModel.aNotes,aNote, context, Modifier.align(Alignment.TopEnd))
            NoteEdit({dNote = it},tunerViewModel.dNotes,dNote, context, Modifier.align(Alignment.CenterStart))
            NoteEdit({gNote = it},tunerViewModel.gNotes,gNote, context, Modifier.align(Alignment.CenterEnd))
            NoteEdit({bNote = it},tunerViewModel.bNotes,bNote, context, Modifier.align(Alignment.BottomStart))
            NoteEdit({highENote = it},tunerViewModel.high,highENote, context, Modifier.align(Alignment.BottomEnd))
        }
    }
}



@Composable
fun NoteEdit(
    onClick: (n: TunerViewModel.Note) -> Unit,
    options: Array<TunerViewModel.Note>,
    note: TunerViewModel.Note,
    context: Context,
    modifier: Modifier
){
    var success by remember {mutableStateOf("")}
    audioFile = File(context.cacheDir, "in.wav")
    val recorder by lazy {
        TunerAudioRecorder(context, audioFile!!)
    }
    val player by lazy {
        TunerAudioPlayer(context)
    }
    Row (
        modifier
    ){
        EditButton(onClick = onClick, options,modifier)
        NoteButton(onClick = {
            if(clicked) {
                recorder.stopRecording()
                val man = TunerAudioManager()
                if(audioFile != null) {
                    context.cacheDir.delete()
                    var wave = Wave(audioFile?.path)
                    wave.leftTrim(0.5)
                    var res = man.dominantNote(wave)
                    Log.d("FREQ", note.freq.toString())
                    if(res.toInt() >= note.freq - 45 && res.toInt() <= note.freq + 45) {
                        Log.d("SUCCESS", "SUCCESS")
                        success = "success"
                    } else if(res.toInt() > note.freq) {
                        success = "high"
                    } else {
                        success = "low"
                    }
                    Log.d("RES", res.toString())
                } else {
                    Log.d("FALSE", "FALSE")
                }
                clicked = false
            } else {
                //while(clicked) {
                recorder.startRecording()
                clicked = true
                //delay(1000000L)
                //recorder.stopRecording()
                //delay(1000000L)
                //recorder.setFile(File(context.cacheDir, "in${Instant.now()}.wav"))
            }

        }, note.toString(), if(success == "success") {
            modifier.background(Color.Green)
        } else if(success == "high") {
            modifier.background(Color.Magenta)
        } else if(success == "low") {
            modifier.background(Color.Red)
        }else {
        modifier})
    }
}

@Composable
fun AppTitle(modifier: Modifier) {
    Text(
        text = "Tuner App",
        fontSize = 38.sp,
        modifier = modifier.padding(bottom = 16.dp)
    )
}

@Composable
fun NoteButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier
) {
    Button(
        onClick = onClick,
        modifier
    ) {
        Text(text)
    }
}

@Composable
fun EditButton(
    onClick: (n: TunerViewModel.Note) -> Unit,
    options: Array<TunerViewModel.Note>,
    modifier: Modifier = Modifier
) {
    MinimalDropdownMenu(options, onClick, modifier)
}

@Composable
fun MinimalDropdownMenu(
    options: Array<TunerViewModel.Note>,
    onClick: (n: TunerViewModel.Note) -> Unit,
    modifier: Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
    ) {
        Button(onClick = { expanded = !expanded }) {
            Text("Edit")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            for(option in options) {
                DropdownMenuItem(
                    text = { Text(option.toString()) },
                    onClick = { onClick(option) }
                )
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TunerTheme {
        Greeting("Android")
    }
}