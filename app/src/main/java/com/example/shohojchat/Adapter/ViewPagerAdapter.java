package com.example.shohojchat.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.shohojchat.Fragments.PostFragment;
import com.example.shohojchat.Fragments.ProfileFragment;
import com.example.shohojchat.Fragments.User_Fragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    String[] names = {
            "User", "Profile","Newsfeed"
    };


    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:

                return new User_Fragment();


            case 1:
                return  new ProfileFragment();

            case 2:
                return new PostFragment();


        }

        return null;
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return names[position];
    }


}
