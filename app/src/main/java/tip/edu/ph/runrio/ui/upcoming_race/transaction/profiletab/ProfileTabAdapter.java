package tip.edu.ph.runrio.ui.upcoming_race.transaction.profiletab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import tip.edu.ph.runrio.ui.upcoming_race.transaction.profiletab.profilefragment.ProfileInfoFragment;


class ProfileTabAdapter extends FragmentStatePagerAdapter {



    public List<Fragment> fragmentList;
    public List<String> titleList;

    public ProfileTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

   @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }



}
