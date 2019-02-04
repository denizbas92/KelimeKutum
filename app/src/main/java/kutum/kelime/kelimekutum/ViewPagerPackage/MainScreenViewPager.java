package kutum.kelime.kelimekutum.ViewPagerPackage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import kutum.kelime.kelimekutum.FalseAnswerFragment;
import kutum.kelime.kelimekutum.FragmentsAdapter.HomeFragment;
import kutum.kelime.kelimekutum.FragmentsAdapter.PracticeFragment;
import kutum.kelime.kelimekutum.FragmentsAdapter.SettingFragment;
import kutum.kelime.kelimekutum.FragmentsAdapter.WordListFragment;
import kutum.kelime.kelimekutum.TrueAnswerFragment;

/**
 * Created by deniz on 7.3.2018.
 */

public class MainScreenViewPager extends FragmentPagerAdapter {
    public MainScreenViewPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                HomeFragment homeFragment=new HomeFragment();
                return homeFragment;
            case 1:
                WordListFragment wordListFragment=new WordListFragment();
                return wordListFragment;
            case 2:
                PracticeFragment practiceFragment=new PracticeFragment();
                return practiceFragment;
            case 3:
                SettingFragment settingFragment=new SettingFragment();
                return settingFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
