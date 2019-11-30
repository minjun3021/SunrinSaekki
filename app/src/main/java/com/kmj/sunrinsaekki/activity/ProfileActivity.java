package com.kmj.sunrinsaekki.activity;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kmj.sunrinsaekki.R;
import com.kmj.sunrinsaekki.adapter.ProfileAdapter;
import com.kmj.sunrinsaekki.data.UserData;
import com.kmj.sunrinsaekki.fragment.FriendsFragment;
import com.kmj.sunrinsaekki.fragment.RestaurantFragment;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    CircleImageView profile;
    TextView name,visit,star;
    RecyclerView starRecycler;
    RecyclerView visitRecycler;

    FirebaseDatabase database;
    DatabaseReference myRef1;

    ArrayList<String> stars;
    ArrayList<String> visits;

    UserData user;
    ProfileAdapter starAdapter,visitAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        if(RestaurantFragment.isMyProfile){
            RestaurantFragment.isMyProfile=false;
            user = new UserData(MainActivity.myName, MainActivity.myProfileURL, MainActivity.myFacebookId);
        }
        else{
            user=MainActivity.friends.get(FriendsFragment.adapterPosition);
        }

        stars = new ArrayList<>();
        visits = new ArrayList<>();

        starAdapter = new ProfileAdapter(stars,this);
        visitAdapter = new ProfileAdapter(visits,this);

        profile = findViewById(R.id.profile_img);
        name = findViewById(R.id.profile_name);
        visit = findViewById(R.id.profile_visit);
        star = findViewById(R.id.profile_star);
        visitRecycler = findViewById(R.id.profile_visit_recycler);
        starRecycler = findViewById(R.id.profile_star_recycler);

        starRecycler.setAdapter(starAdapter);
        visitRecycler.setAdapter(visitAdapter);

        visitRecycler.addItemDecoration(new MyItemDecoration());
        starRecycler.addItemDecoration(new MyItemDecoration());

        visitRecycler.setLayoutManager(new GridLayoutManager(this,2));
        starRecycler.setLayoutManager(new GridLayoutManager(this,2));

        name.setText(user.getName());
        Glide.with(this)
                .load(user.getProfileURL())
                .placeholder(R.drawable.ic_person)
                .centerCrop()

                .transition(DrawableTransitionOptions.withCrossFade())
                .into(profile);

        database= FirebaseDatabase.getInstance();
        myRef1 = database.getReference("Users");
        myRef1.child(user.getId()).child("Information").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stars.clear();
                visits.clear();
                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {
                    Log.e(zoneSnapshot.getKey(),zoneSnapshot.child("star").getValue(String.class)+" "+zoneSnapshot.child("visit").getValue(String.class));

                    if(zoneSnapshot.child("visit").getValue(String.class).equals("1")){
                        visits.add(zoneSnapshot.getKey());
                        Log.e(zoneSnapshot.getKey(),"Visit");
                        visit.setText(String.valueOf(visits.size()));
                        visitAdapter.notifyDataSetChanged();
                    }

                    if(zoneSnapshot.child("star").getValue(String.class).equals("1")){
                        Log.e(zoneSnapshot.getKey(),"star");
                        stars.add(zoneSnapshot.getKey());
                        star.setText(String.valueOf(stars.size()));
                        starAdapter.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    class MyItemDecoration extends RecyclerView.ItemDecoration{
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(10,10,10,10);
            view.setBackgroundColor(Color.parseColor("#E9EBEE"));
            ViewCompat.setElevation(view,10.0f);
        }
    }
}
