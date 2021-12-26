package com.yunna.cc.ingore.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object WeekFormat {
    @SuppressLint("SimpleDateFormat")
    fun getWeek(Jdate: String): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val date = simpleDateFormat.parse(Jdate)

        val calendar = GregorianCalendar()
        calendar.time = Date()
        calendar.add(Calendar.DATE, 1)

        val calendarTom = GregorianCalendar()
        calendarTom.time = SimpleDateFormat("yyyy-MM-dd").parse(Jdate)

        if (Jdate == SimpleDateFormat("yyyy-MM-dd").format(Date())) {
            return "今天"
        }
        if (SimpleDateFormat("yyyy-MM-dd").format(calendarTom.time)
                .equals(SimpleDateFormat("yyyy-MM-dd").format(calendar.time))
        ) {
            return "明天"
        }
        return getWeekOfDate(date);
    }

    private fun getWeekOfDate(dt: Date): String {
        val list = arrayOf("周日", "周一", "周二", "周三", "周四", "周五", "周六")
        val calendar = Calendar.getInstance()
        calendar.time = dt

        var w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0
        return list[w]
    }
}