package com.example.myapplication.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class ViewTableActivityAdapter extends RecyclerView.Adapter<ViewTableActivityAdapter.MyViewHolder> {

    ArrayList<String> availibility, invoicenumber, tableid;

    public ViewTableActivityAdapter(ArrayList<String> availibility, ArrayList<String> invoicenumber, ArrayList<String> tableid) {
        this.availibility = availibility;
        this.invoicenumber = invoicenumber;
        this.tableid = tableid;
    }


    @NonNull
    @Override
    public ViewTableActivityAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_table,parent,false);
        return new ViewTableActivityAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewTableActivityAdapter.MyViewHolder holder, int position) {
        holder.tableno.setText(tableid.get(position));
        if (availibility.get(position).equals("true")) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#90EE90"));
        } else { if (availibility.get(position).equals("false")) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#FF0000"));
        } else {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#FFFF00"));
        }
        }
    }

    @Override
    public int getItemCount() {
        return availibility.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tableno;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tableno = itemView.findViewById(R.id.table_id_tv);
            cardView = itemView.findViewById(R.id.cardviewtable);
        }
    }
}
