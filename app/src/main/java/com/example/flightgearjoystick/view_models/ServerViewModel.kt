package com.example.flightgearjoystick.view_models

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flightgearjoystick.models.Server
import java.lang.Exception

class ServerViewModel : ViewModel(), Observable {
    var server: Server? = null

    @Bindable
    var ip = MutableLiveData<String>()

    var test = MutableLiveData<Boolean>()

    @Bindable
    var port = MutableLiveData<String>()

    @Bindable
    var throttle_value = MutableLiveData<Float>()

    @Bindable
    var rudder_value = MutableLiveData<Float>()

    @RequiresApi(Build.VERSION_CODES.N)
    fun connect(): Result<String>? {
        try {
            if(server==null) {
                server = Server(ip.value.toString(), port.value.toString().toInt())
            }else{
                server!!.ip = ip.value!!
                server!!.port = port.value!!.toInt()
            }
            return server?.connect()
        }
        catch (Excep:Exception){
            return Result.failure(Excep)
        }
    }

    fun setAileron(value: Double) {
        if (server != null) {
            server?.setAileron(value)
        }
    }

    fun setElevator(value: Double) {
        if (server != null) {
            server?.setElevator(value)
        }
    }

    fun setThrottle() {
        if (server != null) {
            server?.setThrottle(throttle_value.value!!.toDouble())
        }
    }

    fun setRudder() {
        if (server != null) {
            server?.setRudder(rudder_value.value!!.toDouble())
        }
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        Log.d("test", "addOnPropertyChangedCallback")
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        Log.d("test", "removeOnPropertyChangedCallback")
    }
}