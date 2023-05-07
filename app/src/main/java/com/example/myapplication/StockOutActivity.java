package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.myapplication.adapter.StockOutActivityAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StockOutActivity extends AppCompatActivity {
    String username;
    DatabaseReference cxmenureference, categoryreference;
    ArrayList<String> CategoryList = new ArrayList<>();
    ArrayList<String> ItemsList = new ArrayList<>();
    ArrayList<String> ItemsCategorylist = new ArrayList<>();
    ArrayList<String> ItemsPriceList = new ArrayList<>();
    ArrayList<String> ItemsDescList = new ArrayList<>();
    ArrayList<String> ItemsTypeList = new ArrayList<>();
    ArrayList<String> ItemsIdList = new ArrayList<>();
    ArrayList<String> Item_Or_Category = new ArrayList<>();
    ArrayList<String> ItemsStock = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_out);
        username = getIntent().getStringExtra("username");
        recyclerView = findViewById(R.id.stock_out_rv);
        categoryreference = FirebaseDatabase.getInstance().getReference("SIDCxMenu").child(username);
        cxmenureference = FirebaseDatabase.getInstance().getReference("SIDCxMenu").child(username);
        layoutManager = new LinearLayoutManager(StockOutActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new StockOutActivityAdapter(Item_Or_Category, CategoryList, ItemsList, ItemsPriceList, ItemsDescList, ItemsTypeList, ItemsIdList, ItemsStock, ItemsCategorylist, username);
        recyclerView.setAdapter(adapter);

        categoryreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CategoryList.clear();
                ItemsList.clear();
                ItemsCategorylist.clear();
                ItemsPriceList.clear();
                ItemsDescList.clear();
                ItemsTypeList.clear();
                ItemsIdList.clear();
                Item_Or_Category.clear();
                ItemsStock.clear();
                for (DataSnapshot categorysnapshot : dataSnapshot.getChildren()){
                    String category = categorysnapshot.getKey();
                    cxmenureference.child(category).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ItemsCategorylist.add(category);
                            ItemsList.add(category);
                            CategoryList.add(category);
                            ItemsPriceList.add(category);
                            ItemsDescList.add(category);
                            ItemsTypeList.add(category);
                            ItemsIdList.add(category);
                            Item_Or_Category.add("Category");
                            ItemsStock.add(category);
                            for (DataSnapshot itemsnapshot : snapshot.getChildren()){
                                String itemId = itemsnapshot.getKey();
                                String itemName = itemsnapshot.child("itemname").getValue(String.class);
                                String itemcategoryname = itemsnapshot.child("itemcategory").getValue(String.class);
                                String itemPrice = itemsnapshot.child("itemprice").getValue(String.class);
                                String itemDesc = itemsnapshot.child("itemdescription").getValue(String.class);
                                String itemType = itemsnapshot.child("itemtype").getValue(String.class);
                                String itemStock = itemsnapshot.child("instock").getValue(String.class);
                                ItemsCategorylist.add(itemcategoryname);
                                ItemsList.add(itemName);
                                CategoryList.add(itemName);
                                ItemsPriceList.add(itemPrice);
                                ItemsDescList.add(itemDesc);
                                ItemsTypeList.add(itemType);
                                ItemsIdList.add(itemId);
                                Item_Or_Category.add("Item");
                                ItemsStock.add(itemStock);
                            }
                            Log.e("QWE",String.valueOf(ItemsStock));
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}