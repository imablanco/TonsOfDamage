package com.ablanco.tonsofdamage.summoner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ablanco.teemo.model.staticdata.RuneDto;
import com.ablanco.teemo.utils.ImageUris;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.utils.Utils;
import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Álvaro Blanco Cabrero on 5/6/16
 * TonsOfDamage
 */
public class RuneDetailDialogActivity extends AppCompatActivity {

    public static final String EXTRA_RUNE = "rune_obj";

    @Bind(R.id.img_rune)
    ImageView mImgRune;
    @Bind(R.id.tv_rune_description)
    TextView mTvRuneDescription;
    @Bind(R.id.root)
    RelativeLayout mRoot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_rune_detail);
        ButterKnife.bind(this);
        RuneDto mRune = (RuneDto) getIntent().getSerializableExtra(EXTRA_RUNE);

        if(mRune != null){
            Utils.setTransitionNameForView(mImgRune, String.valueOf(mRune.getId()));
            Glide.with(this).load(ImageUris.getRuneIcon(SettingsHandler.getCDNVersion(this), mRune.getImage().getFull())).into(mImgRune);

            mTvRuneDescription.setText(mRune.getDescription());

        }

        mRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
    }
}
