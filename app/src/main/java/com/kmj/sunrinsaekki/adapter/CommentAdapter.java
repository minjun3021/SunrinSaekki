package com.kmj.sunrinsaekki.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kmj.sunrinsaekki.R;
import com.kmj.sunrinsaekki.data.CommentData;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    ArrayList<CommentData> comments;
    Context context;

    public CommentAdapter(ArrayList<CommentData> comments, Context context) {
        this.comments = comments;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView commentPro;
        TextView name,date,comment;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            commentPro = itemView.findViewById(R.id.res_commentitem_pro);
            name=itemView.findViewById(R.id.res_commentitem_name);
            date=itemView.findViewById(R.id.res_commentitem_date);
            comment=itemView.findViewById(R.id.res_commentitem_comment);
        }
    }
    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        holder.comment.setText(comments.get(position).getComment());
        holder.date.setText(comments.get(position).getDate());
        holder.name.setText(comments.get(position).getName());
        Glide.with(context)
                .load(comments.get(position).getProURL())
                .centerCrop()
                .into(holder.commentPro);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }


}
