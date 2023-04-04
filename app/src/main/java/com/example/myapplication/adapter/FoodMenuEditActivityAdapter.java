package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.EditAddItemActivity;
import com.example.myapplication.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FoodMenuEditActivityAdapter extends RecyclerView.Adapter<FoodMenuEditActivityAdapter.MyViewHolder> {

    ArrayList<String> itemNameList, itemDescList, itemIdList;
    ArrayList<String> itemPriceList, itemTypeList, itemCategoryList;
    String username;
    DatabaseReference databaseReference;

    Context context;
    ArrayList<String> daysArrayList = new ArrayList<>();

    public FoodMenuEditActivityAdapter(ArrayList<String> itemNameList,ArrayList<String> itemPriceList,ArrayList<String> itemDescList,ArrayList<String> itemIdList,ArrayList<String> itemTypeList,ArrayList<String> itemCategoryList, String username, Context context) {
        this.itemNameList = itemNameList;
        this.itemDescList = itemDescList;
        this.itemPriceList = itemPriceList;
        this.itemCategoryList = itemCategoryList;
        this.context = context;
        this.username = username;
        this.itemIdList = itemIdList;
        this.itemTypeList = itemTypeList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item_display,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.ItemName.setText(itemNameList.get(position));
        holder.ItemDesc.setText(itemDescList.get(position));
        holder.ItemPrice.setText("\u20B9 " + itemPriceList.get(position));
        holder.itemCategory.setText(itemCategoryList.get(position));
        holder.ItemId.setText(itemIdList.get(position));
        if (itemTypeList.get(position).equals("Veg")) {
            holder.itemtypebtn.setImageResource(R.drawable.vegetarian_food);
        } else {
            holder.itemtypebtn.setImageResource(R.drawable.nonvegetarian_food);
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditAddItemActivity.class);
                intent.putExtra("itemname",holder.ItemName.getText().toString());
                intent.putExtra("itemdesc",holder.ItemDesc.getText().toString());
                intent.putExtra("itemprice",itemPriceList.get(position).toString());
                intent.putExtra("itemcategory",itemCategoryList.get(position).toString());
                intent.putExtra("username",username);
                intent.putExtra("itemId",itemIdList.get(position));
                intent.putExtra("visibility","1");
                context.startActivity(intent);
            }
        });
        holder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference = FirebaseDatabase.getInstance().getReference("SIDMenu").child(username).child(itemIdList.get(position));
                databaseReference.removeValue();
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemNameList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView ItemName, ItemDesc, ItemPrice, ItemId, itemCategory;
        CardView cardView;
        ImageView deletebtn, itemtypebtn;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ItemName = itemView.findViewById(R.id.itemname);
            ItemDesc = itemView.findViewById(R.id.itemdesc);
            ItemPrice = itemView.findViewById(R.id.itemprice);
            ItemId = itemView.findViewById(R.id.itemId);
            itemCategory = itemView.findViewById(R.id.itemcategory);
            cardView = itemView.findViewById(R.id.cardviewtouch);
            deletebtn = itemView.findViewById(R.id.itemdeletebtn);
            itemtypebtn = itemView.findViewById(R.id.itemtype);
        }
    }
}