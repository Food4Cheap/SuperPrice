package com.example.food4cheap.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;




@ParseClassName("ProductItem")
public class ProductItem extends ParseObject{
    public static final String KEY_UPC = "UPC";
    public static final String KEY_IMAGE_URL = "imageURL";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_PRICE = "price";
    public static final String KEY_QUANTITY = "quantity";
    public static final String KEY_STORE = "store";
    public static final String KEY_ITEM_NAME = "itemName";
    public static final String KEY_STORE_ADDRESS = "storeAddress";

    //Empty Constructor for Parcelable
    public ProductItem()
    {}


    public String getUPC(){
        return getString(KEY_UPC);
    }
    public String getImageUrl(){
        return getString(KEY_IMAGE_URL);
    }
    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }
    public double getPrice(){
        return getDouble(KEY_PRICE);
    }
    public int getQuantity(){
        return getInt(KEY_QUANTITY);
    }
    public String getStore(){
        return getString(KEY_STORE);
    }
    public String getItemName(){ return getString(KEY_ITEM_NAME); }
    public String getStoreAddress() { return getString(KEY_STORE_ADDRESS); }

    public void setUPC(String UPC){
        put(KEY_UPC, UPC);
    }
    public void setImageUrl(String url){
        put(KEY_IMAGE_URL, url);
    }
    public void setDescription(String description){
        put(KEY_DESCRIPTION, description);
    }
    public void setPrice(double price){
        put(KEY_PRICE, price);
    }
    public void setQuantity(int quantity){ put(KEY_QUANTITY, quantity); }
    public void setStore(String store){
        put(KEY_STORE, store);
    }
    public void setItemName(String itemName){ put(KEY_ITEM_NAME, itemName); }
    public void setStoreAddress(String storeAddress) { put(KEY_STORE_ADDRESS, storeAddress); }

}
