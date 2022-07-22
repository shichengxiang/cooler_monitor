package com.cooler.system

import android.content.Context
import android.util.Log
import com.intelligt.modbus.jlibmodbus.data.DataHolder
import kotlin.Throws
import com.intelligt.modbus.jlibmodbus.exception.IllegalDataValueException
import com.cooler.system.MyDataHolder
import com.intelligt.modbus.jlibmodbus.Modbus
import com.intelligt.modbus.jlibmodbus.data.ModbusHoldingRegisters
import com.intelligt.modbus.jlibmodbus.exception.IllegalDataAddressException

class MyDataHolder internal constructor(private val context: Context) : DataHolder() {
    //06
    @Throws(IllegalDataAddressException::class, IllegalDataValueException::class)
    override fun writeHoldingRegister(offset: Int, value: Int) {
        log("writeHoldingRegister:写单个寄存器  offset = $offset  value = $value")

        //转2进制
        log("writeHoldingRegister: value_2  = " + Integer.toBinaryString(value))
        //转16进制
        log("writeHoldingRegister: value_16  = " + Integer.toHexString(value))
        super.writeHoldingRegister(offset, value)
    }

    //10
    @Throws(IllegalDataAddressException::class, IllegalDataValueException::class)
    override fun writeHoldingRegisterRange(offset: Int, range: IntArray) {
        log("writeHoldingRegisterRange:写多个寄存器 offset = $offset")
        super.writeHoldingRegisterRange(offset, range)
    }

    //03 读寄存器
    @Throws(IllegalDataAddressException::class)
    override fun readHoldingRegisterRange(offset: Int, quantity: Int): IntArray {
        log("readHoldingRegisterRange: 读取信息 offset = $offset")
        return super.readHoldingRegisterRange(offset, quantity)
    }

    init {
        //很关键
        holdingRegisters = ModbusHoldingRegisters(Modbus.MAX_START_ADDRESS)
    }
}