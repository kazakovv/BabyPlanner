package com.damianin.babyplanner.Adaptors;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.damianin.babyplanner.R;
import com.damianin.babyplanner.UserInterfaces.FragmentExistingPartners;
import com.damianin.babyplanner.UserInterfaces.FragmentPartnerRequests;
import com.damianin.babyplanner.UserInterfaces.FragmentSearchPartners;


/**
 * Tova e adapter za manage partners
 */
public class PagerAdapterManagePartners extends FragmentPagerAdapter {
    protected Context mContext;
    public PagerAdapterManagePartners(FragmentManager fm, Context context) {

        super(fm);
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentSearchPartners();
            case 1:
                return new FragmentPartnerRequests();
            case 2:
                return new FragmentExistingPartners();
            default:
                break;
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getResources().getString(R.string.search_partners_tab);
            case 1:
                return mContext.getResources().getString(R.string.requests_partners_tab);
            case 2:
                return mContext.getResources().getString(R.string.existing_partners_tab);


        }
        return super.getPageTitle(position);
    }

    @Override
    public int getItemPosition(Object object) {
      return super.getItemPosition(object);
    }

    @Override
    public int getCount() {
        return 3;
    }
}
