package com.cooler.system.modbus

import android.os.Handler
import android.os.Looper
import com.cooler.system.log
import com.intelligt.modbus.jlibmodbus.Modbus
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException
import com.intelligt.modbus.jlibmodbus.slave.ModbusSlaveTCP
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters
import com.intelligt.modbus.jlibmodbus.utils.FrameEvent
import com.intelligt.modbus.jlibmodbus.utils.FrameEventListener
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class ModbusTools : TcpListener {

    //data listen
    private var dataHolder: MyDataHolder? = null
    private var modbusSlave: ModbusSlaveTCP? = null
    private var mTcpListener:TcpListener?=null
    private constructor(){
        if(uiHandler == null) uiHandler = Handler(Looper.getMainLooper())
    }
    companion object{
        private var tools:ModbusTools?=null
        @JvmStatic
        fun getInstance():ModbusTools{
            if(tools == null){
                synchronized(Any::class.java){
                    if(tools == null) tools = ModbusTools()
                }
            }
            return tools!!
        }
    }

    private var mExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    private var uiHandler:Handler?=null

    /**
     * start service
     * @param port Int
     * @param slaveId Int
     */
    fun start(port: Int,slaveId:Int){
        mExecutor.submit {
            try {
                log("modbus start ")
                var params= getTcpParams(port)
                dataHolder = MyDataHolder(this)
                // 创建一个从机
                modbusSlave = ModbusSlaveTCP(params)
                // 设置控制台输出主机和从机命令交互日志
                Modbus.setLogLevel(Modbus.LogLevel.LEVEL_DEBUG)
                modbusSlave?.setDataHolder(dataHolder)
                modbusSlave?.readTimeout = 2000
                //填写自己的 Slave ID
                modbusSlave?.serverAddress = slaveId
                modbusSlave?.listen()
                modbusSlave?.addListener(object : FrameEventListener {
                    override fun frameSentEvent(p0: FrameEvent?) {
//                        log("onFrameSendEvent")

                    }

                    override fun frameReceivedEvent(p0: FrameEvent?) {
//                        log("OnFrameReceivedEvent")
                    }
                })
            }catch (e: ModbusIOException){
                e.printStackTrace()
            }
        }
    }
    fun addListener(listener:TcpListener):ModbusTools{
        mTcpListener= listener
        return this
    }

    /**
     * release service
     */
    fun release(){
        try {
            if (null != modbusSlave) {
                modbusSlave?.shutdown()
                modbusSlave = null
            }
            dataHolder=null
        } catch (e: ModbusIOException) {
            e.printStackTrace();
        }
    }
    private fun getTcpParams(port: Int):TcpParameters{
        var parameters = TcpParameters()
        try {
            //此处设置Android 端 IP地址
            var address = InetAddress.getByName("127.0.0.1")
            parameters.host = address
            // 设置从机TCP的是否长连接
            parameters.isKeepAlive = true
            // 设置从机TCP的端口
            parameters.port = port
        } catch (e: UnknownHostException) {
            e.printStackTrace();
        }
        return parameters;
    }

    override fun onDataReceived(offset: Int, arr: IntArray) {
        mTcpListener?.onDataReceived(offset,arr)
    }

    override fun onDataReceived(offset: Int, value: Int) {
        mTcpListener?.onDataReceived(offset,value)
    }

}