package com.cooler.system

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cooler.system.data.UserManager
import com.cooler.system.databinding.ActivityMainBinding
import com.cooler.system.dialog.ActiveDialogUtil
import com.cooler.system.dialog.ConfigDialog
import com.cooler.system.entities.CoolerBean
import com.cooler.system.network.Client
import com.cooler.system.util.SpaceItemDecoration
import com.cooler.system.util.Util
import com.gyf.immersionbar.ImmersionBar
import java.util.*
import kotlin.concurrent.timerTask

/**
 * http 方式请求
 */
class Main2Activity : AppCompatActivity() {

    private lateinit var bind: ActivityMainBinding
    var mActiveDialog: Dialog? = null
    var mAdapter: DeviceInfoAdapter2? = null
    var timer:Timer?=null
    var pwd=StringBuffer()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        ImmersionBar.with(this).fullScreen(true).init()
        val statusHeight = ImmersionBar.getStatusBarHeight(this) + ImmersionBar.getNavigationBarHeight(this)
        mAdapter = DeviceInfoAdapter2(statusHeight)
        log("status bar height = $statusHeight")
        bind.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@Main2Activity, RecyclerView.VERTICAL, false)
            addItemDecoration(
                SpaceItemDecoration(
                    applicationContext, RecyclerView.VERTICAL, toPx(10f)
                )
            )
            adapter = mAdapter
        }
        mAdapter?.setList(listOf(CoolerBean(),CoolerBean(),CoolerBean()))
        if (!UserManager.isActived(this)) {
            showActiveDialog()
        } else {
            initServer()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when(keyCode){
            KeyEvent.KEYCODE_ENTER,KeyEvent.KEYCODE_DPAD_CENTER,KeyEvent.KEYCODE_NUMPAD_ENTER ->{
                if(pwd.toString() == "9527"){
                    ConfigDialog.show(this){}
                }else{
                    pwd.setLength(0)
                }
            }
            KeyEvent.KEYCODE_9 ->{
                pwd.append(9)
            }
            KeyEvent.KEYCODE_7 ->{
                pwd.append(7)
            }
            KeyEvent.KEYCODE_5 ->{
                pwd.append(5)
            }
            KeyEvent.KEYCODE_2 ->{
                pwd.append(2)
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun initServer() {
//        mAdapter?.setList(ConvertBean.mCacheInfo)
        Client.setHost("http:")
        timer= Timer()
        timer?.schedule(timerTask {
            Client.instance.requestInfo("['JT1001','JT1002','JT1003']").observe(this@Main2Activity) {
                if (it?.isSuccess() == true) {
                    val list = it.data
                    mAdapter?.setList(list)
                }else{
                    toast(it?.message?:"参数错误")
                }
            }
        }, 0L, 3000L)
    }

    override fun onDestroy() {
        timer?.cancel()
        super.onDestroy()
    }


    /**
     * display dialog
     */
    private fun showActiveDialog() {
        if (mActiveDialog == null) {
            mActiveDialog = ActiveDialogUtil.show(this) { activeCode ->
                val b = UserManager.isValidCode(Util.getUniqueID(this), activeCode)
                if (b) {
                    UserManager.saveActiveCode(activeCode)
                    mActiveDialog?.dismiss()
                    initServer()
                    toast("激活成功")
//                    ModbusTools.getInstance().start(6000,100)
                } else {
                    toast("激活失败")
                }
            }
        } else {
            if (mActiveDialog?.isShowing == false) mActiveDialog?.show()
        }
    }
}