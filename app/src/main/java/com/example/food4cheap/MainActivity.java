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

import com.example.food4cheap.Activities.LoginActivity;
import com.example.food4cheap.Clients.KrogerClient;
import com.example.food4cheap.Models.LocationDetails;
import com.example.food4cheap.fragments.CartFragment;
import com.example.food4cheap.fragments.ProfileFragment;
import com.example.food4cheap.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import com.example.food4cheap.Models.ShoppingCart;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    public KrogerClient krogerClient;
    List<String> locations;
    public List<LocationDetails> locationsDetailsList;

    private String brands[]={"Kroger","Bakers","CityMarket","Dillons","Food4Less","FoodsCo","FredMeyer","Frys","Gerbes","JayC","KingSoopers","MetroMarket","Owens","PayLess","PicknSave","QFC","Ralphs","Smiths"};
    Fragment savedSearchFragment;
    final FragmentManager fragmentManager=getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //No user? Go to the LoginActivity
        if(ParseUser.getCurrentUser() == null){
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        }
        //Setting up the custom krogerClient in MainActivity so that it doesn't have to be created everytime when switching fragments
        krogerClient=new KrogerClient(this);
        locationsDetailsList=new ArrayList<>();
        for (String brand : brands) {
            locationsDetailsList.addAll(krogerClient.getLocations(brand));
        }


        try {
            if(ParseUser.getCurrentUser() == null){
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
            }
            else{
                ShoppingCart currentCart = (ShoppingCart) ParseUser.getCurrentUser().getParseObject("shoppingCart");
                if (currentCart == null)
                {
                    ShoppingCart shoppingCart = new ShoppingCart();
                    shoppingCart.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            ParseUser.getCurrentUser().put("shoppingCart", shoppingCart);
                            ParseUser.getCurrentUser().saveInBackground();
                        }
                    });
                }
                else if (currentCart.fetchIfNeeded() == null){

                }
            }

        } catch (ParseException e) {
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    ParseUser.getCurrentUser().put("shoppingCart", shoppingCart);
                    ParseUser.getCurrentUser().saveInBackground();
                }
            });
        }


        //This basically creates a pop up asking the user for Location permissions. Despite the permissions in Manifest, we still need personal authorization from the user due to security reasons implemented in higher APIs.

        setContentView(R.layout.activity_main);

        //This basically creates a pop up asking the user for Location permissions. Despite the permissions in Manifest, we still need personal authorization from the user due to security reasons implemented in higher APIs.
        ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 123);

        Fragment fragment=new SearchFragment();
        savedSearchFragment=fragment;
        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();

        // Bottom Navigation
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                Bundle bundle = new Bundle();

                switch (menuItem.getItemId()) {
                    case R.id.action_profile:
                        fragment = new ProfileFragment();
                        break;
                    case R.id.action_cart:
                        fragment = new CartFragment();
                        break;
                    case R.id.action_search:
                        //Rather than having the search fragment restart every time we tab off, let's just maintain the first one we created.
                        if (savedSearchFragment!=null)
                        {
                            fragment=savedSearchFragment;
                        }
                        else
                        {
                            fragment=new SearchFragment();
                            savedSearchFragment=fragment;
                        }

                        break;
                    default:
                        fragment = new SearchFragment();
                        savedSearchFragment=fragment;
                        break;
                }

                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.action_search);
    }

}