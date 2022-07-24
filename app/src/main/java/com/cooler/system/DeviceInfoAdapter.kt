package com.cooler.system

import android.app.Activity
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.cooler.system.entities.DeviceBean
import com.gyf.immersionbar.ImmersionBar

class DeviceInfoAdapter :BaseQuickAdapter<DeviceBean,BaseViewHolder>(R.layout.item_device_info){
    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return super.onCreateDefViewHolder(parent, viewType)
    }

    override fun createBaseViewHolder(view: View): BaseViewHolder {
        view.layoutParams.width = (Resources.getSystem().displayMetrics.widthPixels-ImmersionBar.getActionBarHeight(context as Activity)- toPx(10f)*2) / 3
        return super.createBaseViewHolder(view)
    }
    override fun convert(holder: BaseViewHolder, item: DeviceBean) {
        holder.getView<TextView>(R.id.tv_line11).text =item.code
        holder.getView<TextView>(R.id.tv_line12).text ="\\u4fdd" //item.name
        val tvTemp = holder.getView<TextView>(R.id.tv_line22)
        tvTemp.text = "${item.temperature.float()/10}℃"
        val tvHas= holder.getView<TextView>(R.id.tv_line32)
        tvHas.text = when(item.isHas){
            0->""
            1->"有"
            else ->"无"
        }
        val tvState = holder.getView<TextView>(R.id.tv_line42)
        tvState.text = when(item.state){
            1->"正常"
            2->"异常"
            else -> ""
        }
    }
}