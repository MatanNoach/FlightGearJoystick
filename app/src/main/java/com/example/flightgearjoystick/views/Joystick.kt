package com.example.flightgearjoystick.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_UP
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View

/**
 * The class draws the joystick and create it's visual functionality
 * Data members:
 * centerX - The position on the screen of the joystick's center's x value
 * centerY - The position on the screen of the joystick's center's y value
 * baseRadius - The radius size of the inner circle
 * hatRadius - The radius size of the outer circle
 * listener - A listener to be notified when an event is occurring
 */
class Joystick : SurfaceView, SurfaceHolder.Callback, View.OnTouchListener {
    private var centerX: Double = 0.0
    private var centerY: Double = 0.0
    private var baseRadius: Double = 0.0
    private var hatRadius: Double = 0.0
    private lateinit var listener: JoystickListener

    constructor(ctx: Context) : super(ctx) {
        this.holder.addCallback(this)
        setOnTouchListener(this)
        if (ctx is JoystickListener) {
            listener = ctx
        }
    }

    constructor(ctx: Context, set: AttributeSet) : super(ctx, set) {
        this.holder.addCallback(this)
        setOnTouchListener(this)
        if (ctx is JoystickListener) {
            listener = ctx
        }
    }

    constructor(ctx: Context, set: AttributeSet, style: Int) : super(ctx, set, style) {
        this.holder.addCallback(this)
        setOnTouchListener(this)
        if (ctx is JoystickListener) {
            listener = ctx
        }
    }

    /**
     * The function initializes the basic values of the joystick and draws it
     */
    override fun surfaceCreated(holder: SurfaceHolder) {
        setupDimensions()
        drawJoystick(centerX.toFloat(), centerY.toFloat())
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
    }

    /**
     * The function set up the dimensions of the joystick before drawing
     */
    private fun setupDimensions() {
        centerX = width / 2.toDouble()
        centerY = height / 2.toDouble()
        baseRadius = Math.min(width, height) / 3.toDouble()
        hatRadius = Math.min(width, height) / 5.toDouble()
    }

    /**
     * The function draws the joystick on a canvas
     */
    private fun drawJoystick(newX: Float, newY: Float) {
        // prevents from executing when the surface is not created
        if (holder.surface.isValid) {
            val myCanvas: Canvas = this.holder.lockCanvas()
            val colors = Paint()
            // clears the canvas before starts drawing
            myCanvas.drawColor(Color.WHITE)
            // draw both circles of the joystick
            colors.setARGB(255, 50, 50, 50)
            myCanvas.drawCircle(centerX.toFloat(), centerY.toFloat(), baseRadius.toFloat(), colors)
            colors.setARGB(255, 0, 0, 255)
            myCanvas.drawCircle(newX, newY, hatRadius.toFloat(), colors)
            // write the new drawing to the surface view
            holder.unlockCanvasAndPost(myCanvas)
        }
    }

    /**
     * The function creates the joysticks movement on touch event
     */
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        // accept touches coming only from the surface view
        if (event != null) {
            if (v == this) {
                if (event.action != ACTION_UP) {
                    // checks if the circle is out of bounds
                    val displacement: Double = Math.sqrt(
                        Math.pow(event.x.toDouble() - centerX, 2.0) +
                                Math.pow(event.y.toDouble() - centerY, 2.0)
                    )
                    // if not, draw the joystick
                    if (displacement.toFloat() < baseRadius) {
                        drawJoystick(event.x, event.y)
                        // send the data to the listener
                        listener.onJoystickTouch(
                            (event.x - centerX) / baseRadius,
                            (event.y - centerY) / baseRadius
                        )
                    } else { // if it is
                        // stop the joystick from exiting the outer circle
                        val ratio = baseRadius / displacement
                        val constraintX = centerX + (event.x - centerX) * ratio
                        val constraintY = centerY + (event.y - centerY) * ratio
                        drawJoystick(constraintX.toFloat(), constraintY.toFloat())
                        // send the data to the listener
                        listener.onJoystickTouch(
                            ((constraintX - centerX) / baseRadius),
                            ((constraintY - centerY) / baseRadius)
                        )
                    }
                } else {
                    // reset the joysticks, position
                    drawJoystick(centerX.toFloat(), centerY.toFloat())
                    // send the data to the listener
                    listener.onJoystickTouch(0.0, 0.0)
                }
            }
        }
        return true
    }

}