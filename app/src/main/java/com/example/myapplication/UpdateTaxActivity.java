package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.adapter.UpdateTaxActivityAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UpdateTaxActivity extends AppCompatActivity {

    String username;
    Button Addbutton;
    ArrayList<String> taxname = new ArrayList<>();
    ArrayList<String> taxpercent = new ArrayList<>();
    ArrayList<String> uniqueIds = new ArrayList<>();
    DatabaseReference taxdata;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_tax);
        username = getIntent().getStringExtra("username");
        Addbutton = findViewById(R.id.AddTaxInfoBtn);
        taxdata = FirebaseDatabase.getInstance().getReference("TaxData").child(username);
        recyclerView = findViewById(R.id.updatetaxactivityrv);
        layoutManager = new LinearLayoutManager(UpdateTaxActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new UpdateTaxActivityAdapter(taxname, taxpercent, uniqueIds, username);
        recyclerView.setAdapter(adapter);

        taxdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                taxname.clear();
                taxpercent.clear();
                uniqueIds.clear();
                for (DataSnapshot itemsnapshot : snapshot.getChildren()) {
                    String uniqueId = itemsnapshot.getKey();
                    String name = itemsnapshot.child("taxname").getValue(String.class);
                    String percent = itemsnapshot.child("taxpercent").getValue(String.class);
                    taxname.add(name);
                    taxpercent.add(percent);
                    uniqueIds.add(uniqueId);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateTaxActivity.this, AddTaxInfoActivity.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });
    }
}