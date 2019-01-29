package com.example.mjb.musicplayer;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mjb.musicplayer.model.Album;
import com.example.mjb.musicplayer.model.MusicLab;
import com.example.mjb.todo.utils.PictureUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private MusicLab mMusicLab;
    private AlbumAdapter mAlbumAdapter;

    public static final String MUSICLAB_TAG = "album.musiclist";

    public static AlbumListFragment newInstance(MusicLab Musiclab) {

        Bundle args = new Bundle();
        args.putSerializable(MUSICLAB_TAG,Musiclab);
        AlbumListFragment fragment = new AlbumListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public AlbumListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMusicLab = (MusicLab) getArguments().getSerializable(MUSICLAB_TAG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mRecyclerView = view.findViewById(R.id.song_list_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAlbumAdapter = new AlbumAdapter(mMusicLab.getAlbumList());
        mRecyclerView.setAdapter(mAlbumAdapter);
        return view;
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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = ArtistViewActivity.newIntent(mAlbum,getActivity());
                    startActivity(intent);
                }
            });
        }
        public void bindArtist(Album album) {
            mAlbum = album;
            nameTextview.setText(album.getName());
            songsTextView.setText(album.getArtist());
            if(album.getMusicList().get(0).getCoverPath() != null){
                mCircleImageView.setImageBitmap(PictureUtils.getScaledBitmap(album.getMusicList().get(0).getCoverPath(),getActivity()));
            }
        }

    }
    private class AlbumAdapter extends  RecyclerView.Adapter<AlbumHolder>{

        private List<Album> mAlbumList;

        public AlbumAdapter(List<Album> albumList) {
            mAlbumList = albumList;
        }

        public void setArtistList(List<Album> albumList) {
            mAlbumList = albumList;
        }

        @NonNull
        @Override
        public AlbumHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.recyclerview_artist_item, parent, false);
            return new AlbumHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AlbumHolder holder, int position) {
            Album album = mAlbumList.get(position);
            holder.bindArtist(album);

        }

        @Override
        public int getItemCount() {
            return mAlbumList.size();
        }
    }


}



