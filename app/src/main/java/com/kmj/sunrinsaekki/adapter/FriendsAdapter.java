package com.kmj.sunrinsaekki.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kmj.sunrinsaekki.ItemTouchHelperCallback;
import com.kmj.sunrinsaekki.activity.ProfileActivity;
import com.kmj.sunrinsaekki.R;
import com.kmj.sunrinsaekki.data.UserData;
import com.kmj.sunrinsaekki.fragment.FriendsFragment;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> implements ItemTouchHelperCallback.ItemTouchHelperListener {
    ArrayList<UserData> friends;
    Context context;

    public FriendsAdapter(ArrayList<UserData> friends, Context context) {
        this.friends = friends;
        this.context = context;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        UserData fromItem=friends.get(fromPosition);
        friends.remove(fromPosition);
        friends.add(toPosition,fromItem);
        notifyItemMoved(fromPosition,toPosition);

        return true;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profileIMG;
        TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FriendsFragment.adapterPosition = getAdapterPosition();
                    Intent intent=new Intent(context, ProfileActivity.class);
                    context.startActivity(intent);
                }
            });
            profileIMG = itemView.findViewById(R.id.item_profileIMG);
            name = itemView.findViewById(R.id.item_name);
        }
    }

    @NonNull
    @Override
    public FriendsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsAdapter.ViewHolder holder, int position) {
        holder.name.setText(friends.get(position).getName());
        Glide.with(context)
                .load(friends.get(position).getProfileURL())
                .placeholder(R.drawable.ic_person)
                .into(holder.profileIMG);
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }


}
