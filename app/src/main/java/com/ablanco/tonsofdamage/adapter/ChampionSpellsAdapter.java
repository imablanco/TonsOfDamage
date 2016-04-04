package com.ablanco.tonsofdamage.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ablanco.teemo.model.staticdata.ChampionSpellDto;
import com.ablanco.teemo.utils.ImageUris;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.utils.Utils;
import com.bumptech.glide.Glide;

import java.util.List;

import im.ene.lab.toro.ToroAdapter;
import im.ene.lab.toro.ToroVideoViewHolder;
import im.ene.lab.toro.widget.ToroVideoView;

/**
 * Created by Álvaro Blanco on 04/04/2016.
 * TonsOfDamage
 */
public class ChampionSpellsAdapter extends ToroAdapter<ChampionSpellsAdapter.ChampionViewHolder> {

    private List<ChampionSpellDto> spells;
    private Context context;
    private int championId;

    public ChampionSpellsAdapter(Context context, int championId, List<ChampionSpellDto> spells){
        this.context = context;
        this.spells = spells;
        this.championId = championId;
    }
    @Override
    public ChampionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChampionViewHolder(LayoutInflater.from(context).inflate(R.layout.item_champion_spell, parent, false));
    }

    @Nullable
    @Override
    protected Object getItem(int position) {
        return spells.get(position);
    }

    @Override
    public int getItemCount() {
        return spells.size();
    }

    class ChampionViewHolder extends ToroVideoViewHolder {

        private ImageView imgSpell;
        private TextView tvAbilityName;
        private TextView tvAbilityCost;
        private TextView tvAbilityRange;
        private TextView tvAbilityCooldown;
        private TextView tvAbilityDescription;
        private ImageView imgShadow;
        private ImageView imgPlay;
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

        public void pauseAnimate(){
            imgShadow.animate().alpha(1).setDuration(300).start();
            imgPlay.setImageResource(R.drawable.ic_play);
            imgPlay.animate().alpha(1).setDuration(300).start();
            mHandler.removeCallbacks(mRunnable);
        }

        public void playAnimate(){
            imgShadow.animate().alpha(0).setDuration(300).start();
            imgPlay.setImageResource(R.drawable.ic_pause);
            mHandler.postDelayed(mRunnable, 300);
        }

        public void restartAnimate(){
            imgShadow.animate().alpha(1).setDuration(300).start();
            imgPlay.setImageResource(R.drawable.ic_replay);
            imgPlay.animate().alpha(1).setDuration(300).start();
            mHandler.removeCallbacks(mRunnable);
            start();
        }


        public ChampionViewHolder(View itemView) {
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
            tvAbilityCost = (TextView) itemView.findViewById(R.id.tv_spell_cost);
            tvAbilityRange = (TextView) itemView.findViewById(R.id.tv_spell_range);
            tvAbilityCooldown = (TextView) itemView.findViewById(R.id.tv_spell_cooldown);
            tvAbilityDescription = (TextView) itemView.findViewById(R.id.tv_spell_description);

        }

        @Override
        public void bind(@Nullable Object object) {
            // TODO: 04/04/2016 pending open video in external activity

            Log.d("ChampionViewHolder", "binded " + getAdapterPosition());
            int position = getAdapterPosition();
            ChampionSpellDto spell = spells.get(position);
            mVideoView.setVideoURI(Uri.parse(Utils.getChampionAbilityVideoUrl(championId, position + 1)));
            Glide.with(context).load(ImageUris.getChampionAbilityIcon(spell.getImage().getFull())).into(imgSpell);

            tvAbilityName.setText(spell.getName());
            tvAbilityCooldown.setText(spell.getCooldownBurn());
            tvAbilityCost.setText(spell.getCostBurn());
            tvAbilityRange.setText(spell.getRangeBurn());
            tvAbilityDescription.setText(spell.getDescription());

            mVideoView.setOnTouchListener(mOnTouchListener);
        }

        @Override
        protected ToroVideoView findVideoView(View itemView) {
            return (ToroVideoView) itemView.findViewById(R.id.video);
        }

        @Nullable
        @Override
        public String getVideoId() {
            return String.valueOf(getAdapterPosition());
        }


        @Override public void onPlaybackStarted() {
            playAnimate();
        }

        @Override public void onPlaybackPaused() {
            pauseAnimate();
        }

        @Override public void onPlaybackStopped() {
            restartAnimate();
        }

        @Override public boolean wantsToPlay() {
            return super.visibleAreaOffset() >= 0.9;
        }
    }
}
