package com.cooler.system

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cooler.system.data.UserManager
import com.cooler.system.databinding.ActivityMainBinding
import com.cooler.system.dialog.ActiveDialogUtil
import com.cooler.system.modbus.ModbusTools
import com.cooler.system.modbus.TcpListener
import com.cooler.system.util.*
import com.gyf.immersionbar.ImmersionBar
import com.intelligt.modbus.jlibmodbus.Modbus
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException
import com.intelligt.modbus.jlibmodbus.slave.ModbusSlaveTCP
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters
import com.intelligt.modbus.jlibmodbus.utils.FrameEvent
import com.intelligt.modbus.jlibmodbus.utils.FrameEventListener
import com.zgkxzx.modbus4And.serial.ascii.AsciiMaster
import java.net.InetAddress
import java.net.UnknownHostException
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), TcpListener {

    private lateinit var bind: ActivityMainBinding
    var mActiveDialog: Dialog? = null
    var mAdapter: DeviceInfoAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        ImmersionBar.with(this).init()
        val res = CryptoUtil.generateActive(Util.getUniqueID(this)?:"")
        log("oncrate()")
        if (!UserManager.isActived(this)) {
            showActiveDialog()
        } else {
            initServer()
        }
        mAdapter = DeviceInfoAdapter()
        bind.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.HORIZONTAL, false)
            addItemDecoration(
                SpaceItemDecoration(
                    applicationContext,
                    RecyclerView.HORIZONTAL,
                    toPx(10f)
                )
            )
            adapter = mAdapter
        }
        val t= ConvertUtil.txt2Str("你好")
        log("unicode $t")
    }

    private fun initServer() {
        ModbusTools.getInstance().addListener(this).start(6000, 100)
        mAdapter?.setList(ConvertBean.mCacheInfo)
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
                initServer()
                mActiveDialog?.dismiss()
                val b = UserManager.isValidCode(Util.getUniqueID(this), activeCode)
                if (b) {
                    mActiveDialog?.dismiss()
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
        val data = ConvertBean.handleOffsetData(offset, arr)
        runOnUiThread {
            mAdapter?.setList(data)
        }
    }

    override fun onDataReceived(offset: Int, value: Int) {
        log("onDataReceived===============================  $value"  )
    }
}