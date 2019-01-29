package com.example.mjb.musicplayer.model;

import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Music implements Serializable {

        private String path;
        private String title;
        private String album;
        private String artist;
        private String coverPath;

    public Music(String path, String title, String album, String artist,String coverPath) {
        this.path = path;
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.coverPath = coverPath;

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
           if (this.getTitle().equals(music.getTitle())&& this.getArtist().equals(music.getArtist())&&this.album.equals(music.getAlbum())){
               return true;
           }else {
               return false;
           }

       }else return false;
    }
}
