package com.example.flightgearjoystick

/**
 * The interface enforces each joystick listener to implement:
 * onJoystickTouch - function that activates when the joystick is being touched
 */
interface JoystickListener {
    fun onJoystickTouch(x:Double, y:Double)
}