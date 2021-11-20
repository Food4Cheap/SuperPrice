package com.example.food4cheap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductItemsAdapter extends RecyclerView.Adapter<ViewHolder>{

    Context context;
    List<ProductItem> productItems;

    public ProductItemsAdapter(Context c, List<ProductItem> items){
        context = c;
        productItems = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View productItemView = LayoutInflater.from(context).inflate(R.layout.search_item, parent, false);
        return new ViewHolder(productItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductItem item = productItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return productItems.size();
    }
}

class ViewHolder extends RecyclerView.ViewHolder{

    ImageView ivProduct;
    TextView tvItemName;
    TextView tvPrice;
    TextView tvAddress;
    Button btnAdd;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        ivProduct = itemView.findViewById(R.id.imageView3);
        tvItemName = itemView.findViewById(R.id.tvItemName);
        tvPrice = itemView.findViewById(R.id.tvPrice);
        tvAddress = itemView.findViewById(R.id.tvAddress);
        btnAdd = itemView.findViewById(R.id.btnAdd);
    }

    public void bind(ProductItem item) {
        //Will display image after everything else works

        tvItemName.setText(item.getItemName());
        tvPrice.setText("" + item.getPrice());
        tvAddress.setText(item.getStoreAddress());
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Save item to database / add to shopping cart
            }
        });

    }
}