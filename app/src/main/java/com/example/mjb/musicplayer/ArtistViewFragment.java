package com.example.mjb.musicplayer;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mjb.musicplayer.model.Album;
import com.example.mjb.musicplayer.model.Artist;
import com.example.mjb.musicplayer.model.Music;
import com.example.mjb.todo.utils.PictureUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArtistViewFragment extends Fragment {
    Artist artist;
    View.OnClickListener onClickListener;

RecyclerView mRecyclerView;
    public ArtistViewFragment() {
        // Required empty public constructor
    }

    public static ArtistViewFragment newInstance(Artist artist) {

        Bundle args = new Bundle();
        args.putSerializable("dsdsddssd",artist);

        ArtistViewFragment fragment = new ArtistViewFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_artist_view, container, false);

        SectionedRecyclerViewAdapter sectionAdapter = new SectionedRecyclerViewAdapter();

 artist = ((Artist) getArguments().getSerializable("dsdsddssd"));
        sectionAdapter.addSection(new AlbumSection(artist.getAlbums()));
        sectionAdapter.addSection(new MusicSection(artist.getSongs()));

// Set up your RecyclerView with the SectionedRecyclerViewAdapter
         mRecyclerView = view.findViewById(R.id.artistview_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(sectionAdapter);


        return view;
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
               albumHolder.mCircleImageView.setImageBitmap(PictureUtils.getScaledBitmap(album.getMusicList().get(0).getCoverPath(),getActivity()));
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
            MusicHolder musicHolder = (MusicHolder) holder;
          final Music  mMusic = mMusics.get(position);
            if (mMusic.getCoverPath() != null)
               musicHolder.coverart.setImageBitmap(PictureUtils.getScaledBitmap(mMusic.getCoverPath(),getActivity()));

           musicHolder.titleTextview.setText(mMusic.getTitle() != null ? mMusic.getTitle() : "Unknon title");
            musicHolder.artistTextView.setText(mMusic.getArtist() != null ? mMusic.getArtist() : "Unknon Artist");

             onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = Player.newIntent(new ArrayList<Music>(artist.getSongs()),artist.getSongs().indexOf(mMusic),getActivity());
                    startActivity(intent);
                }
            };
            holder.itemView.setOnClickListener(onClickListener);



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


        }
    }

    @Override
    public void onResume() {

        getActivity().setTitle(artist.getName());
        super.onResume();
    }
}


