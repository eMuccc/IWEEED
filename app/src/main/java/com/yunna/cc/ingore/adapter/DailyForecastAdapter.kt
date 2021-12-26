package com.yunna.cc.ingore.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yunna.cc.ingore.R
import com.yunna.cc.ingore.bean.DailyForecastBean
import com.yunna.cc.ingore.util.ResourceUtil
import com.yunna.cc.ingore.util.WeekFormat
import com.yunna.cc.ingore.view.WeatherView

class DailyForecastAdapte(private val mList: List<DailyForecastBean>) :
    RecyclerView.Adapter<DailyForecastAdapte.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val temp = itemView.findViewById<TextView>(R.id.daily_forecast_temp)
        val date = itemView.findViewById<TextView>(R.id.daily_forecast_date)
        val icon = itemView.findViewById<ImageView>(R.id.daily_forecast_text)
        val bg =itemView.findViewById<WeatherView>(R.id.otherBg)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): DailyForecastAdapte.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.daily_forecast_view_rlv, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DailyForecastAdapte.ViewHolder, position: Int) {
        if (position <= 5) {
            holder.date.text = WeekFormat.getWeek(mList[position].fxDate)
        } else {
            if (mList[position].fxDate.substring(5,6)=="0" && mList[position].fxDate.substring(8, 9) == "0"){
                    holder.date.text = mList[position].fxDate.substring(6, 7) + "/" + mList[position].fxDate.substring(9, 10)
            }else if (mList[position].fxDate.substring(5, 6) == "0") {
                holder.date.text = mList[position].fxDate.substring(6, 7) + "/" + mList[position].fxDate.substring(8, 10)
            }else if (mList[position].fxDate.substring(8, 9) == "0"){
                holder.date.text = mList[position].fxDate.substring(5, 7) + "/" + mList[position].fxDate.substring(9, 10)
            }else{
                holder.date.text = mList[position].fxDate.substring(5, 7) + "/" + mList[position].fxDate.substring(8, 10)
            }
        }
        holder.bg.setMyType(WeatherView.Type.sunday)
        holder.icon.setImageResource(ResourceUtil.getIconResource(mList[position].iconDay))
        holder.temp.text = mList[position].tempMin + "°/" + mList[position].tempMax + "°"
    }

    override fun getItemCount(): Int {
        mList.apply {
            return size
        }
    }
}