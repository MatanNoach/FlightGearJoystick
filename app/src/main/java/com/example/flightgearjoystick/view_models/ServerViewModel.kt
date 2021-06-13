package com.example.flightgearjoystick.view_models

import android.util.Log
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flightgearjoystick.models.Server

class ServerViewModel : ViewModel(),Observable {
    lateinit var server:Server
    @Bindable
    var ip = MutableLiveData<String>()

    @Bindable
    var port = MutableLiveData<String>()

    fun connect(){
        server = Server(ip.value.toString(),port.value.toString().toInt())
        server.connect()
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        Log.d("test","addOnPropertyChangedCallback")
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        Log.d("test","removeOnPropertyChangedCallback")
    }
}