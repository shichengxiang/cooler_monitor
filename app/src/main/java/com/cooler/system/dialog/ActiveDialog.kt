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

object ActiveDialogUtil {

    /**
     * display active dialog
     * @param context Context
     * @return Dialog
     */
    fun show(context: Context,click:(String)->Unit):Dialog {
        val contentView = LayoutInflater.from(context).inflate(R.layout.dialog_active, null)
        val uniqueID = Util.getUniqueID(context)
        var dialog = AlertDialog.Builder(context).apply {
            setView(contentView)
            setCancelable(false)
        }.show()
        contentView.findViewById<TextView>(R.id.tv_device_code).text = "设备序列： $uniqueID"
        val tvSure = contentView.findViewById<View>(R.id.tv_sure)
        val etActive = contentView.findViewById<EditText>(R.id.et_active)
        tvSure.setOnClickListener {
            val ac = etActive.text.toString()
            if(ac.isNullOrEmpty()) toast("激活失败")
            else{
                click?.invoke(ac)
            }
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
}