package com.example.thebloomroom.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.thebloomroom.R;
import com.example.thebloomroom.models.DBoperations;
import com.example.thebloomroom.models.Order;

import java.util.ArrayList;

public class ManageOrders extends AppCompatActivity {

    ListView orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_orders);

        orderList = findViewById(R.id.orderList);

        DBoperations db = new DBoperations(this);

        ArrayList<Order> orders = db.getAllOrders();

        if(orders != null) {
            ArrayList<String> list = new ArrayList<>();
            String data = "";

            for(int i = 0; i < orders.size(); i++){
                data = "********** Order id: " + orders.get(i).getOrderID() + " *************\n" +
                        "User Name: " + orders.get(i).getUserName() + "\n" +
                        "User Email: " + orders.get(i).getUserEmail() + "\n" +
                        "User Address: " + orders.get(i).getUserAddress() + "\n" +
                        "Order Date: " + orders.get(i).getOrderDate() + "\n" +
                        "Items: \n" + orders.get(i).getItems() ;
                list.add(data);
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, list);

            orderList.setAdapter(arrayAdapter);
        }

    }


}