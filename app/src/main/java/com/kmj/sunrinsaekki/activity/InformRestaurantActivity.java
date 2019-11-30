package com.kmj.sunrinsaekki.activity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kmj.sunrinsaekki.R;
import com.kmj.sunrinsaekki.adapter.CommentAdapter;
import com.kmj.sunrinsaekki.data.CommentData;
import com.kmj.sunrinsaekki.data.RestaurantData;

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
    public static FirebaseDatabase database;
    public static DatabaseReference myRef1;
    public static DatabaseReference myRef2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform_restaurant);
        reviewcnt=0;
        review = findViewById(R.id.res_review);
        RestaurantData restaurant = MainActivity.restaurants.get(MainActivity.adapterPosition);
        recyclerView = findViewById(R.id.res_recycler);
        database= FirebaseDatabase.getInstance();
        myRef1 = database.getReference("Restaurants");
        myRef2=myRef1.child(restaurant.getName());

        comments = new ArrayList<>();
        myProfile = findViewById(R.id.res_comment_pro);
        comment = findViewById(R.id.res_comment);
        push = findViewById(R.id.res_comment_push);
        Glide.with(this)
                .load(MainActivity.myProfileURL)
                .into(myProfile);

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
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(resIMG);
                }
            }
        });

        name = findViewById(R.id.Res_name);
        category = findViewById(R.id.category);
        resIMG = findViewById(R.id.res_inform_image);
        name.setText(restaurant.getName());
        category.setText("#" + restaurant.getCategory());
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


}
