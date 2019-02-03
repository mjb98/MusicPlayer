package com.example.mjb.musicplayer.model;

import android.support.annotation.Nullable;

import com.example.mjb.musicplayer.database.App;

import java.io.Serializable;

public class Music implements Serializable {

        private String path;
        private String title;
        private String album;
        private String artist;
        private String coverPath;
        private Boolean favorite;
        private String musicID;

    public Boolean getFavorite() {
        return favorite;
    }



    public String getMusicID() {
        return musicID;
    }

    public Music(String path, String title, String album, String artist, String coverPath, String ID) {
        favorite = false;
        this.path = path;
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.coverPath = coverPath;
        this.musicID = ID;

    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public String getPath() {
        return path;

    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
       if (obj instanceof  Music){
           Music music = (Music) obj;
           if (((Music) obj).musicID.equals(musicID)){
               return true;
           }else {
               return false;
           }

       }else return false;
    }
}
