package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CollectPaymentActivity extends AppCompatActivity {
    String username, tableId;
    DatabaseReference tablereference, cxorderreference;
    TextView totalamt;

    Button receive_payment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_payment);
        totalamt = findViewById(R.id.total_amount_tv);
        username = getIntent().getStringExtra("username");
        tableId = getIntent().getStringExtra("tableId");
        tablereference = FirebaseDatabase.getInstance().getReference("TableInfo").child(username).child(tableId);
        cxorderreference = FirebaseDatabase.getInstance().getReference("CxOrder").child(username);
        receive_payment = findViewById(R.id.collect_amount_btn);

        tablereference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                totalamt.setText("Total - " + snapshot.child("totalamount").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        receive_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tablereference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String invoice = snapshot.child("invoicenumber").getValue(String.class);
                        cxorderreference.child(invoice).child("order_status").setValue("Closed");
                        tablereference.child("availibility").setValue("true");
                        tablereference.child("totalamount").setValue("0");
                        tablereference.child("invoicenumber").setValue("0");
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}