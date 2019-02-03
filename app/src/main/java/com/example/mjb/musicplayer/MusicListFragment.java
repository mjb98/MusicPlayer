package com.example.mjb.musicplayer;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mjb.musicplayer.model.Album;
import com.example.mjb.musicplayer.model.Music;
import com.example.mjb.musicplayer.model.MusicLab;
import com.example.mjb.todo.utils.PictureUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MusicListFragment extends Fragment {
    public static final String Album_Tag = "dsds";
    public static final String MUSICLAB_TAG = "musicplayer.musiclist";
    private RecyclerView mRecyclerView;
    private MusicLab mMusicLab;
    private Album mAlbum;
    private MusicAdapter mMusicAdapter;
    private Boolean favoriteMode = false;
    List<Music> musicList;

    public void setFavoriteMode(Boolean favoriteMode) {

        if(favoriteMode){
            List<Music> favList = new ArrayList<>();
            for(Music music : MusicLab.getInstance(getActivity()).getMusicList()){
                if(music.getFavorite())
                    favList.add(music);
            }

            mMusicAdapter.setMusicList(favList);
            mMusicAdapter.notifyDataSetChanged();
        }else {
            mMusicAdapter.setMusicList(musicList);
            mMusicAdapter.notifyDataSetChanged();


        }



        this.favoriteMode = favoriteMode;
    }

    public MusicListFragment() {
        // Required empty public constructor
    }

    public static MusicListFragment newInstance() {
        MusicListFragment fragment = new MusicListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static MusicListFragment newInstance(Album album) {
        MusicListFragment fragment = new MusicListFragment();
        Bundle args = new Bundle();
        args.putSerializable(Album_Tag, album);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mMusicLab = MusicLab.getInstance(getActivity());
        mAlbum = (Album) getArguments().getSerializable(Album_Tag);
        setHasOptionsMenu(mAlbum == null);





    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.musiclist_menu,menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mRecyclerView = view.findViewById(R.id.song_list_recyclerview);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.columns_count)));
        if (mAlbum == null)
            mMusicAdapter = new MusicAdapter(mMusicLab.getMusicList());
        else {
            mMusicAdapter = new MusicAdapter(mAlbum.getMusicList());
        }
        mRecyclerView.setAdapter(mMusicAdapter);
        musicList = mMusicAdapter.mMusicList;
        if(savedInstanceState != null){
            favoriteMode = savedInstanceState.getBoolean("favMode");
            if(favoriteMode)
                setFavoriteMode(true);

        }
        return view;
    }

    @Override
    public void onResume() {
        if (mAlbum != null)
            getActivity().setTitle(mAlbum.getName());

        else {
            mMusicAdapter.notifyDataSetChanged();
            //setFavoriteMode(favoriteMode);
        }
        super.onResume();
    }

    private class MusicHolder extends RecyclerView.ViewHolder {

        private ImageView coverart;
        private TextView titleTextview, artistTextView;
        private Music mMusic;


        public MusicHolder(@NonNull final View itemView) {
            super(itemView);
            coverart = itemView.findViewById(R.id.item_song_cover);
            titleTextview = itemView.findViewById(R.id.item_song_title);
            artistTextView = itemView.findViewById(R.id.item_song_artist);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    if (mAlbum != null) {
                        intent = PlayerActivity.newIntent(new ArrayList<Music>(mAlbum.getMusicList()), getAdapterPosition(), getActivity());
                    } else
                        if(!favoriteMode)
                        intent = PlayerActivity.newIntent(new ArrayList<Music>(mMusicLab.getMusicList()), getAdapterPosition(), getActivity());
                    else intent =PlayerActivity.newIntent(new ArrayList<Music>(mMusicAdapter.mMusicList),getAdapterPosition(),getActivity());
                    startActivity(intent);


                }
            });

        }



        public void bindMusic(Music music) {
            mMusic = music;
            if (music.getCoverPath() != null)

            {

                Glide.with(getContext())
                        .load(new File(music.getCoverPath()))
                        .apply(new RequestOptions().override(300, 300))
                        .into(coverart);

            }

            titleTextview.setText(music.getTitle() != null ? music.getTitle() : "Unknon title");
            artistTextView.setText(music.getArtist() != null ? music.getArtist() : "Unknon Artist");
        }


    }

    private class MusicAdapter extends RecyclerView.Adapter<MusicHolder> {

        private List<Music> mMusicList;

        public MusicAdapter(List<Music> musicList) {
            mMusicList = musicList;
        }

        public void setMusicList(List<Music> musicList) {
            mMusicList = musicList;
        }

        @NonNull
        @Override
        public MusicHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.recyclerview_music_item, parent, false);
            return new MusicHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MusicHolder holder, final int position) {
            Music music = mMusicList.get(position);
            holder.bindMusic(music);


        }

        @Override
        public int getItemCount() {
            return mMusicList.size();

        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if(favoriteMode){
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.showmore));
            menu.getItem(0).setTitle("");
        }else {
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.heart_white));
            menu.getItem(0).setTitle("favoirtes");
        }

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.favoirite_button :{
                if(!favoriteMode){
                    setFavoriteMode(true);
                    item.setIcon(getResources().getDrawable(R.drawable.showmore));

                }else {
                    setFavoriteMode(false);
                    item.setIcon(getResources().getDrawable(R.drawable.heart_white));
                    item.setTitle("favorites");
                }
                return true;

            }

        }





        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        outState.putBoolean("favMode",favoriteMode);


        super.onSaveInstanceState(outState);
    }
}
