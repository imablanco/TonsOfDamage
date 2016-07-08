package com.ablanco.tonsofdamage.runecreator;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ablanco.teemo.Teemo;
import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.constants.StaticAPIQueryParams;
import com.ablanco.teemo.model.staticdata.RuneDto;
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.teemo.utils.ImageUris;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Álvaro Blanco on 05/07/2016.
 * TonsOfDamage
 */
public class RuneStepTemplateSingle extends RelativeLayout {

    @Bind(R.id.img_rune)
    ImageView mImgRune;
    @Bind(R.id.tv_rune_name)
    TextView mTvRuneName;
    @Bind(R.id.tv_rune_details)
    TextView mTvRuneDetails;
    @Bind(R.id.tv_rune_price)
    TextView mTvRunePrice;
    @Bind(R.id.tv_explain)
    TextView mTvExplain;

    public RuneStepTemplateSingle(Context context) {
        this(context, null);
    }

    public RuneStepTemplateSingle(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.view_rune_creator_step_template_single, this);
        ButterKnife.bind(this);
    }

    public void setInfo(String descriptionText, int runeId, final int runeAmount){
        mTvExplain.setText(descriptionText);

        Teemo.getInstance(getContext()).getStaticDataHandler().getRuneById(runeId, SettingsHandler.getLanguage(getContext()), null,
                StaticAPIQueryParams.Runes.all, new ServiceResponseListener<RuneDto>() {
                    @Override
                    public void onResponse(RuneDto response) {
                        Glide.with(getContext()).load(ImageUris.getRuneIcon(SettingsHandler.getCDNVersion(getContext()), response.getImage().getFull())).into(mImgRune);
                        mTvRuneName.setText(response.getName());
                        mTvRuneDetails.setText(response.getDescription());
                        mTvRunePrice.setText(String.valueOf(runeAmount));
                    }

                    @Override
                    public void onError(TeemoException e) {

                    }
                });
    }


}
