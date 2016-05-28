package com.ablanco.tonsofdamage.summoner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ablanco.teemo.model.staticdata.MasteryDto;
import com.ablanco.teemo.model.staticdata.MasteryTreeItemDto;
import com.ablanco.teemo.model.staticdata.MasteryTreeListDto;
import com.ablanco.teemo.model.summoners.Mastery;
import com.ablanco.teemo.model.summoners.MasteryPage;
import com.ablanco.teemo.utils.ImageUris;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.utils.SizeUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Álvaro Blanco on 26/05/2016.
 * TonsOfDamage
 */
public class MasteryTreeDetailItem extends LinearLayout {

    private final MasteryRequestListener listener;
    private int dp5 = SizeUtils.convertDpToPixel(5);
    private int dp10 = SizeUtils.convertDpToPixel(10);
    private int dp15 = SizeUtils.convertDpToPixel(15);
    private LayoutParams rowParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0, 1);
    private LayoutParams itemParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
    private Map<Integer, MasteryView> masteryViewMap = new HashMap<>();

    public interface MasteryRequestListener {
        MasteryDto getMastery(int id);
    }

    public MasteryTreeDetailItem(Context context, List<MasteryTreeListDto> tree, MasteryRequestListener listener) {
        super(context);

        this.listener = listener;



        itemParams.leftMargin = dp15;
        itemParams.rightMargin = dp15;
        this.setPadding(dp10, dp5, dp10, dp5);
        this.setWeightSum(tree.size());
        this.setGravity(Gravity.CENTER);
        setOrientation(VERTICAL);

        for (MasteryTreeListDto masteryTreeListDto : tree) {
            LinearLayout row = getRow();
            row.setWeightSum(masteryTreeListDto.getMasteryTreeItems().size());
            for (MasteryTreeItemDto masteryTreeItemDto : masteryTreeListDto.getMasteryTreeItems()) {
                row.addView(getMastery(masteryTreeItemDto));
            }
            this.addView(row);

        }

    }

    private LinearLayout getRow() {
        LinearLayout row = new LinearLayout(getContext());
        row.setLayoutParams(rowParams);
        row.setOrientation(HORIZONTAL);
        row.setGravity(Gravity.CENTER);
        row.setPadding(0, dp5, 0, dp5);
        return row;
    }

    @SuppressLint("InflateParams")
    private View getMastery(MasteryTreeItemDto item) {
        MasteryView view = new MasteryView(getContext());
        view.setLayoutParams(itemParams);
        if (item != null) {
            if (listener != null) {
                MasteryDto m = listener.getMastery(item.getMasteryId());
                view.setMastery(m);
                if (m != null && m.getImage() != null) {
                    view.setImageUrl(ImageUris.getMasteryIcon(SettingsHandler.getCDNVersion(getContext()), m.getImage().getFull()));
                }
                masteryViewMap.put(item.getMasteryId(), view);

            }
        } else {
            view.hide();
        }

        return view;
    }

    public void setSummonerMasteries(MasteryPage page) {
        MasteryView v;
        for (Integer integer : masteryViewMap.keySet()) {
            masteryViewMap.get(integer).reset();
        }
        for (Mastery mastery : page.getMasteries()) {
            v = masteryViewMap.get(mastery.getId());
            if (v != null) {
                v.setRank(mastery.getRank());
            }
        }
    }

    /*private MasteryView findViewWithId(int id) {
        ViewGroup row;
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof ViewGroup) {
                row = (ViewGroup) getChildAt(i);
                for (int j = 0; j < row.getChildCount(); j++) {
                    Log.d("FIND", "searching for " +  id + " in " + (row.getChildAt(j).getTag() != null ? row.getChildAt(j).getTag() : "NULL"));
                    if (row.getChildAt(j) != null
                            && row.getChildAt(j) instanceof MasteryView
                            && row.getChildAt(j).getTag() != null
                            && (int) row.getChildAt(j).getTag() == id) {
                        return (MasteryView) row.getChildAt(j);
                    }
                }
            }
        }

        return null;
    }*/
}
