package com.example.flightgearjoystick.views

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.SeekBarBindingAdapter
import androidx.lifecycle.ViewModelProvider
import com.example.flightgearjoystick.JoystickFragment
import com.example.flightgearjoystick.JoystickListener
import com.example.flightgearjoystick.R
import com.example.flightgearjoystick.databinding.ActivityMainBinding
import com.example.flightgearjoystick.view_models.ServerViewModel
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.concurrent.CompletableFuture

class MainActivity : AppCompatActivity(),JoystickListener {
    lateinit var viewModel:ServerViewModel
    lateinit var rudder_slider:SeekBar
    lateinit var throttle_slider:SeekBar
    @RequiresApi(Build.VERSION_CODES.N)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val  binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(ServerViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.joystick_fragment,JoystickFragment())
            commit()
        }

        rudder_slider = findViewById(R.id.rudder_slider)
        rudder_slider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                viewModel.rudder_value.value=(p1/100F)
                viewModel.setRudder()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                viewModel.setRudder()
                Toast.makeText(this@MainActivity,"onStopTracking ${viewModel.rudder_value.value}",Toast.LENGTH_SHORT).show()
            }
        })
        throttle_slider = findViewById(R.id.throttle_slider)
        throttle_slider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                viewModel.throttle_value.value=(p1/100F)
                viewModel.setThrottle()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                viewModel.setThrottle()
                Toast.makeText(this@MainActivity,"onStopTracking ${viewModel.throttle_value}",Toast.LENGTH_SHORT).show()
            }

        })

        findViewById<Button>(R.id.connect_button).setOnClickListener {
            if (validate()) {
                var r:Result<String>? = null
                CompletableFuture.supplyAsync {
                    this@MainActivity.runOnUiThread {
                        Toast.makeText(this,"Connecting to server...",Toast.LENGTH_SHORT).show()
                    }
                    r = viewModel.connect()
                }.thenAccept {
                    Log.d("result",r.toString())
                    this@MainActivity.runOnUiThread {
                        Toast.makeText(applicationContext,r.toString(),Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun validate(): Boolean {
        val ip = findViewById<TextInputEditText>(R.id.ip)
        val ipLayout = findViewById<TextInputLayout>(R.id.ip_layout)
        val port = findViewById<TextInputEditText>(R.id.port)
        val portLayout = findViewById<TextInputLayout>(R.id.port_layout)
        var isValid = true

        if (ip.text!!.isEmpty()) {
            ipLayout.error = "Please fill ip"
            isValid = false
        } else if (!Patterns.IP_ADDRESS.matcher(ip.text.toString()).matches()) {
            ipLayout.error = "Not a valid ip"
            isValid = false
        } else {
            ipLayout.error = null
        }
        try {
            if (port.text!!.isEmpty()) {
                portLayout.error = "Please fill port"
                isValid = false
            } else if (port.text.toString().toInt() > 65535) {
                portLayout.error = "Port must be in range 0-65535"
                isValid = false
            } else {
                portLayout.error = null
            }
        } catch (exception: NumberFormatException) {
            portLayout.error = "Port must be in range 0-65535"
            isValid = false
        }
        return isValid
    }
    override fun onJoystickTouch(x: Double, y: Double) {
        viewModel.setAileron(x)
        viewModel.setElevator(-y)
    }

}

