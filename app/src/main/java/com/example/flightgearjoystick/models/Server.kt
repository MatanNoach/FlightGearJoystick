package com.example.flightgearjoystick.models

import android.util.Log
import java.io.PrintWriter
import java.net.Socket

class Server(var ip: String, var port: Int) {
    lateinit var fg:Socket
    lateinit var out:PrintWriter
    fun connect(){
        Thread(Runnable {
            Log.d("IP",ip)
            Log.d("Port",port.toString())
            fg = Socket(ip,port)
            out = PrintWriter(fg.getOutputStream(),true)
            Log.d("server","Connection established!")
        }).start()
    }
    fun setAileron(value: Double) {
        out.print("set /controls/flight/aileron $value \r\n")
        out.flush()
    }
    fun setElevator(value: Double) {
        out.print("set /controls/flight/elevator $value \r\n")
        out.flush()
    }
    fun setRudder(value: Double) {
        out.print("set /controls/flight/rudder $value \r\n")
        out.flush()
    }
    fun setThrottle(value: Double) {
        out.print("set /controls/engines/current-engine/throttle $value \r\n")
        out.flush()
    }
}