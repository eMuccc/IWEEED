package com.yunna.cc.ingore.util

import com.yunna.cc.ingore.R

object ResourceUtil {
    fun getIconResource(code: String?): Int {
        return when (code) {
            "100" -> R.drawable.ic_weather_100
            "101" -> R.drawable.ic_weather_101
            "102" -> R.drawable.ic_weather_102
            "103" -> R.drawable.ic_weather_103
            "104", "154" -> R.drawable.ic_weather_104
            "150" -> R.drawable.ic_weather_150
            "151" -> R.drawable.ic_weather_151
            "152" -> R.drawable.ic_weather_152
            "153" -> R.drawable.ic_weather_153
            "300" -> R.drawable.ic_weather_300
            "301" -> R.drawable.ic_weather_301
            "302" -> R.drawable.ic_weather_302
            "303" -> R.drawable.ic_weather_303
            "304" -> R.drawable.ic_weather_304
            "305" -> R.drawable.ic_weather_305
            "306" -> R.drawable.ic_weather_306
            "307" -> R.drawable.ic_weather_307
            "308" -> R.drawable.ic_weather_308
            "309" -> R.drawable.ic_weather_309
            "310" -> R.drawable.ic_weather_310
            "311" -> R.drawable.ic_weather_311
            "312" -> R.drawable.ic_weather_312
            "313" -> R.drawable.ic_weather_313
            "314" -> R.drawable.ic_weather_314
            "315" -> R.drawable.ic_weather_315
            "316" -> R.drawable.ic_weather_316
            "317" -> R.drawable.ic_weather_317
            "318" -> R.drawable.ic_weather_318
            "350" -> R.drawable.ic_weather_350
            "351" -> R.drawable.ic_weather_351
            "399" -> R.drawable.ic_weather_399
            "400" -> R.drawable.ic_weather_400
            "401" -> R.drawable.ic_weather_401
            "402" -> R.drawable.ic_weather_402
            "403" -> R.drawable.ic_weather_403
            "404" -> R.drawable.ic_weather_404
            "405" -> R.drawable.ic_weather_405
            "406" -> R.drawable.ic_weather_406
            "407" -> R.drawable.ic_weather_407
            "408" -> R.drawable.ic_weather_408
            "409" -> R.drawable.ic_weather_409
            "410" -> R.drawable.ic_weather_410
            "456" -> R.drawable.ic_weather_456
            "457" -> R.drawable.ic_weather_457
            "499" -> R.drawable.ic_weather_499
            "500" -> R.drawable.ic_weather_500
            "501" -> R.drawable.ic_weather_501
            "502" -> R.drawable.ic_weather_502
            "503" -> R.drawable.ic_weather_503
            "504" -> R.drawable.ic_weather_504
            "507" -> R.drawable.ic_weather_507
            "508" -> R.drawable.ic_weather_508
            "509" -> R.drawable.ic_weather_509
            "510" -> R.drawable.ic_weather_510
            "511" -> R.drawable.ic_weather_511
            "512" -> R.drawable.ic_weather_512
            "513" -> R.drawable.ic_weather_513
            "514" -> R.drawable.ic_weather_514
            "515" -> R.drawable.ic_weather_515
            "800" -> R.drawable.ic_weather_800
            "801" -> R.drawable.ic_weather_801
            "802" -> R.drawable.ic_weather_802
            "803" -> R.drawable.ic_weather_803
            "804" -> R.drawable.ic_weather_804
            "805" -> R.drawable.ic_weather_805
            "806" -> R.drawable.ic_weather_806
            "807" -> R.drawable.ic_weather_807
            "900" -> R.drawable.ic_weather_900
            "901" -> R.drawable.ic_weather_901
            "999" -> R.drawable.ic_weather_999
            else -> R.drawable.ic_launcher_foreground
        }
    }
}