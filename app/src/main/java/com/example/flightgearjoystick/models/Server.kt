package com.example.flightgearjoystick.models

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.PrintWriter
import java.lang.Exception
import java.net.Socket
import java.util.concurrent.*

/**
 * The class is responsible for connecting the flight gear server and send set requests
 * Data members:
 * socket - The sockets that connects the server
 * out - The writer sends flight gear commands to the server
 * q - A ThreadPool for sending requests to the server
 * isConnected - True when thee server is connected and false otherwise
 * tryConnect - A semaphore that prevents two from trying to connect the server simultaneously
 */
class Server(var ip: String, var port: Int) {
    private lateinit var fg: Socket
    private lateinit var out: PrintWriter
    private var q: ExecutorService = Executors.newFixedThreadPool(10)
    private var isConnected: Boolean = false
    private var tryConnect: Semaphore = Semaphore(1)

    /**
     * The function connects to the flight gear server
     * The function returns the result as string
     */
    @RequiresApi(Build.VERSION_CODES.N)
    fun connect(): Result<String> {
        // if the server is not connected
        if (!isConnected) {
            // try to acquire the semaphore
            if (tryConnect.tryAcquire()) {
                // create a completable future thread that tries to connect the server
                val fc: CompletableFuture<Result<String>> = CompletableFuture.supplyAsync {
                    try {
                        Log.d("IP", ip)
                        Log.d("Port", port.toString())
                        // connect the sever with ip and port
                        fg = Socket(ip, port)
                        // create a writer
                        out = PrintWriter(fg.getOutputStream(), true)
                        Log.d("server", "Connection established!")
                        isConnected = true
                        return@supplyAsync Result.success("Connection established!")
                    } catch (Excep: InterruptedException) { // throw an exception if an error occurred
                        return@supplyAsync Result.failure<String>(Excep)
                    }
                }
                return try {
                    // if completable future didn't came with a result in 5 seconds, return an error
                    val r: Result<String> = fc.get(5, TimeUnit.SECONDS)
                    // else return the successful result
                    isConnected = true
                    tryConnect.release()
                    r
                } catch (ex: TimeoutException) {
                    // if an exception occurred, release the semaphore and return it
                    tryConnect.release()
                    Result.failure(Exception("There was a problem connecting to server. Please try again"))
                }
            } else {
                // if user pressed connect multiple times in a row, return exception
                return Result.failure(Exception("Already trying to connect to server"))
            }
        } else {
            // if the user is already connected, return exception
            return Result.failure(Exception("Already connected to server"))
        }
    }

    /**
     * The function insert a new thread to the threadPool for setting new aileron value
     */
    fun setAileron(value: Double) {
        if (isConnected) {
            q.execute {
                out.print("set /controls/flight/aileron $value \r\n")
                out.flush()
            }
        }
    }
    /**
     * The function insert a new thread to the threadPool for setting new elevator value
     */
    fun setElevator(value: Double) {
        if (isConnected) {
            q.execute {
                out.print("set /controls/flight/elevator $value \r\n")
                out.flush()
            }
        }
    }
    /**
     * The function insert a new thread to the threadPool for setting new rudder value
     */
    fun setRudder(value: Double) {
        if (isConnected) {
            q.execute {
                out.print("set /controls/flight/rudder $value \r\n")
                out.flush()
            }
        }
    }
    /**
     * The function insert a new thread to the threadPool for setting new throttle value
     */
    fun setThrottle(value: Double) {
        if (isConnected) {
            q.execute {
                out.print("set /controls/engines/current-engine/throttle $value \r\n")
                out.flush()
            }
        }
    }
}