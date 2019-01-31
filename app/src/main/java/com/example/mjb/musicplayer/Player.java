package com.example.mjb.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mjb.musicplayer.model.Album;
import com.example.mjb.musicplayer.model.Artist;
import com.example.mjb.musicplayer.model.Music;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Player extends AppCompatActivity {
    private Album mAlbum;
    private Artist mArtist;
    private int number;
    private ArrayList<Music> mMusicList;


    public Fragment createFragment() {
    if(mMusicList != null)
        return  PlayFragment.newInstance(mMusicList,number);

        else
            return null;
    }




    public static  Intent newIntent(Album album,int number, Context context) {
        Intent intent = new Intent(context,Player.class);
        intent.putExtra("albumforplay",album);
        intent.putExtra("numberintent",number);
        return intent;
    }
    public static  Intent newIntent(Artist artist,int number, Context context) {
        Intent intent = new Intent(context,Player.class);
        intent.putExtra("aritistforplay",artist);
        intent.putExtra("numberintent",number);
        return intent;
    }
    public static  Intent newIntent(ArrayList<Music> musicList, int number, Context context) {
        Intent intent = new Intent(context,Player.class);
        intent.putExtra("musis", musicList);
        intent.putExtra("numberintent",number);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mAlbum = ((Album) getIntent().getSerializableExtra("albumforplay"));
        mArtist = ((Artist) getIntent().getSerializableExtra("aritistforplay"));
        number = getIntent().getIntExtra("numberintent",0);
        mMusicList = ((ArrayList<Music>) getIntent().getSerializableExtra("musis"));


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.findFragmentById(R.id.fragment_container) == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, createFragment())
                    .commit();
        }


    }
}
