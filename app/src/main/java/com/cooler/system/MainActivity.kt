package com.cooler.system

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.cooler.system.databinding.ActivityMainBinding
import com.digitalpetri.modbus.requests.ReadHoldingRegistersRequest
import com.digitalpetri.modbus.responses.ReadHoldingRegistersResponse
import com.digitalpetri.modbus.slave.ModbusTcpSlave
import com.digitalpetri.modbus.slave.ModbusTcpSlaveConfig
import com.digitalpetri.modbus.slave.ServiceRequestHandler
import com.intelligt.modbus.jlibmodbus.Modbus
import com.intelligt.modbus.jlibmodbus.data.SimpleSlaveId
import com.intelligt.modbus.jlibmodbus.data.SlaveId
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException
import com.intelligt.modbus.jlibmodbus.slave.ModbusSlaveTCP
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters
import com.intelligt.modbus.jlibmodbus.utils.FrameEvent
import com.intelligt.modbus.jlibmodbus.utils.FrameEventListener
import io.netty.buffer.PooledByteBufAllocator
import io.netty.util.ReferenceCountUtil
import java.net.InetAddress
import java.net.UnknownHostException
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
//    var modbusSlave :ModbusTcpSlave?=null
    var tcpParameters:TcpParameters?=null
    var dataHolder:MyDataHolder?=null
    var modbusSlave:ModbusSlaveTCP?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            initServer()
        }
    }

    private fun initServer() {
        //创建从站 slave
        thread {
            try {
                log("run: startSalve")
                //填写自己的端口号
                tcpParameters = getParameters(6000);
                dataHolder = MyDataHolder(this)
//                dataHolder?.slaveId = SimpleSlaveId(1).apply { set(byteArrayOf(1)) }
//                log("slave id = ${dataHolder?.slaveId?.get()?.first()}")
                // 创建一个从机
                modbusSlave = ModbusSlaveTCP(tcpParameters);
                // 设置控制台输出主机和从机命令交互日志
                Modbus.setLogLevel(Modbus.LogLevel.LEVEL_DEBUG);
                modbusSlave?.setDataHolder(dataHolder);
                modbusSlave?.setReadTimeout(2000);
                //填写自己的 Slave ID
                modbusSlave?.setServerAddress(100)
                modbusSlave?.listen()
                modbusSlave?.addListener(object :FrameEventListener{
                    override fun frameSentEvent(p0: FrameEvent?) {
                        log("onFrameSendEvent")

                    }

                    override fun frameReceivedEvent(p0: FrameEvent?) {
                        log("OnFrameReceivedEvent")
                    }
                })
                log("run:  开启完成")
            } catch (e: ModbusIOException) {
                e.printStackTrace();
                log("onError  ${e.message}")
            }
        }
    }
    /**
     * 配置 参数
     *
     * @param port
     * @return
     */
    private fun getParameters(port:Int):TcpParameters {

        var parameters = TcpParameters();
        try {
            //此处设置Android 端 IP地址
            var address = InetAddress.getByName("192.168.15.37");
            parameters.setHost(address);
            // 设置从机TCP的是否长连接
            parameters.setKeepAlive(true);
            // 设置从机TCP的端口
            parameters.setPort(port);
            log("getParameters: address = ${address}  port = $port");
        } catch (e: UnknownHostException) {
            e.printStackTrace();
        }
        return parameters;
    }
    public fun release() {
        try {
            if (null != modbusSlave) {
                modbusSlave?.shutdown();
                modbusSlave = null;
            }
            if (null != tcpParameters) {
                tcpParameters = null;
            }

        } catch (e:ModbusIOException) {
            e.printStackTrace();
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        release()
    }
}