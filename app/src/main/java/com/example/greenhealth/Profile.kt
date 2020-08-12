package com.example.greenhealth

class Profile( val name: String, val age: Int, val  weight: Double, val height: Double,val userID:String,val maleFemale:String,val checklbs:String,val checkInch:String) {
    constructor():this("",0,0.0,0.0,"","","","")

}