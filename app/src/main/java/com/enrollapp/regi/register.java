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


public class register<spinner, button> extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth mAuth;
    private TextView textviewlogin;
    private EditText editTextNumber,editTextName,editTextEmail,editTextPassword;
    private spinner gender,year,branch;
    private Button buttonregister;
    private ProgressBar progressBar;



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
        gender= (spinner) findViewById(R.id.gender);
        year= (spinner) findViewById(R.id.year);
        branch=(spinner) findViewById(R.id.branch);
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
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            details details=new details(name,email,phone);

                            FirebaseDatabase.getInstance().getReference("details")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(details).addOnCompleteListener(new OnCompleteListener<Void>() {
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