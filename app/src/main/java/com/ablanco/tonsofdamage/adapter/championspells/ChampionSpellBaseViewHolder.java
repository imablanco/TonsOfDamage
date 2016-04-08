package com.ablanco.tonsofdamage.adapter.championspells;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ablanco.tonsofdamage.R;

import im.ene.lab.toro.ToroVideoViewHolder;
import im.ene.lab.toro.widget.ToroVideoView;

/**
 * Created by Álvaro Blanco on 08/04/2016.
 * TonsOfDamage
 */
public class ChampionSpellBaseViewHolder extends ToroVideoViewHolder {

    protected ImageView imgSpell;
    protected TextView tvAbilityName;
    protected TextView tvAbilityDescription;
    protected ImageView imgShadow;
    protected ImageView imgPlay;
    private android.os.Handler mHandler;

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

    public final void pauseAnimate(){
        imgShadow.animate().alpha(1).setDuration(300).start();
        imgPlay.setImageResource(R.drawable.ic_play);
        imgPlay.animate().alpha(1).setDuration(300).start();
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
        mHandler.removeCallbacks(mRunnable);
    }

    public final void errorAnimate(){
        imgShadow.animate().alpha(1).setDuration(300).start();
        imgPlay.setImageResource(R.drawable.ic_link_error);
        imgPlay.animate().alpha(1).setDuration(300).start();
        mHandler.removeCallbacks(mRunnable);
    }


    public ChampionSpellBaseViewHolder(View itemView) {
        super(itemView);

        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                imgPlay.animate().alpha(0).setDuration(300).start();
            }
        };
        imgSpell = (ImageView) itemView.findViewById(R.id.img_spell);
        imgShadow = (ImageView) itemView.findViewById(R.id.img_shadow);
        imgPlay = (ImageView) itemView.findViewById(R.id.img_play);
        tvAbilityName = (TextView) itemView.findViewById(R.id.tv_ability_name);
        tvAbilityDescription = (TextView) itemView.findViewById(R.id.tv_spell_description);

    }

    @Override
    public void bind(@Nullable Object object) {
        mVideoView.setOnTouchListener(mOnTouchListener);
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
