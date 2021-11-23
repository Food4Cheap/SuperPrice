package com.example.food4cheap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.food4cheap.fragments.CartFragment;
import com.example.food4cheap.fragments.ItemDetailsFragment;
import com.example.food4cheap.fragments.ProfileFragment;
import com.example.food4cheap.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
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


        // Bottom Navigation
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.action_profile:
                        fragment = new ProfileFragment();
                        break;
                    case R.id.action_search:
                        fragment = new SearchFragment();
                        break;
                    case R.id.action_cart:
                        fragment = new CartFragment();
                        break;
                    default:
                        fragment = new SearchFragment();
                }

                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.action_search);

    }
}