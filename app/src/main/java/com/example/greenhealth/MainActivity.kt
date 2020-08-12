package com.example.greenhealth

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.widget.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    val fb = FirebaseAuth.getInstance();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val loginBut = findViewById<Button>(R.id.buttonLogIn)
        val regiTextBut = findViewById<TextView>(R.id.textView)

        if (fb.currentUser!=null)
        {
            finish()
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        loginBut.setOnClickListener {
            regi()
        }
        regiTextBut.setOnClickListener {
            finish()
            val intent = Intent(this, RegiActivity::class.java)
            startActivity(intent)
        }
    }
    private fun regi()
    {
        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)

        var email = editTextEmail.getText().toString().trim();
        var password = editTextPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Please enter email",Toast.LENGTH_SHORT).show();
            return
        }
        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Please enter Password",Toast.LENGTH_SHORT).show();
            return
        }
        val prog = ProgressDialog(this@MainActivity)
        prog.setMessage("Login User...")
        prog.show()
        fb.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    //Registration OK
                    Toast.makeText(this,"Login Success",Toast.LENGTH_SHORT).show();
                    finish()
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                } else {
                    //Registration error
                    Toast.makeText(this,"Login Fail",Toast.LENGTH_SHORT).show();
                }
            }
    }
}
