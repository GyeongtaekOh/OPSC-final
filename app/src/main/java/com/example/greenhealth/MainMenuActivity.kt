package com.example.greenhealth

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main_menu.*
import kotlinx.android.synthetic.main.activity_profile.*

class MainMenuActivity : AppCompatActivity() {
    lateinit var ref : DatabaseReference
    val fb = FirebaseAuth.getInstance();
    val userID = FirebaseAuth.getInstance().currentUser?.uid.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        val foodDiaryBut = findViewById<ImageButton>(R.id.imageButtonFood)
        val exerciseDiaryBut = findViewById<ImageButton>(R.id.imageButtonEx)
        val habitBut = findViewById<ImageButton>(R.id.imageButtonHabit)
        val profileBut = findViewById<ImageButton>(R.id.imageButtonProf)
        val foodGoal = findViewById<TextView>(R.id.textView12)
        val exerciseGoal = findViewById<TextView>(R.id.textView13)
        val weightGoal = findViewById<TextView>(R.id.textView15)
        val habitWText = findViewById<TextView>(R.id.editTextHabit)
        val progressBar1 = findViewById<ProgressBar>(R.id.progressBar)
        val progressBar2 = findViewById<ProgressBar>(R.id.progressBar2)

        foodDiaryBut.setOnClickListener{
            val intent = Intent(this, FoodActivity::class.java)
            startActivity(intent)
            finish()
        }
        exerciseDiaryBut.setOnClickListener{
            val intent = Intent(this, ExerciseActivity::class.java)
            startActivity(intent)
            finish()
        }
        habitBut.setOnClickListener{
            val intent = Intent(this, HabitActivity::class.java)
            startActivity(intent)
            finish()
        }
        profileBut.setOnClickListener{
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
            finish()
        }


        ref = FirebaseDatabase.getInstance().getReference("users")
        //val goalSaveBut = findViewById<Button>(R.id.buttonChangeSave)

        //goalSaveBut.setOnClickListener{
            //SaveGoal()
        //}
        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0!!.exists()){

                    for (i in p0.children){

                        val user = i.child("Profile").getValue(Profile::class.java)
                        if (user!!.userID == userID) {
                            weightGoal.setText(user!!.weight.toString())
                        }

                        if (i.child("FoodDiary").exists()){

                            val goals = i.child("FoodDiary").getValue(FoodDiary::class.java)

                            if (user!!.userID == userID){
                                //editTextGoalKg.setText(goals!!.goalKg.toString())
                                foodGoal.setText(goals!!.currentCal.toString())
                                progressBar1.setMax(goals.goalCal.toInt())
                                progressBar1.setProgress(goals.currentCal.toInt())
                            }
                        }
                        if (i.child("ExcerciseDiary").exists()){

                            val goals = i.child("ExcerciseDiary").getValue(ExerciseDiary::class.java)

                            if (user!!.userID == userID){
                                //editTextGoalKg.setText(goals!!.goalKg.toString())
                                exerciseGoal.setText(goals!!.burntCal.toString())
                                progressBar2.setMax(goals.goalWorkOut.toInt())
                                progressBar2.setProgress(goals.burntCal.toInt())
                            }
                        }
                        if (i.child("HabitTracker").exists()){

                            val goals = i.child("HabitTracker").getValue(Habit::class.java)

                            if (user!!.userID == userID){
                                habitWText.setText(goals!!.waterHb.toString())
                            }
                        }
                    }
                }
            }
        })
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Exit Green Health")
        builder.setMessage("Do you want exit?")
        builder.setPositiveButton("Yes", {dialog, which -> super.onBackPressed()})
        builder.setNegativeButton("No",{dialog, which ->  })
        builder.show()

    }
    /*private fun SaveGoal(){
        val goalKgText = findViewById<TextView>(R.id.editTextGoalKg)
        val goalCalText = findViewById<TextView>(R.id.editTextGoalCal)
        val foodDiary = "FoodDiary"

        val goalKg = goalKgText.text.toString().trim()
        val goalCal = goalCalText.text.toString().trim()

        if (goalKg.isEmpty())
        {
            goalKgText.error = "Please enter a GoalKg"
            return
        }
        if (goalCal.isEmpty())
        {
            goalCalText.error = "Please enter a GoalCal"
            return
        }
        val fdGoal = FoodDiary(goalKg.toDouble(),goalCal.toDouble())
        ref.child(userID).child(foodDiary).setValue(fdGoal)
    }*/
}
