package com.example.flightgearjoystick.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flightgearjoystick.models.Joystick

/**
 * The class is a fragment class that returns the joystick onCreateView
 */
class JoystickFragment: Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return Joystick(this.requireContext())
    }
}