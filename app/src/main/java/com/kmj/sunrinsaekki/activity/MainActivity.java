package com.kmj.sunrinsaekki.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kmj.sunrinsaekki.R;
import com.kmj.sunrinsaekki.adapter.MyPagerAdapter;
import com.kmj.sunrinsaekki.data.RestaurantData;
import com.kmj.sunrinsaekki.data.UserData;
import com.kmj.sunrinsaekki.fragment.RestaurantFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static String myName;
    public static String myFacebookId;
    public static String myProfileURL;
    public static FirebaseDatabase database;
    public static DatabaseReference myRef;
    public static DatabaseReference myRef1;
    public static ArrayList<String> friendsId = new ArrayList<>();
    public static ArrayList<UserData> friends = new ArrayList<>();
    public static ArrayList<UserData> users = new ArrayList<>();
    public static ArrayList<RestaurantData> restaurants = new ArrayList<>();
    public static int adapterPosition=0;
    public static int reviewCnt;
    public static boolean isResFragCreated=false;
    TabLayout tabLayout;
    ViewPager viewPager;
    MyPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.main_tabs);
        viewPager = findViewById(R.id.main_viewpager);

        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);


    }

    public static void getFriendFromFireBase() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                friends.clear();
                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {
                    boolean isExisted = false;
                    for (String i : friendsId) {
                        Log.e("test", i + "," + zoneSnapshot.getKey());
                        if (i.equals(zoneSnapshot.getKey())) {
                            isExisted = true;
                            Log.e("friends", "true");
                            break;
                        }
                    }
                    users.add(new UserData(zoneSnapshot.child("name").getValue(String.class), zoneSnapshot.child("profileURL").getValue(String.class), zoneSnapshot.getKey()));
                    if (isExisted) {
                        friends.add(new UserData(zoneSnapshot.child("name").getValue(String.class), zoneSnapshot.child("profileURL").getValue(String.class), zoneSnapshot.getKey()));
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getRestaurantsFromFirebase() {
        database = FirebaseDatabase.getInstance();
        myRef1 = database.getReference("Restaurants");

        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                restaurants.clear();

                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {
                    reviewCnt=0;
                    //zoneSnapshot.getKey() 음식점이름
                    //zoneSnapshot.child("category").getValue(String.class) 음식점 카테고리
                    myRef1.child(zoneSnapshot.getKey()).child("comments").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            reviewCnt= (int) dataSnapshot.getChildrenCount();
                            Log.e(zoneSnapshot.getKey(), reviewCnt + "");
                            restaurants.add(new RestaurantData(zoneSnapshot.getKey(),reviewCnt,zoneSnapshot.child("category").getValue(String.class)));
                            if(isResFragCreated){
                                RestaurantFragment.adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
