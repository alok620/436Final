# Tuner App
This is a guitar tuner app that listens uses the microphone to record audio, analyzes the frequency of the audio and gives back a result based on where the frequency falls, 
either above or below, in relation to the intendedd target. It does the recording using the AudioRecord class. The regular media player was not enough due to
the other tool I used for analysis, the musicg library, only supporting .wav files, which the MediaRecorder cannot produce. The musicg library is used
to create a digital spectrogram of the recording based on a sample of it, and apply the FTT algorithm to relate the time and frequency of a sound during the recording to the
intensity level of that frequency in the recording at that given time. These are organized into buckets, with higher sample sizes leading to smaller and more finely tuned categories.
Due to the nature of tuning guitars often starting with a louder sound and tapering off into quiter sounds, I thought that a good way to check for the dominant frequency 
meant for comparision could be to check the loudest sound's frequency and the area around it and see if that was close to the intended frequency for the note selected to tune.
The app allows you to choose a variety of notes to tune to for each of the different strings. If the note is too high the button shows a magenta light behind it, too low is red,
and just right is green.

## Figma Wireframes
![Frame 1](https://github.com/user-attachments/assets/d738186d-b8b3-4db8-8da2-828cc6d19a1d)
![Frame 2](https://github.com/user-attachments/assets/15edcf46-05bd-446f-bfe5-38e68f1129aa)


# Android/Jetpack Compose Features
The recording functionality was absolutely required. The surprisingly robust ability to read and write audio in so many formats with AudioRecord was fantastic as well.
Organization of the UI was done using composables and that system does a lot of the heavy lifting. I used a third party library, musicg, to streamline the audio analysis
process. The task is really complicated and requires much more knowledge to fully understand than I would have been able to meaningfully implement on my own in the given time
frame. I also owe a lot of credit to two stack overflow posts that were really helpful when figuring out how to record audio and write it as a .wav file. Writing and playing with the header
was made significantly easier because of their example and I wanted to credit them and will be linking the posts below. The ability to write files to the app's cache was also really helpful for writing audio files to.
Threads were also essential to allow interaction with the app while recording for an indefinite period.
#Links:
https://stackoverflow.com/questions/5245497/how-to-record-wav-format-file-in-android
https://stackoverflow.com/questions/9179536/writing-pcm-recorded-data-into-a-wav-file-java-android
https://docs.fileformat.com/audio/wav/
https://code.google.com/archive/p/musicg/ Though it seems most of the musicg documentation has been removed.
https://github.com/loisaidasam/musicg I read through the source to learn more about it.

## Version
The lowest version I tested was on "R" API 30 Android 11. I think it could go below but I remember chosing not to go too far below as it had caused me to lose some features of the composable format that
I wanted to be able to employ.

## Final Notes
Musicg is included as a JAR dependency and while it should work after pulling and building (it did for me while testing) it caused me some issues while figuring it out so I wanted to note it here.
I did not finish implementing real time audio recording, but the draft of my attempt is still included, I implemented a really simple circular buffer and I was working on getting it to
work with File streams and then putting the writing process on a timer so that it would change from recording into the original file into a new one, while allowing access to the already recorded
chunk for processing. Another step for future improvement would be decreasing the effects of background noise. Right now the tuner is very sensitive. Some measures have been taken to aid in that,
narrowing the allowed audio range and allowing a more generous grace area for the frequency to fall within to get the green light there is still a lot of room for improvement. A final note, for the best
experience while running in the emulator, it is necessary to pop the device into windowed mode, go to the three dots on the far side of the lower top toolbar and enable the emulator to use the host microphone
under microphone settings in the extended controls panel that shows up. Without this it can be hard to get any good readings and the audio can end up pretty muddy.
