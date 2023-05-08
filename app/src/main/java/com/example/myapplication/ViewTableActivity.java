package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.adapter.ViewTableActivityAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewTableActivity extends AppCompatActivity {

    String username;
    DatabaseReference tablereference;
    ArrayList<String> availibility = new ArrayList<>();
    ArrayList<String> invoicenumber = new ArrayList<>();
    ArrayList<String> tableid = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    TextView addtable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_table);
        username = getIntent().getStringExtra("username");
        tablereference = FirebaseDatabase.getInstance().getReference("TableInfo").child(username);
        recyclerView = findViewById(R.id.view_table_recyclerview);
        addtable = findViewById(R.id.add_table);
        adapter = new ViewTableActivityAdapter(availibility,invoicenumber,tableid);
        recyclerView.setAdapter(adapter);

        tablereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                availibility.clear();
                invoicenumber.clear();
                tableid.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String avail = dataSnapshot.child("availibility").getValue(String.class);
                    String inv = dataSnapshot.child("invoicenumber").getValue(String.class);
                    String tid = dataSnapshot.child("tableid").getValue(String.class);
                    availibility.add(avail);
                    invoicenumber.add(inv);
                    tableid.add(tid);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addtable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tablereference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        availibility.clear();
                        invoicenumber.clear();
                        tableid.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String avail = dataSnapshot.child("availibility").getValue(String.class);
                            String inv = dataSnapshot.child("invoicenumber").getValue(String.class);
                            String tid = dataSnapshot.child("tableid").getValue(String.class);
                            availibility.add(avail);
                            invoicenumber.add(inv);
                            tableid.add(tid);
                        }
                        if (tableid.isEmpty()){
                            int tableno = 1;
                            tablereference.child(String.valueOf(tableno)).child("availibility").setValue("true");
                            tablereference.child(String.valueOf(tableno)).child("invoicenumber").setValue("0");
                            tablereference.child(String.valueOf(tableno)).child("tableid").setValue(String.valueOf(tableno));
                        } else {
                            int tableno = Integer.parseInt(tableid.get(tableid.size()-1)) + 1;
                            tablereference.child(String.valueOf(tableno)).child("availibility").setValue("true");
                            tablereference.child(String.valueOf(tableno)).child("invoicenumber").setValue("0");
                            tablereference.child(String.valueOf(tableno)).child("tableid").setValue(String.valueOf(tableno));
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