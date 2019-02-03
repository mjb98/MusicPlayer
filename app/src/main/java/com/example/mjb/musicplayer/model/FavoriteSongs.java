package com.example.mjb.musicplayer.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import java.util.List;

import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class FavoriteSongs  {
    @Id
    @Unique
    private String mMusicID;

    @Generated(hash = 1150319602)
    public FavoriteSongs(String mMusicID) {
        this.mMusicID = mMusicID;
    }

    @Generated(hash = 748535838)
    public FavoriteSongs() {
    }

    public String getMMusicID() {
        return this.mMusicID;
    }

    public void setMMusicID(String mMusicID) {
        this.mMusicID = mMusicID;
    }
    



}
