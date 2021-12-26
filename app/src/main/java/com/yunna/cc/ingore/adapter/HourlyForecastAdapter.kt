package com.yunna.cc.ingore.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yunna.cc.ingore.R
import com.yunna.cc.ingore.bean.HourlyForecastBean
import com.yunna.cc.ingore.util.ResourceUtil

class HourlyForecastAdapter(private val mList: List<HourlyForecastBean>) :
    RecyclerView.Adapter<HourlyForecastAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val temp = itemView.findViewById<TextView>(R.id.hourly_heather_temp)
        val fxTime = itemView.findViewById<TextView>(R.id.hourly_heather_fxTime)
        val icon = itemView.findViewById<ImageView>(R.id.hourly_heather_iv_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyForecastAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.hourly_weather_view_rlv, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: HourlyForecastAdapter.ViewHolder, position: Int) {
        holder.temp.text = mList[position].temp
        holder.fxTime.text = mList[position].fxTime
        holder.icon.setImageResource(ResourceUtil.getIconResource(mList[position].icon))
    }

    override fun getItemCount(): Int {
        mList.apply { return size }
    }
}