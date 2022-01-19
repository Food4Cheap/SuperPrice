package com.example.food4cheap.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.food4cheap.Models.ProductItem;
import com.example.food4cheap.R;
import com.example.food4cheap.Models.ShoppingCart;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class ProductItemsAdapter extends RecyclerView.Adapter<ProductItemsAdapter.ViewHolder>{

    Context context;
    List<ProductItem> productItems;
    List<ProductItem> shoppingCart;

    public ProductItemsAdapter(Context c, List<ProductItem> items, List<ProductItem> s){
        context = c;
        productItems = items;
        shoppingCart = s;
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
            btnAdd = itemView.findViewById(R.id.btnQuantity);
        }

        public void bind(ProductItem item) {
            //Will display image after everything else works

            tvItemName.setText(item.getItemName());
            tvPrice.setText("" + item.getPrice());
            tvAddress.setText(item.getStoreAddress());
            //Glide.with(context).load(movie.getPosterPath()).into(ivPoster);
            Glide.with(context).load(item.getImageUrl()).into(ivProduct);
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean inCart = false;
                    int posInCart = 0;
                    for(int i = 0 ; i < shoppingCart.size(); i++){
                        if(item.getUPC().compareTo(shoppingCart.get(i).getUPC()) == 0){
                            inCart = true;
                            posInCart = i;
                            break;
                        }
                    }
                    if(inCart == true){
                        shoppingCart.get(posInCart).setQuantity(shoppingCart.get(posInCart).getQuantity() + 1);
                        shoppingCart.get(posInCart).saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Toast.makeText(context, "Quantity increased", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else{
                        item.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e == null){
                                    ShoppingCart shoppingCart = (ShoppingCart) ParseUser.getCurrentUser().getParseObject("shoppingCart");
                                    shoppingCart.addItem(item);
                                    shoppingCart.saveInBackground();
                                    Toast.makeText(context, "Item added to cart", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });

        }
    }

}


