package com.example.greenhealth

class ExerciseDiary(val  goalWorkOut: Double,val burntCal: Double,val exercise1Info: String,val exercise2Info: String,val exercise1Cal: Double,val exercise2Cal: Double) {
    constructor():this(0.0,0.0,"","",0.0,0.0)
}