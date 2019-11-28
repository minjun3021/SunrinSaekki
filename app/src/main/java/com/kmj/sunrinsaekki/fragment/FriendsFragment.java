package com.kmj.sunrinsaekki.fragment;


import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kmj.sunrinsaekki.adapter.FriendsAdapter;
import com.kmj.sunrinsaekki.ItemTouchHelperCallback;
import com.kmj.sunrinsaekki.R;
import com.kmj.sunrinsaekki.activity.MainActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {
    FriendsAdapter mAdapter;
    RecyclerView recyclerView;
    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_friends, container, false);
        recyclerView = v.findViewById(R.id.friends_recycler);
        mAdapter=new FriendsAdapter(MainActivity.friends,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new MyItemDecoration());
        ItemTouchHelper itemTouchHelper= new ItemTouchHelper(new ItemTouchHelperCallback(mAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return v;
    }
    class MyItemDecoration extends RecyclerView.ItemDecoration{
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(20,20,20,20);
            view.setBackgroundColor(0xFFFFFFFF);
            ViewCompat.setElevation(view,10.0f);
        }
    }

}
