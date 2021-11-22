package com.example.food4cheap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import com.example.food4cheap.fragments.SearchFragment;
import com.parse.ParseUser;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    KrogerClient krogerClient;
    List<String> locations;
    List<LocationDetails> locationsDetailsList;
    final FragmentManager fragmentManager=getSupportFragmentManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(ParseUser.getCurrentUser() == null){
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        }

        //This basically creates a pop up asking the user for Location permissions. Despite the permissions in Manifest, we still need personal authorization from the user due to security reasons implemented in higher APIs.

        setContentView(R.layout.activity_main);

        //This basically creates a pop up asking the user for Location permissions. Despite the permissions in Manifest, we still need personal authorization from the user due to security reasons implemented in higher APIs.
        ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 123);

        Fragment fragment=new SearchFragment();
        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
       //krogerClient= new KrogerClient(this);
       //locationsDetailsList=krogerClient.getLocations("Ralphs");
       //krogerClient.getItems("Milk",locationsDetailsList.get(0));

    }
}