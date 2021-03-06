package com.example.greenhealth

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MonthExerciseActivity : AppCompatActivity() {

    lateinit var ref : DatabaseReference
    val fb = FirebaseAuth.getInstance();
    val userID = FirebaseAuth.getInstance().currentUser?.uid.toString()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_month_exercise)

        ref = FirebaseDatabase.getInstance().getReference("users")
        val dailyGoalEx1 = findViewById<TextView>(R.id.editTextDailyGoalEx1)
        val dailyCalBurnt1 = findViewById<TextView>(R.id.editTextDailyCalBurnt1)
        val today = findViewById<TextView>(R.id.textView43)
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formatted = current.format(formatter)
        today.setText(formatted.toString())

        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0!!.exists()){

                    for (i in p0.children){

                        val user = i.child("Profile").getValue(Profile::class.java)
                        if (i.child("ExcerciseDiary").exists()){

                            val goals = i.child("ExcerciseDiary").getValue(ExerciseDiary::class.java)

                            if (user!!.userID == userID){
                                dailyGoalEx1.setText(goals!!.goalWorkOut.toString())
                                dailyCalBurnt1.setText(goals!!.burntCal.toString())
                            }
                        }
                    }
                }
            }
        })
    }
}
