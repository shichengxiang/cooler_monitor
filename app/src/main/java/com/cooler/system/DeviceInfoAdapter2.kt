package com.cooler.system

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.cooler.system.entities.DeviceBean

class DeviceInfoAdapter2(var statusHeight:Int) :BaseQuickAdapter<DeviceBean,BaseViewHolder>(R.layout.item_device_info3){
    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return super.onCreateDefViewHolder(parent, viewType)
    }

    override fun createBaseViewHolder(view: View): BaseViewHolder {
        view.layoutParams.height = (App.getScreenHeight()- 110- toPx(44f)-statusHeight) / 3
        return super.createBaseViewHolder(view)
    }
    override fun convert(holder: BaseViewHolder, item: DeviceBean) {
//        holder.getView<TextView>(R.id.tv_line11).text =item.code
//        holder.getView<TextView>(R.id.tv_line12).text =item.name
//        val tvTemp = holder.getView<TextView>(R.id.tv_line22)
//        tvTemp.text = String.format("%.1f℃",item.temperature?.div(10)?:0f)
//        val tvHas= holder.getView<TextView>(R.id.tv_line32)
//        val tvBeizu = holder.getView<TextView>(R.id.tv_line52)
//        with(tvBeizu){
//            isFocusable=true
//            isSingleLine=true
//            marqueeRepeatLimit = -1
//            isFocusableInTouchMode=true
//        }
//        when(item.isHas){
//            0->{
//                tvHas.text =""
////                tvHas.setTextColor(ContextCompat.getColor(context,R.color.txt_mark))
//            }
//            1->{
//                tvHas.text ="有"
//                tvHas.setTextColor(ContextCompat.getColor(context,R.color.txt_green))
//            }
//            else ->{
//                tvHas.text ="无"
//                tvHas.setTextColor(ContextCompat.getColor(context,R.color.txt_red))
//            }
//        }
//        val tvState = holder.getView<TextView>(R.id.tv_line42)
//        when(item.state){
//            0->{
//                tvState.text = ""
//                tvState.setBackgroundResource(R.drawable.rect_box_normal)
//            }
//            1->{
//                tvState.text = "正 常"
//                tvState.setTextColor(ContextCompat.getColor(context,R.color.white))
//                tvState.setBackgroundResource(R.drawable.rect_box_green)
//            }
//            else->{
//                tvState.text = "异 常"
//                tvState.setTextColor(ContextCompat.getColor(context,R.color.white))
//                tvState.setBackgroundResource(R.drawable.rect_box_red)
//            }
//        }
    }

    override fun convert(holder: BaseViewHolder, item: DeviceBean, payloads: List<Any>) {
        super.convert(holder, item, payloads)
        if (payloads.isEmpty()) return
//        when(payloads[0]){
//            "0"->holder.getView<TextView>(R.id.tv_line11).text = item.code
//            "1"->holder.getView<TextView>(R.id.tv_line12).text = item.name
//            "2"->holder.getView<TextView>(R.id.tv_line22).text =String.format("%.1f℃",item.temperature?.div(10)?:0f)
//            "3"->{
//                val tvHas = holder.getView<TextView>(R.id.tv_line32)
//                when(item.isHas){
//                    0->{
//                        tvHas.text =""
//                        tvHas.setBackgroundResource(R.drawable.rect_box_normal)
//                    }
//                    1->{
//                        tvHas.text ="有"
//                        tvHas.setTextColor(ContextCompat.getColor(context,R.color.txt_green))
//                    }
//                    else ->{
//                        tvHas.text ="无"
//                        tvHas.setTextColor(ContextCompat.getColor(context,R.color.txt_red))
//                    }
//                }
//            }
//            "4"->{
//                val tvState = holder.getView<TextView>(R.id.tv_line42)
//                when(item.state){
//                    0->{
//                        tvState.text = ""
//                        tvState.setBackgroundResource(R.drawable.rect_box_normal)
//                    }
//                    1->{
//                        tvState.text = "正 常"
//                        tvState.setTextColor(ContextCompat.getColor(context,R.color.white))
//                        tvState.setBackgroundResource(R.drawable.rect_box_green)
//                    }
//                    else->{
//                        tvState.text = "异 常"
//                        tvState.setTextColor(ContextCompat.getColor(context,R.color.white))
//                        tvState.setBackgroundResource(R.drawable.rect_box_red)
//                    }
//                }
//            }
//        }
    }
}