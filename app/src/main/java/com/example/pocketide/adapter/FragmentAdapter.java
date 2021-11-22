package com.example.pocketide.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.pocketide.Fragments.C_Frag;
import com.example.pocketide.Fragments.Cpp_Frag;
import com.example.pocketide.Fragments.Java_Frag;

public class FragmentAdapter extends FragmentPagerAdapter {
    public FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:return new C_Frag();
            case 1:return new Cpp_Frag();
            case 2:return new Java_Frag();
        }
        return new C_Frag();
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if(position==1){
            title="C";
        }
        else if(position==2){
            title="C++";
        }
        else if(position==3){
            title="Java";
        }
        return title;
    }
}
