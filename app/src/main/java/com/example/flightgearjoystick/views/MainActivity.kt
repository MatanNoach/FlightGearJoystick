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
import androidx.lifecycle.ViewModelProvider
import com.example.flightgearjoystick.R
import com.example.flightgearjoystick.databinding.ActivityMainBinding
import com.example.flightgearjoystick.view_models.ServerViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.concurrent.CompletableFuture

/**
 * The class holds the UI logic of the main activity
 * The class inherits from AppCompactActivity and JoystickListener
 * Data Members:
 * viewModel - The server view model
 * rudder_slider - The rudder slider
 * throttle_slider - The throttle slider
 */
class MainActivity : AppCompatActivity(), JoystickListener {
    private lateinit var viewModel: ServerViewModel
    private lateinit var rudder_slider: SeekBar
    private lateinit var throttle_slider: SeekBar

    /**
     * The function performs initialization of multiple UI objects in the main activity
     */
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set create a binding object for the activity
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        // get a view model object and set it as the view model for the activity
        viewModel = ViewModelProvider(this).get(ServerViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        // insert the joystick fragment in the activity
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.joystick_fragment, JoystickFragment())
            commit()
        }
        // create the rudder slider and it's listener functions
        rudder_slider = findViewById(R.id.rudder_slider)
        rudder_slider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                viewModel.rudder_value.value = (p1 / 100F)
                viewModel.setRudder()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                viewModel.setRudder()
            }
        })
        // create the throttle slider and it's listener functions
        throttle_slider = findViewById(R.id.throttle_slider)
        throttle_slider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                viewModel.throttle_value.value = (p1 / 100F)
                viewModel.setThrottle()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                viewModel.setThrottle()
            }

        })
        // set the onClick event for the connect button
        findViewById<Button>(R.id.connect_button).setOnClickListener {
            // if the validation was successful
            if (validate()) {
                var r: Result<String>? = null
                // create a Completable future thread that connects the model
                CompletableFuture.supplyAsync {
                    this@MainActivity.runOnUiThread {
                        Toast.makeText(this, "Connecting to server...", Toast.LENGTH_SHORT).show()
                    }
                    r = viewModel.connect()
                }.thenAccept { // when the thread finished, print the result from it
                    Log.d("result", r.toString())
                    this@MainActivity.runOnUiThread {
                        Toast.makeText(applicationContext, r.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    /**
     * The function validates the data fields required before connecting the server
     */
    private fun validate(): Boolean {
        // get the ip and port fields and layouts
        val ip = findViewById<TextInputEditText>(R.id.ip)
        val ipLayout = findViewById<TextInputLayout>(R.id.ip_layout)
        val port = findViewById<TextInputEditText>(R.id.port)
        val portLayout = findViewById<TextInputLayout>(R.id.port_layout)
        var isValid = true
        // if the text is empty or the ip is not valid - print an error
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
            // if the port is empty or not a valid number - print an error
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
        // return the result
        return isValid
    }

    /**
     *  On the touch of the joystick
     */
    override fun onJoystickTouch(x: Double, y: Double) {
        // send the x and y data to the server
        viewModel.setAileron(x)
        viewModel.setElevator(-y)
    }

}


