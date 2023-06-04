package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class GenerateReportActivity extends AppCompatActivity {

    TextView startdatetv, enddatetv;
    DatePickerDialog picker;
    String username;
    Button downloadreport;
    DatabaseReference cxorderreference;
    StringBuilder csvData = new StringBuilder();

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

                        csvData.append("InvoiceDate, InvoiceNumber, SubTotal, Total, OrderStatus, Taxes\n");

                        if (startDate.isBefore(endDate) || startDate.isEqual(endDate)) {
                                cxorderreference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            String inno = dataSnapshot.getKey();
                                            cxorderreference.child(inno).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        String invoiceDate = snapshot.child("invoicedate").getValue(String.class);
                                                    for (LocalDate date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
                                                        String formattedDate = date.format(output);
                                                        if (invoiceDate != null && invoiceDate.equals(String.valueOf(formattedDate))) {
                                                            String invsubtotal = snapshot.child("invoicesubtotal").getValue(String.class);
                                                            String invtotal = snapshot.child("invoicetotal").getValue(String.class);
                                                            String ordstatus = snapshot.child("order_status").getValue(String.class);
                                                            int index = invsubtotal.indexOf("Total ");
                                                            csvData.append(invoiceDate + ",");
                                                            csvData.append(inno + ",");
                                                            if (index != 1) {
                                                                csvData.append(invsubtotal.substring(index + 6) + ",");
                                                            }
                                                            csvData.append(invtotal + ",");
                                                            csvData.append(ordstatus + ",");
                                                            csvData.append(String.valueOf(Float.parseFloat(invtotal) - Float.parseFloat(invsubtotal.substring(index + 6))) + "\n");
                                                        }
                                                    }
                                                    File dir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                                                    File file = new File(dir, "report.csv");

                                                    if (!file.exists()){
                                                        try {
                                                            file.createNewFile();
                                                        } catch (IOException e) {
                                                            throw new RuntimeException(e);
                                                        }
                                                    }

                                                    try {
                                                        FileOutputStream outputStream = new FileOutputStream(file);
                                                        outputStream.write(csvData.toString().getBytes());
                                                        outputStream.close();
                                                        Toast.makeText(GenerateReportActivity.this, "Success", Toast.LENGTH_SHORT).show();

                                                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                                                        emailIntent.setType("text/csv");
                                                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Report File");
                                                        emailIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(GenerateReportActivity.this,BuildConfig.APPLICATION_ID + ".fileprovider", file));
                                                        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                                                        startActivity(Intent.createChooser(emailIntent, "Send Email..."));
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
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