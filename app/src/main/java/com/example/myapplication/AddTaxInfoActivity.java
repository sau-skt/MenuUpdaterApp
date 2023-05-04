package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddTaxInfoActivity extends AppCompatActivity {

    EditText taxname, taxpercent;
    String username, uniqueId;
    Button Savebtn;
    DatabaseReference taxdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tax_info);
        taxname = findViewById(R.id.tax_name_edittext);
        taxpercent = findViewById(R.id.tax_percent_edittext);
        Savebtn = findViewById(R.id.tax_info_save_btn);
        username = getIntent().getStringExtra("username");
        taxdata = FirebaseDatabase.getInstance().getReference("TaxData").child(username);

        Savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uniqueId = taxdata.push().getKey();
                if (taxname != null && !taxname.getText().toString().trim().isEmpty() &&
                taxpercent != null && !taxpercent.getText().toString().trim().isEmpty()) {
                    taxdata.child(uniqueId).child("taxname").setValue(taxname.getText().toString());
                    taxdata.child(uniqueId).child("taxpercent").setValue(taxpercent.getText().toString());
                    Toast.makeText(AddTaxInfoActivity.this, "Tax details saved", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddTaxInfoActivity.this, UpdateTaxActivity.class);
                    intent.putExtra("username",username);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(AddTaxInfoActivity.this, "Please fill all the tax details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}