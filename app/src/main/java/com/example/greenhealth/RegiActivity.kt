package com.example.greenhealth

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class RegiActivity : AppCompatActivity() {
    val fb = FirebaseAuth.getInstance();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regi)
        val regiBut = findViewById<Button>(R.id.buttonRegister)
        val loginTextBut = findViewById<TextView>(R.id.textView2)

        regiBut.setOnClickListener {
            regi()
        }
        loginTextBut.setOnClickListener {
            finish()
            val intent = Intent(this, MainActivity::class.java)
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
            Toast.makeText(this,"Please enter email", Toast.LENGTH_SHORT).show();
            return
        }
        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Please enter Password", Toast.LENGTH_SHORT).show();
            return
        }
        val prog = ProgressDialog(this@RegiActivity)
        prog.setMessage("Register User...")
        prog.show()
        fb.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    //Registration OK
                    Toast.makeText(this,"Regi Success", Toast.LENGTH_SHORT).show();
                    finish()
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                } else {
                    //Registration error
                    Toast.makeText(this,"Regi Fail", Toast.LENGTH_SHORT).show();
                }
            }
    }
}
