package com.levanb.movies_app.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MoviePagerAdapter extends FragmentPagerAdapter {
    private PageInfoProvider provider;

    public interface PageInfoProvider {
        int getCount();
        Fragment getFragmentAt(int index);
        String getTitleAt(int index);
    }

    public MoviePagerAdapter(
        @NonNull FragmentManager manager,
        int behavior,
        PageInfoProvider provider
    ) {
        super(manager, behavior);
        this.provider = provider;
    }

    @Override
    public int getCount() {
        return provider.getCount();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return provider.getTitleAt(position);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return provider.getFragmentAt(position);
    }
}
