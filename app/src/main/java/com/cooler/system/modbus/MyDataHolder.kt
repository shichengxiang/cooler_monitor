package com.cooler.system.modbus

import android.icu.number.IntegerWidth
import com.cooler.system.log
import com.intelligt.modbus.jlibmodbus.Modbus
import com.intelligt.modbus.jlibmodbus.data.DataHolder
import com.intelligt.modbus.jlibmodbus.data.ModbusHoldingRegisters
import com.intelligt.modbus.jlibmodbus.exception.IllegalDataAddressException
import com.intelligt.modbus.jlibmodbus.exception.IllegalDataValueException

class MyDataHolder internal constructor(var tcpListener: TcpListener?) : DataHolder() {
//    private var tcpListener: TcpListener?=null
//    fun setListener(listener: TcpListener){
//        tcpListener = listener
//    }
    //06
    @Throws(IllegalDataAddressException::class, IllegalDataValueException::class)
    override fun writeHoldingRegister(offset: Int, value: Int) {
        log("writeHoldingRegister:写单个寄存器  offset = $offset  value = $value")
        tcpListener?.onDataReceived(offset,value)
        //转2进制

        log("writeHoldingRegister: value_2  = " + Integer.parseInt(Integer.toBinaryString(value),2))
        //转16进制
        log("writeHoldingRegister: value_16  = " + Integer.toHexString(value))
        super.writeHoldingRegister(offset, value)
    }
    fun convertBinaryToDecimal(num: Long): Int {
        var num = num
        var decimalNumber = 0
        var i = 0
        var remainder: Long

        while (num.toInt() != 0) {
            remainder = num % 10
            num /= 10
            decimalNumber += (remainder * Math.pow(2.0, i.toDouble())).toInt()
            ++i
        }
        return decimalNumber
    }

    //10
    @Throws(IllegalDataAddressException::class, IllegalDataValueException::class)
    override fun writeHoldingRegisterRange(offset: Int, range: IntArray) {
        var buffer= StringBuilder()
        range.forEach { buffer.append(it).append(" ") }
        log(buffer.toString())
        log("writeHoldingRegisterRange:写多个寄存器 offset = $offset  value = $buffer")
        tcpListener?.onDataReceived(offset,range)
        super.writeHoldingRegisterRange(offset, range)
    }

    override fun getInputRegisters(): ModbusHoldingRegisters {
        return super.getInputRegisters()
    }

    override fun readInputRegisterRange(offset: Int, quantity: Int): IntArray {
        log("readInputRegisterRange")
        return super.readInputRegisterRange(offset, quantity)
    }

    //03 读寄存器
    @Throws(IllegalDataAddressException::class)
    override fun readHoldingRegisterRange(offset: Int, quantity: Int): IntArray {
//        log("readHoldingRegisterRange: 读取信息 offset = $offset  quantity = $quantity")
        return super.readHoldingRegisterRange(offset, quantity)
    }

    init {
        //很关键
        holdingRegisters = ModbusHoldingRegisters(Modbus.MAX_START_ADDRESS)
    }
}