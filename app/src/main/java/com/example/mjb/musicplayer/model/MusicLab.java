package com.example.mjb.musicplayer.model;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.mjb.todo.utils.PictureUtils;
import com.example.mjb.todo.utils.PictureUtils;

import java.util.ArrayList;
import java.util.List;

public class MusicLab {


    public MusicLab(Activity activity) {
        loadAudio(activity);



    }

    public List<Music> getMusicList() {
        return mMusicList;
    }

    private List<Music> mMusicList;


    private void loadAudio(Activity activity) {
        ContentResolver contentResolver = activity.getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cursor = contentResolver.query(uri, null, selection, null, sortOrder);

        if (cursor != null && cursor.getCount() > 0) {
            mMusicList = new ArrayList<>();
            while (cursor.moveToNext()) {
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                Long albumId = cursor.getLong(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));

                Uri sArtworkUri = Uri
                        .parse("content://media/external/audio/albumart");
                Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);
                String  coverpath = PictureUtils.getImageRealPath(contentResolver,albumArtUri);



                // Save to audioList
                mMusicList.add(new Music(path, title, album, artist,coverpath));
            }

        }
        cursor.close();
    }

}
