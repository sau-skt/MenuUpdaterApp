package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class GenerateReportActivity extends AppCompatActivity {

    TextView startdatetv, enddatetv;
    DatePickerDialog picker;
    String username;
    Button downloadreport;
    DatabaseReference cxorderreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_report);
        startdatetv = findViewById(R.id.start_date_tv);
        enddatetv = findViewById(R.id.end_date_tv);
        downloadreport = findViewById(R.id.downloadbutton);
        username = getIntent().getStringExtra("username");
        cxorderreference = FirebaseDatabase.getInstance().getReference("CxOrder").child(username);

        startdatetv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                picker = new DatePickerDialog(GenerateReportActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                startdatetv.setText(i2 + "/" + (i1 + 1) + "/" + i);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        enddatetv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                picker = new DatePickerDialog(GenerateReportActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                enddatetv.setText(i2 + "/" + (i1 + 1) + "/" + i);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        downloadreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!startdatetv.getText().equals("Click to select start date") && !enddatetv.getText().equals("Click to select end date")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d/M/yyyy");
                        DateTimeFormatter output = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                        LocalDate startDate = LocalDate.parse(startdatetv.getText().toString(), dateFormatter);
                        LocalDate endDate = LocalDate.parse(enddatetv.getText().toString(), dateFormatter);

                        if (startDate.isBefore(endDate) || startDate.isEqual(endDate)) {
                            for (LocalDate date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
                                String formattedDate = date.format(output);
                                cxorderreference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            String inno = dataSnapshot.getKey();
                                            cxorderreference.child(inno).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        String invoiceDate = snapshot.child("invoicedate").getValue(String.class);
                                                        if (invoiceDate != null && invoiceDate.equals(String.valueOf(formattedDate))) {
                                                            Log.e("QWE", String.valueOf(invoiceDate));
                                                        }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        } else {
                            Toast.makeText(GenerateReportActivity.this, "Start date cannot be after end date", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(GenerateReportActivity.this, "Please select dates...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}