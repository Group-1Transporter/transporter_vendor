package com.transportervendor.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.transportervendor.fragment.HistoryFragment;
import com.transportervendor.fragment.HomeFragment;
import com.transportervendor.fragment.MarketFragment;

public class TabAccessorAdapter extends FragmentPagerAdapter {
    public TabAccessorAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        switch (position){
            case 0: fragment=new HomeFragment();break;
            case 1: fragment=new MarketFragment();break;
            case 2: fragment=new HistoryFragment();break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title="";
        if (position==0){
            title="Home";
        }
        else if (position==1){
            title="Market";
        }
        else if (position==2){
            title="History";
        }
        return title;
    }
}
