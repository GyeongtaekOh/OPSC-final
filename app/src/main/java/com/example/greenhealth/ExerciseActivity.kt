package com.example.greenhealth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ExerciseActivity : AppCompatActivity() {

    lateinit var ref : DatabaseReference
    val fb = FirebaseAuth.getInstance();
    val userID = FirebaseAuth.getInstance().currentUser?.uid.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        ref = FirebaseDatabase.getInstance().getReference("users")
        val foodDiaryBut = findViewById<ImageButton>(R.id.imageButtonFood)
        val homeBut = findViewById<ImageButton>(R.id.imageButtonHome)
        val habitBut = findViewById<ImageButton>(R.id.imageButtonHabit)
        val profileBut = findViewById<ImageButton>(R.id.imageButtonProf)
        val saveExerciseGoalBut = findViewById<ImageButton>(R.id.imageButtonSaveWorkoutGoal)
        val saveExerciseBurntCal = findViewById<Button>(R.id.buttonExerciseSave)
        val goalWorkoutText = findViewById<TextView>(R.id.editTextGoalWorkout)
        val calBurntText = findViewById<TextView>(R.id.editTextCurrentCalBurnt)
        val exerciseInfo1Text = findViewById<TextView>(R.id.editTextExerciseInfo1)
        val exerciseInfo2Text = findViewById<TextView>(R.id.editTextExerciseInfo2)
        val exerciseBurnt1Text = findViewById<TextView>(R.id.editTextExerciseCalBurn1)
        val exerciseBurnt2Text = findViewById<TextView>(R.id.editTextExerciseCalBurn2)
        val monthExerciseBut = findViewById<Button>(R.id.buttonMonthExercise)

        homeBut.setOnClickListener{
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
            finish()
        }
        foodDiaryBut.setOnClickListener{
            val intent = Intent(this, FoodActivity::class.java)
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
        monthExerciseBut.setOnClickListener{
            val intent = Intent(this, MonthExerciseActivity::class.java)
            startActivity(intent)
        }
        saveExerciseGoalBut.setOnClickListener{
            SaveExerciseGoal()
        }
        saveExerciseBurntCal.setOnClickListener{
            SaveTotalBurntCal()
        }
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
                                goalWorkoutText.setText(goals!!.goalWorkOut.toString())
                                calBurntText.setText(goals!!.burntCal.toString())
                                exerciseInfo1Text.setText(goals!!.exercise1Info)
                                exerciseInfo2Text.setText(goals!!.exercise2Info)
                                exerciseBurnt1Text.setText(goals!!.exercise1Cal.toString())
                                exerciseBurnt2Text.setText(goals!!.exercise2Cal.toString())
                            }
                        }
                    }
                }
            }
        })
    }
    private fun SaveExerciseGoal(){
        val goalWorkoutText = findViewById<TextView>(R.id.editTextGoalWorkout)
        val calBurntText = findViewById<TextView>(R.id.editTextCurrentCalBurnt)
        val exerciseInfo1Text = findViewById<TextView>(R.id.editTextExerciseInfo1)
        val exerciseInfo2Text = findViewById<TextView>(R.id.editTextExerciseInfo2)
        val exerciseBurnt1Text = findViewById<TextView>(R.id.editTextExerciseCalBurn1)
        val exerciseBurnt2Text = findViewById<TextView>(R.id.editTextExerciseCalBurn2)
        val excerciseDiary = "ExcerciseDiary"

        if (goalWorkoutText.text.isEmpty())
        {
            goalWorkoutText.setText("0")
        }
        if (calBurntText.text.isEmpty())
        {
            calBurntText.setText("0")
        }
        if (exerciseInfo1Text.text.isEmpty())
        {
            exerciseInfo1Text.setText(" ")
        }
        if (exerciseInfo2Text.text.isEmpty())
        {
            exerciseInfo2Text.setText(" ")
        }
        if (exerciseBurnt1Text.text.isEmpty())
        {
            exerciseBurnt1Text.setText("0")
        }
        if (exerciseBurnt2Text.text.isEmpty())
        {
            exerciseBurnt2Text.setText("0")
        }

        val goalWorkout = goalWorkoutText.text.toString().trim()
        val calBurnt = calBurntText.text.toString().trim()
        val exInfo1 = exerciseInfo1Text.text.toString().trim()
        val exInfo2 = exerciseInfo2Text.text.toString().trim()
        val exBurnt1 = exerciseBurnt1Text.text.toString().trim()
        val exBurnt2 = exerciseBurnt2Text.text.toString().trim()

        val edGoal = ExerciseDiary(goalWorkout.toDouble(),calBurnt.toDouble(),exInfo1,exInfo2,exBurnt1.toDouble(),exBurnt2.toDouble())
        ref.child(userID).child(excerciseDiary).setValue(edGoal)
    }
    private fun SaveTotalBurntCal(){
        val goalWorkoutText = findViewById<TextView>(R.id.editTextGoalWorkout)
        val calBurntText = findViewById<TextView>(R.id.editTextCurrentCalBurnt)
        val exerciseInfo1Text = findViewById<TextView>(R.id.editTextExerciseInfo1)
        val exerciseInfo2Text = findViewById<TextView>(R.id.editTextExerciseInfo2)
        val exerciseBurnt1Text = findViewById<TextView>(R.id.editTextExerciseCalBurn1)
        val exerciseBurnt2Text = findViewById<TextView>(R.id.editTextExerciseCalBurn2)
        val excerciseDiary = "ExcerciseDiary"

        if (goalWorkoutText.text.isEmpty())
        {
            goalWorkoutText.setText("0")
        }
        if (calBurntText.text.isEmpty())
        {
            calBurntText.setText("0")
        }
        if (exerciseInfo1Text.text.isEmpty())
        {
            exerciseInfo1Text.setText(" ")
        }
        if (exerciseInfo2Text.text.isEmpty())
        {
            exerciseInfo2Text.setText(" ")
        }
        if (exerciseBurnt1Text.text.isEmpty())
        {
            exerciseBurnt1Text.setText("0")
        }
        if (exerciseBurnt2Text.text.isEmpty())
        {
            exerciseBurnt2Text.setText("0")
        }

        val goalWorkout = goalWorkoutText.text.toString().trim()
        val exInfo1 = exerciseInfo1Text.text.toString().trim()
        val exInfo2 = exerciseInfo2Text.text.toString().trim()
        val exBurnt1 = exerciseBurnt1Text.text.toString().trim()
        val exBurnt2 = exerciseBurnt2Text.text.toString().trim()

        val total = exBurnt1.toDouble() + exBurnt2.toDouble()

        calBurntText.setText(total.toString().trim())

        val calBurnt = calBurntText.text.toString().trim()

        val edGoal = ExerciseDiary(goalWorkout.toDouble(),calBurnt.toDouble(),exInfo1,exInfo2,exBurnt1.toDouble(),exBurnt2.toDouble())
        ref.child(userID).child(excerciseDiary).setValue(edGoal)
    }
}
