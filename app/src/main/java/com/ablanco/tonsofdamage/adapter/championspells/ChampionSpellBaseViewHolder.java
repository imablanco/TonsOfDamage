package com.ablanco.tonsofdamage.adapter.championspells;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.ui.activities.VideoPlayerActivity;

import im.ene.lab.toro.ToroVideoViewHolder;
import im.ene.lab.toro.widget.ToroVideoView;

/**
 * Created by Álvaro Blanco on 08/04/2016.
 * TonsOfDamage
 */
public abstract class ChampionSpellBaseViewHolder extends ToroVideoViewHolder {

    protected ImageView imgSpell;
    protected TextView tvAbilityName;
    protected TextView tvAbilityDescription;
    protected ImageView imgShadow;
    protected ImageView imgPlay;
    private View imgFullScreen;
    private android.os.Handler mHandler;
    protected Context mContext;

    private Runnable mRunnable;


    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if(event.getAction() == MotionEvent.ACTION_DOWN){
                if(isPlaying()){
                    pauseAnimate();
                    pause();
                }else {
                    playAnimate();
                    start();
                }
            }

            return false;
        }
    };

    private View.OnClickListener fullScreenListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, VideoPlayerActivity.class);
            intent.putExtra(VideoPlayerActivity.EXTRA_URL, getVideoUri());
            mContext.startActivity(intent);
        }
    };

    public final void pauseAnimate(){
        imgShadow.animate().alpha(1).setDuration(300).start();
        imgPlay.setImageResource(R.drawable.ic_play);
        imgPlay.animate().alpha(1).setDuration(300).start();
        imgFullScreen.animate().alpha(1).setDuration(300).start();
        imgFullScreen.setOnClickListener(fullScreenListener);
        mHandler.removeCallbacks(mRunnable);
    }

    public final void playAnimate(){
        imgShadow.animate().alpha(0).setDuration(300).start();
        imgPlay.setImageResource(R.drawable.ic_pause);
        mHandler.postDelayed(mRunnable, 300);
    }

    public final void restartAnimate(){
        imgShadow.animate().alpha(1).setDuration(300).start();
        imgPlay.setImageResource(R.drawable.ic_replay);
        imgPlay.animate().alpha(1).setDuration(300).start();
        imgFullScreen.animate().alpha(1).setDuration(300).start();
        imgFullScreen.setOnClickListener(fullScreenListener);
        mHandler.removeCallbacks(mRunnable);
    }

    public final void errorAnimate(){
        imgShadow.animate().alpha(1).setDuration(300).start();
        imgPlay.setImageResource(R.drawable.ic_link_error);
        imgPlay.animate().alpha(1).setDuration(300).start();
        imgFullScreen.animate().alpha(0).setDuration(300).start();
        imgFullScreen.setOnClickListener(null);
        mHandler.removeCallbacks(mRunnable);
    }


    public ChampionSpellBaseViewHolder(Context context, View itemView) {
        super(itemView);

        this.mContext = context;

        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                imgFullScreen.animate().alpha(0).setDuration(300).start();
                imgFullScreen.setOnClickListener(null);
                imgPlay.animate().alpha(0).setDuration(300).start();
            }
        };
        imgSpell = (ImageView) itemView.findViewById(R.id.img_spell);
        imgShadow = (ImageView) itemView.findViewById(R.id.img_shadow);
        imgPlay = (ImageView) itemView.findViewById(R.id.img_play);
        tvAbilityName = (TextView) itemView.findViewById(R.id.tv_ability_name);
        tvAbilityDescription = (TextView) itemView.findViewById(R.id.tv_spell_description);
        imgFullScreen = itemView.findViewById(R.id.ic_full_screen);
    }

    protected abstract String getVideoUri();

    @Override
    public void bind(@Nullable Object object) {
        mVideoView.setOnTouchListener(mOnTouchListener);
        mVideoView.setVideoURI(Uri.parse(getVideoUri()));
        imgFullScreen.setOnClickListener(fullScreenListener);
    }

    @Override
    protected final ToroVideoView findVideoView(View itemView) {
        return (ToroVideoView) itemView.findViewById(R.id.video);
    }

    @Nullable
    @Override
    public final String getVideoId() {
        return String.valueOf(getAdapterPosition());
    }


    @Override
    public final void onPlaybackError(MediaPlayer mp, int what, int extra) {
        super.onPlaybackError(mp, what, extra);
        errorAnimate();
    }

    @Override public final void onPlaybackStarted() {
        playAnimate();
    }

    @Override public final void onPlaybackPaused() {
        pauseAnimate();
    }

    @Override public final void onPlaybackStopped() {
        restartAnimate();
    }

    @Override public final boolean wantsToPlay() {
        return super.visibleAreaOffset() >= 0.9;
    }
}
