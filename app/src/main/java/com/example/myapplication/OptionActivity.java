package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OptionActivity extends AppCompatActivity {

    String username;
    Button updatemenu, updatetax, stockout, viewtables, generatereport, viewinvoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        updatemenu = findViewById(R.id.update_menu_btn);
        updatetax = findViewById(R.id.update_tax);
        stockout = findViewById(R.id.stockout);
        viewtables = findViewById(R.id.view_tables);
        generatereport = findViewById(R.id.generate_sales_report);
        viewinvoice = findViewById(R.id.view_invoice);
        username = getIntent().getStringExtra("username");

        updatemenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OptionActivity.this, FoodMenuEditActivity.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        updatetax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OptionActivity.this, UpdateTaxActivity.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        stockout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OptionActivity.this, StockOutActivity.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        viewtables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OptionActivity.this, ViewTableActivity.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        generatereport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OptionActivity.this, GenerateReportActivity.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        viewinvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OptionActivity.this, ViewInvoiceActivity.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });
    }
}