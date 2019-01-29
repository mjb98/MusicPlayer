package com.example.mjb.musicplayer;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ThemedSpinnerAdapter;

import com.example.mjb.musicplayer.model.Album;
import com.example.mjb.musicplayer.model.Artist;
import com.example.mjb.musicplayer.model.Music;
import com.example.mjb.todo.utils.PictureUtils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class PlayFragment extends Fragment {
   private CircleImageView mCoverArt;
   private Button playButton,nextButton,previousButton;
   private SeekBar timeSeekBar;
   private   TextView elapsedTextView,remainingTextView,nameTextField,artistTextFiled;
   private MediaPlayer mMediaPlayer;
   private int totalTime;
   private List<Music> mMusicList;
   private Album mAlbum;
   private Artist mArtist;
   private Music mCurrentMusic;
   private int number;

    public static PlayFragment newInstance(Album album,int number) {

        Bundle args = new Bundle();
        args.putSerializable("albumseted",album);
        args.putInt("number",number);
        PlayFragment fragment = new PlayFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public static PlayFragment newInstance(Artist artist,int number) {

        Bundle args = new Bundle();
        args.putSerializable("artistseted",artist);
        args.putInt("number",number);
        PlayFragment fragment = new PlayFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public static PlayFragment newInstance(ArrayList<Music> musicList, int number) {

        Bundle args = new Bundle();
        args.putSerializable("musiclisted",  musicList);
        args.putInt("number",number);
        PlayFragment fragment = new PlayFragment();
        fragment.setArguments(args);
        return fragment;
    }




    public PlayFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);
        mAlbum = (Album) getArguments().getSerializable("albumseted");
        mArtist = (Artist) getArguments().getSerializable("artistseted");
        if(mArtist != null) {
            mMusicList = mArtist.getSongs();
        }
        if(mAlbum != null) {
            mMusicList = mAlbum.getMusicList();
        }

        if(mMusicList == null)
            mMusicList = (ArrayList<Music>) getArguments().getSerializable("musiclisted");

        playButton = view.findViewById(R.id.playbtn);
        nextButton = view.findViewById(R.id.next_button);
        previousButton = view.findViewById(R.id.previous_button);
        mCoverArt = view.findViewById(R.id.playing_imageview);
        timeSeekBar = view.findViewById(R.id.positionBar);
        elapsedTextView = view.findViewById(R.id.elapsTimeLabel2);
        remainingTextView = view.findViewById(R.id.remainingTimeLabel2);
        nameTextField = view.findViewById(R.id.musicname_playing);
        artistTextFiled = view.findViewById(R.id.artistname_playing);
        number = getArguments().getInt("number");
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChangeMusicTo((number+1)%mMusicList.size());
            }
        });
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeMusicTo((number-1)%mMusicList.size());
            }
        });

        timeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser)
                    mMediaPlayer.seekTo(progress);
                    timeSeekBar.setProgress(progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (mMediaPlayer != null){
                    try {
                        Message message = new Message();
                        message.what = mMediaPlayer.getCurrentPosition();
                        mHandler.sendMessage(message);
                        Thread.sleep(1000);


                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }

                }

            }
        });
        thread.start();


        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detectPlayButton();
                if(!mMediaPlayer.isPlaying())
                    mMediaPlayer.start();
                    else
                    mMediaPlayer.pause();


            }
        });

        return view;
    }
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int currentPositon = msg.what;
            timeSeekBar.setProgress(currentPositon);
            String elapsedTime = createTimeLabel(currentPositon);
            elapsedTextView.setText(elapsedTime);
            String remainingTime = createTimeLabel(totalTime-currentPositon);
            remainingTextView.setText("- "+remainingTime);
            if(currentPositon == totalTime) {
                mMediaPlayer.seekTo(0);
                mMediaPlayer.start();
            }

        }
    };

public String createTimeLabel(int time){

    String timelabel = "";
    int min = time/1000/60;
    int sec =  time/1000%60;

    timelabel = min + ":";

    if (sec<10)
        timelabel += "0";
    timelabel += sec;
return timelabel;

}

    @Override
    public void onPause() {
    mMediaPlayer.pause();
        super.onPause();
    }

    @Override
    public void onResume() {
   detectPlayButton();
        super.onResume();
    }
    private void ChangeMusicTo(int Number){

    mMediaPlayer.stop();
    number = Number;
    mCurrentMusic = mMusicList.get(number);
        artistTextFiled.setText(" -"+mCurrentMusic.getArtist());
        nameTextField.setText(mCurrentMusic.getTitle());
        mMediaPlayer.seekTo(0);
        mMediaPlayer.setVolume(0.5f,0.5f);
        mMediaPlayer.isLooping();
        totalTime = mMediaPlayer.getDuration();
        timeSeekBar.setMax(totalTime);
        mCoverArt.setImageBitmap(PictureUtils.getScaledBitmap(mCurrentMusic.getCoverPath() != "" ? mCurrentMusic.getCoverPath(),getActivity()));
    mMediaPlayer = MediaPlayer.create(getActivity(),Uri.fromFile(new File(mCurrentMusic.getPath())));
    mMediaPlayer.start();
    detectPlayButton();

    }
   private void detectPlayButton(){
       if(mMediaPlayer.isPlaying())
           playButton.setBackgroundResource(R.drawable.pause);
       else
           playButton.setBackgroundResource(R.drawable.play);

   }
}
