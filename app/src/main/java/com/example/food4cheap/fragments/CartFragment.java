package com.example.food4cheap.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.food4cheap.Models.ProductItem;
import com.example.food4cheap.R;
import com.example.food4cheap.Models.ShoppingCart;
import com.example.food4cheap.Adapters.ShoppingCartItemsAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CartFragment extends Fragment {

    public static final String TAG = "CartFragment";

    RecyclerView rvItems;
    List<ProductItem> itemsCart;
    List<ProductItem> copyItemsCart; //Used to update cart by checking for changes
    ShoppingCartItemsAdapter shoppingCartItemsAdapter;
    TextView tvCart;
    double totalPrice = 0;

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvCart = view.findViewById(R.id.tvCart);
        ShoppingCart shoppingCart = (ShoppingCart) ParseUser.getCurrentUser().getParseObject("shoppingCart");
        tvCart.setText("Total: $" + shoppingCart.getPrice());
        rvItems = view.findViewById(R.id.rvItems);
        itemsCart = new ArrayList<ProductItem>();
        copyItemsCart = new ArrayList<ProductItem>();
        ShoppingCartItemsAdapter shoppingCartItemsAdapter = new ShoppingCartItemsAdapter(getContext(), itemsCart, tvCart);
        rvItems.setLayoutManager(new LinearLayoutManager(getContext()));
        rvItems.setAdapter(shoppingCartItemsAdapter);
        fillModel(shoppingCartItemsAdapter);

        }

        public void fillModel(ShoppingCartItemsAdapter shoppingCartItemsAdapter){
            List<ProductItem> cartItems = new ArrayList<ProductItem>();
            ShoppingCart cart = (ShoppingCart) ParseUser.getCurrentUser().getParseObject("shoppingCart");
            JSONArray cartArr = cart.getItems();
            if(cartArr!=null) {
                ParseQuery<ProductItem> query = ParseQuery.getQuery("ProductItem");
                ArrayList<String> collection = new ArrayList<String>();
                for (int i = 0; i < cartArr.length(); i++) {
                    try {
                        collection.add(cartArr.getJSONObject(i).getString("objectId"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                query.whereContainedIn("objectId", collection);
                query.findInBackground(new FindCallback<ProductItem>() {
                    @Override
                    public void done(List<ProductItem> objects, ParseException e) {
                        if (e == null) {
                            for (ProductItem item : objects) {
                                itemsCart.add(item);
                                totalPrice += item.getPrice() * item.getQuantity();
                            }
                            Collections.sort(itemsCart, new Comparator<ProductItem>() {
                                @Override
                                public int compare(ProductItem productItem, ProductItem t1) {
                                    return productItem.getStoreAddress().compareTo(t1.getStoreAddress());
                                }
                            });
                            itemsCart = addSeparators(itemsCart);
                            shoppingCartItemsAdapter.notifyDataSetChanged();
                            tvCart.setText("Total: $" + String.format("%.2f", totalPrice));
                        } else {
                            Log.e(TAG, "Failed to fetch items in cart", e);
                        }
                    }
                });
            }
        }
        public List<ProductItem> addSeparators(List<ProductItem> items){
        if(items.size()!=0)
        {
            String currentStoreName = items.get(0).getStoreAddress();
            String currentStoreBrand=items.get(0).getStore();
            double totalPrice = 0;
            for(int i = 0; i < items.size(); i++){
                if(currentStoreName.compareTo(items.get(i).getStoreAddress()) != 0){
                    ProductItem separator = new ProductItem();
                    separator.setStoreAddress(currentStoreName);
                    separator.setPrice(totalPrice);
                    separator.setStore(currentStoreBrand);
                    separator.setItemName("STORE TOTAL");
                    separator.setQuantity(0);
                    currentStoreName = items.get(i).getStoreAddress();
                    currentStoreBrand = items.get(i).getStore();
                    totalPrice = 0;
                    items.add(i, separator);
                }
                else{
                    totalPrice += items.get(i).getPrice() *  items.get(i).getQuantity();
                }
            }
            ProductItem end = new ProductItem();
            end.setStoreAddress(currentStoreName);
            end.setPrice(totalPrice);
            end.setStore(currentStoreBrand);
            end.setItemName("STORE TOTAL");
            end.setQuantity(0);
            items.add(end);
            for(int i = 0; i < items.size(); i++){
                copyItemsCart.add(items.get(i));
            }
        }
            return items;
    }

}