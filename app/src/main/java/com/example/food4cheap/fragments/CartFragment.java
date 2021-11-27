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

import com.example.food4cheap.ProductItem;
import com.example.food4cheap.ProductItemsAdapter;
import com.example.food4cheap.R;
import com.example.food4cheap.ShoppingCart;
import com.google.gson.JsonArray;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CartFragment extends Fragment {

    public static final String TAG = "CartFragment";

    RecyclerView rvItems;
    List<ProductItem> itemsCart;
    ProductItemsAdapter productItemsAdapter;

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
        rvItems = view.findViewById(R.id.rvItems);
        itemsCart = new ArrayList<ProductItem>();
        ProductItemsAdapter productItemsAdapter = new ProductItemsAdapter(getContext(), itemsCart);
        rvItems.setLayoutManager(new LinearLayoutManager(getContext()));
        rvItems.setAdapter(productItemsAdapter);
        /*
        List<ProductItem> cartItems = new ArrayList<ProductItem>();
        ShoppingCart cart = (ShoppingCart) ParseUser.getCurrentUser().getParseObject("shoppingCart");
        JSONArray cartArr = cart.getItems();

        ParseQuery<ProductItem> query = ParseQuery.getQuery("ProductItem");
        ArrayList<String> collection = new ArrayList<String>();
        for(int i = 0; i < cartArr.length(); i++){
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
                if(e == null){
                    for(ProductItem item : objects){
                        itemsCart.add(item);
                    }
                    productItemsAdapter.notifyDataSetChanged();
                }
                else{
                    Log.e(TAG, "Failed to fetch items in cart", e);
                }
            }
        }); */
        fillModel(productItemsAdapter);

        }

        //For some reason crashes when calling the method but if i paste the code it works. Says that the productItemsAdapter is a null object reference not really sure why
        public void fillModel(ProductItemsAdapter productItemsAdapter){
            List<ProductItem> cartItems = new ArrayList<ProductItem>();
            ShoppingCart cart = (ShoppingCart) ParseUser.getCurrentUser().getParseObject("shoppingCart");
            JSONArray cartArr = cart.getItems();

            ParseQuery<ProductItem> query = ParseQuery.getQuery("ProductItem");
            ArrayList<String> collection = new ArrayList<String>();
            for(int i = 0; i < cartArr.length(); i++){
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
                    if(e == null){
                        for(ProductItem item : objects){
                            itemsCart.add(item);
                        }
                        productItemsAdapter.notifyDataSetChanged();
                    }
                    else{
                        Log.e(TAG, "Failed to fetch items in cart", e);
                    }
                }
            });
        }
    }