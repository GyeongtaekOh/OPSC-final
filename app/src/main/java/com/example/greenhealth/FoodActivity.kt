package com.example.greenhealth

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FoodActivity : AppCompatActivity() {
    lateinit var ref : DatabaseReference
    val fb = FirebaseAuth.getInstance();
    val userID = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private val PERMISSION_CODE = 1000;
    private val IMAGE_CAPTURE_CODE = 1001
    var image_uri: Uri? = null
    lateinit var image_view:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food)

        val exerciseDiaryBut = findViewById<ImageButton>(R.id.imageButtonEx)
        val habitBut = findViewById<ImageButton>(R.id.imageButtonHabit)
        val profileBut = findViewById<ImageButton>(R.id.imageButtonProf)
        val calGoalSaveBut = findViewById<ImageButton>(R.id.imageButtonSaveCalGoal)
        val currentTotalSaveBut = findViewById<Button>(R.id.buttonFoodSave)
        val homeBut = findViewById<ImageButton>(R.id.imageButtonHome)
        ref = FirebaseDatabase.getInstance().getReference("users")
        val goalCalText = findViewById<TextView>(R.id.editTextGoalCal)
        val currentCalText = findViewById<TextView>(R.id.editTextCurrentCal)
        val food1InfoText = findViewById<TextView>(R.id.editTextFoodInfo1)
        val food2InfoText = findViewById<TextView>(R.id.editTextFoodInfo2)
        val food3InfoText = findViewById<TextView>(R.id.editTextFoodInfo3)
        val food1CalText = findViewById<TextView>(R.id.editTextFoodCal1)
        val food2CalText = findViewById<TextView>(R.id.editTextFoodCal2)
        val food3CalText = findViewById<TextView>(R.id.editTextFoodCal3)
        val food1Pic = findViewById<ImageView>(R.id.imageViewFoodPic1)
        val food2Pic = findViewById<ImageView>(R.id.imageViewFoodPic2)
        val food3Pic = findViewById<ImageView>(R.id.imageViewFoodPic3)
        val monthFoodBut = findViewById<Button>(R.id.buttonMonthFood)

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
        monthFoodBut.setOnClickListener{
            val intent = Intent(this, MonthFoodActivity::class.java)
            startActivity(intent)
        }

        calGoalSaveBut.setOnClickListener{
            SaveGoal()
        }
        currentTotalSaveBut.setOnClickListener{
            SaveCurrentCal()
        }

        food1Pic.setOnClickListener {
            image_view = food1Pic
            //if system os is Marshmallow or Above, we need to request runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED){
                    //permission was not enabled
                    val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    //show popup to request permission
                    requestPermissions(permission, PERMISSION_CODE)
                }
                else{
                    //permission already granted
                    openCamera()
                }
            }
            else{
                //system os is < marshmallow
                openCamera()
            }
        }
        food2Pic.setOnClickListener {
            image_view = food2Pic
            //if system os is Marshmallow or Above, we need to request runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED){
                    //permission was not enabled
                    val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    //show popup to request permission
                    requestPermissions(permission, PERMISSION_CODE)
                }
                else{
                    //permission already granted
                    openCamera()
                }
            }
            else{
                //system os is < marshmallow
                openCamera()
            }
        }
        food3Pic.setOnClickListener {
            image_view = food3Pic
            //if system os is Marshmallow or Above, we need to request runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED){
                    //permission was not enabled
                    val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    //show popup to request permission
                    requestPermissions(permission, PERMISSION_CODE)
                }
                else{
                    //permission already granted
                    openCamera()
                }
            }
            else{
                //system os is < marshmallow
                openCamera()
            }
        }



        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0!!.exists()){

                    for (i in p0.children){

                        val user = i.child("Profile").getValue(Profile::class.java)

                        if (i.child("FoodDiary").exists()){

                            val goals = i.child("FoodDiary").getValue(FoodDiary::class.java)

                            if (user!!.userID == userID){
                                goalCalText.setText(goals!!.goalCal.toString())
                                currentCalText.setText(goals!!.currentCal.toString())
                                food1InfoText.setText(goals!!.food1Info)
                                food2InfoText.setText(goals!!.food2Info)
                                food3InfoText.setText(goals!!.food3Info)
                                food1CalText.setText(goals!!.food1Cal.toString())
                                food2CalText.setText(goals!!.food2Cal.toString())
                                food3CalText.setText(goals!!.food3Cal.toString())
                            }
                        }
                    }
                }
            }
        })


    }
    private fun SaveGoal(){
        val goalCalText = findViewById<TextView>(R.id.editTextGoalCal)
        val currentCalText = findViewById<TextView>(R.id.editTextCurrentCal)
        val food1InfoText = findViewById<TextView>(R.id.editTextFoodInfo1)
        val food2InfoText = findViewById<TextView>(R.id.editTextFoodInfo2)
        val food3InfoText = findViewById<TextView>(R.id.editTextFoodInfo3)
        val food1CalText = findViewById<TextView>(R.id.editTextFoodCal1)
        val food2CalText = findViewById<TextView>(R.id.editTextFoodCal2)
        val food3CalText = findViewById<TextView>(R.id.editTextFoodCal3)
        if (currentCalText.text.isEmpty())
        {
            currentCalText.setText("0")
        }
        if (goalCalText.text.isEmpty())
        {
            goalCalText.setText("0")
        }
        if (food1InfoText.text.isEmpty())
        {
            food1InfoText.setText(" ")
        }
        if (food2InfoText.text.isEmpty())
        {
            food2InfoText.setText(" ")
        }
        if (food3InfoText.text.isEmpty())
        {
            food3InfoText.setText(" ")
        }
        if (food1CalText.text.isEmpty())
        {
            food1CalText.setText("0")
        }
        if (food2CalText.text.isEmpty())
        {
            food2CalText.setText("0")
        }
        if (food3CalText.text.isEmpty())
        {
            food3CalText.setText("0")
        }

        val foodDiary = "FoodDiary"

        val goalFoodCal = goalCalText.text.toString().trim()
        val currentCal = currentCalText.text.toString().trim()
        val food1Info = food1InfoText.text.toString().trim()
        val food2Info = food2InfoText.text.toString().trim()
        val food3Info = food3InfoText.text.toString().trim()
        val food1Cal = food1CalText.text.toString().trim()
        val food2Cal = food2CalText.text.toString().trim()
        val food3Cal = food3CalText.text.toString().trim()

        val fdGoal = FoodDiary(goalFoodCal.toDouble(),currentCal.toDouble(),food1Info,food2Info,food3Info,food1Cal.toDouble(),food2Cal.toDouble(),food3Cal.toDouble())
        ref.child(userID).child(foodDiary).setValue(fdGoal)
    }

    private fun SaveCurrentCal() {
        val goalCalText = findViewById<TextView>(R.id.editTextGoalCal)
        val currentCalText = findViewById<TextView>(R.id.editTextCurrentCal)
        val food1InfoText = findViewById<TextView>(R.id.editTextFoodInfo1)
        val food2InfoText = findViewById<TextView>(R.id.editTextFoodInfo2)
        val food3InfoText = findViewById<TextView>(R.id.editTextFoodInfo3)
        val food1CalText = findViewById<TextView>(R.id.editTextFoodCal1)
        val food2CalText = findViewById<TextView>(R.id.editTextFoodCal2)
        val food3CalText = findViewById<TextView>(R.id.editTextFoodCal3)

        if (currentCalText.text.isEmpty())
        {
            currentCalText.setText("0")
        }
        if (goalCalText.text.isEmpty())
        {
            goalCalText.setText("0")
        }
        if (food1InfoText.text.isEmpty())
        {
            food1InfoText.setText(" ")
        }
        if (food2InfoText.text.isEmpty())
        {
            food2InfoText.setText(" ")
        }
        if (food3InfoText.text.isEmpty())
        {
            food3InfoText.setText(" ")
        }
        if (food1CalText.text.isEmpty())
        {
            food1CalText.setText("0")
        }
        if (food2CalText.text.isEmpty())
        {
            food2CalText.setText("0")
        }
        if (food3CalText.text.isEmpty())
        {
            food3CalText.setText("0")
        }

        val goalFoodCal = goalCalText.text.toString().trim()
        val food1Info = food1InfoText.text.toString().trim()
        val food2Info = food2InfoText.text.toString().trim()
        val food3Info = food3InfoText.text.toString().trim()
        val food1Cal = food1CalText.text.toString().trim()
        val food2Cal = food2CalText.text.toString().trim()
        val food3Cal = food3CalText.text.toString().trim()

        val total = food1Cal.toDouble() + food2Cal.toDouble() + food3Cal.toDouble()

        currentCalText.setText(total.toString().trim())

        val currentCal = currentCalText.text.toString().trim()

        val foodDiary = "FoodDiary"

        val fdGoal = FoodDiary(goalFoodCal.toDouble(),currentCal.toDouble(),food1Info,food2Info,food3Info,food1Cal.toDouble(),food2Cal.toDouble(),food3Cal.toDouble())
        ref.child(userID).child(foodDiary).setValue(fdGoal)

    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //called when user presses ALLOW or DENY from Permission Request Popup
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup was granted
                    openCamera()
                }
                else{
                    //permission from popup was denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //called when image was captured from camera intent
        if (resultCode == Activity.RESULT_OK){
            //set image captured to image view

            image_view.setImageURI(image_uri)
        }
    }
}
