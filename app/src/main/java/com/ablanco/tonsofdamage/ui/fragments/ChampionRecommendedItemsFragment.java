package com.ablanco.tonsofdamage.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ablanco.teemo.model.staticdata.BlockDto;
import com.ablanco.teemo.model.staticdata.ChampionDto;
import com.ablanco.teemo.model.staticdata.RecommendedDto;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.ui.views.RecommendedItemsView;
import com.ablanco.tonsofdamage.utils.Constants;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Álvaro Blanco on 09/04/2016.
 * TonsOfDamage
 */
public class ChampionRecommendedItemsFragment extends ChampionDetailBaseFragment {

    @Bind(R.id.spinner_map)
    Spinner mSpinnerMap;
    @Bind(R.id.img_map)
    ImageView mImgMap;
    @Bind(R.id.tv_map_name)
    TextView mTvMapName;
    @Bind(R.id.layout_recommended_items)
    LinearLayout mLayoutRecommendedItems;

    private Map<String, List<RecommendedDto>> mRecommendedByMap;
    private Map<String, String> mNameAcronymMap;
    private ArrayList<String> mNames;

    public static Fragment newInstance(ChampionDto champion) {
        ChampionDetailBaseFragment f = new ChampionRecommendedItemsFragment();
        f.setChampion(champion);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNames = new ArrayList<>();
        mNameAcronymMap = new HashMap<>();
        mRecommendedByMap = new HashMap<>();

        for (RecommendedDto recommendedDto : mChampion.getRecommended()) {
            if (mRecommendedByMap.get(recommendedDto.getMap()) == null) {
                mNames.add(Constants.Maps.getNameForAcronym(recommendedDto.getMap()));
                mNameAcronymMap.put(Constants.Maps.getNameForAcronym(recommendedDto.getMap()), recommendedDto.getMap());
                mRecommendedByMap.put(recommendedDto.getMap(), new ArrayList<RecommendedDto>());
            }

            mRecommendedByMap.get(recommendedDto.getMap()).add(recommendedDto);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_champion_recommended, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayAdapter spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerMap.setAdapter(spinnerAdapter);
        mSpinnerMap.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                Glide.with(getActivity()).load(Constants.Maps.getMapBackgroundImageUrl(mNameAcronymMap.get(mNames.get(position)))).into(mImgMap);
                mTvMapName.setText(mNames.get(position));

                final String acronym = mNameAcronymMap.get(mNames.get(position));
                mLayoutRecommendedItems.removeAllViews();

                Observable.from(mRecommendedByMap.get(acronym)).filter(new Func1<RecommendedDto, Boolean>() {
                    @Override
                    public Boolean call(RecommendedDto recommendedDto) {
                        if(recommendedDto.getTitle().equalsIgnoreCase(Constants.Maps.HOWLING_ABYSS_BLOCK_ITEM_LITERAL)){
                            return true;
                        }else{getView()
                            return recommendedDto.getTitle().equalsIgnoreCase(mChampion.getName().concat(acronym));
                        }
                    }
                }).subscribe(new Action1<RecommendedDto>() {
                    @Override
                    public void call(RecommendedDto recommendedDto) {
                        RecommendedItemsView recommendedItemsView;
                        recommendedItemsView  = new RecommendedItemsView(getActivity());
                        recommendedItemsView.setTitle(getString(R.string.recommended_items));
                        for (BlockDto block : recommendedDto.getBlocks()){
                            recommendedItemsView.addItemsBlock(block);
                        }

                        mLayoutRecommendedItems.addView(recommendedItemsView);

                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
