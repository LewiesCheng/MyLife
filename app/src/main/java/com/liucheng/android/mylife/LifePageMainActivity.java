package com.liucheng.android.mylife;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;
import java.util.UUID;

/**
 * Created by liucheng on 2017/5/23.
 */

public class LifePageMainActivity extends AppCompatActivity{

    private static final String EXTRA_LIFE_ID = "com.liucheng.android.mylife.life_id";

    private ViewPager mViewPager;
    private List<Life> mLifes;

    public static Intent newIntent(Context packageContext, UUID lifeId){
        Intent intent = new Intent(packageContext, LifePageMainActivity.class);
        intent.putExtra(EXTRA_LIFE_ID, lifeId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_page_main);

        UUID lifeId = (UUID) getIntent().getSerializableExtra(EXTRA_LIFE_ID);
        mViewPager = (ViewPager) findViewById(R.id.activity_life_page_main_view_page);

        mLifes = LifeLab.get(this).getLifes();
        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Life life = mLifes.get(position);
                return LifeMainFragment.newInstance(life.getId());
            }

            @Override
            public int getCount() {
                return mLifes.size();
            }
        });

        for (int i = 0; i < mLifes.size(); i++){
            if (mLifes.get(i).getId().equals(lifeId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

}
