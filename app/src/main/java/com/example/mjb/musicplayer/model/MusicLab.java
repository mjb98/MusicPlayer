package com.example.mjb.musicplayer.model;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.mjb.musicplayer.database.App;
import com.example.mjb.todo.utils.PictureUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MusicLab  {

    private List<Music> mMusicList;
    private List<Artist> mArtistList = new ArrayList<>();
    private List<Album> mAlbumList =   new ArrayList<>();
    private    FavoriteSongsDao mFavoriteSongsDao = App.getApp().getDaoSession().getFavoriteSongsDao();
    private static MusicLab mMusicLab;


    public static MusicLab getInstance(Activity activity){
        if (mMusicLab == null)
            mMusicLab = new MusicLab(activity);

        return mMusicLab;

    }

    private MusicLab(Activity activity) {

        loadMusics(activity);


    }



    public FavoriteSongsDao getFavoriteSongsDao() {
        return mFavoriteSongsDao;
    }

    public List<Album> getAlbumList() {
        return mAlbumList;
    }

    public List<Music> getMusicList() {
        return mMusicList;
    }

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
                String musicId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                Long albumId = cursor.getLong(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));

                Uri sArtworkUri = Uri
                        .parse("content://media/external/audio/albumart");
                Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);
                String coverpath = PictureUtils.getImageRealPath(contentResolver, albumArtUri);
                Music song = new Music(path, title, album, artist, coverpath,musicId);
                if (!mMusicList.contains(song)) {
                    mMusicList.add(song);


                    Artist newArtist = new Artist(artist);
                    Album newAlbum = new Album(artist, album);
                    Boolean containsAlbum = false;
                    for (Album album1 : mAlbumList) {
                        if (album1.getName().equals(album) && album1.getArtist().equals(artist)) {
                            newAlbum = album1;
                            newAlbum.addSong(song);
                            containsAlbum = true;
                        }
                    }
                    if (!containsAlbum) {
                        newAlbum = new Album(album, artist);
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
            setFavoriteMusics();

        }
        cursor.close();
    }
   private   void setFavoriteMusics(){
        List<String> favoriteSongs = new ArrayList<>();
        for (FavoriteSongs favoriteSongs1 : mFavoriteSongsDao.loadAll()){
            favoriteSongs.add(favoriteSongs1.getMMusicID());
        }

      for (Music music : mMusicList){
            if(favoriteSongs.contains(music.getMusicID()))
                music.setFavorite(true);
      }


      }
    public void ChangeFavorite(Music music,Boolean favorite) {
        music.setFavorite(favorite);
        FavoriteSongsDao favoriteSongsDao = App.getApp().getDaoSession().getFavoriteSongsDao();
        if(favorite) {

            if (favoriteSongsDao.load(music.getMusicID()) == null)

                favoriteSongsDao.insert(new FavoriteSongs(music.getMusicID()));
        }
        else
        if(favoriteSongsDao.load(music.getMusicID() ) != null)
            favoriteSongsDao.delete(favoriteSongsDao.load(music.getMusicID()));


    }
    public static Music getSong(String musicId){
       for (Music music : mMusicLab.getMusicList()){
           if(musicId .equals(music.getMusicID()))
               return music;
       }
       return null;
    }


}
