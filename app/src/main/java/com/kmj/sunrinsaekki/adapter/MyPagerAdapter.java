package com.kmj.sunrinsaekki.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.kmj.sunrinsaekki.fragment.FriendsFragment;
import com.kmj.sunrinsaekki.fragment.RestaurantFragment;

import java.util.ArrayList;

public class MyPagerAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> fragments;
    String titles[]=new String[]{"음식점","친구들"};

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments=new ArrayList<>();
        fragments.add(new RestaurantFragment());
        fragments.add(new FriendsFragment());
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
