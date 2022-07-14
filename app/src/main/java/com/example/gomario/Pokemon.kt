package com.example.gomario

import android.location.Location

class Pokemon {
    var name:String?=null
    var des:String?=null
    var power:Double?=null
    var image:Int?=null
    var location:Location?=null
    var isCatch:Boolean?=false
    constructor(name:String,des:String,power:Double,image:Int,lat:Double,log:Double){
        this.name=name
        this.des=des
        this.power=power
        this.image=image
        location= Location(name)
        location!!.latitude=lat
        location!!.longitude=log
        this.isCatch=false
    }
}