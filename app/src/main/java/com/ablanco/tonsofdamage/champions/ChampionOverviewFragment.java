package com.ablanco.tonsofdamage.champions;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ablanco.teemo.Teemo;
import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.model.championmastery.ChampionMasteryDto;
import com.ablanco.teemo.model.staticdata.ChampionDto;
import com.ablanco.teemo.model.staticdata.StatsDto;
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.utils.SizeUtils;
import com.ablanco.tonsofdamage.utils.Utils;
import com.daasuu.ahp.AnimateHorizontalProgressBar;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.DecoDrawEffect;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import butterknife.Bind;
import uk.co.chrisjenx.calligraphy.CalligraphyUtils;

/**
 * Created by Álvaro Blanco on 04/04/2016.
 * TonsOfDamage
 */
public class ChampionOverviewFragment extends ChampionDetailBaseFragment {

    @Bind(R.id.pg_attack)
    AnimateHorizontalProgressBar pgAttack;

    @Bind(R.id.pg_tank)
    AnimateHorizontalProgressBar pgTank;

    @Bind(R.id.pg_magic)
    AnimateHorizontalProgressBar pgMagic;

    @Bind(R.id.pg_difficult)
    AnimateHorizontalProgressBar pgDifficult;

    @Bind(R.id.tv_health)
    TextView tvHealth;
    @Bind(R.id.tv_health_regen)
    TextView tvHealthRegen;
    @Bind(R.id.tv_mana)
    TextView tvMana;
    @Bind(R.id.tv_mana_regen)
    TextView tvManaRegen;
    @Bind(R.id.tv_attack)
    TextView tvAttack;
    @Bind(R.id.tv_attack_speed)
    TextView tvAttackSpeed;
    @Bind(R.id.tv_armor)
    TextView tvArmor;
    @Bind(R.id.tv_magic_resist)
    TextView tvMagicResist;
    @Bind(R.id.tv_range)
    TextView tvRange;
    @Bind(R.id.tv_movement)
    TextView tvMovement;
    @Bind(R.id.tv_crit)
    TextView tvCrit;

    @Bind(R.id.tv_lore)
    TextView tvLore;
    @Bind(R.id.layout_allytips)
    LinearLayout mLayoutAllytips;
    @Bind(R.id.layout_enemytipstips)
    LinearLayout mLayoutEnemytipstips;
    @Bind(R.id.dv_champion_mastery)
    DecoView dvChampionMastery;
    @Bind(R.id.tv_champion_level)
    TextView tvChampionLevel;
    @Bind(R.id.cv_champion_mastery)
    View cvChampionMastery;
    @Bind(R.id.tv_champion_points)
    TextView tvChampionPoints;
    @Bind(R.id.tv_total_exp_needed)
    TextView tvExpNeeded;

    public static Fragment newInstance(ChampionDto champion) {
        ChampionDetailBaseFragment f = new ChampionOverviewFragment();
        f.setChampion(champion);
        return f;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       return inflater.inflate(R.layout.activity_champion_overview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fillChampionMastery();
        fillAttributes();
        fillStats();
        fillTips();
        fillLore();
    }

    private void fillChampionMastery() {
        Teemo.getInstance(getActivity()).getChampionMasteryHandler().getChampionMastery(Utils.getPlatformForRegion(getActivity()), SettingsHandler.getSummoner(getActivity()), mChampion.getId().longValue(), new ServiceResponseListener<ChampionMasteryDto>() {
            @Override
            public void onResponse(ChampionMasteryDto response) {
                if (getActivity() != null) {

                    cvChampionMastery.setVisibility(View.VISIBLE);
                    long max = response.getChampionPointsSinceLastLevel() + response.getChampionPointsUntilNextLevel();
                    SeriesItem championTotalMasteryItem = new SeriesItem.Builder(ContextCompat.getColor(getActivity(), R.color.colorAccent))
                            .setRange(0, max, 0)
                            .setInitialVisibility(false)
                            .setCapRounded(false)
                            .setInterpolator(new AccelerateDecelerateInterpolator())
                            .build();

                    SeriesItem championSinceMasteryItem = new SeriesItem.Builder(ContextCompat.getColor(getActivity(), R.color.magenta))
                            .setRange(0, max, 0)
                            .setInitialVisibility(false)
                            .setCapRounded(false)
                            .setInterpolator(new AccelerateDecelerateInterpolator())
                            .build();


                    int index = dvChampionMastery.addSeries(championTotalMasteryItem);
                    startAnimation(dvChampionMastery, index, 300, 1500, 800, max);
                    index = dvChampionMastery.addSeries(championSinceMasteryItem);
                    startAnimation(dvChampionMastery, index, 600, 1500, 800, response.getChampionPointsSinceLastLevel());

                    tvChampionPoints.setText(String.valueOf(response.getChampionPoints()));
                    tvChampionLevel.setText(String.valueOf(response.getChampionLevel()));
                    tvExpNeeded.setText(getString(R.string.total_exp_needed, response.getChampionPointsSinceLastLevel(), response.getChampionPointsSinceLastLevel() + response.getChampionPointsUntilNextLevel()));
                }
            }

            @Override
            public void onError(TeemoException e) {

            }
        });
    }

    private void startAnimation(DecoView view, int index, long spiralDelay, long spiralDuration, long fillDuration, float fillValue) {
        view.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(index)
                .setDuration(spiralDuration)
                .setDelay(spiralDelay)
                .build());

        view.addEvent(new DecoEvent.Builder(fillValue)
                .setIndex(index)
                .setDuration(fillDuration)
                .setDelay(spiralDelay + spiralDuration)
                .build());
    }

    private void fillLore() {
        if (mChampion.getLore() != null) {
            tvLore.setText(Html.fromHtml(mChampion.getLore()));
        }
    }

    private void fillAttributes() {

        if (mChampion.getInfo() != null) {
            pgAttack.setProgressWithAnim(mChampion.getInfo().getAttack() * 100);
            pgTank.setProgressWithAnim(mChampion.getInfo().getDefense() * 100);
            pgMagic.setProgressWithAnim(mChampion.getInfo().getMagic() * 100);
            pgDifficult.setProgressWithAnim(mChampion.getInfo().getDifficulty() * 100);
        }
    }

    private void fillTips() {

        TextView tv;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = SizeUtils.convertDpToPixel(6);
        for (String tip : mChampion.getAllytips()) {
            tv = new TextView(getActivity());
            tv.setLayoutParams(params);
            tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color));
            CalligraphyUtils.applyFontToTextView(tv, Typeface.createFromAsset(getContext().getAssets(), "friz_quad.ttf"));
            tv.setText("- ".concat(tip));
            mLayoutAllytips.addView(tv);
        }

        for (String tip : mChampion.getEnemytips()) {
            tv = new TextView(getActivity());
            tv.setLayoutParams(params);
            tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color));
            tv.setText("- ".concat(tip));
            CalligraphyUtils.applyFontToTextView(tv, Typeface.createFromAsset(getContext().getAssets(), "friz_quad.ttf"));
            mLayoutEnemytipstips.addView(tv);
        }
    }

    private void fillStats() {
        if (mChampion.getStats() != null) {
            StatsDto stats = mChampion.getStats();
            StringBuilder mBuilder = new StringBuilder();
            if (mChampion.getStats().getHp() != null && mChampion.getStats().getHp() > 0) {
                mBuilder.append(getString(R.string.health));
                mBuilder.append(" ");
                mBuilder.append(getFormattedValue(stats.getHp()));
                if (stats.getHpperlevel() != null) {
                    mBuilder.append(" (+");
                    mBuilder.append(getFormattedValue(stats.getHpperlevel()));
                    mBuilder.append(")");
                }
                tvHealth.setText(mBuilder.toString());
            } else {
                tvHealth.setVisibility(View.GONE);
            }

            if (mChampion.getStats().getMp() != null && mChampion.getStats().getMp() > 0) {
                mBuilder = new StringBuilder();
                mBuilder.append(getString(R.string.mana));
                mBuilder.append(" ");
                mBuilder.append(getFormattedValue(stats.getMp()));
                if (stats.getMpperlevel() != null) {
                    mBuilder.append(" (+");
                    mBuilder.append(getFormattedValue(stats.getMpperlevel()));
                    mBuilder.append(")");
                }
                tvMana.setText(mBuilder.toString());
            } else {
                tvMana.setVisibility(View.GONE);
            }

            if (mChampion.getStats().getHpregen() != null && mChampion.getStats().getHpregen() > 0) {
                mBuilder = new StringBuilder();
                mBuilder.append(getString(R.string.health_regen));
                mBuilder.append(" ");
                mBuilder.append(getFormattedValue(stats.getHpregen()));
                if (stats.getHpregenperlevel() != null) {
                    mBuilder.append(" (+");
                    mBuilder.append(getFormattedValue(stats.getHpregenperlevel()));
                    mBuilder.append(")");
                }
                tvHealthRegen.setText(mBuilder.toString());
            } else {
                tvHealthRegen.setVisibility(View.GONE);
            }

            if (mChampion.getStats().getMpregen() != null && mChampion.getStats().getMpregen() > 0) {
                mBuilder = new StringBuilder();
                mBuilder.append(getString(R.string.mana_regen));
                mBuilder.append(" ");
                mBuilder.append(getFormattedValue(stats.getMpregen()));
                if (stats.getMpregenperlevel() != null) {
                    mBuilder.append(" (+");
                    mBuilder.append(getFormattedValue(stats.getMpregenperlevel()));
                    mBuilder.append(")");
                }
                tvManaRegen.setText(mBuilder.toString());
            } else {
                tvManaRegen.setVisibility(View.GONE);
            }

            if (mChampion.getStats().getAttackdamage() != null && mChampion.getStats().getAttackdamage() > 0) {
                mBuilder = new StringBuilder();
                mBuilder.append(getString(R.string.attack));
                mBuilder.append(" ");
                mBuilder.append(getFormattedValue(stats.getAttackdamage()));
                if (stats.getAttackdamageperlevel() != null) {
                    mBuilder.append(" (+");
                    mBuilder.append(getFormattedValue(stats.getAttackdamageperlevel()));
                    mBuilder.append(")");
                }
                tvAttack.setText(mBuilder.toString());
            } else {
                tvAttack.setVisibility(View.GONE);
            }

            if (mChampion.getStats().getAttackspeedoffset() != null && mChampion.getStats().getAttackspeedoffset() > 0) {
                mBuilder = new StringBuilder();
                mBuilder.append(getString(R.string.attack_speed));
                mBuilder.append(" ");
                mBuilder.append(getFormattedValue(stats.getAttackspeedoffset()));
                if (stats.getAttackspeedperlevel() != null) {
                    mBuilder.append(" (+");
                    mBuilder.append(getFormattedValue(stats.getAttackspeedperlevel()));
                    mBuilder.append(")");
                }
                tvAttackSpeed.setText(mBuilder.toString());
            } else {
                tvAttackSpeed.setVisibility(View.GONE);
            }

            if (mChampion.getStats().getArmor() != null && mChampion.getStats().getArmor() > 0) {
                mBuilder = new StringBuilder();
                mBuilder.append(getString(R.string.armor));
                mBuilder.append(" ");
                mBuilder.append(getFormattedValue(stats.getArmor()));
                if (stats.getArmorperlevel() != null) {
                    mBuilder.append(" (+");
                    mBuilder.append(getFormattedValue(stats.getArmorperlevel()));
                    mBuilder.append(")");
                }
                tvArmor.setText(mBuilder.toString());
            } else {
                tvArmor.setVisibility(View.GONE);
            }

            if (mChampion.getStats().getSpellblock() != null && mChampion.getStats().getSpellblock() > 0) {
                mBuilder = new StringBuilder();
                mBuilder.append(getString(R.string.magic_resist));
                mBuilder.append(" ");
                mBuilder.append(getFormattedValue(stats.getSpellblock()));
                if (stats.getSpellblockperlevel() != null) {
                    mBuilder.append(" (+");
                    mBuilder.append(getFormattedValue(stats.getSpellblockperlevel()));
                    mBuilder.append(")");
                }
                tvMagicResist.setText(mBuilder.toString());
            } else {
                tvMagicResist.setVisibility(View.GONE);
            }

            if (mChampion.getStats().getCrit() != null && mChampion.getStats().getCrit() > 0) {
                mBuilder = new StringBuilder();
                mBuilder.append(getString(R.string.crit));
                mBuilder.append(" ");
                mBuilder.append(getFormattedValue(stats.getCrit()));
                if (stats.getCritperlevel() != null) {
                    mBuilder.append(" (+");
                    mBuilder.append(getFormattedValue(stats.getCritperlevel()));
                    mBuilder.append(")");
                }
                tvCrit.setText(mBuilder.toString());
            } else {
                tvCrit.setVisibility(View.GONE);
            }

            if (mChampion.getStats().getAttackrange() != null && mChampion.getStats().getAttackrange() > 0) {
                mBuilder = new StringBuilder();
                mBuilder.append(getString(R.string.range));
                mBuilder.append(" ");
                mBuilder.append(getFormattedValue(stats.getAttackrange()));
                tvRange.setText(mBuilder.toString());
            }

            if (mChampion.getStats().getMovespeed() != null && mChampion.getStats().getMovespeed() > 0) {
                mBuilder = new StringBuilder();
                mBuilder.append(getString(R.string.movement));
                mBuilder.append(" ");
                mBuilder.append(getFormattedValue(stats.getMovespeed()));
                tvMovement.setText(mBuilder.toString());
            } else {
                tvMovement.setVisibility(View.GONE);
            }
        }
    }

    private String getFormattedValue(Double value) {
        if (value == null) return "";

        return String.valueOf(value % 1 == 0 ? Math.round(value) : value);
    }
}
