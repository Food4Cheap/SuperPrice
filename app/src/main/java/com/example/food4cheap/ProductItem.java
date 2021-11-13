package com.example.food4cheap;

import com.parse.ParseClassName;
import com.parse.ParseObject;


@ParseClassName("ProductItem")
public class ProductItem extends ParseObject{
    public static final String KEY_UPC = "UPC";
    public static final String KEY_IMAGE_URL = "imageURL";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_PRICE = "price";
    public static final String KEY_UNIT = "unit";
    public static final String KEY_STORE = "store";

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
    public String getUnit(){
        return getString(KEY_UNIT);
    }
    public String getStore(){
        return getString(KEY_STORE);
    }

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
    public void setUnit(String unit){
        put(KEY_UNIT, unit);
    }
    public void setStore(String store){
        put(KEY_STORE, store);
    }
}
