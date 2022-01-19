package com.example.food4cheap.Clients;

import android.app.Application;

import com.example.food4cheap.Models.ProductItem;
import com.example.food4cheap.Models.ShoppingCart;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Register subclasses up here
        ParseObject.registerSubclass(ProductItem.class);
        ParseObject.registerSubclass(ShoppingCart.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("0PeQm5NCTYO5qHi1cdw2FpPPm4WRpAReqRTNI3ty")
                .clientKey("HVY8iq7se99xAc0fPK6NyBWsSDOzlgl2ylNJunVg")
                .server("https://parseapi.back4app.com/")
                .build());
    }
}
