package com.yunna.cc.ingore.util

import android.content.Context
import android.widget.Toast

object ToastUtil {
    fun longMessage(message: String, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun shortMessage(message: String, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}