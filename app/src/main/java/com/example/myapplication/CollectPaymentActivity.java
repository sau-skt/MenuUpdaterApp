package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CollectPaymentActivity extends AppCompatActivity {
    String username, tableId;
    DatabaseReference tablereference;

    Button receive_payment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_payment);
        username = getIntent().getStringExtra("username");
        tableId = getIntent().getStringExtra("tableId");
        tablereference = FirebaseDatabase.getInstance().getReference("TableInfo").child(username).child(tableId);
        receive_payment = findViewById(R.id.collect_amount_btn);

        receive_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tablereference.child("availibility").setValue("true");
                tablereference.child("totalamount").setValue("0");
                tablereference.child("invoicenumber").setValue("0");
                finish();
            }
        });
    }
}