package com.example.mr_kotlin.utils

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import com.example.mr_kotlin.ui.movie_detail.MovieDetailActivity
import java.util.*
import kotlin.math.sqrt

class SensorAdmin {

    private var sensorListener: SensorEventListener? = null
    private var sensorManager: SensorManager? = null
    private var acceleration: Float = 0f
    private var currentAcceleration: Float = 0f
    private var lastAcceleration: Float = 0f

    fun initializeSensorListener(currentActivity: AppCompatActivity) {
        sensorListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                lastAcceleration = currentAcceleration
                currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
                val delta: Float = currentAcceleration - lastAcceleration
                acceleration = acceleration * 0.9f + delta
                if (acceleration > 12) {

                    val intent = Intent(currentActivity, MovieDetailActivity::class.java)
                    currentActivity.startActivity(intent)
                }
            }
            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
        }

        sensorManager = currentActivity.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        Objects.requireNonNull(sensorManager)!!.registerListener(sensorListener,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL)
        acceleration = 10f
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH
    }

    fun registerListener(currentActivity: AppCompatActivity) {
        if(sensorManager == null)
            initializeSensorListener(currentActivity)
        sensorManager!!.registerListener(sensorListener, sensorManager!!.getDefaultSensor(
            Sensor .TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    fun unregisterListener(currentActivity: AppCompatActivity) {
        if(sensorManager == null)
            initializeSensorListener(currentActivity)
        sensorManager!!.unregisterListener(sensorListener)
    }
}