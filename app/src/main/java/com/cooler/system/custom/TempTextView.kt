package com.cooler.system.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.cooler.system.log
import java.util.*

class TempTextView :AppCompatTextView {
    private var isTwinkle = false
    private var count = 0
    private var mTimer:Timer?=null
    private var mTask:TimerTask?=null
    private var mColors= arrayOf(Color.parseColor("#891D03"),Color.parseColor("#0F7B02"))
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){

    }
    fun enableTwinkle(able:Boolean){
        log("enable =$able")
        if(isTwinkle == able ) return
        isTwinkle = able
        log("isTwinkle =$able")
        if(isTwinkle){
            if(mTimer== null){
                mTimer = Timer()
            }
            if(mTask == null){
                mTask = object :TimerTask() {
                    override fun run() {
                        setTextColor(mColors[count % 2])
                        count++
                    }
                }
            }
            mTimer?.schedule(mTask,600L,600L)
        }else {
            setTextColor(mColors[1])
            mTask?.cancel()
            mTask=null
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
//        if(isTwinkle){
//            log("======== true")
//            postDelayed({
//                setTextColor(mColors[count % 2])
//                count++
//            },600)
//        }
    }
}