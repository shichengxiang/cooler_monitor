package com.cooler.system

import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.cooler.system.entities.CoolerBean

class DeviceInfoAdapter2(var statusHeight:Int) :BaseQuickAdapter<CoolerBean,BaseViewHolder>(R.layout.item_device_info2){
    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return super.onCreateDefViewHolder(parent, viewType)
    }

    override fun createBaseViewHolder(view: View): BaseViewHolder {
        view.layoutParams.height = (App.getScreenHeight()- 110- toPx(44f)-statusHeight) / 3
        return super.createBaseViewHolder(view)
    }
    override fun convert(holder: BaseViewHolder, item: CoolerBean) {
    }
}