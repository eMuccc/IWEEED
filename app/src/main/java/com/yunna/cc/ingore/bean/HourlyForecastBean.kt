package com.yunna.cc.ingore.bean

import org.litepal.crud.LitePalSupport

data class HourlyForecastBean(val fxTime:String,val temp:String,val text:String,val icon:String): LitePalSupport()