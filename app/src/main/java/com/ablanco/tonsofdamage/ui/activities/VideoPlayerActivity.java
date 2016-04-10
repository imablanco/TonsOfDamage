package com.ablanco.tonsofdamage.ui.activities;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.VideoView;

import com.ablanco.tonsofdamage.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Álvaro Blanco on 08/04/2016.
 * TonsOfDamage
 */
public class VideoPlayerActivity extends Activity implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, SeekBar.OnSeekBarChangeListener, View.OnTouchListener {

    public final static String EXTRA_URL = "extra_url";

    private final static int PROGRESS_UPDATE_TIME = 150;
    private final static int CONTROLS_DIM_TIME = 400;

    @Bind(R.id.video)
    VideoView videoView;
    @Bind(R.id.loading)
    ProgressBar mLoading;
    @Bind(R.id.img_play)
    ImageView mImgPlay;
    @Bind(R.id.seek_progress)
    SeekBar mSeekProgress;
    @Bind(R.id.img_shadow)
    View mImgShadow;

    private Runnable mProgressRunnable = new Runnable() {
        @Override
        public void run() {
            if (videoView != null && videoView.isPlaying()) {
                mSeekProgress.setProgress(videoView.getCurrentPosition());
                mProgressHandler.postDelayed(this, PROGRESS_UPDATE_TIME);
            }

        }
    };

    private Handler mProgressHandler = new Handler();


    private Runnable mControlsRunnable = new Runnable() {
        @Override
        public void run() {
            mImgPlay.animate().alpha(0).setDuration(CONTROLS_DIM_TIME);
        }
    };

    private Handler mControlsHandler = new Handler();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        ButterKnife.bind(this);


        String mUrl = getIntent().getStringExtra(EXTRA_URL);
        if (mUrl != null) {
            videoView.setVideoURI(Uri.parse(mUrl));
            videoView.setOnCompletionListener(this);
            videoView.setOnErrorListener(this);
            videoView.setOnPreparedListener(this);
            videoView.start();
        } else {
            mLoading.setVisibility(View.GONE);
            mImgPlay.setImageResource(R.drawable.ic_link_error);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.start();

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mImgShadow.animate().alpha(1).setDuration(CONTROLS_DIM_TIME);
        mSeekProgress.animate().alpha(0).setDuration(CONTROLS_DIM_TIME);
        mImgPlay.setImageResource(R.drawable.ic_replay);
        mImgPlay.animate().alpha(1).setDuration(CONTROLS_DIM_TIME);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mLoading.setVisibility(View.GONE);
        mImgShadow.animate().alpha(1).setDuration(CONTROLS_DIM_TIME);
        mSeekProgress.animate().alpha(0).setDuration(CONTROLS_DIM_TIME);
        mImgPlay.setImageResource(R.drawable.ic_link_error);
        mImgPlay.animate().alpha(1).setDuration(CONTROLS_DIM_TIME);

        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mLoading.setVisibility(View.GONE);
        mImgShadow.animate().alpha(0).setDuration(CONTROLS_DIM_TIME);
        mSeekProgress.animate().alpha(1).setDuration(CONTROLS_DIM_TIME);
        mSeekProgress.setProgress(0);
        mSeekProgress.setMax(videoView.getDuration());
        mSeekProgress.setOnSeekBarChangeListener(this);
        videoView.setOnTouchListener(this);
        mProgressHandler.post(mProgressRunnable);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser && videoView != null) {
            videoView.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //nothing
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //nothing
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && videoView != null) {
            if (videoView.isPlaying()) {
                mImgPlay.setImageResource(R.drawable.ic_play);
                videoView.pause();
                mImgPlay.animate().alpha(1).setDuration(CONTROLS_DIM_TIME);
                mImgShadow.animate().alpha(1).setDuration(CONTROLS_DIM_TIME);
                mSeekProgress.animate().alpha(0).setDuration(CONTROLS_DIM_TIME);
                mControlsHandler.removeCallbacks(mControlsRunnable);
            } else {
                mImgPlay.setImageResource(R.drawable.ic_pause);
                videoView.start();
                mProgressHandler.post(mProgressRunnable);
                mImgShadow.animate().alpha(0).setDuration(CONTROLS_DIM_TIME);
                mSeekProgress.animate().alpha(1).setDuration(CONTROLS_DIM_TIME);
                mControlsHandler.postDelayed(mControlsRunnable, 800);
            }
        }
        return false;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
