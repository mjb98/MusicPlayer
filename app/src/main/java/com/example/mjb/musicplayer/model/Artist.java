package com.example.mjb.musicplayer.model;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Artist implements Serializable {

    private String name;
    private List<Music> songs;
    private  List<Album> Albums;

    public List<Album> getAlbums() {
        return Albums;
    }

    public Artist(String name) {
        this.name = name;
       this.songs = new ArrayList<>();
       this.Albums = new ArrayList<>();


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Music> getSongs() {
        return songs;
    }

    public void setSongs(List<Music> songs) {
        this.songs = songs;
    }
    public void addSong(Music music){
        songs.add(music);
    }




    @Override
    public boolean equals(@android.support.annotation.Nullable Object obj) {
       if( obj instanceof Artist ){
           if(((Artist) obj).name == this.name)
               return true;
           else
               return false;
       }
       else
           return false;
        }

}
