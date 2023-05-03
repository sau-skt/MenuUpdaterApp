package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    Button loginbtn, forgotpasswordbtn;
    EditText user,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseReference = FirebaseDatabase.getInstance().getReference("SID");
        loginbtn = findViewById(R.id.activity_main_login_btn);
        user = findViewById(R.id.activity_main_user);
        pass = findViewById(R.id.activity_main_password);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Attach a listener to read the data at the SID node
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Iterate through each child node of the SID node
                        for (DataSnapshot sidSnapshot : dataSnapshot.getChildren()) {
                            String username = sidSnapshot.child("username").getValue(String.class);
                            String password = sidSnapshot.child("password").getValue(String.class);
                            if (user.getText().toString().equals(username) && pass.getText().toString().equals(password)){
                                Toast.makeText(MainActivity.this, "Welcome to your store - " + username, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, OptionActivity.class);
                                intent.putExtra("username",username);
                                startActivity(intent);
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
    }
}