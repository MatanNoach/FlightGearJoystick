package com.example.flightgearjoystick.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.example.flightgearjoystick.R
import com.example.flightgearjoystick.databinding.ActivityMainBinding
import com.example.flightgearjoystick.view_models.ServerViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.lang.NumberFormatException

class MainActivity : AppCompatActivity() {
    lateinit var viewModel:ServerViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val  binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(ServerViewModel::class.java)
        binding.viewModel = viewModel
//        viewModel.ip.observe(this,{
//            Toast.makeText(this,"ip changed",Toast.LENGTH_SHORT).show()
//        })
//        viewModel.port.observe(this,{
//            Toast.makeText(this,"port changed",Toast.LENGTH_SHORT).show()
//        })
        findViewById<Button>(R.id.connect_button).setOnClickListener {
            if (validate()) {
                viewModel.connect()
                Log.d("test","Connected")
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
}