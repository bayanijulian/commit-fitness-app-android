package com.bayanijulian.glasskoala.view.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bayanijulian.glasskoala.view.discover.DiscoverFragment;
import com.bayanijulian.glasskoala.view.profile.ProfileFragment;

public class NavigationPagerAdapter extends FragmentPagerAdapter {
    private HomeFragment homeFragment;
    private ProfileFragment profileFragment;
    private DiscoverFragment discoverFragment;

    public NavigationPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setHomeFragment(HomeFragment homeFragment) {
        this.homeFragment = homeFragment;
    }

    public void setProfileFragment(ProfileFragment profileFragment) {
        this.profileFragment = profileFragment;
    }

    public void setDiscoverFragment(DiscoverFragment discoverFragment) {
        this.discoverFragment = discoverFragment;
    }

    @Override
    public Fragment getItem(int currentPage) {
        switch (currentPage) {
            case 0:
                return this.homeFragment;
            case 1:
                return this.profileFragment;
            case 2:
                return this.discoverFragment;
            default:
                return this.homeFragment;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
