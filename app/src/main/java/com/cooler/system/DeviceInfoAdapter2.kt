package com.cooler.system

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.cooler.system.custom.TempTextView
import com.cooler.system.entities.CoolerBean
import com.cooler.system.util.Util

class DeviceInfoAdapter2(var statusHeight: Int) :
    BaseQuickAdapter<CoolerBean, BaseViewHolder>(R.layout.item_device_info2) {
    private var mWarning: Float? = null
    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return super.onCreateDefViewHolder(parent, viewType)
    }

    override fun createBaseViewHolder(view: View): BaseViewHolder {
        view.layoutParams.height =
            (App.getScreenHeight() - 110 - toPx(44f) - statusHeight) / getDefItemCount()
        return super.createBaseViewHolder(view)
    }

    override fun convert(holder: BaseViewHolder, item: CoolerBean) {
        val tvCode = holder.getView<TextView>(R.id.tv_code)
        val tvName = holder.getView<TextView>(R.id.tv_name)
        val tvSex = holder.getView<TextView>(R.id.tv_sex)
        val tvAge = holder.getView<TextView>(R.id.tv_age)
        val tvStoreTime = holder.getView<TextView>(R.id.tv_store_time)
        val tvTemperature = holder.getView<TempTextView>(R.id.tv_temperature)
        val tvIsHas = holder.getView<TextView>(R.id.tv_isHas)
        val tvState = holder.getView<TextView>(R.id.tv_state)
        val tvRemark = holder.getView<TextView>(R.id.tv_remark)
        tvCode.text = item.equipmentCode
        tvName.text = item.deadName
        tvName.isSelected = true
        tvSex.text = item.deadGender
        tvAge.text = item.deadAge
        tvStoreTime.isSelected = true
        if (tvStoreTime.text.toString() != item.realityInTime) {
            tvStoreTime.text = item.realityInTime
        }
        if (item.temperature.isNullOrEmpty()) {
            tvTemperature.text = ""
            tvTemperature.enableTwinkle(false)
        } else {
            tvTemperature.text = "${item.temperature ?: ""}℃"
            tvTemperature.enableTwinkle(compareTemp(item.temperature))
        }
        tvIsHas.text = item.asHaveDead
        if (item.asHaveDead == "有") {
            tvIsHas.setTextColor(ContextCompat.getColor(context, R.color.txt_green))
        } else {
            tvIsHas.setTextColor(ContextCompat.getColor(context, R.color.txt_red))
        }
        if (item.disableStateStr == "正常") {
            tvState.setTextColor(ContextCompat.getColor(context, R.color.white))
            tvState.setBackgroundResource(R.drawable.rect_box_green)
        } else if (item.disableStateStr == "异常") {
            tvState.setTextColor(ContextCompat.getColor(context, R.color.white))
            tvState.setBackgroundResource(R.drawable.rect_box_red)
        } else {
            tvState.setBackgroundResource(R.drawable.rect_box_normal)
        }
        tvState.text = item.disableStateStr
        if (tvRemark.text.toString() != item.remark) {
            tvRemark.text = item.remark
        }
    }

    override fun convert(holder: BaseViewHolder, item: CoolerBean, payloads: List<Any>) {
        if (payloads.isNotEmpty()) {
            val tvName = holder.getView<TextView>(R.id.tv_name)
            val tvSex = holder.getView<TextView>(R.id.tv_sex)
            val tvAge = holder.getView<TextView>(R.id.tv_age)
            val tvStoreTime = holder.getView<TextView>(R.id.tv_store_time)
            val tvTemperature = holder.getView<TempTextView>(R.id.tv_temperature)
            val tvIsHas = holder.getView<TextView>(R.id.tv_isHas)
            val tvState = holder.getView<TextView>(R.id.tv_state)
            val tvRemark = holder.getView<TextView>(R.id.tv_remark)
            if (payloads[0] == "part") {
                if (tvName.text.toString() != item.deadName) tvName.text = item.deadName
                if (tvSex.text.toString() != item.deadGender) tvSex.text = item.deadGender
                if (tvAge.text.toString() != item.deadAge) tvAge.text = item.deadAge
                if (tvStoreTime.text.toString() != item.realityInTime) {
                    tvStoreTime.text = item.realityInTime
                }
                if (item.temperature.isNullOrEmpty()) {
                    tvTemperature.text = ""
                    tvTemperature.enableTwinkle(false)
                } else {
                    tvTemperature.text = "${item.temperature ?: ""}℃"
                    tvTemperature.enableTwinkle(compareTemp(item.temperature))
                }
                if (tvIsHas.text.toString() != item.asHaveDead) {
                    tvIsHas.text = item.asHaveDead
                    if (item.asHaveDead == "有") {
                        tvIsHas.setTextColor(ContextCompat.getColor(context, R.color.txt_green))
                    } else {
                        tvIsHas.setTextColor(ContextCompat.getColor(context, R.color.txt_red))
                    }
                    if (item.disableStateStr == "正常") {
                        tvState.setTextColor(ContextCompat.getColor(context, R.color.white))
                        tvState.setBackgroundResource(R.drawable.rect_box_green)
                    } else if (item.disableStateStr == "异常") {
                        tvState.setTextColor(ContextCompat.getColor(context, R.color.white))
                        tvState.setBackgroundResource(R.drawable.rect_box_red)
                    } else {
                        tvState.setBackgroundResource(R.drawable.rect_box_normal)
                    }
                }
                if (tvState.text.toString() != item.disableStateStr) tvState.text =
                    item.disableStateStr
                if (tvRemark.text.toString() != item.remark) {
                    tvRemark.text = item.remark
                }
            }
        }

    }
//
//    override fun setList(list: Collection<CoolerBean>?) {
//        super.setList(list)
//    }

    /**
     * 初始化数据 根据输入的设备编号陈列
     */
    fun initDevideCode(codes: Array<String>) {
        mWarning = Util.getWarningTemp()
        val list = mutableListOf<CoolerBean>()
        codes.forEach {
            list.add(CoolerBean().apply {
                equipmentCode = it
            })
        }
        setList(list)
    }

    /**
     * 刷新数据
     */
    fun refreshData(list: Collection<CoolerBean>?) {
        list?.forEach { c ->
            data.forEachIndexed { index, coolerBean ->
                if (coolerBean.equipmentCode.equals(c.equipmentCode, true)) {
                    if(coolerBean.asHaveDead  == c.asHaveDead &&
                        coolerBean.deadName  == c.deadName &&
                        coolerBean.deadAge  == c.deadAge &&
                        coolerBean.disableStateStr  == c.disableStateStr &&
                        coolerBean.deadGender  == c.deadGender &&
                        coolerBean.temperature  == c.temperature &&
                        coolerBean.realityInTime  == c.realityInTime &&
                        coolerBean.remark  == c.remark){
                    }else{
                        coolerBean.asHaveDead = c.asHaveDead
                        coolerBean.deadName = c.deadName
                        coolerBean.deadAge = c.deadAge
                        coolerBean.disableStateStr = c.disableStateStr
                        coolerBean.deadGender = c.deadGender
                        coolerBean.temperature = c.temperature
                        coolerBean.realityInTime = c.realityInTime
                        coolerBean.remark = c.remark
                        notifyItemChanged(index, "part")
                    }
                    return@forEachIndexed
                }
            }
        }
    }
    /**
     * 刷新数据
     */
    fun refreshData() {
            data.forEachIndexed { index, coolerBean ->
                    coolerBean.asHaveDead = "1"
                    coolerBean.deadName = "张三"
                    coolerBean.deadAge = "20"
                    coolerBean.disableStateStr ="有"
                    coolerBean.deadGender = "d"
                    coolerBean.temperature = "20"
                    coolerBean.realityInTime = "dddd"
                    coolerBean.remark = "dddddddddd"
//                    notifyItemChanged(index)
                    notifyItemChanged(index, "part")
                    return@forEachIndexed
            }
    }

    fun compareTemp(des: String?): Boolean {
        if (des.isNullOrEmpty()) {
            return false
        }
        if (mWarning == null) {
            return false
        }
        try {
            val d = des.toFloat()
            return d >= (mWarning ?: 0f)
        }catch (e:Exception){

        }
        return false
    }
}