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

/**
 * The class is a view model for the server
 * Data members:
 * model - The server model
 * ip - an object bound to the textBox of the ip
 * port - an object bound to the textBox of the port
 * throttle_value - an object bound the throttle seek bar value
 * rudder_value - an object bound the rudder seek bar value
 */
class ServerViewModel : ViewModel(), Observable {
    var model: Server? = null
    @Bindable
    var ip = MutableLiveData<String>()

    @Bindable
    var port = MutableLiveData<String>()

    @Bindable
    var throttle_value = MutableLiveData(0F)

    @Bindable
    var rudder_value = MutableLiveData(0F)

    /**
     * The function connect app to the server
     */
    @RequiresApi(Build.VERSION_CODES.N)
    fun connect(): Result<String>? {
        try {
            // if the model is not null - initialize it
            if(model==null) {
                model = Server(ip.value.toString(), port.value.toString().toInt())
            }else{ // else, set the model ip and values to the ones in the view model
                model!!.ip = ip.value!!
                model!!.port = port.value!!.toInt()
            }
            // connect to the server and return the result
            return model?.connect()
        }
        catch (Excep:Exception){
            return Result.failure(Excep)
        }
    }

    /**
     * Send set aileron request to the model
     */
    fun setAileron(value: Double) {
        if (model != null) {
            model?.setAileron(value)
        }
    }
    /**
     * Send set elevator request to the model
     */
    fun setElevator(value: Double) {
        if (model != null) {
            model?.setElevator(value)
        }
    }
    /**
     * Send set throttle request to the model
     */
    fun setThrottle() {
        if (model != null) {
            model?.setThrottle(throttle_value.value!!.toDouble())
        }
    }
    /**
     * Send set rudder request to the model
     */
    fun setRudder() {
        if (model != null) {
            model?.setRudder(rudder_value.value!!.toDouble())
        }
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        Log.d("Observable", "addOnPropertyChangedCallback")
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        Log.d("Observable", "removeOnPropertyChangedCallback")
    }
}