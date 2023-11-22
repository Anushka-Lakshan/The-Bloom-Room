package com.example.thebloomroom.Adaptors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thebloomroom.R;
import com.example.thebloomroom.models.Product;

import java.util.ArrayList;

public class GridAdaptor extends BaseAdapter {

    Context context;
    ArrayList<Product> products = new ArrayList<>();

    LayoutInflater inflater;

    public GridAdaptor(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.product_grid_item, viewGroup, false);

            holder.productImage = view.findViewById(R.id.gridImage);
            holder.productName = view.findViewById(R.id.gridName);
            holder.productPrice = view.findViewById(R.id.gridPrice);
            holder.productID = view.findViewById(R.id.gridProId);
            holder.productCat = view.findViewById(R.id.gridCat);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.productID.setText(String.valueOf(products.get(i).getId()));
        holder.productName.setText(products.get(i).getName());
        holder.productPrice.setText("Rs. " + String.valueOf(products.get(i).getPrice()));
        holder.productCat.setText(String.valueOf(products.get(i).getCategoryId()));

        Bitmap bitmap = BitmapFactory.decodeByteArray(products.get(i).getImage(), 0, products.get(i).getImage().length);
        holder.productImage.setImageBitmap(bitmap);

        return view;
    }

    static class ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView productPrice;
        TextView productID;
        TextView productCat;
    }

}
