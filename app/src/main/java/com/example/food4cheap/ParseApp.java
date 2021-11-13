package com.example.food4cheap;

import android.app.Application;
import com.parse.Parse;

public class ParseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Register subclasses up here


        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("0PeQm5NCTYO5qHi1cdw2FpPPm4WRpAReqRTNI3ty")
                .clientKey("HVY8iq7se99xAc0fPK6NyBWsSDOzlgl2ylNJunVg")
                .server("https://parseapi.back4app.com/")
                .build());
    }
}
