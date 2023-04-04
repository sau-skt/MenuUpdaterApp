package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.adapter.FoodMenuEditActivityAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FoodMenuEditActivity extends AppCompatActivity {
    String username;
    DatabaseReference databaseReference;
    ArrayList<String> itemNameList = new ArrayList<>();
    ArrayList<String> itemPriceList = new ArrayList<>();
    ArrayList<String> itemDescList = new ArrayList<>();
    ArrayList<String> itemIdList = new ArrayList<>();
    ArrayList<String> itemTypeList = new ArrayList<>();
    ArrayList<String> itemCategoryList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    Button additembtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu_edit);
        username = getIntent().getStringExtra("username");
        recyclerView = findViewById(R.id.FoodMenuEditActivity_Recyclerview);
        additembtn = findViewById(R.id.FoodMenuEditActivityAdditembtn);
        layoutManager = new LinearLayoutManager(FoodMenuEditActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new FoodMenuEditActivityAdapter(itemNameList,itemPriceList,itemDescList,itemIdList,itemTypeList,itemCategoryList,username,FoodMenuEditActivity.this);
        recyclerView.setAdapter(adapter);
        databaseReference = FirebaseDatabase.getInstance().getReference("SIDMenu").child(username);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemNameList.clear();
                itemPriceList.clear();
                itemIdList.clear();
                itemDescList.clear();
                itemTypeList.clear();
                itemCategoryList.clear();
                for (DataSnapshot sidSnapshot : dataSnapshot.getChildren()) {
                    String itemid = sidSnapshot.getKey();
                    String itemName = sidSnapshot.child("itemname").getValue(String.class);
                    String itemDesc = sidSnapshot.child("itemdescription").getValue(String.class);
                    String itemPrice = sidSnapshot.child("itemprice").getValue(String.class);
                    String itemType = sidSnapshot.child("itemtype").getValue(String.class);
                    String itemCategory = sidSnapshot.child("itemcategory").getValue(String.class);
                    itemNameList.add(itemName);
                    itemDescList.add(itemDesc);
                    itemPriceList.add(itemPrice);
                    itemIdList.add(itemid);
                    itemTypeList.add(itemType);
                    itemCategoryList.add(itemCategory);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error reading data: " + databaseError.getMessage());
            }
        });

        additembtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoodMenuEditActivity.this, EditAddItemActivity.class);
                if (itemIdList.size() > 0) {
                    intent.putExtra("itemId", String.valueOf(Integer.valueOf(itemIdList.get(itemIdList.size() - 1)) + 1));
                    intent.putExtra("itemIdList",itemIdList);
                    intent.putExtra("itemNameList",itemNameList);
                    intent.putExtra("itemPriceList",itemPriceList);
                    intent.putExtra("itemDescList",itemDescList);
                    intent.putExtra("itemTypeList",itemTypeList);
                    intent.putExtra("itemCategoryList",itemCategoryList);
                    intent.putExtra("visibility","2");
                } else {
                    intent.putExtra("itemId", String.valueOf(1));
                }
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
    }
}