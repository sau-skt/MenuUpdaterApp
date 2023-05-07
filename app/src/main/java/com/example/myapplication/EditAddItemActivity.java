package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditAddItemActivity extends AppCompatActivity {

    EditText itemname, itemprice, itemdesc, itemcategory;
    Button Savebtn, BulkUploadBtn, GetCSV;
    String username, itemName, itemPrice, itemDesc, itemId, itemCategory, Vis;
    ArrayList<String> itemIdList = new ArrayList<>();
    ArrayList<String> itemNameList = new ArrayList<>();
    ArrayList<String> itemPriceList = new ArrayList<>();
    ArrayList<String> itemDescList = new ArrayList<>();
    ArrayList<String> itemTypeList = new ArrayList<>();
    ArrayList<String> itemCategoryList = new ArrayList<>();
    DatabaseReference databaseReference, csvdatabasereference, cxdatabasereference;
    RadioGroup radioGroup;
    RadioButton radioButton;
    StorageReference storageReference, csvRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_add_item);
        itemname = findViewById(R.id.ActivityEditItemItemNametv);
        BulkUploadBtn = findViewById(R.id.ActivityEditItemBulkUpload);
        radioGroup = findViewById(R.id.ActivityEditItemRadioGroup);
        itemprice = findViewById(R.id.ActivityEditItemItemPricetv);
        GetCSV = findViewById(R.id.ActivityEditItemSendMeCsv);
        itemdesc = findViewById(R.id.ActivityEditItemItemDesctv);
        Savebtn = findViewById(R.id.ActivityEditItemSavebtn);
        itemcategory = findViewById(R.id.ActivityEditItemItemCategorytv);
        itemIdList = getIntent().getStringArrayListExtra("itemIdList");
        itemNameList = getIntent().getStringArrayListExtra("itemNameList");
        itemPriceList = getIntent().getStringArrayListExtra("itemPriceList");
        itemDescList = getIntent().getStringArrayListExtra("itemDescList");
        itemTypeList = getIntent().getStringArrayListExtra("itemTypeList");
        itemCategoryList = getIntent().getStringArrayListExtra("itemCategoryList");
        username = getIntent().getStringExtra("username");
        itemName = getIntent().getStringExtra("itemname");
        itemPrice = getIntent().getStringExtra("itemprice");
        itemDesc = getIntent().getStringExtra("itemdesc");
        itemId = getIntent().getStringExtra("itemId");
        itemCategory = getIntent().getStringExtra("itemcategory");
        Vis = getIntent().getStringExtra("visibility");
        itemname.setText(itemName);
        itemdesc.setText(itemDesc);
        itemprice.setText(itemPrice);
        itemcategory.setText(itemCategory);
        databaseReference = FirebaseDatabase.getInstance().getReference("SIDMenu").child(username).child(itemId);
        csvdatabasereference = FirebaseDatabase.getInstance().getReference("SIDMenu").child(username);
        cxdatabasereference = FirebaseDatabase.getInstance().getReference("SIDCxMenu").child(username);
        storageReference = FirebaseStorage.getInstance().getReference();
        csvRef = storageReference.child(username + ".csv");

        if (Vis.equals("1")){
            GetCSV.setVisibility(View.INVISIBLE);
            BulkUploadBtn.setVisibility(View.INVISIBLE);
        }

        Savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemname != null && !itemname.getText().toString().trim().isEmpty() &&
                        itemdesc != null && !itemdesc.getText().toString().trim().isEmpty() &&
                        itemprice != null && !itemprice.getText().toString().trim().isEmpty() &&
                        itemcategory != null && !itemcategory.getText().toString().trim().isEmpty()) {
                    databaseReference.child("itemId").setValue(itemId);
                    databaseReference.child("itemname").setValue(itemname.getText().toString());
                    databaseReference.child("itemdescription").setValue(itemdesc.getText().toString());
                    databaseReference.child("itemprice").setValue(itemprice.getText().toString());
                    databaseReference.child("itemcategory").setValue(itemcategory.getText().toString());
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    radioButton = findViewById(selectedId);
                    databaseReference.child("itemtype").setValue(radioButton.getText());
                    cxdatabasereference.child(itemcategory.getText().toString()).child(itemId).child("itemId").setValue(itemId);
                    cxdatabasereference.child(itemcategory.getText().toString()).child(itemId).child("itemname").setValue(itemname.getText().toString());
                    cxdatabasereference.child(itemcategory.getText().toString()).child(itemId).child("itemdescription").setValue(itemdesc.getText().toString());
                    cxdatabasereference.child(itemcategory.getText().toString()).child(itemId).child("itemprice").setValue(itemprice.getText().toString());
                    cxdatabasereference.child(itemcategory.getText().toString()).child(itemId).child("itemcategory").setValue(itemcategory.getText().toString());
                    cxdatabasereference.child(itemcategory.getText().toString()).child(itemId).child("itemtype").setValue(radioButton.getText());
                    cxdatabasereference.child(itemcategory.getText().toString()).child(itemId).child("instock").setValue("true");
                    finish();
                } else {
                    Toast.makeText(EditAddItemActivity.this, "Fill all the values first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        BulkUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(EditAddItemActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // Permission already granted
                    // Your code to read from external storage goes here
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("text/*");
                    startActivityForResult(intent, 1);
                } else {
                    // Permission not yet granted, ask the user
                    ActivityCompat.requestPermissions(EditAddItemActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }
            }
        });

        GetCSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder csvData = new StringBuilder();
                csvData.append("itemId, itemdescription, itemname, itemprice, itemtype, itemcategory\n");
                for (int i = 0; i < itemIdList.size(); i++) {
                    csvData.append(itemIdList.get(i)+",");
                    csvData.append(itemDescList.get(i)+",");
                    csvData.append(itemNameList.get(i)+",");
                    csvData.append(itemPriceList.get(i)+",");
                    csvData.append(itemTypeList.get(i)+",");
                    csvData.append(itemCategoryList.get(i)+"\n");
                }

// Get the path to the Documents directory in internal storage
                File dir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                File file = new File(dir, "data.csv");

                if (!file.exists()) {
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
                    Toast.makeText(EditAddItemActivity.this, "Success", Toast.LENGTH_SHORT).show();

// Create the email Intent
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setType("text/csv");
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Data File");
                    emailIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(EditAddItemActivity.this, BuildConfig.APPLICATION_ID + ".fileprovider", file));
                    emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    // Start the email activity
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri csvUri = data.getData();
            csvRef.putFile(csvUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(EditAddItemActivity.this, "CSV file uploaded successfully.", Toast.LENGTH_SHORT).show();
                        // Handle successful upload
                        InputStream inputStream = null;
                        try {
                            inputStream = getContentResolver().openInputStream(csvUri);
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build(); // skip the first line (headers)

                        String[] headers = {"itemId","itemdescription", "itemname", "itemprice", "itemtype", "itemcategory"}; // replace with your own headers
                        String[] nextRecord;

                        try {
                            while ((nextRecord = csvReader.readNext()) != null) {
                                for (int i = 0; i < headers.length; i++) {
                                    csvdatabasereference.child(nextRecord[0]).child(headers[i]).setValue(nextRecord[i]);
                                    if (i == 5) {
                                        cxdatabasereference.child(nextRecord[5]).child(nextRecord[0]).child(headers[0]).setValue(nextRecord[0]);
                                        cxdatabasereference.child(nextRecord[5]).child(nextRecord[0]).child(headers[1]).setValue(nextRecord[1]);
                                        cxdatabasereference.child(nextRecord[5]).child(nextRecord[0]).child(headers[2]).setValue(nextRecord[2]);
                                        cxdatabasereference.child(nextRecord[5]).child(nextRecord[0]).child(headers[3]).setValue(nextRecord[3]);
                                        cxdatabasereference.child(nextRecord[5]).child(nextRecord[0]).child(headers[4]).setValue(nextRecord[4]);
                                        cxdatabasereference.child(nextRecord[5]).child(nextRecord[0]).child(headers[5]).setValue(nextRecord[5]);
                                        cxdatabasereference.child(nextRecord[5]).child(nextRecord[0]).child("instock").setValue("true");
                                    }
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (CsvValidationException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(EditAddItemActivity.this, "Failed to upload CSV file.", Toast.LENGTH_SHORT).show();
                        // Handle failed upload
                    });
        }
    }
}