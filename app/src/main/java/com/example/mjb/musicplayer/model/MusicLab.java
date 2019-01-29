package com.example.mjb.musicplayer.model;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.mjb.todo.utils.PictureUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MusicLab implements Serializable {

    public List<Album> getAlbumList() {
        return mAlbumList;
    }

    public MusicLab(Activity activity) {
        mArtistList = new ArrayList<>();
        mAlbumList = new ArrayList<>();
        loadMusics(activity);



    }

    public List<Music> getMusicList() {
        return mMusicList;
    }

    private List<Music> mMusicList;
    private List<Artist> mArtistList;
    private List<Album> mAlbumList;

    public List<Artist> getArtistList() {
        return mArtistList;
    }

    public void setArtistList(List<Artist> artistList) {
        mArtistList = artistList;
    }

    private void loadMusics(Activity activity) {
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
                String coverpath = PictureUtils.getImageRealPath(contentResolver, albumArtUri);
                Music song = new Music(path, title, album, artist, coverpath);
                if (!mMusicList.contains(song)) {
                    mMusicList.add(song);


                   Artist newArtist = new Artist(artist);
                   Album  newAlbum =  new Album(artist,album);
                    Boolean containsAlbum = false;
                    for (Album album1 : mAlbumList) {
                        if (album1.getName().equals(album)) {
                            newAlbum = album1;
                            newAlbum.addSong(song);
                            containsAlbum = true;
                        }
                    }
                    if (!containsAlbum) {
                        newAlbum = new Album(album,artist);
                        newAlbum.addSong(song);
                        mAlbumList.add(newAlbum);

                    }
                    Boolean containsArtist = false;
                    for (Artist artist1 : mArtistList) {
                        if (artist1.getName().equals(artist)) {
                            newArtist = artist1;
                            newArtist.addSong(song);
                            containsArtist = true;
                        }
                    }
                    if (!containsArtist) {
                        newArtist = new Artist(artist);
                        newArtist.addSong(song);
                        mArtistList.add(newArtist);

                    }
                    if (!newArtist.getAlbums().contains(newAlbum))
                    newArtist.getAlbums().add(newAlbum);

                }
            }

        }
        cursor.close();
    }

}
