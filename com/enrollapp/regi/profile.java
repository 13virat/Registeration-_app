package com.enrollapp.regi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.regi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Year;

public class profile extends AppCompatActivity {
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userid;
    private Button logout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(profile.this,MainActivity.class));
            }
        });
        user=FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users");
        userid=user.getUid();

       final TextView fullnametextview=findViewById(R.id.fullname);
        final TextView mailtextview=findViewById(R.id.mail);
        final TextView numbertextview=findViewById(R.id.number);
        final TextView branchtextview=findViewById(R.id.Branch);
        final TextView yeartextview=findViewById(R.id.Year);

        reference.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userprofile=snapshot.getValue(User.class);

                if(userprofile != null){
                    String fullname=userprofile.name;
                    String mail= userprofile.email;
                    String number= userprofile.phone;
                    String branch=userprofile.branch1;
                    String year=userprofile.year1;

                    fullnametextview.setText(fullname);
                    mailtextview.setText(mail);
                    numbertextview.setText(number);
                    branchtextview.setText(branch);
                    yeartextview.setText(year);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(profile.this, "Something wrong happened!", Toast.LENGTH_SHORT).show();

            }
        });
    }
}