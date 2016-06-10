package com.ablanco.tonsofdamage.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ablanco.teemo.Teemo;
import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.constants.StaticAPIQueryParams;
import com.ablanco.teemo.model.staticdata.ChampionDto;
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.teemo.utils.ImageUris;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.champions.ChampionDetailActivity;
import com.ablanco.tonsofdamage.handler.NavigationHandler;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.utils.Utils;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Álvaro Blanco Cabrero on 6/6/16
 * TonsOfDamage
 */
@SuppressLint("ViewConstructor")
public class FavoriteChampionsPlaceholderItem extends LinearLayout implements View.OnClickListener {


    @Bind(R.id.img_champion_left)
    CircleImageView imgChampionLeft;
    @Bind(R.id.img_champion_middle)
    CircleImageView imgChampionMiddle;
    @Bind(R.id.img_champion_right)
    CircleImageView imgChampionRight;
    @Bind(R.id.cv_left)
    CardView cvLeft;
    @Bind(R.id.cv_middle)
    CardView cvMiddle;
    @Bind(R.id.cv_right)
    CardView cvRight;

    @Bind(R.id.tv_champion_left_name)
    TextView tvChampionLeftName;
    @Bind(R.id.tv_champion_left_tags)
    TextView tvChampionLeftTags;
    @Bind(R.id.tv_champion_middle_name)
    TextView tvChampionMiddleName;
    @Bind(R.id.tv_champion_middle_tags)
    TextView tvChampionMiddleTags;
    @Bind(R.id.tv_champion_right_name)
    TextView tvChampionRightName;
    @Bind(R.id.tv_champion_right_tags)
    TextView tvChampionRightTags;

    private final List<String> champIds;


    public FavoriteChampionsPlaceholderItem(Context context, List<String> champIds) {
        super(context);

        inflate(context, R.layout.item_ph_favorite_champion, this);
        ButterKnife.bind(this);

        setOrientation(HORIZONTAL);

        this.champIds = champIds;

        if (champIds.size() >= 1 && champIds.get(0) != null) {
            Teemo.getInstance(context).getStaticDataHandler().getChampionById(Integer.parseInt(champIds.get(0)), SettingsHandler.getLanguage(context), null,
                    new StaticAPIQueryParams.StaticQueryParamsBuilder().include(StaticAPIQueryParams.Champions.IMAGE).include(StaticAPIQueryParams.Champions.TAGS).build(), new ServiceResponseListener<ChampionDto>() {
                        @Override
                        public void onResponse(ChampionDto response) {
                            if (Utils.isContextValid(getContext())) {
                                cvLeft.setVisibility(VISIBLE);
                                cvLeft.setOnClickListener(FavoriteChampionsPlaceholderItem.this);
                                Glide.with(getContext()).load(ImageUris.getChampionSquareIcon(SettingsHandler.getCDNVersion(getContext()), response.getImage().getFull())).into(imgChampionLeft);
                                tvChampionLeftName.setText(response.getName());
                                buildTags(tvChampionLeftTags, response.getTags());
                            }

                        }

                        @Override
                        public void onError(TeemoException e) {

                        }
                    });
        }


        if (champIds.size() >= 2 && champIds.get(1) != null) {
            Teemo.getInstance(context).getStaticDataHandler().getChampionById(Integer.parseInt(champIds.get(1)), SettingsHandler.getLanguage(context), null,
                    new StaticAPIQueryParams.StaticQueryParamsBuilder().include(StaticAPIQueryParams.Champions.IMAGE).include(StaticAPIQueryParams.Champions.TAGS).build(), new ServiceResponseListener<ChampionDto>() {
                        @Override
                        public void onResponse(ChampionDto response) {
                            if (Utils.isContextValid(getContext())) {
                                cvMiddle.setVisibility(VISIBLE);
                                cvMiddle.setOnClickListener(FavoriteChampionsPlaceholderItem.this);
                                Glide.with(getContext()).load(ImageUris.getChampionSquareIcon(SettingsHandler.getCDNVersion(getContext()), response.getImage().getFull())).into(imgChampionMiddle);
                                tvChampionMiddleName.setText(response.getName());
                                buildTags(tvChampionMiddleTags, response.getTags());
                            }
                        }

                        @Override
                        public void onError(TeemoException e) {

                        }
                    });
        }

        if (champIds.size() >= 3 && champIds.get(2) != null) {
            Teemo.getInstance(context).getStaticDataHandler().getChampionById(Integer.parseInt(champIds.get(2)), SettingsHandler.getLanguage(context), null,
                    new StaticAPIQueryParams.StaticQueryParamsBuilder().include(StaticAPIQueryParams.Champions.IMAGE).include(StaticAPIQueryParams.Champions.TAGS).build(), new ServiceResponseListener<ChampionDto>() {
                        @Override
                        public void onResponse(ChampionDto response) {
                            if (Utils.isContextValid(getContext())) {
                                cvRight.setVisibility(VISIBLE);
                                cvRight.setOnClickListener(FavoriteChampionsPlaceholderItem.this);
                                Glide.with(getContext()).load(ImageUris.getChampionSquareIcon(SettingsHandler.getCDNVersion(getContext()), response.getImage().getFull())).into(imgChampionRight);
                                tvChampionRightName.setText(response.getName());
                                buildTags(tvChampionRightTags, response.getTags());
                            }
                        }

                        @Override
                        public void onError(TeemoException e) {

                        }
                    });
        }

    }

    private void buildTags(TextView tv, List<String> tags) {
        StringBuilder b = new StringBuilder();

        for (String tag : tags) {
            if (!TextUtils.isEmpty(b)) {
                b.append(", ");
            }

            b.append(tag);
        }

        tv.setText(b.toString());
    }

    @Override
    public void onClick(View v) {

        int id = 0;
        if (v == cvLeft) {
            id = Integer.parseInt(champIds.get(0));
        } else if (v == cvMiddle) {
            id = Integer.parseInt(champIds.get(1));
        } else if (v == cvRight) {
            id = Integer.parseInt(champIds.get(2));

        }

        if (id > 0) {
            Bundle bundle = new Bundle();
            bundle.putInt(ChampionDetailActivity.EXTRA_CHAMPION_ID, id);
            NavigationHandler.navigateTo(getContext(), NavigationHandler.CHAMPION_DETAIL, bundle);
        }

    }
}
