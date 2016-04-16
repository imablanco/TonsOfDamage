package com.ablanco.tonsofdamage.handler;

import android.content.Context;

import com.ablanco.teemo.Teemo;
import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.constants.StaticAPIQueryParams;
import com.ablanco.teemo.model.staticdata.ChampionDto;
import com.ablanco.teemo.model.staticdata.ChampionListDto;
import com.ablanco.teemo.model.staticdata.ItemDto;
import com.ablanco.teemo.model.staticdata.ItemListDto;
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.tonsofdamage.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Álvaro Blanco on 16/04/2016.
 * TonsOfDamage
 */
public class StaticDataHandler {

    public interface ResponseListener<T> {
        void onResponse(T response);

        void onError(TeemoException e);
    }

    private static StaticDataHandler mInstance;

    private List<ChampionDto> mChampions;
    private List<ItemDto> mItems;

    public static StaticDataHandler getInstance() {
        if (mInstance == null) {
            mInstance = new StaticDataHandler();
        }

        return mInstance;
    }

    private StaticDataHandler() {
        this.mChampions = new ArrayList<>();
        this.mItems = new ArrayList<>();
    }

    public void getChampions(Context context, final ResponseListener<List<ChampionDto>> listener) {
        if (!mChampions.isEmpty()) {
            listener.onResponse(mChampions);
        } else {
            Teemo.getInstance(context).getStaticDataHandler().getChampions(Locale.getDefault().toString(), null, null,
                    Utils.buildStaticQueryParams(StaticAPIQueryParams.Champions.IMAGE, StaticAPIQueryParams.Champions.INFO, StaticAPIQueryParams.Champions.TAGS, StaticAPIQueryParams.Champions.SKINS),
                    new ServiceResponseListener<ChampionListDto>() {
                        @Override
                        public void onResponse(ChampionListDto response) {
                            mChampions.clear();
                            if (response != null && !response.getData().isEmpty()) {
                                mChampions.addAll(response.getData().values());
                                listener.onResponse(mChampions);
                            } else {
                                listener.onResponse(new ArrayList<ChampionDto>());
                            }

                        }

                        @Override
                        public void onError(TeemoException e) {
                            listener.onError(e);
                        }
                    });
        }

    }

    public void getItems(Context context, final ResponseListener<List<ItemDto>> listener) {
        if (!mItems.isEmpty()) {
            listener.onResponse(mItems);
        } else {
            Teemo.getInstance(context).getStaticDataHandler().getItems(Locale.getDefault().toString(), null, Utils.buildStaticQueryParams(StaticAPIQueryParams.Items.tags, StaticAPIQueryParams.Items.gold), new ServiceResponseListener<ItemListDto>() {
                @Override
                public void onResponse(ItemListDto response) {
                    mItems.clear();
                    if (response != null && !response.getData().isEmpty()) {
                        mItems.addAll(response.getData().values());
                        listener.onResponse(mItems);

                    } else {
                        listener.onResponse(new ArrayList<ItemDto>());
                    }
                }

                @Override
                public void onError(TeemoException e) {
                    listener.onError(e);
                }
            });
        }
    }
}
