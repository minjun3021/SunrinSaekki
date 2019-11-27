package com.kmj.sunrinsaekki;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static String myName;
    public static String myFacebookId;
    public static String myProfileURL;
    public static FirebaseDatabase database;
    public static DatabaseReference myRef;
    public static ArrayList<String> friendsId = new ArrayList<>();
    public static ArrayList<UserData> friends=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

//                    Log.e(zoneSnapshot.getKey(), friends.get(friends.size()-1).getName());
//                    Log.e(zoneSnapshot.getKey(), friends.get(friends.size()-1).getProfileURL());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
