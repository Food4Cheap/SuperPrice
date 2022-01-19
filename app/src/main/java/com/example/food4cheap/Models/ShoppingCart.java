package com.example.food4cheap.Models;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("ShoppingCart")
public class ShoppingCart extends ParseObject{
    public static final String KEY_ITEMS = "items";
    public static final String TAG = "ShoppingCart";

    public JSONArray getItems(){
        return getJSONArray(KEY_ITEMS);
    }

    public void addItem(ProductItem item){
        JSONArray items = getItems();
        if(items == null){
            items = new JSONArray();
        }
        items.put(item);
        put(KEY_ITEMS, items);
    }

    public void clearCart(){
        JSONArray items = new JSONArray();
        put(KEY_ITEMS, items);
    }

    public double getPrice() {
        final double[] totalPrice = {0};
        JSONArray cartArr = getItems();
        if(cartArr!=null)
        {
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
                            totalPrice[0] += item.getPrice() * item.getQuantity();
                        }
                    } else {
                        Log.e(TAG, "Failed to fetch items in cart", e);
                    }
                }
            });
        }

        return totalPrice[0];
    }

}
