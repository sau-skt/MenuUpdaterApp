package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.CancelOrderActivity;
import com.example.myapplication.CollectPaymentActivity;
import com.example.myapplication.OptionActivity;
import com.example.myapplication.R;
import com.example.myapplication.StockOutActivity;

import java.util.ArrayList;

public class ViewTableActivityAdapter extends RecyclerView.Adapter<ViewTableActivityAdapter.MyViewHolder> {

    ArrayList<String> availibility, invoicenumber, tableid, totalamount;
    String username;

    public ViewTableActivityAdapter(ArrayList<String> availibility, ArrayList<String> invoicenumber, ArrayList<String> tableid, ArrayList<String> totalamount, String username) {
        this.availibility = availibility;
        this.invoicenumber = invoicenumber;
        this.tableid = tableid;
        this.totalamount = totalamount;
        this.username = username;
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
            holder.collectpayment.setText(totalamount.get(position));
        } else { if (availibility.get(position).equals("false")) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#FF0000"));
            holder.collectpayment.setText(totalamount.get(position));
        } else {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#FFFF00"));
            holder.collectpayment.setText(totalamount.get(position));
        }
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.cardView.getCardBackgroundColor().getDefaultColor() == Color.parseColor("#FFFF00")){
                    Intent intent = new Intent(holder.itemView.getContext(), CollectPaymentActivity.class);
                    intent.putExtra("username",username);
                    intent.putExtra("tableId", tableid.get(position));
                    holder.itemView.getContext().startActivity(intent);
                }
                if ((holder.cardView.getCardBackgroundColor().getDefaultColor() == Color.parseColor("#FF0000"))) {
                    Intent intent = new Intent(holder.itemView.getContext(), CancelOrderActivity.class);
                    intent.putExtra("username",username);
                    intent.putExtra("tableId", tableid.get(position));
                    holder.itemView.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return availibility.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tableno, collectpayment;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tableno = itemView.findViewById(R.id.table_id_tv);
            cardView = itemView.findViewById(R.id.cardviewtable);
            collectpayment = itemView.findViewById(R.id.collect_payment_tv);
        }
    }
}
