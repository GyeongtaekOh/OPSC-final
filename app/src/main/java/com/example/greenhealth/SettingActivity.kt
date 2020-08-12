package com.example.greenhealth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_profile.*

class SettingActivity : AppCompatActivity() {

    lateinit var ref : DatabaseReference
    val fb = FirebaseAuth.getInstance();
    val userID = FirebaseAuth.getInstance().currentUser?.uid.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)


        val foodDiaryBut = findViewById<ImageButton>(R.id.imageButtonFood)
        val homeBut = findViewById<ImageButton>(R.id.imageButtonHome)
        val exerciseDiaryBut = findViewById<ImageButton>(R.id.imageButtonEx)
        val habitBut = findViewById<ImageButton>(R.id.imageButtonHabit)
        ref = FirebaseDatabase.getInstance().getReference("users")
        val logoutBut = findViewById<Button>(R.id.buttonLogout)
        val saveBut = findViewById<Button>(R.id.buttonSave)
        val maleCheck = findViewById<CheckBox>(R.id.checkBoxMale)
        val femaleCheck = findViewById<CheckBox>(R.id.checkBoxFemale)
        val lbsCheck = findViewById<CheckBox>(R.id.checkBoxlbs)
        val inchCheck = findViewById<CheckBox>(R.id.checkBoxInch)

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
        habitBut.setOnClickListener{
            val intent = Intent(this, HabitActivity::class.java)
            startActivity(intent)
            finish()
        }
        logoutBut.setOnClickListener {
            fb.signOut()
            finish()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        saveBut.setOnClickListener{
            SavingProfile()
        }
        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(pe: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(pe: DataSnapshot) {
                if (pe!!.exists())
                {
                    for (u in pe.children)
                    {
                        if (u.child("Profile").exists()) {
                            val user = u.child("Profile").getValue(Profile::class.java)
                            if (user!!.userID == userID) {
                                editTextName.setText(user.name)
                                editTextAge.setText(user.age.toString())
                                editTextWeight.setText("%.2f".format(user.weight))
                                editTextHeight.setText(user.height.toString())

                                if (user.checklbs == "kg")
                                {
                                    lbsCheck.isChecked = false
                                }
                                else if (user.checklbs == "lbs")
                                {
                                    lbsCheck.isChecked = true
                                }
                                if (user.checkInch == "m")
                                {
                                    inchCheck.isChecked = false
                                }
                                else if (user.checkInch == "inch")
                                {
                                    inchCheck.isChecked = true
                                }
                                lbsCheck.setOnClickListener {
                                    if (lbsCheck.isChecked == false)
                                    {
                                        val tolbs = editTextWeight.text.toString().toDouble()*0.45359237
                                        editTextWeight.setText("%.2f".format(tolbs))
                                        //val userChangelbs = Profile(user.name,user.age,tolbs,user.height,userID,user.maleFemale,user.checklbs)
                                        //ref.child(userID).child("Profile").setValue(userChangelbs)
                                    }
                                    else if (lbsCheck.isChecked == true)
                                    {
                                        val tolbs = editTextWeight.text.toString().toDouble()/0.45359237
                                        editTextWeight.setText("%.2f".format(tolbs))
                                        //val userChangekg = Profile(user.name,user.age,tolbs,user.height,userID,user.maleFemale,user.checklbs)
                                        //ref.child(userID).child("Profile").setValue(userChangekg)
                                    }
                                }
                                inchCheck.setOnClickListener {
                                    if (inchCheck.isChecked == false)
                                    {
                                        val toInch = editTextHeight.text.toString().toDouble()*0.0254
                                        editTextHeight.setText("%.2f".format(toInch))
                                        //val userChangelbs = Profile(user.name,user.age,tolbs,user.height,userID,user.maleFemale,user.checklbs)
                                        //ref.child(userID).child("Profile").setValue(userChangelbs)
                                    }
                                    else if (inchCheck.isChecked == true)
                                    {
                                        val toInch = editTextHeight.text.toString().toDouble()/0.0254
                                        editTextHeight.setText("%.2f".format(toInch))
                                        //val userChangekg = Profile(user.name,user.age,tolbs,user.height,userID,user.maleFemale,user.checklbs)
                                        //ref.child(userID).child("Profile").setValue(userChangekg)
                                    }
                                }
                                if (user.maleFemale == "male")
                                {
                                    maleCheck.isChecked = true
                                }
                                if (user.maleFemale == "female")
                                {
                                    femaleCheck.isChecked = true
                                }
                            }
                        }
                    }
                }
            }
        })
    }
    private fun SavingProfile()
    {
        val nameText = findViewById<TextView>(R.id.editTextName)
        val ageText = findViewById<TextView>(R.id.editTextAge)
        val weightText = findViewById<TextView>(R.id.editTextWeight)
        val heightText = findViewById<TextView>(R.id.editTextHeight)
        val maleCheck = findViewById<CheckBox>(R.id.checkBoxMale)
        val femaleCheck = findViewById<CheckBox>(R.id.checkBoxFemale)
        val lbsCheck = findViewById<CheckBox>(R.id.checkBoxlbs)
        val inchCheck = findViewById<CheckBox>(R.id.checkBoxInch)
        val userPro = "Profile"
        var weightlbs = ""
        var maleFemale = ""
        var mInch = ""

        val name = nameText.text.toString().trim()
        val age = ageText.text.toString().trim()
        val weight = weightText.text.toString().trim()
        val height = heightText.text.toString().trim()

        if (maleCheck.isChecked == false && femaleCheck.isChecked == false)
        {
            maleCheck.error = "Choose the gender"
            return
        }
        if (maleCheck.isChecked == true && femaleCheck.isChecked == true)
        {
            maleCheck.error = "Choose one gender"
            return
        }
        if (name.isEmpty())
        {
            nameText.error = "Please enter a name"
            return
        }
        if (age.isEmpty())
        {
            nameText.error = "Please enter a age"
            return
        }
        if (weight.isEmpty())
        {
            nameText.error = "Please enter a weight"
            return
        }
        if (height.isEmpty())
        {
            nameText.error = "Please enter a height"
            return
        }
        if (maleCheck.isChecked == true)
        {
            maleFemale = "male"
        }
        else if (femaleCheck.isChecked == true)
        {
            maleFemale = "female"
        }
        if (lbsCheck.isChecked == false)
        {
            weightlbs = "kg"
        }
        else if (lbsCheck.isChecked == true)
        {
            weightlbs = "lbs"
        }
        if (inchCheck.isChecked == false)
        {
            mInch = "m"
        }
        else if (inchCheck.isChecked == true)
        {
            mInch = "inch"
        }

        val user = Profile(name,age.toInt(),weight.toDouble(),height.toDouble(),userID,maleFemale,weightlbs,mInch)
        ref.child(userID).child(userPro).setValue(user)
    }
}
