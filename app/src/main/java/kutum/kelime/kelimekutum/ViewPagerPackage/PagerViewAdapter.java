package kutum.kelime.kelimekutum.ViewPagerPackage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import kutum.kelime.kelimekutum.FalseAnswerFragment;
import kutum.kelime.kelimekutum.TrueAnswerFragment;

/**
 * Created by deniz on 10.2.2018.
 */

public class PagerViewAdapter extends FragmentPagerAdapter {
    public PagerViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                TrueAnswerFragment trueAnswerFragment=new TrueAnswerFragment();
                return trueAnswerFragment;
            case 1:
                FalseAnswerFragment falseAnswerFragment=new FalseAnswerFragment();
                return falseAnswerFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
