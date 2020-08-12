package com.example.greenhealth

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_profile.*
import java.util.*
import kotlin.math.roundToInt

class ProfileActivity : AppCompatActivity() {
    lateinit var ref : DatabaseReference
    val fb = FirebaseAuth.getInstance()
    val fbStorage = FirebaseStorage.getInstance()
    val userID = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private val PERMISSION_CODE = 1000
    private val IMAGE_CAPTURE_CODE = 1001
    var image_uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val logoutBut = findViewById<Button>(R.id.buttonLogout)
        val saveBut = findViewById<Button>(R.id.buttonSave)
        ref = FirebaseDatabase.getInstance().getReference("users")
        val intent2 = Intent(this, MainMenuActivity::class.java)
        val maleCheck = findViewById<CheckBox>(R.id.checkBoxMale)
        val femaleCheck = findViewById<CheckBox>(R.id.checkBoxFemale)
        val lbsCheck = findViewById<CheckBox>(R.id.checkBoxlbs)
        val inchCheck = findViewById<CheckBox>(R.id.checkBoxInch)
        val capture_btn = findViewById<Button>(R.id.button)

        if (fb.currentUser==null)
        {
            finish()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        logoutBut.setOnClickListener {
            fb.signOut()
            finish()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        saveBut.setOnClickListener{
            //upLoadImageToFirebaseStorage()
            SavingProfile()
            //finish()
        }
        capture_btn.setOnClickListener {
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

        ref.addValueEventListener(object: ValueEventListener{
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
                                editTextHeight.setText("%.2f".format(user.height))
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
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, MainMenuActivity::class.java)
                    startActivity(intent)
                    finish()
                }
        }
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
    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val image_view = findViewById<ImageView>(R.id.imageView12)
        //called when image was captured from camera intent
        if (resultCode == Activity.RESULT_OK){
            //set image captured to image view
            selectedPhotoUri = data?.data
            image_view.setImageURI(image_uri)
        }
    }
    /*private fun upLoadImageToFirebaseStorage(){
        if (selectedPhotoUri == null) return
        val fileName = UUID.randomUUID().toString()
        val ref = fbStorage.getReference("/image/$fileName")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("ProfileActivity","Success: ${it.metadata?.path}")
            }
    }*/
}
