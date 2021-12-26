package com.yunna.cc.ingore.util

import android.graphics.Color

data class LinearGradientUtil(val mStartColor: Int, val mEndColor: Int) {
    fun getColor(radio: Float): Int {
        val redStart = Color.red(mStartColor)
        val blueStart = Color.blue(mStartColor)
        val greenStart = Color.green(mStartColor)
        val redEnd = Color.red(mEndColor)
        val blueEnd = Color.blue(mEndColor)
        val greenEnd = Color.green(mEndColor)

        val red = (redStart + ((redEnd - redStart) * radio + 0.5f)).toInt()
        val greed = (greenStart + ((greenEnd - greenStart) * radio + 0.5f)).toInt()
        val blue = (blueStart + ((blueEnd - blueStart) * radio + 0.5f)).toInt()
        return Color.argb(255, red, greed, blue)
    }
}