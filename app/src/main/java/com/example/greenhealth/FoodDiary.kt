package com.example.greenhealth

class FoodDiary(val  goalCal: Double,val currentCal: Double,val food1Info: String,val food2Info: String,val food3Info: String,val food1Cal: Double,val food2Cal: Double,val food3Cal: Double) {
    constructor():this(0.0,0.0,"","","",0.0,0.0,0.0)
}