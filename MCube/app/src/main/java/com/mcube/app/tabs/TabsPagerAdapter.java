package com.mcube.app.tabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.mcube.app.tabs.followup.FollowUpFragment;
import com.mcube.app.tabs.ivrs.IVRSFragment;
import com.mcube.app.tabs.lead.LeadFragment;
import com.mcube.app.tabs.settings.SettingsFragment;
import com.mcube.app.tabs.track.TrackFragment;
import com.mcube.app.tabs.x.XFragment;

/**
 * Created by Amit Kumar on 04-05-2015.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = "FragmentPagerAdapter";

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    final int PAGE_COUNT = 5;

    private FollowUpFragment followUpFragment = new FollowUpFragment();
    private TrackFragment trackFragment = new TrackFragment();
    private XFragment xFragment = new XFragment();
    private IVRSFragment ivrsFragment = new IVRSFragment();
    private SettingsFragment settingsFragment = new SettingsFragment();
    private LeadFragment leadFragment = new LeadFragment();

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return followUpFragment;
            case 1:
                return trackFragment;
            case 2:
                return xFragment;
            case 3:
                return ivrsFragment;
            case 4:
                return leadFragment;
            case 5:
                return settingsFragment;
        }

        Log.d(TAG,"returning null");
        return null;
    }

    @Override
    public int getCount() {
        return 6;
    }
}
