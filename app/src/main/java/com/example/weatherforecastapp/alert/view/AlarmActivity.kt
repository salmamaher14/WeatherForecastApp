package com.example.weatherforecastapp.alert.view

import android.content.Context
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.weatherforecastapp.R

class AlarmActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        val weatherStatus = intent.getStringExtra("weatherStatus")

        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Weather Alert")
            .setMessage("Weather status $weatherStatus")
            .setPositiveButton("Dismiss") { dialog, _ ->
                stopSound()
                dialog.dismiss()

                finish()
            }
            .setCancelable(false)
            .create()
        playSound(this)

        alertDialog.show()
    }
    private fun playSound(context: Context?) {
        mediaPlayer = MediaPlayer.create(context, R.raw.audio)
        mediaPlayer?.isLooping = true // Set to true if you want the sound to loop
        mediaPlayer?.start()
    }

    private fun stopSound() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

}
