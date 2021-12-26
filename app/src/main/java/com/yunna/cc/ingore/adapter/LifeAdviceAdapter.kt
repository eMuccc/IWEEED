package com.yunna.cc.ingore.adapter

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.liys.view.WaterWaveProView
import com.yunna.cc.ingore.R
import com.yunna.cc.ingore.bean.LifeAdviceBean

class LifeAdviceAdapter(val mList: List<LifeAdviceBean>) :
    RecyclerView.Adapter<LifeAdviceAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val advice = itemView.findViewById<TextView>(R.id.rlv_life_idea_advice)
        val speed360 = itemView.findViewById<WaterWaveProView>(R.id.rlv_life_idea_speed360)
        val lvl = itemView.findViewById<TextView>(R.id.rlv_life_idea_level)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): LifeAdviceAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.life_advice_view_rlv, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: LifeAdviceAdapter.ViewHolder, position: Int) {
        if (mList[position].name == "空气污染扩散条件指数") {
            holder.advice.text = "污染指数"
        }else{
            holder.advice.text=mList[position].name
        }
        holder.lvl.text = mList[position].category
        holder.speed360.progress = (mList[position].level.toDouble())
        when (mList[position].name) {
            "运动指数", "钓鱼指数" -> {
                holder.speed360.maxProgress = 3.0
            }

            "洗车指数", "感冒指数", "空调开启指数" -> {
                holder.speed360.maxProgress = 4.0

            }

            "穿衣指数", "舒适度指数" -> {
                holder.speed360.maxProgress = 7.0
            }

            "紫外线指数", "旅游指数", "花粉过敏指数", "空气污染扩散条件指数", "太阳镜指数", "交通指数", "防晒指数" -> {
                holder.speed360.maxProgress = 5.0
            }

            "化妆指数" -> {
                holder.speed360.maxProgress = 8.0
            }

            "晾晒指数" -> {
                holder.speed360.maxProgress = 6.0
            }


        }
    }

    override fun getItemCount(): Int {
        mList.apply {
            return size
        }
    }

}