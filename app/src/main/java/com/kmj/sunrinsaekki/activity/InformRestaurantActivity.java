package com.kmj.sunrinsaekki.activity;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kmj.sunrinsaekki.R;
import com.kmj.sunrinsaekki.adapter.CommentAdapter;
import com.kmj.sunrinsaekki.data.CommentData;
import com.kmj.sunrinsaekki.data.RestaurantData;
import com.kmj.sunrinsaekki.data.UserRestaurant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class InformRestaurantActivity extends AppCompatActivity {
    TextView category, name, push,review;
    ImageView resIMG;
    ArrayList<CommentData> comments;
    CircleImageView myProfile;
    EditText comment;
    RecyclerView recyclerView;
    int reviewcnt;
    CommentAdapter mAdapter;
    Button visit;
    ImageButton star;
    RestaurantData restaurant;
    boolean isStared;
    boolean hasVisited;
    public static FirebaseDatabase database;
    public static DatabaseReference myRef1;
    public static DatabaseReference myRef2;
    DatabaseReference myRef3,myRef4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform_restaurant);

        restaurant = MainActivity.restaurants.get(MainActivity.adapterPosition);

        database= FirebaseDatabase.getInstance();
        myRef1 = database.getReference("Restaurants");
        myRef2=myRef1.child(restaurant.getName());

        myRef3=database.getReference("Users");
        myRef4=myRef3.child(MainActivity.myFacebookId).child("Information");
        star = findViewById(R.id.star);
        review = findViewById(R.id.res_review);
        visit = findViewById(R.id.visit);
        recyclerView = findViewById(R.id.res_recycler);
        myProfile = findViewById(R.id.res_comment_pro);
        comment = findViewById(R.id.res_comment);
        push = findViewById(R.id.res_comment_push);
        name = findViewById(R.id.Res_name);
        category = findViewById(R.id.category);
        resIMG = findViewById(R.id.res_inform_image);



        isStared=false;
        hasVisited=false;
        reviewcnt=0;
        comments = new ArrayList<>();


        name.setText(restaurant.getName());
        category.setText("#" + restaurant.getCategory());
        Glide.with(this)
                .load(MainActivity.myProfileURL)
                .into(myProfile);

        visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hasVisited){
                    visit.setBackgroundResource(0);
                    visit.setTextColor(Color.parseColor("#ADA1A1"));
                    hasVisited=true;
                    pushInformToFB();
                }


            }
        });
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(!isStared){
                   isStared=true;
                   ViewCompat.setBackgroundTintList(star, ContextCompat.getColorStateList(InformRestaurantActivity.this,R.color.isStared));
                   pushInformToFB();

               }
               else{
                   isStared=false;
                   ViewCompat.setBackgroundTintList(star, ContextCompat.getColorStateList(InformRestaurantActivity.this,R.color.isnoStared));
                   pushInformToFB();
               }

            }
        });

        push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!comment.getText().toString().replace(" ", "").equals("")){
                    myRef2.child("comments").push().setValue(new CommentData(MainActivity.myName,doYearMonthDay(),comment.getText().toString(),MainActivity.myProfileURL));
                    comment.setText("");

                }
                else{
                    Toast.makeText(InformRestaurantActivity.this, "리뷰를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }



            }
        });



        myRef4.child(restaurant.getName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount()!=0){
                    UserRestaurant user = dataSnapshot.getValue(UserRestaurant.class);
                    Log.e(user.getStar(),user.getVisit());

                    if(user.getVisit().equals("1")){
                        hasVisited=true;
                        Log.e("visit", "visit");
                        visit.setBackgroundResource(0);
                        visit.setTextColor(Color.parseColor("#ADA1A1"));
                    }
                    if (user.getStar().equals("1")){
                        Log.e("star","star");
                        ViewCompat.setBackgroundTintList(star, ContextCompat.getColorStateList(InformRestaurantActivity.this,R.color.isStared));
                        isStared=true;
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child("images/" + MainActivity.restaurants.get(MainActivity.adapterPosition).getName()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Log.e("img", task.getResult().toString());
                if (task.isComplete()) {
                    Glide.with(InformRestaurantActivity.this)
                            .load(task.getResult())
                            .centerCrop()
                            .placeholder(R.drawable.ic_image)
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(resIMG);
                }
            }
        });




        mAdapter = new CommentAdapter(comments, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        myRef2.child("comments").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                CommentData commentData = dataSnapshot.getValue(CommentData.class);
                comments.add(0,new CommentData(commentData.getName(),commentData.getDate(),commentData.getComment(),commentData.getProURL()));
                mAdapter.notifyDataSetChanged();
                reviewcnt++;
                review.setText("리뷰 "+reviewcnt+"개");

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public String doYearMonthDay() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분", Locale.KOREA);
        Date date = new Date();
        String currentDate = formatter.format(date);
        return currentDate;
    }

    public void pushInformToFB(){
        String isstar;
        String hasvisit;

        if(isStared){
            isstar="1";
        }
        else{
            isstar = "0";
        }
        if(hasVisited){
            hasvisit = "1";
        }
        else{
            hasvisit="0";
        }
        myRef4.child(restaurant.getName()).setValue(new UserRestaurant(restaurant.getReview(),restaurant.getCategory(), isstar, hasvisit));

    }
}
