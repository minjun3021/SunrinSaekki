package com.kmj.sunrinsaekki.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kmj.sunrinsaekki.R;
import com.kmj.sunrinsaekki.activity.InformRestaurantActivity;
import com.kmj.sunrinsaekki.activity.MainActivity;
import com.kmj.sunrinsaekki.data.RestaurantData;

import java.util.ArrayList;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {
    ArrayList<RestaurantData> restaurants;
    Context context;

    public RestaurantAdapter(ArrayList<RestaurantData> restaurants, Context context) {
        this.restaurants = restaurants;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView restaurantIMG;
        TextView name, review, category;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurantIMG = itemView.findViewById(R.id.item_restaurent_image);
            name = itemView.findViewById(R.id.item_restaurent_name);
            review = itemView.findViewById(R.id.item_restaurent_review);
            category = itemView.findViewById(R.id.item_restaurent_category);
            review = itemView.findViewById(R.id.item_restaurent_review);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, InformRestaurantActivity.class);
                    MainActivity.adapterPosition=getAdapterPosition();
                    context.startActivity(intent);
                }
            });

        }
    }

    @NonNull
    @Override
    public RestaurantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantAdapter.ViewHolder holder, int position) {
        holder.name.setText(restaurants.get(position).getName());
        holder.category.setText(restaurants.get(position).getCategory());
        holder.review.setText("리뷰 "+restaurants.get(position).getReview());

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child("images/"+restaurants.get(position).getName()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Log.e("img",task.getResult().toString());
                if (task.isComplete()){
                    Glide.with(context)
                            .load(task.getResult())
                            .centerCrop()
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(holder.restaurantIMG);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }


}
