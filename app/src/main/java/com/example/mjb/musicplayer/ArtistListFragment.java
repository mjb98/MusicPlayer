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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mjb.musicplayer.model.Artist;
import com.example.mjb.musicplayer.model.MusicLab;
import com.example.mjb.todo.utils.PictureUtils;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArtistListFragment extends Fragment {
    public static final String MUSICLAB_TAG = "artist.musiclist";
    private RecyclerView mRecyclerView;
    private MusicLab mMusicLab;
    private ArtistAdapter mArtistAdapter;

    public ArtistListFragment() {
        // Required empty public constructor
    }

    public static ArtistListFragment newInstance() {

        Bundle args = new Bundle();

        ArtistListFragment fragment = new ArtistListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMusicLab = MusicLab.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mRecyclerView = view.findViewById(R.id.song_list_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mArtistAdapter = new ArtistAdapter(mMusicLab.getArtistList());
        mRecyclerView.setAdapter(mArtistAdapter);
        return view;
    }


    private class ArtistHolder extends RecyclerView.ViewHolder {

        private CircleImageView mCircleImageView;
        private TextView nameTextview, songsTextView;
        private Artist mArtist;


        public ArtistHolder(@NonNull View itemView) {
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

    private class ArtistAdapter extends RecyclerView.Adapter<ArtistHolder> {

        private List<Artist> mArtistList;

        public ArtistAdapter(List<Artist> artistList) {
            mArtistList = artistList;
        }

        public void setArtistList(List<Artist> artistList) {
            mArtistList = artistList;
        }

        @NonNull
        @Override
        public ArtistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.recyclerview_artist_item, parent, false);
            return new ArtistHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ArtistHolder holder, int position) {
            Artist artist = mArtistList.get(position);
            holder.bindArtist(artist);

        }

        @Override
        public int getItemCount() {
            return mArtistList.size();
        }
    }

}



