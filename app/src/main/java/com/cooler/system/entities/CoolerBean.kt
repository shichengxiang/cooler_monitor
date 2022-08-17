package com.cooler.system.entities

/**
 * 描述：CoorBean
 * 创建者: shichengxiang
 * 创建时间：2022/8/15
 */
data class CoolerBean(
    var asHaveDead: String? = null, //是否有逝者
    var deadAge: String? = null, //年龄
    var deadGender: String? = null, //性别
    var deadName: String? = null, //姓名
    var disableStateStr: String? = null, //状态
    var equipmentCode: String? = null, //编号
    var realityInTime: String? = null, //入藏时间
    var temperature: String? = null, //温度
    var remark:String?=null  //备注
)