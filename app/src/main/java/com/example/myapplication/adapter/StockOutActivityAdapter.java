package com.example.myapplication.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StockOutActivityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int CATEGORY_VIEW_TYPE = 0;
    private static final int ITEM_VIEW_TYPE = 1;

    private ArrayList<String> item_or_category, CategoryList, ItemsList,ItemsPrice, ItemsDesc, ItemsType, ItemsIdList, ItemsStock, ItemsCategoryList;
    String username;
    DatabaseReference cxmenu;

    public StockOutActivityAdapter(ArrayList<String> item_or_category, ArrayList<String> categoryList, ArrayList<String> itemsList, ArrayList<String> itemsPrice, ArrayList<String> itemsDesc, ArrayList<String> itemsType, ArrayList<String> itemsIdList, ArrayList<String> itemsStock, ArrayList<String> itemsCategoryList, String username) {
        this.item_or_category = item_or_category;
        CategoryList = categoryList;
        ItemsList = itemsList;
        ItemsPrice = itemsPrice;
        ItemsDesc = itemsDesc;
        ItemsType = itemsType;
        ItemsIdList = itemsIdList;
        ItemsStock = itemsStock;
        ItemsCategoryList = itemsCategoryList;
        this.username = username;
        cxmenu = FirebaseDatabase.getInstance().getReference("SIDCxMenu").child(username);
    }

    @Override
    public int getItemViewType(int position) {
        if (item_or_category.get(position).equals("Category")){
            return CATEGORY_VIEW_TYPE;
        } else {
            return ITEM_VIEW_TYPE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case CATEGORY_VIEW_TYPE:
                View categoryView = inflater.inflate(R.layout.cardview_stock_category_display,parent,false);
                return new CategoryViewHolder(categoryView);
            case ITEM_VIEW_TYPE:
                View itemView = inflater.inflate(R.layout.cardview_stock_item_display,parent,false);
                return new ItemViewHolder(itemView);
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).Itemname.setText(ItemsList.get(position));
            ((ItemViewHolder) holder).ItemPrice.setText("\u20B9 " + ItemsPrice.get(position));
            ((ItemViewHolder) holder).ItemDesc.setText(ItemsDesc.get(position));
            ((ItemViewHolder) holder).cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            if (ItemsType.get(position).equals("Veg")) {
                ((ItemViewHolder) holder).ItemType.setImageResource(R.drawable.vegetarian_food);
            } else {
                ((ItemViewHolder) holder).ItemType.setImageResource(R.drawable.nonvegetarian_food);
            }
            if (ItemsStock.get(position).equals("true")) {
                ((ItemViewHolder) holder).cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            } else {
                ((ItemViewHolder) holder).cardView.setCardBackgroundColor(Color.parseColor("#FF0000"));
            }
            ((ItemViewHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cxmenu.child(ItemsCategoryList.get(position)).child(ItemsIdList.get(position)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String stock_status = snapshot.child("instock").getValue(String.class);
                            if (stock_status.equals("true")) {
                                ((ItemViewHolder) holder).cardView.setCardBackgroundColor(Color.parseColor("#FF0000"));
                                cxmenu.child(ItemsCategoryList.get(position)).child(ItemsIdList.get(position)).child("instock").setValue("false");
                            } else {
                                ((ItemViewHolder) holder).cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                                cxmenu.child(ItemsCategoryList.get(position)).child(ItemsIdList.get(position)).child("instock").setValue("true");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
        } else if (holder instanceof CategoryViewHolder) {
            ((CategoryViewHolder) holder).CategoryName.setText(CategoryList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return item_or_category.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView Itemname, ItemDesc, ItemPrice;
        ImageView ItemType;
        CardView cardView;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            Itemname = itemView.findViewById(R.id.itemnamestock);
            ItemDesc = itemView.findViewById(R.id.itemdescstock);
            ItemPrice = itemView.findViewById(R.id.itempricestock);
            ItemType = itemView.findViewById(R.id.itemtypestock);
            cardView = itemView.findViewById(R.id.cardviewstocktouch);
        }
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder{
        TextView CategoryName;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            CategoryName = itemView.findViewById(R.id.category_name_stock);
        }
    }

}
