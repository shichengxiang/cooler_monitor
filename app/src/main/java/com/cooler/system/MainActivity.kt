package com.cooler.system

import android.net.InetAddresses
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.cooler.system.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.zgkxzx.modbus4And.*
import com.zgkxzx.modbus4And.ip.IpParameters
import com.zgkxzx.modbus4And.ip.tcp.TcpSlave
import com.zgkxzx.modbus4And.requset.ModbusReq
import java.net.InetAddress
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var mObservers = mutableListOf<ShortArray>(shortArrayOf(), shortArrayOf(), shortArrayOf())
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
            log(InetAddress.getLocalHost().toString())
            val tcpSlave = TcpSlave(5020, false)
            val processImage = BasicProcessImage(100).apply {
                setHoldingRegister(40001, mObservers[0])
                setHoldingRegister(40002, mObservers[1])
                setHoldingRegister(40003, mObservers[2])
                addListener(object : ProcessImageListener {
                    override fun coilWrite(p0: Int, p1: Boolean, p2: Boolean) {
                        log("coil  $p0")
                    }

                    override fun holdingRegisterWrite(p0: Int, p1: Short, p2: Short) {
                        log("holdingRegisterWrite  $p0   $p1  $p2")
                    }
                })
            }
            tcpSlave.addProcessImage(processImage)
            tcpSlave.start()
        }
        thread {
            while (true) {
                Thread.sleep(2000)
                if (mObservers.get(0).isNotEmpty()) log(
                    "observers =   ${mObservers.get(0).get(0)}  ${mObservers.get(1).get(0)}  ${
                        mObservers.get(2)
                            .get(0)
                    }"
                )
                else log("1")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ModbusReq.getInstance().destory()
    }
}