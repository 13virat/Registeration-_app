package com.enrollapp.regi;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.regi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.time.Year;


public class register<spinner, button> extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth mAuth;
    private TextView textviewlogin;
    private EditText editTextNumber,editTextName,editTextEmail,editTextPassword;
    private EditText year,branch;
    private Button buttonregister;
    private ProgressBar progressBar;
    private EditText gender;


    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        mAuth = FirebaseAuth.getInstance();
        textviewlogin = findViewById(R.id.textviewlogin);
        textviewlogin.setOnClickListener(this);
        buttonregister = (Button) findViewById(R.id.buttonregister);
        buttonregister.setOnClickListener(this);
        editTextName= findViewById(R.id.editTextName);
        editTextEmail=findViewById(R.id.editTextEmail);
        editTextPassword=findViewById(R.id.editTextPassword);
        editTextNumber=findViewById(R.id.editTextNumber);
        gender = findViewById(R.id.gender);
        year=  findViewById(R.id.year);
        branch= findViewById(R.id.branch);
        progressBar=findViewById(R.id.progressBar3);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.textviewlogin:
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.buttonregister:
                buttonregister();
                break;
        }
        
    }

    private void buttonregister() {
        String email=editTextEmail.getText().toString().trim();
        String password=editTextName.getText().toString().trim();
        String name=editTextPassword.getText().toString().trim();
        String phone=editTextNumber.getText().toString().trim();
        String gender1=gender.getText().toString().trim();
        String year1=year.getText().toString().trim();
        String branch1=branch.getText().toString().trim();

        if(name.isEmpty()){
            editTextName.setError("Name is required!");
            editTextName.requestFocus();
            return;
        }
        if(email.isEmpty()){
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please provide valid email!");
            editTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()){
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length()<6){
            editTextPassword.setError("Password length should be atleast 6 characters!");
            editTextPassword.requestFocus();
            return;
        }
        if (phone.isEmpty()){
            editTextNumber.setError("Phone number is required!");
            editTextNumber.requestFocus();
            return;
        }
        if(phone.length()<10){
            editTextNumber.setError("Must have 10 numbers!");
            editTextNumber.requestFocus();
            return;
        }
        if(gender1.isEmpty()){
            gender.setError("Gender is required!");
            gender.requestFocus();
            return;
        }
        if(year1.isEmpty()){
            year.setError("Year is required!");
            year.requestFocus();
            return;
        }
        if(branch1.isEmpty()){
            branch.setError("Branch is required!");
            branch.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User User=new User(name,email,phone,branch1,year1,gender1);

                            FirebaseDatabase.getInstance().getReference("User")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(User).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(register.this,"Registered successfully!",Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);

                                                //back to login
                                            }else{
                                                Toast.makeText(register.this,"Failed! Try again!",Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        }else{
                            Toast.makeText(register.this,"Failed!",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

























    }
}