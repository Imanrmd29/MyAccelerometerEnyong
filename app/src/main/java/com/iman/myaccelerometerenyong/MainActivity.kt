package com.iman.myaccelerometerenyong

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity(), SensorEventListener {
//Langkah 1 : Mendklarasikan nilai private untuk menyiapkan sensor yang akan di gunakan
    private lateinit var sensorManager: SensorManager
    private lateinit var square: TextView
//Langkah 2 : Membuat nilai sensor yang kan di gunakan
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//Langkah 3 : Menjaga ponsel dalam mode terang
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        square = findViewById(R.id.tv_square)

        setUpSensorStuff()
    }
//Langkah 4 : Membuat nilai untuk Menyetting bagian sensor
    private fun setUpSensorStuff() {

//Langkah 5 : Membuat pengelola sensor
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager


//Langkah 6 : Menentukan sensor yang ingin Anda dengarkan
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
            sensorManager.registerListener(
                this,
                accelerometer,
                SensorManager.SENSOR_DELAY_FASTEST,
                SensorManager.SENSOR_DELAY_FASTEST
            )
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {

//Langkah 7 : Memeriksa sensor yang telah kita daftarkan
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            //Log.d("Main", "onSensorChanged: sides ${event.values[0]} front/back ${event.values[1]} ")

            // Sides = Tilting phone left(10) and right(-10)
            val sides = event.values[0]

            // Up/Down = Tilting phone up(10), flat (0), upside-down(-10)
            val upDown = event.values[1]

            square.apply {
                rotationX = upDown * 3f
                rotationY = sides * 3f
                rotation = -sides
                translationX = sides * -10
                translationY = upDown * 10
            }


//Langkah 8 : Mengubah warna persegi jika benar-benar rata
            val color = if (upDown.toInt() == 0 && sides.toInt() == 0) Color.GREEN else Color.RED
            square.setBackgroundColor(color)

            square.text = "up/down ${upDown.toInt()}\nleft/right ${sides.toInt()}"
        }
    }
//Langkah 9 : Menampilkan sensor yang berjalan dengan menggunakan akurasi sensor
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }
//Langkah 10 : Membuat kondisi dimana activity di memori
    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }
}