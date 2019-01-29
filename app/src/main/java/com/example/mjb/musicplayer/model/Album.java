package com.example.mjb.musicplayer.model;

import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Album implements Serializable {

    private String name;
    private String artist;
    private List<Music> mMusicList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public List<Music> getMusicList() {
        return mMusicList;
    }

    public void setMusicList(List<Music> musicList) {
        mMusicList = musicList;
    }

    public Album(String name, String artist) {
        this.name = name;
        this.artist = artist;
        this.mMusicList = new ArrayList<>();
    }
    public void addSong(Music music){
        mMusicList.add(music);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
       if (obj instanceof Album){

           Album album = (Album) obj;
           if(album.getArtist().equals(getArtist())&& album.getName().equals(getName()))
               return true;
               else
                   return false;
       }else return false;
    }
}
