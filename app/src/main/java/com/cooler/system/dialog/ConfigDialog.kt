package com.cooler.system.dialog

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.cooler.system.R
import com.cooler.system.toast
import com.cooler.system.util.Util
import com.tencent.mmkv.MMKV
import java.lang.Exception
import java.util.regex.Pattern

object ConfigDialog {

    /**
     * display active dialog
     * @param context Context
     * @return Dialog
     */
    fun show(context: Context,click:(String)->Unit):Dialog {
        val contentView = LayoutInflater.from(context).inflate(R.layout.dialog_config, null)
        var dialog = AlertDialog.Builder(context).apply {
            setView(contentView)
            setCancelable(false)
        }.show()
        val etIp = contentView.findViewById<EditText>(R.id.et_ip)
        val etPort = contentView.findViewById<EditText>(R.id.et_port)
        val etPerTime = contentView.findViewById<EditText>(R.id.et_per_time)
        val btnSure = contentView.findViewById<View>(R.id.tv_sure)
        val btnCancle = contentView.findViewById<View>(R.id.tv_cancel)
        val host = Util.getHost()
        etIp.setText(host.first?:"0.0.0.0")
        etPort.setText(host.second?:"")
        etPerTime.setText(Util.getPerTime().toString())
        btnSure.setOnClickListener {
            val ip = etIp.text.toString()
            val port = etPort.text.toString()
            val perTime =  etPerTime.text.toString()
            if(port.isEmpty()){
                toast("端口号不能为空")
                return@setOnClickListener
            }
            if(checkIp(ip) && checkTime(perTime)){
                //保存数据

                toast("设置成功！")
                dialog.dismiss()
            }
        }
        btnCancle.setOnClickListener {
            dialog.dismiss()
        }
        dialog?.window?.apply {
            setLayout(Resources.getSystem().displayMetrics.widthPixels/2,ViewGroup.LayoutParams.WRAP_CONTENT)
            setWindowAnimations(R.style.TopDialogAnim)
        }
        dialog?.window?.decorView?.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.transparency
            )
        )
        return dialog
    }

    /**
     * 校验ip
     */
    private fun  checkIp(ip:String?):Boolean{
        if(ip.isNullOrEmpty()){
            toast("ip不能为空")
            return false
        }
        val str= "\\d+\\.\\d+\\.\\d+\\.\\d+"
        val compile = Pattern.compile(str)
        val matcher = compile.matcher(ip)
        return if(matcher.matches()){
            true
        }else {
            toast("请验证ip格式是否正确")
            false
        }
    }
    private fun checkTime(time:String):Boolean{
        try {
            if(time.toInt()<1){
                toast("间隔时间不能少于1秒")
                return false
            }
        }catch (e:Exception){
            toast("时间格式错误！")
            return false
        }
        return true
    }

}