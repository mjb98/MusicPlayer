package com.example.mjb.musicplayer;

import android.support.annotation.Nullable;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mjb.musicplayer.model.Music;
import com.example.mjb.musicplayer.model.MusicLab;
import com.example.mjb.todo.utils.PictureUtils;

import java.util.List;

public class ViewPagerActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    TabLayout mTabLayout;
    TabItem mTabItemLeft;
    TabItem mTabItemCenter;
    TabItem mTabItemRight;
    MusicLab mMusicLab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager = findViewById(R.id.music_view_pager);
        mTabLayout = findViewById(R.id.tab_layout);
        mTabItemLeft = findViewById(R.id.tab_left);
        mTabItemCenter = findViewById(R.id.tab_center);
        mTabItemRight = findViewById(R.id.tab_right);
        mViewPager.setOffscreenPageLimit(2);
        mMusicLab = new MusicLab(this);

        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {


            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0 :return MusicListFragment.newInstance(mMusicLab);
                    case 1 : return ArtistListFragment.newInstance(mMusicLab);
                    case 2 : return ArtistListFragment.newInstance(mMusicLab);
                    default:return null;
                }

            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return getResources().getString(R.string.songs);
                    case 1:
                        return getResources().getString(R.string.artists);
                    case 2:
                        return getResources().getString(R.string.albums);
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        });

        mTabLayout.setupWithViewPager(mViewPager, true);


    }
}
