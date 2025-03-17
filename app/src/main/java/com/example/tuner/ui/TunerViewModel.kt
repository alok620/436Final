package com.example.tuner.ui

import androidx.lifecycle.ViewModel

class TunerViewModel: ViewModel() {
    var low = arrayOf<Note>(Note.LowC, Note.LowD, Note.LowF, Note.LowG, Note.LowE)
    var aNotes = arrayOf<Note>(Note.A_F, Note.A_G, Note.A_B, Note.A)
    var dNotes = arrayOf<Note>(Note.D_C, Note.D_E, Note.D)
    var gNotes = arrayOf<Note>(Note.G_F, Note.G_A, Note.G)
    var bNotes = arrayOf<Note>(Note.B_A, Note.B_C, Note.B)
    var high = arrayOf<Note>(Note.HighD, Note.HighF, Note.HighE)

    public enum class Note(val freq: Int) {
        LowE(82),
        LowD(73),
        LowF(87),
        LowG(98),
        LowC(65),
        A(110),
        A_F(87),
        A_G(98),
        A_B(123),
        D(146),
        D_C(130),
        D_E(164),
        G(196),
        G_F(174),
        G_A(220),
        B(247),
        B_A(220),
        B_C(261),
        HighE(329),
        HighD(293),
        HighF(349), ;

        override fun toString(): String {
            if(name == "LowE") {
                return "Low E"
            } else if(name == "HighE") {
                return "High E"
            }
            return name.last().toString()
        }
    }


}

