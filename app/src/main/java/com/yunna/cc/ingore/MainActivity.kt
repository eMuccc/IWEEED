package com.yunna.cc.ingore

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.google.gson.Gson
import com.liys.view.LineProView
import com.qweather.sdk.bean.weather.WeatherNowBean
import com.qweather.sdk.view.HeConfig
import com.qweather.sdk.view.QWeather
import com.tbruyelle.rxpermissions2.RxPermissions
import com.yunna.cc.ingore.adapter.DailyForecastAdapte
import com.yunna.cc.ingore.adapter.HourlyForecastAdapter
import com.yunna.cc.ingore.adapter.LifeAdviceAdapter
import com.yunna.cc.ingore.bean.*
import com.yunna.cc.ingore.gson.*
import com.yunna.cc.ingore.util.LinearGradientUtil
import com.yunna.cc.ingore.util.ResourceUtil
import com.yunna.cc.ingore.util.ToastUtil
import com.yunna.cc.ingore.view.CircleBarView
import com.yunna.cc.ingore.view.WeatherView
import com.yunna.cc.ingore.view.WhiteWindmills
import okhttp3.*
import org.litepal.LitePal
import java.io.IOException
import java.net.SocketTimeoutException
import java.text.DecimalFormat


class MainActivity : AppCompatActivity() {
    val SERVER_RELOAD_MAXTIME = 3
    var SERVER_LOAD_TIME = 0

    val LOCATION_LOAD_SUCESS = 1
    val LOCATION_LOAD_FAILED = 2
    val NOW_WEATHER_SUCESS = 3
    val NOW_WEATHER_FAILED = 4
    val HOURLY_WEATHER_SUCESS = 5
    val HOURLY_WEATHER_FAILED = 6
    val DAILY_WEATHER_SUCESS = 7
    val DAILY_WEATHER_FAILED = 8
    val AIR_SUPPORT_LOAD_SUCCESS = 9
    val AIR_SUPPORT_LOAD_FAILED = 10
    val LIFE_ADVICE_LOAD_SUCCESS = 11
    val LIFE_ADVICE_LOAD_FAILED = 12

    private var locationClient: LocationClient? = null;
    private val thisLocationListener = ssLocationListener()

    private val uiHandler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        @SuppressLint("SetTextI18n")
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                LOCATION_LOAD_SUCESS -> {
                    val address: List<LocationBean> = LitePal.findAll(LocationBean::class.java)
                    (f<TextView>(R.id.tv_city)).text =
                        address[0].LocationFir + " · " + address[0].LocationSecond

                    f<WhiteWindmills>(R.id.wind_Big).startRotate()
                    f<WhiteWindmills>(R.id.wind_small).startRotate()

                    f<WeatherView>(R.id.wind_weatherCard).setMyType(WeatherView.Type.sunday)

                    f<WeatherView>(R.id.Header_Weather).setMyType(WeatherView.Type.snowNight)
                    f<WeatherView>(R.id.footer_Weather).setMyType(WeatherView.Type.snowNight)

                    getNoWeather(address[0].LocationId)
                    getHourlyWeather(address[0].LocationId)
                    getDailyWeather(address[0].LocationId)
                    getAirSupport(address[0].LocationId)
                    getLifeAdvice(address[0].LocationId)
                }

                LOCATION_LOAD_FAILED -> {
                    val address: List<LocationBean> = LitePal.findAll(LocationBean::class.java)

                    (f<TextView>(R.id.tv_city)).text =
                        address[0].LocationFir + " · " + address[0].LocationSecond

                }

                NOW_WEATHER_FAILED, NOW_WEATHER_SUCESS -> {
                    val nowWeather: List<NowWeatherBean> =
                        LitePal.findAll(NowWeatherBean::class.java)
                    f<TextView>(R.id.tv_temp).text = nowWeather[0].temp
                    f<TextView>(R.id.tv_obsTime).text =
                        nowWeather[0].obsTime.substring(11, 16) + " 更新"
                    f<ImageView>(R.id.iv_now_text).setImageResource(ResourceUtil.getIconResource(
                        nowWeather[0].icon))

                    (f<TextView>(R.id.tv_cloud)).text = nowWeather[0].cloud + "%"
                    (f<TextView>(R.id.tv_precip)).text = nowWeather[0].precip + "mm"
                    (f<TextView>(R.id.tv_pressure)).text = nowWeather[0].pressure + "hPa"
                    (f<TextView>(R.id.tv_humidity)).text = nowWeather[0].humidity + "%"
                    (f<TextView>(R.id.tv_dew)).text = nowWeather[0].dew + "°"
                    (f<TextView>(R.id.tv_vis)).text = nowWeather[0].vis + "KM"
                    (f<TextView>(R.id.wind_status)).text =
                        nowWeather[0].windDir + " 风速: " + nowWeather[0].windSpeed


                    (f<ImageView>(R.id.icon_cloud)).setImageResource(R.drawable.cloud)
                    (f<ImageView>(R.id.icon_dew)).setImageResource(R.drawable.temp)
                    (f<ImageView>(R.id.icon_humidity)).setImageResource(R.drawable.water)
                    (f<ImageView>(R.id.vis_icon)).setImageResource(R.drawable.sight)
                    (f<ImageView>(R.id.precip_icon)).setImageResource(R.drawable.rain)
                    (f<ImageView>(R.id.pressure_icon)).setImageResource(R.drawable.pressure)


                }


                HOURLY_WEATHER_FAILED, HOURLY_WEATHER_SUCESS -> {
                    val hourlyForecastAdapter =
                        HourlyForecastAdapter(LitePal.findAll(HourlyForecastBean::class.java))
                    val manager = LinearLayoutManager(applicationContext)
                    manager.orientation = LinearLayoutManager.HORIZONTAL
                    val hourlyForecastRlv = f<RecyclerView>(R.id.hourly_heather)
                    hourlyForecastRlv.adapter = hourlyForecastAdapter
                    hourlyForecastRlv.layoutManager = manager


                }

                DAILY_WEATHER_FAILED, DAILY_WEATHER_SUCESS -> {
                    val dailyForecastBean = LitePal.findAll(DailyForecastBean::class.java)
                    val manager = LinearLayoutManager(applicationContext)
                    manager.orientation = LinearLayoutManager.VERTICAL
                    val dailyForecastRlv = f<RecyclerView>(R.id.daily_forecast)
                    (f<TextView>(R.id.tv_max_min)).text =
                        dailyForecastBean[0].tempMin + "°/" + dailyForecastBean[0].tempMax + "°"
                    dailyForecastRlv.layoutManager = manager
                    dailyForecastRlv.adapter = DailyForecastAdapte(dailyForecastBean)
                }

                AIR_SUPPORT_LOAD_SUCCESS, AIR_SUPPORT_LOAD_FAILED -> {

                    LitePal.findAll(AirSupportBean::class.java).let {
                        val circle = f<CircleBarView>(R.id.circleBarView)
                        circle.setProgressNum(it[0].aqi.toFloat(), 3000)
                        circle.setOnAnimationListener(object : CircleBarView.OnAnimationListener {
                            override fun howToChangeText(
                                interpolatedTime: Float,
                                updateNum: Float,
                                maxNum: Float,
                            ): String {
                                val decimalFormat = DecimalFormat("0")
                                val s =
                                    decimalFormat.format(interpolatedTime * updateNum / maxNum * 100)
                                return s;
                            }

                            override fun howTiChangeProgressColor(
                                paint: Paint?,
                                interpolatedTime: Float,
                                updateNum: Float,
                                maxNum: Float,
                            ) {
                                val linearGradientUtil = LinearGradientUtil(Color.GREEN, Color.BLUE)
                                paint!!.color = linearGradientUtil.getColor(interpolatedTime)


                            }

                        })
                        circle.setTextView(f(R.id.tv_AQI))
                        (f<TextView>(R.id.tv_PM2_5)).text = it[0].pm2p5
                        (f<TextView>(R.id.tv_PM10)).text = it[0].pm10
                        (f<TextView>(R.id.tv_O3)).text = it[0].o3
                        (f<TextView>(R.id.tv_CO)).text = it[0].co
                        (f<TextView>(R.id.tv_so2)).text = it[0].so2
                        (f<TextView>(R.id.tv_no2)).text = it[0].no2

                        (f<LineProView>(R.id.PM2_5_bar)).progress = it[0].pm2p5.toDouble()
                        (f<LineProView>(R.id.PM10_bar)).progress = it[0].pm10.toDouble()
                        (f<LineProView>(R.id.O3_bar)).progress = it[0].o3.toDouble()
                        (f<LineProView>(R.id.CO_bar)).progress = (it[0].co.toDouble() * 100)
                        (f<LineProView>(R.id.bar_so2)).progress = it[0].so2.toDouble()
                        (f<LineProView>(R.id.no2_bar)).progress = it[0].no2.toDouble()


                    }
                }

                LIFE_ADVICE_LOAD_SUCCESS, LIFE_ADVICE_LOAD_FAILED -> {
                    LitePal.findAll(LifeAdviceBean::class.java).let {
                        val lifeAdvices = f<RecyclerView>(R.id.rlv_life_idea)
                        val manager = LinearLayoutManager(applicationContext)
                        manager.orientation = LinearLayoutManager.HORIZONTAL
                        lifeAdvices.layoutManager = manager
                        lifeAdvices.adapter = LifeAdviceAdapter(it)
                    }
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        locationClient = LocationClient(applicationContext)
        locationClient!!.registerLocationListener(thisLocationListener)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR




        LitePal.initialize(this)



        (isNetworkConnect(applicationContext)).let {
            if (it) {
                getLocationPermiss()
            } else {
                ToastUtil.shortMessage("你这网络他不保熟啊🐎", applicationContext)
                uiHandler.sendEmptyMessage(LOCATION_LOAD_FAILED)
                uiHandler.sendEmptyMessage(NOW_WEATHER_FAILED)
                uiHandler.sendEmptyMessage(DAILY_WEATHER_FAILED)
                uiHandler.sendEmptyMessage(AIR_SUPPORT_LOAD_FAILED)
                uiHandler.sendEmptyMessage(HOURLY_WEATHER_FAILED)
                uiHandler.sendEmptyMessage(LIFE_ADVICE_LOAD_FAILED)

                f<WhiteWindmills>(R.id.wind_Big).startRotate()
                f<WhiteWindmills>(R.id.wind_small).startRotate()

                f<WeatherView>(R.id.wind_weatherCard).setMyType(WeatherView.Type.sunday)

                f<WeatherView>(R.id.Header_Weather).setMyType(WeatherView.Type.snowNight)
                f<WeatherView>(R.id.footer_Weather).setMyType(WeatherView.Type.snowNight)
            }
        }
    }

    //网络判断
    fun isNetworkConnect(context: Context): Boolean {
        val mConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mNetworkInfo = mConnectivityManager.activeNetworkInfo
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable
        }
        return false
    }


    fun getLifeAdvice(loId: String) {
        val client = OkHttpClient()
        val reqeust = Request.Builder()
            .url("https://devapi.qweather.com/v7/indices/1d?type=0&location=$loId&key=ff6f13d33aa841779574dabda82dc191")
            .build()
        client.newCall(reqeust).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (e is SocketTimeoutException && SERVER_RELOAD_MAXTIME > SERVER_LOAD_TIME) {
                    client.newCall(call.request()).enqueue(this)
                } else {
                    uiHandler.sendEmptyMessage(LIFE_ADVICE_LOAD_FAILED)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    LitePal.deleteAll(LifeAdviceBean::class.java)
                    response.body()?.string().let {

                        Gson().fromJson(it, LifeAdviceG::class.java).apply {

                            for (i in 0..15) {
                                LifeAdviceBean(
                                    daily[i].category,
                                    daily[i].date,
                                    daily[i].level,
                                    daily[i].name,
                                    daily[i].text,
                                    daily[i].type
                                ).save()
                            }
                            uiHandler.sendEmptyMessage(LIFE_ADVICE_LOAD_SUCCESS)
                        }
                    }
                }
            }
        })

    }

    //获取空气指数
    fun getAirSupport(loId: String) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://devapi.qweather.com/v7/air/now?location=$loId&key=ff6f13d33aa841779574dabda82dc191")
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (e is SocketTimeoutException && SERVER_RELOAD_MAXTIME > SERVER_LOAD_TIME) {
                    client.newCall(call.request()).enqueue(this)
                } else {
                    uiHandler.sendEmptyMessage(AIR_SUPPORT_LOAD_FAILED)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    LitePal.deleteAll(AirSupportBean::class.java)
                    response.body()?.string().let {
                        Gson().fromJson(it, AirSupport::class.java).apply {
                            AirSupportBean(
                                now.aqi,
                                now.category,
                                now.co,
                                now.level,
                                now.no2,
                                now.o3,
                                now.pm10,
                                now.pm2p5,
                                now.primary,
                                now.pubTime,
                                now.so2
                            ).save()
                        }
                        uiHandler.sendEmptyMessage(AIR_SUPPORT_LOAD_SUCCESS)
                    }
                }
            }
        })
    }


    //逐天天气
    fun getDailyWeather(loId: String) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://devapi.qweather.com/v7/weather/15d?location=$loId&key=ff6f13d33aa841779574dabda82dc191")
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (e is SocketTimeoutException && SERVER_RELOAD_MAXTIME > SERVER_LOAD_TIME) {
                    client.newCall(call.request()).enqueue(this)
                } else {
                    uiHandler.sendEmptyMessage(DAILY_WEATHER_FAILED)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body()?.string().let {
                        LitePal.deleteAll(DailyForecastBean::class.java)
                        Gson().fromJson(it, DailyForecast::class.java).apply {
                            for (i in 0..14) {
                                DailyForecastBean(
                                    daily[i].cloud,
                                    daily[i].fxDate,
                                    daily[i].humidity,
                                    daily[i].iconDay,
                                    daily[i].iconNight,
                                    daily[i].moonPhase,
                                    daily[i].moonPhaseIcon,
                                    daily[i].moonrise,
                                    daily[i].moonset,
                                    daily[i].precip,
                                    daily[i].pressure,
                                    daily[i].sunrise,
                                    daily[i].sunset,
                                    daily[i].tempMax,
                                    daily[i].tempMin,
                                    daily[i].textDay,
                                    daily[i].textNight,
                                    daily[i].uvIndex,
                                    daily[i].vis,
                                    daily[i].wind360Day,
                                    daily[i].wind360Night,
                                    daily[i].windDirDay,
                                    daily[i].windDirNight,
                                    daily[i].windScaleDay,
                                    daily[i].windScaleNight,
                                    daily[i].windSpeedDay,
                                    daily[i].windSpeedNight

                                ).save()
                                uiHandler.sendEmptyMessage(DAILY_WEATHER_SUCESS)
                            }

                        }
                    }
                }
            }
        })
    }

    //逐小时天气
    fun getHourlyWeather(loId: String) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://devapi.qweather.com/v7/weather/24h?location=$loId&key=ff6f13d33aa841779574dabda82dc191")
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (e is SocketTimeoutException && SERVER_RELOAD_MAXTIME > SERVER_LOAD_TIME) {
                    client.newCall(call.request()).enqueue(this)
                } else {
                    uiHandler.sendEmptyMessage(HOURLY_WEATHER_FAILED)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body()!!.string().let { it ->
                        String
                        Gson().fromJson<HourlyForecast>(it, HourlyForecast::class.java).apply {
                            LitePal.deleteAll(HourlyForecastBean::class.java)

                            for (i in 0..23) {
                                HourlyForecastBean(
                                    hourly[i].fxTime.substring(11, 16),
                                    hourly[i].temp + "°C",
                                    hourly[i].text,
                                    hourly[i].icon
                                ).save()
                            }


                        }
                    }
                    uiHandler.sendEmptyMessage(HOURLY_WEATHER_SUCESS)
                }
            }
        })
    }


    //获取当日天气
    fun getNoWeather(loId: String) {
//        Log.d("YYY","123")
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://devapi.qweather.com/v7/weather/now?location=$loId&key=ff6f13d33aa841779574dabda82dc191")
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (e is SocketTimeoutException && SERVER_RELOAD_MAXTIME > SERVER_LOAD_TIME) {
                    client.newCall(call.request()).enqueue(this)
                } else {
                    uiHandler.sendEmptyMessage(NOW_WEATHER_FAILED)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body()?.string().let { it ->
                        String
                        LitePal.deleteAll(NowWeatherBean::class.java)
                        Gson().fromJson<NowWeather>(it, NowWeather::class.java).let {
                            //波动拳！
                            NowWeatherBean(
                                it.now.cloud,
                                it.now.dew,
                                it.now.feelsLike,
                                it.now.humidity,
                                it.now.icon,
                                it.now.obsTime,
                                it.now.precip,
                                it.now.pressure,
                                it.now.temp,
                                it.now.text,
                                it.now.vis,
                                it.now.wind360,
                                it.now.windDir,
                                it.now.windScale,
                                it.now.windSpeed,
                            ).save()

                        }
                    }

                    uiHandler.sendEmptyMessage(NOW_WEATHER_SUCESS);

                }
            }
        })
    }


    @SuppressLint("CheckResult")
    fun getLocationPermiss() {
        val rxPermissions = RxPermissions(this);
        rxPermissions.requestEach(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ).subscribe {
            if (it.granted) {
                initLocation()
            }
        }
    }

    //初始化百度定位
    fun initLocation() {
        val option = LocationClientOption()
        option.setNeedNewVersionRgc(true)
        option.setIsNeedAddress(true)
        locationClient?.locOption = option
        locationClient?.start()
    }

    //百度SDK定位
    inner class ssLocationListener() : BDAbstractLocationListener() {
        override fun onReceiveLocation(p0: BDLocation?) {
            p0?.district?.let { Log.d("YYY", it) };
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://geoapi.qweather.com/v2/city/lookup?location=" + p0?.district + "&key=ff6f13d33aa841779574dabda82dc191")
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("TTT", "炸了")
                    if (e is SocketTimeoutException && SERVER_RELOAD_MAXTIME > SERVER_LOAD_TIME) {
                        client.newCall(call.request()).enqueue(this);
                    } else {
                        ToastUtil.shortMessage("你这网络保熟🐎", applicationContext)
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        LitePal.deleteAll(LocationBean::class.java)
                        response.body()!!.string().let { it: String? ->
                            val location = Gson().fromJson<Location>(it, Location::class.java)
                            location.let {
                                LocationBean(
                                    it.location[0].id,
                                    it.location[0].adm2,
                                    it.location[0].name
                                ).save()
                                uiHandler.sendEmptyMessage(LOCATION_LOAD_SUCESS)


                            }
                        }
                    }
                }
            })
        }

    }


    //优化findById映射
    fun <T> f(ViewId: Int): T = findViewById(ViewId)


//    fun <T:View> Int.view():T = findViewById<T>(this)
}