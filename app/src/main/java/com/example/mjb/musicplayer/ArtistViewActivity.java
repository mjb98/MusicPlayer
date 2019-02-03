package com.example.mjb.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.mjb.musicplayer.model.Album;
import com.example.mjb.musicplayer.model.Artist;

public class ArtistViewActivity extends SingleFragmentActivity {

    public static Intent newIntent(Artist artist, Context context){
        Intent intent = new Intent(context,ArtistViewActivity.class);
        intent.putExtra("artist",artist);
        return intent;


    }
    public static Intent newIntent(Album album, Context context){
        Intent intent = new Intent(context,ArtistViewActivity.class);
        intent.putExtra("album",album);
        return intent;


    }


    @Override
    public Fragment createFragment() {
        Artist artist = ((Artist) getIntent().getSerializableExtra("artist"));
        Album album = (Album) getIntent().getSerializableExtra("album");
        if (artist != null)
            return ArtistViewFragment.newInstance(artist);
        else
            return MusicListFragment.newInstance(album);
    }
}
