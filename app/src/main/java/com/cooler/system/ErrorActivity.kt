package com.cooler.system

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.cooler.system.databinding.ActivityErrorBinding

class ErrorActivity : AppCompatActivity() {
    private var bind: ActivityErrorBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityErrorBinding.inflate(layoutInflater)
        setContentView(bind!!.root)
        val err = intent.getStringExtra("error")
        showErrorDialog(this,err?:"")
    }

    private fun showErrorDialog(context: Context,error:String) {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_error, null)
        val dialog = AlertDialog.Builder(context).apply {
            setView(view)
            setOnDismissListener {
                finish()
            }
        }.create()
        if (!isFinishing && !isDestroyed) dialog.show()
        view.findViewById<TextView>(R.id.tv_close).setOnClickListener {
            dialog.dismiss()
            finish()
            System.exit(0)
        }
        view.findViewById<TextView>(R.id.tv_content).text = error
    }
}