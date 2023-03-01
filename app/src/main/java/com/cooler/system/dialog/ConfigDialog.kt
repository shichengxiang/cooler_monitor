package com.cooler.system.dialog

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.cooler.system.R
import com.cooler.system.toast
import com.cooler.system.util.Util
import java.util.regex.Pattern

object ConfigDialog {

    /**
     * display active dialog
     * @param context Context
     * @return Dialog
     */
    fun show(context: Context,click:()->Unit):Dialog {
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
        val code1= contentView.findViewById<EditText>(R.id.et_device_code1)
        val code2 = contentView.findViewById<EditText>(R.id.et_device_code2)
        val code3 = contentView.findViewById<EditText>(R.id.et_device_code3)
        val warningTemp = contentView.findViewById<EditText>(R.id.et_warning_temp)
        val host = Util.getHost()
        etIp.setText(host.first?:"0.0.0.0")
        etPort.setText(host.second?:"")
        etPerTime.setText(Util.getPerTime().toString())
        warningTemp.setText("${Util.getWarningTemp()?:""}")
        val deviceCodes = Util.getDeviceCodes()
        deviceCodes?.forEachIndexed { index, s ->
            when (index) {
                0 -> code1.setText(s)
                1 -> code2.setText(s)
                2 -> code3.setText(s)
            }
        }
        btnSure.setOnClickListener {
            val ip = etIp.text.toString()
            val port = etPort.text.toString()
            val perTime =  etPerTime.text.toString()
            val warningTemp = warningTemp.text.toString()
            var floatTemp :Float?=null
            try{
                floatTemp = warningTemp.toFloat()
            }catch (e:Exception){
            }
            if(floatTemp == null){
                toast("预警阀值请输入数字")
                return@setOnClickListener
            }
            if(code1.text.toString().isEmpty() && code2.text.toString().isEmpty() && code2.text.toString().isEmpty()){
                toast("请至少填写一个设备号 ！")
                return@setOnClickListener
            }
            if(checkIp(ip)){ //输入的是ip形式
                if(port.isEmpty()){
                    toast("端口号不能为空")
                    return@setOnClickListener
                }else{
                    Util.saveHost(ip,port)
                }
            }else{
                Util.saveHost(ip,"")
            }
            Util.saveWarningTemp(warningTemp)
            if(checkTime(perTime)){
                //保存数据
                Util.savePerTime(perTime.toInt())
                Util.saveDeviceCodes(code1.text.toString(),code2.text.toString(),code3.text.toString())
                toast("设置成功！")
                dialog.dismiss()
                click.invoke()
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
            return false
        }
        val str= "\\d+\\.\\d+\\.\\d+\\.\\d+"
        val compile = Pattern.compile(str)
        val matcher = compile.matcher(ip)
        return matcher.matches()
    }

    /**
     * 检验时间不能小于1
     */
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