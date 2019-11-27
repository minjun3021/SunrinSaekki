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
import com.kmj.sunrinsaekki.MyPagerAdapter;
import com.kmj.sunrinsaekki.R;
import com.kmj.sunrinsaekki.UserData;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static String myName;
    public static String myFacebookId;
    public static String myProfileURL;
    public static FirebaseDatabase database;
    public static DatabaseReference myRef;
    public static ArrayList<String> friendsId = new ArrayList<>();
    public static ArrayList<UserData> friends=new ArrayList<>();

    TabLayout tabLayout;
    ViewPager viewPager;
    MyPagerAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.main_tabs);
        viewPager = findViewById(R.id.main_viewpager);

        mAdapter=new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);



    }
    public static void getFromFireBase(){
        database= FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot zoneSnapshot: dataSnapshot.getChildren()) {
                    boolean isExisted=false;
                    for(String i:friendsId){
                        Log.e("test",i+","+zoneSnapshot.getKey());
                        if(i.equals(zoneSnapshot.getKey())){
                            isExisted=true;
                            Log.e("friends","true");
                            break;
                        }
                    }
                    if(isExisted){
                        friends.add(new UserData(zoneSnapshot.child("name").getValue(String.class),zoneSnapshot.child("profileURL").getValue(String.class),zoneSnapshot.getKey()));
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
