package com.kmj.sunrinsaekki.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kmj.sunrinsaekki.R;
import com.kmj.sunrinsaekki.activity.AddActivity;
import com.kmj.sunrinsaekki.activity.MainActivity;
import com.kmj.sunrinsaekki.adapter.RestaurantAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantFragment extends Fragment {
    MainActivity mainActivity;
    FloatingActionButton addButton;
    RecyclerView recyclerView;

    public static RestaurantAdapter adapter;
    public RestaurantFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainActivity= (MainActivity) getActivity();
        View v=inflater.inflate(R.layout.fragment_restaurant, container, false);

        recyclerView = v.findViewById(R.id.restaurant_recylcer);
        adapter = new RestaurantAdapter(MainActivity.restaurants, mainActivity);
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
        recyclerView.setAdapter(adapter);
        MainActivity.isResFragCreated=true;
        addButton=v.findViewById(R.id.restaurant_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), AddActivity.class);
                mainActivity.startActivity(intent);
            }
        });
        return v;
    }

}
