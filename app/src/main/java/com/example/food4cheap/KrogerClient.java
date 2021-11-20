package com.example.food4cheap;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class KrogerClient {

    public static final String TAG = "KrogerClient";
    public static final String RADIUS="100";
    public static final String LOCATIONLIMIT="10";
    private String Access_Token;
    private OkHttpClient client;
    public List<LocationDetails> locations;
    private String brands[]={"Kroger","Bakers","City Market","Dillons","Food 4 Less","Foods Co","Fred Meyer","Frys","Gerbes","JayC","King Soopers","Metro Market","Owen's","Pay Less","Pick 'n Save","QFC","Ralphs","Smith's"};
    private AppCompatActivity activity;

    public KrogerClient(AppCompatActivity activity) {

        client = new OkHttpClient();
        locations=new ArrayList<>();
        this.activity=activity;

        Access_Token = getNewAccessToken();

        Log.d(TAG, "Right after " + Access_Token);

    }

    protected String getNewAccessToken() {
        final String[] access_token = new String[1];
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

        //Declaring the scope that we want. We only need to search for products so stuff like a customer authorization is not necessary
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&scope=product.compact");
        //Create a new request with the necessary requirements. The important part is the "Basic ..." which is the client key and secret converted to Base64
        Request request1 = new Request.Builder()
                .url("https://api.kroger.com/v1/connect/oauth2/token")
                .post(body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", "Basic c3VwZXJwcmljZS1lYjdmNmEwOGZlMGJiMDQ3YWJmZTIyODRhOTVjNzRmYjM4MzQzMDkyMTk0OTQ3MTgwNTU6T3pydHlPbnFXamU4VVc2NDhTMEdZV092R3h2R2VCZERUbkdFYkkzUQ==")
                .build();
        //Asynchronous call for client to grab a new access token
        client.newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    //Store the response.body into a JSONObject and then grab the access token
                    JSONObject temp = new JSONObject(response.body().string());
                    access_token[0] = temp.getString("access_token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        //Waiting for the client to get the new Access Token and store it. Without "sleep" the method returns null
        while (access_token[0] == null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return access_token[0];
    }

    public List<LocationDetails> getLocations(String chain) {
        //Re initialize locations as an empty array so we don't add the same locations again
        locations=new ArrayList<>();
        LocationManager lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            //This basically creates a pop up asking the user for Location permissions. Despite the permissions in Manifest, we still need personal authorization from the user due to security reasons implemented in higher APIs.
            ActivityCompat.requestPermissions(activity,new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 123);
            Log.e(TAG,"No permissions for location");

        }


        //Code to get the best location. For some reason .getLastKnownLocation will return null sometimes even when location permission are active, so this is just gonna check all the providers to
        //see if they have a good location
        List<String> providers = lm.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = lm.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        //Get the longitude and latitude using LocationManager and Location

        double longitude = bestLocation.getLongitude();
        double latitude = bestLocation.getLatitude();
        Log.d(TAG,"Longitude "+longitude);
        Log.d(TAG,"Latitude "+latitude);
        //By default, the emulator is set to be in a Google Office building in Northern California, so we need to change the emulator's location to check the proper locations.


        //For the request we can pass in longitude and latitude. We can also pass in a zipcode, though longitude and latitude is more accurate
        //Additionally, we can change the range of possible locations, from 1 to 100 miles, and we can filter out which brands we want.
        //For some reason, brands that are included is Shell (the gas station) and Covid (I have no idea how this is a brand of Kroger) so we will need to filter these out.
        Request request = new Request.Builder()
                .url("https://api.kroger.com/v1/locations?filter.lat.near="+latitude+"&filter.lon.near="+longitude+"&filter.radiusInMiles="+RADIUS+"&filter.limit="+LOCATIONLIMIT+"&filter.chain="+chain)
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer "+Access_Token)
                .build();


        final List<LocationDetails>[] temp = new List[]{null};
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    JSONObject json = new JSONObject(response.body().string());
                    Log.d(TAG,"FFFFFFFFFFFF"+json.toString());
                    try {
                        JSONArray listOfLocations=json.getJSONArray("data");
                        //TODO Extract the proper information. This will likely be the brand,address,name, and locationId
                        for (int i=0;i<listOfLocations.length();i++)
                        {
                            //Creating a new locationdetails object
                            locations.add(new LocationDetails());
                            locations.get(i).setLocationId(listOfLocations.getJSONObject(i).getInt("locationId"));
                            locations.get(i).setStoreName(listOfLocations.getJSONObject(i).getString("name"));
                            Log.i(TAG,"LocationId: "+listOfLocations.getJSONObject(i).getString("name"));
                        }
                        temp[0] =locations;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        //Waiting until we get the proper location before returning
        while (temp[0] ==null){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return locations;


    }

    public void getItems(String search, LocationDetails location)
    {
        //
        List<ProductItem> listofProductItems=new ArrayList<>();
        Request request = new Request.Builder()
                .url("https://api.kroger.com/v1/products?filter.term="+search+"&filter.locationId="+location.getLocationId())
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + Access_Token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e(TAG, "Failed as expected " + e.getMessage() + call.toString());
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                //Log.d(TAG, "In onResponse " + response.body().string());
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    Log.i(TAG,"Items from "+location.getLocationId()+" "+jsonObject.toString());

                    JSONArray listOfItems=jsonObject.getJSONArray("data");
                    for (int i=0;i<listOfItems.length();i++)
                    {
                        listofProductItems.add(new ProductItem());
                        listofProductItems.get(i).setDescription(listOfItems.getJSONObject(i).getString("description"));
                        //Why is getting the price so annoying
                        listofProductItems.get(i).setPrice(listOfItems.getJSONObject(i).getJSONArray("items").getJSONObject(0).getJSONObject("price").getDouble("regular"));

                        //Inconsistent image sizes. Hope that glide will account for this or we will have to search for the correct sizes
                        listofProductItems.get(i).setImageUrl(listOfItems.getJSONObject(i).getJSONArray("images").getJSONObject(0).getJSONArray("sizes").getJSONObject(3).getString("url"));
                        listofProductItems.get(i).setUPC(listOfItems.getJSONObject(i).getString("upc"));
                        listofProductItems.get(i).setStore(location.getStoreName());

                        Log.i(TAG,"Store name: "+ listofProductItems.get(i).getStore());
                        Log.i(TAG,"Description: "+listofProductItems.get(i).getDescription());
                        Log.i(TAG,"Price: "+listofProductItems.get(i).getPrice());
                        Log.i(TAG,"Image url: "+listofProductItems.get(i).getImageUrl());
                        Log.i(TAG,"UPC : "+listofProductItems.get(i).getUPC());
                    }
                }catch (JSONException err){
                    Log.d("Error", err.toString());
                }
            }
        });
    }

    public void getChainList()
    {
        Request request = new Request.Builder()
                .url("https://api.kroger.com/v1/chains")
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer "+Access_Token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    Log.i(TAG,jsonObject.toString());
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for (int i=0;i<jsonArray.length();i++)
                    {
                        Log.i(TAG,"NAME: "+jsonArray.getJSONObject(i).getString("name"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
