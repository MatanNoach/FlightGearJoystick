package com.example.flightgearjoystick.views

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.flightgearjoystick.JoystickFragment
import com.example.flightgearjoystick.JoystickListener
import com.example.flightgearjoystick.R
import com.example.flightgearjoystick.databinding.ActivityMainBinding
import com.example.flightgearjoystick.view_models.ServerViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.lang.NumberFormatException
import java.util.concurrent.CompletableFuture

class MainActivity : AppCompatActivity(),JoystickListener {
    lateinit var viewModel:ServerViewModel
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val  binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(ServerViewModel::class.java)
        binding.viewModel = viewModel
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.joystick_fragment,JoystickFragment())
            commit()
        }
        findViewById<Button>(R.id.connect_button).setOnClickListener {
            if (validate()) {
                var r:Result<String>? = null
                CompletableFuture.supplyAsync {
                    this@MainActivity.runOnUiThread {
                        Toast.makeText(this,"Connecting to server...",Toast.LENGTH_LONG).show()
                    }
                    r = viewModel.connect()
                }.thenAccept {
                    Log.d("test",r.toString())
                    this@MainActivity.runOnUiThread {
                        Toast.makeText(applicationContext,r.toString(),Toast.LENGTH_LONG).show()
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
//        Toast.makeText(this,"x = $x, y = $y",Toast.LENGTH_SHORT).show()
        viewModel.setAileron(x)
        viewModel.setElevator(y)
    }
}