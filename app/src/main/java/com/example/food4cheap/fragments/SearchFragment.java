package com.example.food4cheap.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.food4cheap.KrogerClient;
import com.example.food4cheap.LocationDetails;
import com.example.food4cheap.MainActivity;
import com.example.food4cheap.ProductItem;
import com.example.food4cheap.ProductItemsAdapter;
import com.example.food4cheap.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;


public class SearchFragment extends Fragment {

    final static String TAG="SearchFragment";
    EditText etSearchText;
    RecyclerView rvProductItems;
    KrogerClient krogerClient;
    List<String> locations;
    private String brands[]={"Kroger","Bakers","CityMarket","Dillons","Food4Less","FoodsCo","FredMeyer","Frys","Gerbes","JayC","KingSoopers","MetroMarket","Owens","PayLess","PicknSave","QFC","Ralphs","Smiths"};
    List<LocationDetails> locationsDetailsList;
    List<ProductItem> productItemList;
    ProductItemsAdapter productItemsAdapter;
    LinearLayoutManager layoutManager;
    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etSearchText=view.findViewById(R.id.tvCart);
        rvProductItems=view.findViewById(R.id.rvItems);
        productItemList= new ArrayList<>();
        krogerClient=new KrogerClient(((MainActivity)getActivity()));
        productItemsAdapter=new ProductItemsAdapter(((MainActivity)getActivity()),productItemList);
        layoutManager=new LinearLayoutManager(((MainActivity)getActivity()));




        locationsDetailsList=new ArrayList<>();
        for (String brand : brands) {
            locationsDetailsList.addAll(krogerClient.getLocations(brand));
        }
        //locationsDetailsList.addAll(krogerClient.getLocations("Ralphs"));
        rvProductItems.setAdapter(productItemsAdapter);
        rvProductItems.setLayoutManager(layoutManager);


        //Why is there is so much stuff for something that I thought was going to be simple
        //This brings up the results on screen whenever the user presses the enter key on the soft keyboard
        //Maybe we should put this in a method so onViewCreated isn't a mess
        etSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                    String term=etSearchText.getText().toString();
                    clear();
                    //Because we don't want the phone to freeze up and die, we start a new thread
                    //I tried to do it on the main thread and it would freeze and make the phone think the app had crashed
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for(int j=0;j<locationsDetailsList.size();j++)
                            {
                                if(locationsDetailsList.get(j)!=null)
                                {
                                    productItemList.addAll(krogerClient.getItems(term,locationsDetailsList.get(j)));
                                    //We can't simply update the adapter because we can't apparently update the ui from a thread other than the main one
                                    //Therefore we need ANOTHER runnable but on the UiThread. Without this section the results only shows when the user closes the soft keyboard
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            productItemsAdapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                            }
                        }
                    }).start();
                    return true;
            }
        });

    }

    public void addAll()
    {

    }

    public void clear()
    {
        productItemList.clear();
        productItemsAdapter.notifyDataSetChanged();
    }
}