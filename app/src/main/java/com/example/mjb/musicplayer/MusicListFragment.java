package com.example.mjb.musicplayer;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mjb.musicplayer.model.Album;
import com.example.mjb.musicplayer.model.Music;
import com.example.mjb.musicplayer.model.MusicLab;
import com.example.mjb.todo.utils.PictureUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MusicListFragment extends Fragment {
    public static final String Album_Tag = "dsds";
    private RecyclerView mRecyclerView;
    private MusicLab mMusicLab;
    private Album mAlbum;
    private MusicAdapter mMusicAdapter;
    public static final String MUSICLAB_TAG = "musicplayer.musiclist";


    public MusicListFragment() {
        // Required empty public constructor
    }
    public static MusicListFragment newInstance(MusicLab MusicLab) {
        MusicListFragment fragment = new MusicListFragment();
        Bundle args = new Bundle();
        args.putSerializable(MUSICLAB_TAG,MusicLab);
        fragment.setArguments(args);
        return fragment;
    }
    public static MusicListFragment newInstance(Album album) {
        MusicListFragment fragment = new MusicListFragment();
        Bundle args = new Bundle();
        args.putSerializable(Album_Tag,album);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMusicLab = (MusicLab) getArguments().getSerializable(MUSICLAB_TAG);
        mAlbum = (Album) getArguments().getSerializable(Album_Tag);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mRecyclerView = view.findViewById(R.id.song_list_recyclerview);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),getResources().getInteger(R.integer.columns_count)));
        if(mMusicLab != null)
        mMusicAdapter = new MusicAdapter(mMusicLab.getMusicList());
        else {
            mMusicAdapter = new MusicAdapter(mAlbum.getMusicList());
        }
        mRecyclerView.setAdapter(mMusicAdapter);



        return view;
    }

    private class MusicHolder extends RecyclerView.ViewHolder {

        private ImageView coverart;
        private TextView titleTextview,artistTextView;
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
                    if(mAlbum != null) {
                        intent = Player.newIntent(new ArrayList<Music>(mAlbum.getMusicList()), getAdapterPosition(), getActivity());
                    }else
                        intent = Player.newIntent(new ArrayList<Music>(mMusicLab.getMusicList()),getAdapterPosition(),getActivity());
                    startActivity(intent);


                }
            });

        }
        public void bindMusic(Music music) {
            mMusic = music;
            if (music.getCoverPath() != null)
            coverart.setImageBitmap(PictureUtils.getScaledBitmap(music.getCoverPath(),400,400));
            titleTextview.setText(music.getTitle() != null ? music.getTitle() : "Unknon title");
            artistTextView.setText(music.getArtist() != null ? music.getArtist() : "Unknon Artist");
        }

    }
    private class MusicAdapter extends  RecyclerView.Adapter<MusicHolder>{

        private List<Music> mMusicList;

        public void setMusicList(List<Music> musicList) {
            mMusicList = musicList;
        }

        public MusicAdapter(List<Music> musicList) {
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
    public void onResume() {
        if (mAlbum != null)
            getActivity().setTitle(mAlbum.getName());
        super.onResume();
    }


}
