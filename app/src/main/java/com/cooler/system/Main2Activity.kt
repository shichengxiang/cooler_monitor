package com.cooler.system

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.KeyEvent
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cooler.system.data.UserManager
import com.cooler.system.databinding.ActivityMainBinding
import com.cooler.system.dialog.ActiveDialogUtil
import com.cooler.system.dialog.ConfigDialog
import com.cooler.system.entities.CoolerBean
import com.cooler.system.network.BaseResponse
import com.cooler.system.network.Client
import com.cooler.system.util.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.gyf.immersionbar.ImmersionBar
import com.tencent.bugly.crashreport.CrashReport
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.timerTask

/**
 * http 方式请求
 */
class Main2Activity : AppCompatActivity() {

    private lateinit var bind: ActivityMainBinding
    var mActiveDialog: Dialog? = null
    var mAdapter: DeviceInfoAdapter2? = null
    private var mGson: Gson? = null
    private var mPerTime = 5
    private var host =
        "https://console-mock.apipost.cn/app/mock/project/7c32e56c-6972-4c15-c276-c6339f27bc7f/"
    private var mDeviceCodes = arrayOf("")
    var timer: Timer? = null
    var pwd = StringBuffer()
//    var cacheBody: RequestBody? = null
    private var mMessageHandler: Handler? = null
//    private var mMessageHandler = object : Handler(Looper.getMainLooper()) {
//        override fun handleMessage(msg: Message) {
//            if (msg.what == 1) {
//                requestData()
//            }
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        bind = ActivityMainBinding.inflate(layoutInflater)
//        CryptoUtil.generateActive("ODFJMZC2OG")
        log(" MainActivity onCreate()")
//        App.postDelay({
//            var i = 0
//            val j = 12 / i
//        }, 2000)
        setContentView(bind.root)
        onStartUI()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        log(" MainActivity onNewIntent()")
        onStartUI()
    }
    private fun onStartUI(){
        ImmersionBar.with(this).fullScreen(true).init()
        mMessageHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                if (msg.what == 1) {
                    requestInfo2()
//                    requestData()
                }
            }
        }
        val statusHeight =
            ImmersionBar.getStatusBarHeight(this) + ImmersionBar.getNavigationBarHeight(this)
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
//        mAdapter?.setList(listOf(CoolerBean(), CoolerBean(), CoolerBean())) //初始数据默认3个
        if (!UserManager.isActived(this)) {
            showActiveDialog()
        } else {
            checkConfig()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_ENTER, KeyEvent.KEYCODE_DPAD_CENTER -> {
                if (pwd.toString() == "9527") {
                    ConfigDialog.show(this) { initServer() }
                } else {
                    pwd.setLength(0)
                }
            }

            KeyEvent.KEYCODE_9 -> {
                pwd.append(9)
            }

            KeyEvent.KEYCODE_7 -> {
                pwd.append(7)
            }

            KeyEvent.KEYCODE_5 -> {
                pwd.append(5)
            }

            KeyEvent.KEYCODE_2 -> {
                pwd.append(2)
            }

            KeyEvent.KEYCODE_BACK -> {
                ConfigDialog.show(this) { initServer() }
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun checkConfig() {
        val host = Util.getHost()
        val perTime = Util.getPerTime()
        val codes = Util.getDeviceCodes()
        mDeviceCodes = Util.getDeviceCodes() ?: arrayOf()
        if (host.first.isNullOrEmpty() || codes.isNullOrEmpty() || mDeviceCodes.size < 3) {
            ConfigDialog.show(this) { initServer() }
        } else {
            initServer()
        }
    }

    private fun initServer() {
//        mAdapter?.setList(ConvertBean.mCacheInfo)
        Client.instance.clearCache()
        mDeviceCodes = Util.getDeviceCodes() ?: arrayOf()
        mAdapter?.initDevideCode(mDeviceCodes)
        mPerTime = Util.getPerTime()
//        val params = Gson().toJson(mDeviceCodes)
        Client.instance.setNewHost(Util.getHostStr())
        mMessageHandler?.removeMessages(1)
        mMessageHandler?.sendEmptyMessageDelayed(1, 1000)
    }

    private var result: LiveData<BaseResponse<List<CoolerBean>>>? = null

//    /**
//     * 请求数据
//     */
//    private fun requestData() {
//        if (cacheBody == null) {
//            val map = hashMapOf<String, Any>().apply {
//                put("codes", mDeviceCodes)
//            }
//            val encrypStr = EncryptionUtil.encrypToBase64Str(Gson().toJson(map))
//            val obj = hashMapOf<String, String>().apply {
//                put("content", encrypStr)
//            }
//            cacheBody = Gson().toJson(obj)
//                .toRequestBody("application/json;charset=UTF-8".toMediaTypeOrNull())
//        }
//        if (result == null) {
//            result = Client.instance.requestInfo(cacheBody!!)
//        }
//        result?.removeObserver(onMessage)
//        result?.observe(this, onMessage)
//    }

//    private var onMessage = Observer<BaseResponse<List<CoolerBean>>> {
//        if (it?.isSuccess() == true) {
//            val listStr = it.data
//            mAdapter?.refreshData(listStr)
//        } else {
//            if (it.code != 200) {
//                toast("${it?.code} 网络错误")
//            } else {
//                toast(it?.message ?: "参数错误")
//            }
////                log("resposne == ${it?.message}")
//        }
//        mMessageHandler?.removeMessages(1)
//        mMessageHandler?.sendEmptyMessageDelayed(1, (mPerTime + 2) * 1000L)
//    }

    private fun requestInfo2() {
        Client.instance.postInfo(mDeviceCodes, onCallBack)
    }
    private var onCallBack = object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            if(isDestroyed || isFinishing) return
            runOnUiThread {
                log(e.message?:"")
                toast(e.message ?: "网络错误")
                mMessageHandler?.removeMessages(1)
                mMessageHandler?.sendEmptyMessageDelayed(1, (mPerTime + 2) * 1000L)
            }
        }

        override fun onResponse(call: Call, response: Response) {
            if(isDestroyed || isFinishing) return
            runOnUiThread {
                if (response.code == 200) {
                    val obj = JSONObject(response.body?.string())
                    val code = obj.optInt("code")
                    if (code == 1) {
                        val dataStr = obj.optString("data")
                        if (mGson == null) mGson = Gson()
                        val list = mGson?.fromJson<List<CoolerBean>>(dataStr,
                            object : TypeToken<List<CoolerBean>>() {}.type)
                        mAdapter?.refreshData(list)
                    } else {
                        val msg = obj.optString("message")
                        toast(msg)
                    }
                } else {
                    toast("${response.code} 返回错误")
                }
                mMessageHandler?.removeMessages(1)
                mMessageHandler?.sendEmptyMessageDelayed(1, (mPerTime + 2) * 1000L)
            }
        }
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
                    checkConfig()
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