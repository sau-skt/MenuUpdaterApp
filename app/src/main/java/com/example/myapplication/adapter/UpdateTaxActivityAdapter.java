package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UpdateTaxActivityAdapter extends RecyclerView.Adapter<UpdateTaxActivityAdapter.MyViewHolder> {

    ArrayList<String> taxname, taxpercent, uniqueIds;
    DatabaseReference taxdata;
    String username;

    public UpdateTaxActivityAdapter(ArrayList<String> taxname, ArrayList<String> taxpercent, ArrayList<String> uniqueIds, String username) {
        this.taxname = taxname;
        this.taxpercent = taxpercent;
        this.uniqueIds = uniqueIds;
        this.username = username;
    }

    @NonNull
    @Override
    public UpdateTaxActivityAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_tax_name,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpdateTaxActivityAdapter.MyViewHolder holder, int position) {
        holder.taxnametv.setText(taxname.get(position));
        holder.taxpercenttv.setText(taxpercent.get(position));
        holder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taxdata = FirebaseDatabase.getInstance().getReference("TaxData").child(username).child(uniqueIds.get(position));
                taxdata.removeValue();
            }
        });
    }

    @Override
    public int getItemCount() {
        return taxname.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView taxnametv, taxpercenttv;
        ImageView deletebtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            taxnametv = itemView.findViewById(R.id.tax_name);
            taxpercenttv = itemView.findViewById(R.id.tax_percent);
            deletebtn = itemView.findViewById(R.id.tax_info_delete_btn);
        }
    }
}
