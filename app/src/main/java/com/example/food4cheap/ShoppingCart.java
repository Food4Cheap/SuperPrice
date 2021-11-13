package com.example.food4cheap;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;

@ParseClassName("shoppingCart")
public class ShoppingCart extends ParseObject{
    public static final String KEY_ITEMS = "items";

    public JSONArray getItems(){
        return getJSONArray(KEY_ITEMS);
    }

    public void addItem(ProductItem item){
        JSONArray items = getItems();
        items.put(item);
        put(KEY_ITEMS, items);
    }

    public void clearCart(){
        JSONArray items = new JSONArray();
        put(KEY_ITEMS, items);
    }
}
