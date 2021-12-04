package com.example.food4cheap;

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

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCartItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int NORMAL=33;
    public static final int STORE_SEPARATOR = 32;
    Context context;
    List<ProductItem> productItems;

    public ShoppingCartItemsAdapter(Context c, List<ProductItem> i){
        context = c;
        productItems = i;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==NORMAL)
        {
            View productItemView = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
            return new ViewHolder(productItemView);
        }
        else{
            View productItemView = LayoutInflater.from(context).inflate(R.layout.cart_total, parent, false);
            return new ViewHolder2(productItemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        ProductItem item = productItems.get(position);
        if(holder.getItemViewType()==STORE_SEPARATOR)
        {
            ViewHolder2 temp=(ViewHolder2) holder;
            temp.bind(item);
        }
        else{
            ViewHolder temp=(ViewHolder) holder;
            temp.bind(item);
        }

    }


    public void clearAll(){
        productItems = new ArrayList<ProductItem>();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return productItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(productItems.get(position).getItemName().equals("STORE TOTAL"))
        {
            return STORE_SEPARATOR;
        }
        return NORMAL;

    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivImage;
        TextView tvItemName;
        TextView tvPrice;
        TextView tvAddress;
        TextView tvQuantity;
        Button btnIncrease;
        Button btnDecrease;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.imageView3);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
        }

        public void bind(ProductItem item){
            Glide.with(context).load(item.getImageUrl()).into(ivImage);
            tvItemName.setText(item.getItemName());
            tvPrice.setText("" + item.getPrice());
            tvAddress.setText(item.getStoreAddress());
            tvQuantity.setText("" + item.getQuantity());
            btnIncrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item.setQuantity(item.getQuantity() + 1);
                    tvQuantity.setText("" + item.getQuantity());
                }
            });
            btnDecrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(item.getQuantity() > 1){
                        item.setQuantity(item.getQuantity() - 1);
                        tvQuantity.setText("" + item.getQuantity());
                    }
                    else{
                        Toast.makeText(context, "Item will be removed upon updating cart", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            if(item.getItemName() == "STORE TOTAL"){
                btnIncrease.setVisibility(View.INVISIBLE);
                btnDecrease.setVisibility(View.INVISIBLE);
                tvQuantity.setVisibility(View.INVISIBLE);
            }
        }
    }
    class ViewHolder2 extends RecyclerView.ViewHolder
    {
        TextView tvStoreName;
        TextView tvStoreAddress;
        TextView tvTotalPrice;
        public ViewHolder2(@NonNull @NotNull View itemView) {
            super(itemView);
            tvStoreAddress=itemView.findViewById(R.id.tvStoreAddress);
            tvStoreName=itemView.findViewById(R.id.tvStoreName);
            tvTotalPrice=itemView.findViewById(R.id.tvTotalPrice);
        }
        public void bind(ProductItem item)
        {
            DecimalFormat numberFormat = new DecimalFormat("#.00");
            tvStoreAddress.setText(item.getStoreAddress());
            tvStoreName.setText(item.getStore());
            tvTotalPrice.setText("$ "+numberFormat.format(item.getPrice()));
        }
    }
}
