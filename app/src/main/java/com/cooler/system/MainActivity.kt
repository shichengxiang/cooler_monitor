package com.cooler.system

import android.app.Dialog
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cooler.system.data.UserManager
import com.cooler.system.databinding.ActivityMainBinding
import com.cooler.system.dialog.ActiveDialogUtil
import com.cooler.system.modbus.ModbusTools
import com.cooler.system.modbus.MyDataHolder
import com.cooler.system.modbus.TcpListener
import com.cooler.system.util.*
import com.gyf.immersionbar.ImmersionBar
import com.tencent.mmkv.MMKV
import java.math.BigInteger

class MainActivity : AppCompatActivity(), TcpListener {

    private lateinit var bind: ActivityMainBinding
    var mActiveDialog: Dialog? = null
    var mAdapter: DeviceInfoAdapter2? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        ImmersionBar.with(this).fullScreen(true).init()
        val statusHeight = ImmersionBar.getStatusBarHeight(this)+ImmersionBar.getNavigationBarHeight(this)
        mAdapter = DeviceInfoAdapter2(statusHeight)
        log("status bar height = $statusHeight")
        bind.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
            addItemDecoration(
                SpaceItemDecoration(
                    applicationContext, RecyclerView.VERTICAL, toPx(10f)
                )
            )
            adapter = mAdapter
        }
        if (!UserManager.isActived(this)) {
            showActiveDialog()
        } else {
            initServer()
        }
    }

    private fun initServer() {
        ModbusTools.getInstance().addListener(this).start(9999, 1)
        mAdapter?.setList(ConvertBean.mCacheInfo)
        bind.tvIp.text= "IP : ${Util.getLocalIp(this)}"
//        mAdapter?.data?.get(0)?.code= "JT0001"
//        mAdapter?.notifyItemChanged(0,"0")
    }

    override fun onDestroy() {
        super.onDestroy()
        ModbusTools.getInstance().release()
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

    override fun onDataReceived(offset: Int, arr: IntArray) {
        log("onDataReceived==============================================")
        ConvertBean.handleOffsetData(offset, arr, object : ConvertBean.OnCallBack {
            override fun onReceived(index: Int, tag: Int, v: Any) {
                handleData(index, tag, v)
            }
        })
//        runOnUiThread {
//            mAdapter?.setList(data)
//        }
    }

    override fun onDataReceived(offset: Int, value: Int) {
        log("onDataReceived===============================  $value")
        ConvertBean.handleOffsetData(offset, value, object : ConvertBean.OnCallBack {
            override fun onReceived(index: Int, tag: Int, v: Any) {
                handleData(index, tag, v)
            }
        })
    }

    /**
     * 接收数据并处理
     */
    fun handleData(index: Int, tag: Int, v: Any) {
        runOnUiThread {
            when (tag) {
                0 -> {
                    //编号
                    mAdapter?.data?.get(index)?.code = v as String
                    mAdapter?.notifyItemChanged(index, "0")
                }
                1 -> {
                    //姓名
                    mAdapter?.data?.get(index)?.name = v as String
                    mAdapter?.notifyItemChanged(index, "1")
                }
                2 -> {
                    //温度
                    mAdapter?.data?.get(index)?.temperature = (v as Short).toFloat()
                    mAdapter?.notifyItemChanged(index, "2")
                }
                3 -> {
                    //有无
                    mAdapter?.data?.get(index)?.isHas = v as Int
                    mAdapter?.notifyItemChanged(index, "3")
                }
                4 -> {
                    //状态
                    mAdapter?.data?.get(index)?.state = v as Int
                    mAdapter?.notifyItemChanged(index, "4")
                }
            }
        }
    }
}