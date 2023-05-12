package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CancelOrderActivity extends AppCompatActivity {
    String username, tableId, invoicenumber;
    DatabaseReference tablereference, cxorderreference, SIDreference;
    TextView total, calculation, item_name_list, item_qty_list, item_price_list, invoicenumbertv, datetv;
    ArrayList<String> ItemNameList = new ArrayList<>();
    ArrayList<String> ItemQtyList = new ArrayList<>();
    ArrayList<String> ItemPriceList = new ArrayList<>();
    int qtylist = 0;
    Button cancelorderbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_order);
        username = getIntent().getStringExtra("username");
        tableId = getIntent().getStringExtra("tableId");
        total = findViewById(R.id.item_total_textview);
        calculation = findViewById(R.id.cal_textview);
        item_name_list = findViewById(R.id.item_name_list_textview);
        item_qty_list = findViewById(R.id.item_qty_list_textview);
        item_price_list = findViewById(R.id.item_price_list_textview);
        invoicenumbertv = findViewById(R.id.invoice_number_textview);
        datetv = findViewById(R.id.invoice_date_textview);
        cancelorderbtn = findViewById(R.id.cancel_order_btn);
        tablereference = FirebaseDatabase.getInstance().getReference("TableInfo").child(username).child(tableId);
        cxorderreference = FirebaseDatabase.getInstance().getReference("CxOrder").child(username);
        SIDreference = FirebaseDatabase.getInstance().getReference("SID").child(username);
        total.append("\n\nSub-Total\n");

        tablereference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                invoicenumber = snapshot.child("invoicenumber").getValue(String.class);
                invoicenumbertv.setText(invoicenumber);
                cxorderreference.child(invoicenumber).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ItemNameList.clear();
                        ItemQtyList.clear();
                        ItemPriceList.clear();
                        String subtotal = snapshot.child("invoicesubtotal").getValue(String.class);
                        String invtotal = snapshot.child("invoicetotal").getValue(String.class);
                        String date = snapshot.child("invoicedate").getValue(String.class);
                        datetv.setText("Invoice date - " + date);
                        int index = subtotal.indexOf("Total ");
                        if (index != -1) {
                            String totalValue = subtotal.substring(index + 6);
                            calculation.append("\n\n" + totalValue + "\n");
                        }
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String name = dataSnapshot.child("itemname").getValue(String.class);
                            String qty = dataSnapshot.child("itemqty").getValue(String.class);
                            String price = dataSnapshot.child("itemtotal").getValue(String.class);
                            String taxname = dataSnapshot.getKey();
                            String taxrate = dataSnapshot.child("rate").getValue(String.class);
                            String taxamt = dataSnapshot.child("taxamount").getValue(String.class);
                            if (name != null && !name.isEmpty()) {
                                ItemNameList.add(name);
                                ItemQtyList.add(qty);
                                ItemPriceList.add(price);
                            } else if (taxrate != null && !taxrate.isEmpty()) {
                                    total.append(taxname + " " + taxrate + "\n");
                                    calculation.append(taxamt + "\n");
                            }
                        }
                        total.append("Total");
                        calculation.append(invtotal);
                        for (int i = 0; i < ItemNameList.size(); i++) {
                            item_name_list.append("\n\n" + ItemNameList.get(i));
                        }
                        item_name_list.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                // Get the current line count
                                int[] blankLineCounts = new int[item_name_list.getLineCount()];

                                for (int i = 0; i < item_name_list.getLineCount(); i++) {
                                    // Get the current line's text
                                    int lineStart = item_name_list.getLayout().getLineStart(i);
                                    int lineEnd = item_name_list.getLayout().getLineEnd(i);
                                    String lineText = item_name_list.getText().subSequence(lineStart, lineEnd).toString();

                                    // Check if the line is blank

                                    if (lineText.trim().isEmpty()) {
                                        // Increment the blank line counter for this line's index
                                        blankLineCounts[i]++;
                                    }
                                }

// Print the blank line counts for each line
                                for (int i = 0; i < blankLineCounts.length - 1; i++) {
                                    if (blankLineCounts[i] == 0 && blankLineCounts[i+1] == 1) {
                                        item_qty_list.append("\n\n");
                                        item_price_list.append("\n\n");
                                    }
                                    if (blankLineCounts[i] == 0 && blankLineCounts[i+1] == 0) {
                                        item_qty_list.append("\n");
                                        item_price_list.append("\n");
                                    }
                                    if (blankLineCounts[i] == 1 && blankLineCounts[i+1] == 0) {
                                        item_qty_list.append(ItemQtyList.get(qtylist));
                                        item_price_list.append(ItemPriceList.get(qtylist));
                                        qtylist++;
                                    }
                                }


                                // Remove the listener so it doesn't keep getting called
                                item_name_list.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        cancelorderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tablereference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        invoicenumber = snapshot.child("invoicenumber").getValue(String.class);
                        SIDreference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String inv = snapshot.child("invoicenumber").getValue(String.class);
                                int invamt = Integer.parseInt(inv) - 1;
                                SIDreference.child("invoicenumber").setValue(String.valueOf(invamt));
                                cxorderreference.child(invoicenumber).child("order_status").setValue("Cancelled");
                                tablereference.child("availibility").setValue("true");
                                tablereference.child("invoicenumber").setValue("0");
                                tablereference.child("totalamount").setValue("0");
                                finish();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}