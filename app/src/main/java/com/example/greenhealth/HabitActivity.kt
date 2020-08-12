package com.example.greenhealth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HabitActivity : AppCompatActivity() {

    lateinit var ref : DatabaseReference
    val fb = FirebaseAuth.getInstance();
    val userID = FirebaseAuth.getInstance().currentUser?.uid.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habit)

        val foodDiaryBut = findViewById<ImageButton>(R.id.imageButtonFood)
        val homeBut = findViewById<ImageButton>(R.id.imageButtonHome)
        val exerciseDiaryBut = findViewById<ImageButton>(R.id.imageButtonEx)
        val profileBut = findViewById<ImageButton>(R.id.imageButtonProf)
        ref = FirebaseDatabase.getInstance().getReference("users")
        val waterUpBut = findViewById<ImageButton>(R.id.imageButtonWaterUp)
        val waterDownBut = findViewById<ImageButton>(R.id.imageButtonWaterDown)
        val meatUpBut = findViewById<ImageButton>(R.id.imageButtonMeatUp)
        val meatDownBut = findViewById<ImageButton>(R.id.imageButtonMeatDown)
        val vegeUpBut = findViewById<ImageButton>(R.id.imageButtonVegeUp)
        val vegeDownBut = findViewById<ImageButton>(R.id.imageButtonVegeDown)
        val waterText = findViewById<TextView>(R.id.textViewWater)
        val meatText = findViewById<TextView>(R.id.textViewMeat)
        val vegeText = findViewById<TextView>(R.id.textViewVege)

        homeBut.setOnClickListener{
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
            finish()
        }
        exerciseDiaryBut.setOnClickListener{
            val intent = Intent(this, ExerciseActivity::class.java)
            startActivity(intent)
            finish()
        }
        foodDiaryBut.setOnClickListener{
            val intent = Intent(this, FoodActivity::class.java)
            startActivity(intent)
            finish()
        }
        profileBut.setOnClickListener{
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
            finish()
        }
        waterUpBut.setOnClickListener{
            PlusWater()
        }
        waterDownBut.setOnClickListener{
            MinusWater()
        }
        meatUpBut.setOnClickListener{
            PlusMeat()
        }
        meatDownBut.setOnClickListener{
            MinusMeat()
        }
        vegeUpBut.setOnClickListener{
            PlusVege()
        }
        vegeDownBut.setOnClickListener{
            MinusVege()
        }
        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0!!.exists()){

                    for (i in p0.children){

                        val user = i.child("Profile").getValue(Profile::class.java)

                        if (i.child("HabitTracker").exists()){

                            val goals = i.child("HabitTracker").getValue(Habit::class.java)

                            if (user!!.userID == userID){
                                waterText.setText(goals!!.waterHb.toString())
                                meatText.setText(goals!!.meatHb.toString())
                                vegeText.setText(goals!!.vegeHb.toString())
                            }
                        }
                    }
                }
            }
        })
    }
    private fun PlusWater()
    {
        val waterText = findViewById<TextView>(R.id.textViewWater)
        val meatText = findViewById<TextView>(R.id.textViewMeat)
        val vegeText = findViewById<TextView>(R.id.textViewVege)
        val habitTracker = "HabitTracker"

        if (waterText.text.isEmpty())
        {
            waterText.text = "0"
        }
        if (meatText.text.isEmpty())
        {
            meatText.text = "0"
        }
        if (vegeText.text.isEmpty())
        {
            vegeText.text = "0"
        }

        val meat = meatText.text.toString().trim()
        val vege = vegeText.text.toString().trim()

        val waterP = waterText.text.toString().toInt() + 1

        waterText.setText(waterP.toString())

        val water = waterText.text.toString().trim()

        val habitT = Habit(water.toInt(),meat.toInt(),vege.toInt())
        ref.child(userID).child(habitTracker).setValue(habitT)
    }
    private fun MinusWater()
    {
        val waterText = findViewById<TextView>(R.id.textViewWater)
        val meatText = findViewById<TextView>(R.id.textViewMeat)
        val vegeText = findViewById<TextView>(R.id.textViewVege)
        val habitTracker = "HabitTracker"

        if (waterText.text.isEmpty())
        {
            waterText.text = "0"
        }
        if (meatText.text.isEmpty())
        {
            meatText.text = "0"
        }
        if (vegeText.text.isEmpty())
        {
            vegeText.text = "0"
        }

        val meat = meatText.text.toString().trim()
        val vege = vegeText.text.toString().trim()

        if(waterText.text == "0")
        {
            return
        }
        val waterD = waterText.text.toString().toInt() - 1

        waterText.setText(waterD.toString())

        val water = waterText.text.toString().trim()

        val habitT = Habit(water.toInt(),meat.toInt(),vege.toInt())
        ref.child(userID).child(habitTracker).setValue(habitT)
    }
    private fun PlusMeat()
    {
        val waterText = findViewById<TextView>(R.id.textViewWater)
        val meatText = findViewById<TextView>(R.id.textViewMeat)
        val vegeText = findViewById<TextView>(R.id.textViewVege)
        val habitTracker = "HabitTracker"

        if (waterText.text.isEmpty())
        {
            waterText.text = "0"
        }
        if (meatText.text.isEmpty())
        {
            meatText.text = "0"
        }
        if (vegeText.text.isEmpty())
        {
            vegeText.text = "0"
        }

        val water = waterText.text.toString().trim()
        val vege = vegeText.text.toString().trim()

        val meatP = meatText.text.toString().toInt() + 1

        meatText.setText(meatP.toString())

        val meat = meatText.text.toString().trim()

        val habitT = Habit(water.toInt(),meat.toInt(),vege.toInt())
        ref.child(userID).child(habitTracker).setValue(habitT)
    }
    private fun MinusMeat()
    {
        val waterText = findViewById<TextView>(R.id.textViewWater)
        val meatText = findViewById<TextView>(R.id.textViewMeat)
        val vegeText = findViewById<TextView>(R.id.textViewVege)
        val habitTracker = "HabitTracker"

        if (waterText.text.isEmpty())
        {
            waterText.text = "0"
        }
        if (meatText.text.isEmpty())
        {
            meatText.text = "0"
        }
        if (vegeText.text.isEmpty())
        {
            vegeText.text = "0"
        }

        val water = waterText.text.toString().trim()
        val vege = vegeText.text.toString().trim()

        if(meatText.text == "0")
        {
            return
        }

        val meatD = meatText.text.toString().toInt() - 1

        meatText.setText(meatD.toString())

        val meat = meatText.text.toString().trim()

        val habitT = Habit(water.toInt(),meat.toInt(),vege.toInt())
        ref.child(userID).child(habitTracker).setValue(habitT)
    }
    private fun PlusVege()
    {
        val waterText = findViewById<TextView>(R.id.textViewWater)
        val meatText = findViewById<TextView>(R.id.textViewMeat)
        val vegeText = findViewById<TextView>(R.id.textViewVege)
        val habitTracker = "HabitTracker"

        if (waterText.text.isEmpty())
        {
            waterText.text = "0"
        }
        if (meatText.text.isEmpty())
        {
            meatText.text = "0"
        }
        if (vegeText.text.isEmpty())
        {
            vegeText.text = "0"
        }

        val water = waterText.text.toString().trim()
        val meat = meatText.text.toString().trim()

        val vegeP = vegeText.text.toString().toInt() + 1

        vegeText.setText(vegeP.toString())

        val vege = vegeText.text.toString().trim()

        val habitT = Habit(water.toInt(),meat.toInt(),vege.toInt())
        ref.child(userID).child(habitTracker).setValue(habitT)
    }
    private fun MinusVege()
    {
        val waterText = findViewById<TextView>(R.id.textViewWater)
        val meatText = findViewById<TextView>(R.id.textViewMeat)
        val vegeText = findViewById<TextView>(R.id.textViewVege)
        val habitTracker = "HabitTracker"

        if (waterText.text.isEmpty())
        {
            waterText.text = "0"
        }
        if (meatText.text.isEmpty())
        {
            meatText.text = "0"
        }
        if (vegeText.text.isEmpty())
        {
            vegeText.text = "0"
        }

        val water = waterText.text.toString().trim()
        val meat = meatText.text.toString().trim()

        if(vegeText.text == "0")
        {
            return
        }

        val vegeD = vegeText.text.toString().toInt() - 1

        vegeText.setText(vegeD.toString())

        val vege = vegeText.text.toString().trim()

        val habitT = Habit(water.toInt(),meat.toInt(),vege.toInt())
        ref.child(userID).child(habitTracker).setValue(habitT)
    }
}
