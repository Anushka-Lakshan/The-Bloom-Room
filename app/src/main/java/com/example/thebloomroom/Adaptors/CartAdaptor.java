package com.example.thebloomroom.Adaptors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thebloomroom.R;
import com.example.thebloomroom.models.CartItem;

import java.util.ArrayList;


public class CartAdaptor extends BaseAdapter {
    Context context;
    ArrayList<CartItem> cartItems = new ArrayList<>();

    public CartAdaptor(Context context, ArrayList<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }


    @Override
    public int getCount() {
        return cartItems.size();
    }

    @Override
    public Object getItem(int i) {
        return cartItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        TextView pro_name, pro_price, pro_qnt;
        ImageView close;
        Button plus, minus;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view1 = inflater.inflate(R.layout.custom_cart_item, viewGroup, false);

        pro_name = view1.findViewById(R.id.CI_name);
        pro_price = view1.findViewById(R.id.CI_price);
        pro_qnt = view1.findViewById(R.id.CI_qnt);
        close = view1.findViewById(R.id.IC_close);
        plus = view1.findViewById(R.id.IC_plus);
        minus = view1.findViewById(R.id.IC_minus);
        close.setTag(i);
        plus.setTag(i);
        minus.setTag(i);

        CartItem cartItem = cartItems.get(i);

        pro_name.setText(cartItem.getProductName());
        pro_price.setText( "Unit Proice: Rs." + String.valueOf(cartItem.getPrice()));
        pro_qnt.setText(String.valueOf(cartItem.getQuantity()));

        return view1;
    }
}
