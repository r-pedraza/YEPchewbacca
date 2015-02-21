package es.raul.pedraza.yepchewaka;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Locale;

/**
 * Created by raulpedraza on 6/2/15.
 */

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private static final int NUMBERS_OF_TABS = 2;
    Context context;

    public SectionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if (position == 0) {
            InboxFragment inboxFragment = new InboxFragment();
            return inboxFragment;
        } else {
            FriendsFragment friendsFragment = new FriendsFragment();
            return friendsFragment;
        }
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return NUMBERS_OF_TABS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return context.getString(R.string.title_section1).toUpperCase(l);
            case 1:
                return context.getString(R.string.title_section2).toUpperCase(l);
        }
        return null;
    }

    /**
     * This method add two icons botton at Action bar
     *
     * @param position
     * @return icons
     */
    public int getIcon(int position) {
        switch (position) {
            case 0:
                return R.drawable.ic_tab_inbox;
            case 1:
                return R.drawable.ic_tab_friends;

        }

        return R.drawable.ic_tab_inbox;
    }
}

