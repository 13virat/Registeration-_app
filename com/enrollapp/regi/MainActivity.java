package com.enrollapp.regi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.regi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView register,forgetpassword;
    private EditText editTextEmailAddress,editTextPassword2;
    private Button buttonlogin;
    private FirebaseAuth mAuth;
    private ProgressBar progressbar2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        register=findViewById(R.id.register);
        register.setOnClickListener(this);
        forgetpassword=findViewById(R.id.forgetpassword);
        forgetpassword.setOnClickListener(this);
        buttonlogin= findViewById(R.id.buttonlogin);
        buttonlogin.setOnClickListener(this);
        editTextEmailAddress=findViewById(R.id.editTextEmailAddress);
        editTextPassword2=findViewById(R.id.editTextPassword2);
        progressbar2=findViewById(R.id.progressBar2);
        mAuth=FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.register:
                startActivity(new Intent(this, register.class));
                break;

            case R.id.buttonlogin:
                buttonlogin();
                break;
            case R.id.forgetpassword:
                startActivity(new Intent(this,forget_password.class));
                break;
        }
    }

    private void buttonlogin() {
        String email=editTextEmailAddress.getText().toString().trim();
        String password=editTextPassword2.getText().toString().trim();

        if (email.isEmpty()){
            editTextEmailAddress.setError("Email is required!");
            editTextEmailAddress.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmailAddress.setError("Please provide valid email!");
            editTextEmailAddress.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editTextPassword2.setError("Password is required!");
            editTextPassword2.requestFocus();
            return;
        }
        if (password.length()<6) {
            editTextPassword2.setError("Password length should be atleast 6 characters!");
            editTextPassword2.requestFocus();
            return;
        }
        progressbar2.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if (user.isEmailVerified()) {
                        // redirect to user profile
                        startActivity(new Intent(MainActivity.this, profile.class));
                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Check your email to verify your account!", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this,"Registered successfully!",Toast.LENGTH_LONG).show();
                    progressbar2.setVisibility(View.GONE);

                }
            }
        });
    }
}