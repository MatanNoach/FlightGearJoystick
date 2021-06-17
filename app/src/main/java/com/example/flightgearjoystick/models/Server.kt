package com.example.flightgearjoystick.models

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.PrintWriter
import java.lang.Exception
import java.net.Socket
import java.util.concurrent.*

class Server(var ip: String, var port: Int) {
    lateinit var fg: Socket
    lateinit var out: PrintWriter

    // thread pool for the setters
    var q: ExecutorService = Executors.newFixedThreadPool(10)
    var isConnected: Boolean = false
    var tryConnect: Semaphore = Semaphore(1)

    @RequiresApi(Build.VERSION_CODES.N)
    fun connect(): Result<String> {
        if (!isConnected) {
            if (tryConnect.tryAcquire()) {
                val fc: CompletableFuture<Result<String>> = CompletableFuture.supplyAsync {
                    try {
                        Log.d("IP", ip)
                        Log.d("Port", port.toString())
                        fg = Socket(ip, port)
                        out = PrintWriter(fg.getOutputStream(), true)
                        Log.d("server", "Connection established!")
                        isConnected = true
                        return@supplyAsync Result.success("Connection established!")
                    } catch (Excep: InterruptedException) {
                        return@supplyAsync Result.failure<String>(Excep)
                    }
                }
                return try {
                    val r: Result<String> = fc.get(5, TimeUnit.SECONDS)
                    isConnected = true
                    r
                } catch (ex: TimeoutException) {
                    tryConnect.release()
                    Result.failure(Exception("There was a problem connecting to server. Please try again"))
                }
            } else {
                return Result.failure(Exception("Already trying to connect to server"))
            }
        } else {
            return Result.failure(Exception("Already connected to server"))
        }
    }

    fun setAileron(value: Double) {
        if (isConnected) {
            q.execute {
                out.print("set /controls/flight/aileron $value \r\n")
                out.flush()
            }
        }
    }

    fun setElevator(value: Double) {
        if (isConnected) {
            q.execute {
                out.print("set /controls/flight/elevator $value \r\n")
                out.flush()
            }
        }
    }

    fun setRudder(value: Double) {
        if (isConnected) {
            q.execute {
                out.print("set /controls/flight/rudder $value \r\n")
                out.flush()
            }
        }
    }

    fun setThrottle(value: Double) {
        if (isConnected) {
            q.execute {
                out.print("set /controls/engines/current-engine/throttle $value \r\n")
                out.flush()
            }
        }
    }
}