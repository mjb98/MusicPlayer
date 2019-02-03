package com.example.mjb.musicplayer;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mjb.musicplayer.model.Album;
import com.example.mjb.musicplayer.model.Artist;
import com.example.mjb.musicplayer.model.Music;
import com.example.mjb.musicplayer.model.MusicLab;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    RecyclerView mRecyclerView;


    public static SearchFragment newInstance() {

        Bundle args = new Bundle();
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
     View  view = inflater.inflate(R.layout.fragment_search, container, false);
        SectionedRecyclerViewAdapter sectionAdapter = new SectionedRecyclerViewAdapter();
        MusicLab musicLab = MusicLab.getInstance(getActivity());
        sectionAdapter.addSection(new ArtistSection(musicLab.getArtistList()));
        sectionAdapter.addSection(new MusicSection(musicLab.getMusicList()));
        sectionAdapter.addSection(new AlbumSection(musicLab.getAlbumList()));

        mRecyclerView = view.findViewById(R.id.search_recuclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(sectionAdapter);
        return view;
    }
    class ArtistSection extends StatelessSection {
        String title = "Artists";
        List<Artist> mArtistList;

        public ArtistSection(List<Artist> artistList) {

            super(SectionParameters.builder()
                    .itemResourceId(R.layout.recyclerview_artist_item)
                    .headerResourceId(R.layout.header_artists)
                    .build());
            mArtistList = artistList;
        }

        @Override
        public int getContentItemsTotal() {
            return mArtistList.size(); // number of items of this section
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            // return a custom instance of ViewHolder for the items of this section
            return new ArtistViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            ArtistViewHolder itemHolder = (ArtistViewHolder) holder;

            // bind your view here
            itemHolder.bindArtist(mArtistList.get(position));
        }
    }
    class ArtistViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView mCircleImageView;
        private TextView nameTextview, songsTextView;
        private Artist mArtist;


        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);

            mCircleImageView = itemView.findViewById(R.id.artist_item_imageView);
            nameTextview = itemView.findViewById(R.id.artist_item_artistview);
            songsTextView = itemView.findViewById(R.id.artist_item_songs);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = ArtistViewActivity.newIntent(mArtist, getActivity());
                    startActivity(intent);

                }
            });
        }
        public void bindArtist(Artist artist) {
            mArtist = artist;
            nameTextview.setText(artist.getName());
            songsTextView.setText(artist.getAlbums().size() + " Album  " + artist.getSongs().size() + " song");
            if (artist.getSongs().get(0).getCoverPath() != null) {
                Glide.with(getContext())
                        .load(new File(mArtist.getSongs().get(0).getCoverPath()))
                        .apply(new RequestOptions().override(80, 80))
                        .into(mCircleImageView);
            }
        }

    }
    class AlbumSection extends StatelessSection {
        String title = "Albums";
        List<Album> mAlbums = new ArrayList<>();

        public List<Album> getAlbums() {
            return mAlbums;
        }

        public void setAlbums(List<Album> albums) {
            mAlbums = albums;
        }

        public AlbumSection(List<Album> albums) {
            // call constructor with layout resources for this Section header and items
            super(SectionParameters.builder()
                    .itemResourceId(R.layout.recyclerview_artist_item)
                    .headerResourceId(R.layout.header)
                    .build());
            mAlbums = albums;
        }

        @Override
        public int getContentItemsTotal() {
            return mAlbums.size(); // number of items of this section
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            // return a custom inst

            return new AlbumHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            AlbumHolder albumHolder = (AlbumHolder) holder;
            final Album album = mAlbums.get(position);
            albumHolder.nameTextview.setText(album.getName());
            albumHolder.songsTextView.setText(album.getArtist());
            if(album.getMusicList().get(0).getCoverPath() != null){
                Glide.with(getContext())
                        .load(new File(album.getMusicList().get(0).getCoverPath()))
                        .apply(new RequestOptions().override(80, 80))
                        .into(albumHolder.mCircleImageView);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = ArtistViewActivity.newIntent(album,getActivity());
                    startActivity(intent);
                }
            });



        }
    }
    public  class AlbumHolder extends RecyclerView.ViewHolder {

        private CircleImageView mCircleImageView;
        private TextView nameTextview,songsTextView;
        private Album mAlbum;


        public AlbumHolder(@NonNull View itemView) {
            super(itemView);

            mCircleImageView = itemView.findViewById(R.id.artist_item_imageView);
            nameTextview = itemView.findViewById(R.id.artist_item_artistview);
            songsTextView = itemView.findViewById(R.id.artist_item_songs);
        }

    }
    class MusicSection extends StatelessSection {
        List<Music> mMusics = new ArrayList<>();
        String title = "Albums";

        public List<Music> getMusics() {
            return mMusics;
        }

        public void setMusics(List<Music> musics) {
            mMusics = musics;
        }

        public MusicSection(List<Music> musics) {
            // call constructor with layout resources for this Section header and items
            super(SectionParameters.builder()
                    .itemResourceId(R.layout.recyclerview_music_item)
                    .headerResourceId(R.layout.headerfor_music)
                    .build());
            mMusics = musics;

        }

        @Override
        public int getContentItemsTotal() {
            return mMusics.size(); // number of items of this section
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            // return a custom inst

            return new MusicHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            MusicHolder musicHolder = ((MusicHolder) holder);
            final Music  mMusic = mMusics.get(position);
            if (mMusic.getCoverPath() != null) {
                Glide.with(getContext())
                        .load(new File(mMusic.getCoverPath()))
                        .apply(new RequestOptions().override(400, 300))
                        .into(musicHolder.coverart);
//                if(bitmap!=null)
//                {
//                    bitmap.recycle();
//                    bitmap=null;
//                }
            }
            musicHolder.titleTextview.setText(mMusic.getTitle() != null ? mMusic.getTitle() : "Unknon title");
            musicHolder.artistTextView.setText(mMusic.getArtist() != null ? mMusic.getArtist() : "Unknon Artist");
            musicHolder.mMusic = mMusic;





        }


    }
    private class MusicHolder extends RecyclerView.ViewHolder {

        private ImageView coverart;
        private TextView titleTextview, artistTextView;
        private Music mMusic;


        public MusicHolder(@NonNull View itemView) {
            super(itemView);
            coverart = itemView.findViewById(R.id.item_song_cover);
            titleTextview = itemView.findViewById(R.id.item_song_title);
            artistTextView = itemView.findViewById(R.id.item_song_artist);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


        }

    }

}
