package com.ablanco.tonsofdamage.summoner;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ablanco.teemo.model.staticdata.MasteryDto;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.handler.NavigationHandler;
import com.ablanco.tonsofdamage.utils.Utils;
import com.ablanco.tonsofdamage.views.SquareRelativeLayout;
import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Álvaro Blanco on 28/05/2016.
 * TonsOfDamage
 */
public class MasteryView extends SquareRelativeLayout implements View.OnClickListener {

    @Bind(R.id.img)
    ImageView mImg;
    @Bind(R.id.tv_rank)
    TextView mTvRank;
    private int rank = 0;

    private MasteryDto mMastery;

    public MasteryView(Context context) {
        super(context);
        inflate(context, R.layout.view_item_mastery, this);
        ButterKnife.bind(this);
        Utils.applyGrayScaleFilter(mImg);
        this.setOnClickListener(this);

    }

    public void hide(){
        this.setVisibility(INVISIBLE);
    }

    public void setImageUrl(String url){
        Glide.with(getContext()).load(url).into(mImg);
    }

    public void setMastery(MasteryDto mastery){
        this.mMastery = mastery;
        Utils.setTransitionNameForView(this, getContext().getString(R.string.shared_transition, mMastery.getId()));

    }

    public void setRank(int rank){
        if(mMastery != null){
            this.rank = rank;
            Utils.resetColorFilter(mImg);
            this.mTvRank.setText(getContext().getString(R.string.mastery_rank, rank, mMastery.getRanks()));
            this.mTvRank.setVisibility(VISIBLE);
        }
    }

    public void reset(){
        Utils.applyGrayScaleFilter(mImg);
        mTvRank.setText("");
        mTvRank.setVisibility(GONE);
    }

    @Override
    public void onClick(View v) {
        if(mMastery != null){
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation((Activity) getContext(), this, getContext().getString(R.string.shared_transition, mMastery.getId()));
            Bundle bundle = new Bundle();
            bundle.putSerializable(MasteryDetailActivityDialog.EXTRA_MASTERY, mMastery);
            bundle.putSerializable(MasteryDetailActivityDialog.EXTRA_RANK, rank);
            NavigationHandler.navigateTo(getContext(), NavigationHandler.MASTERY_DETAIL, bundle, options);
        }
    }
}
