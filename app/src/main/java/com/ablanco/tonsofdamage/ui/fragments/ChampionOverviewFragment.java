package com.ablanco.tonsofdamage.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ablanco.teemo.model.staticdata.ChampionDto;
import com.ablanco.teemo.model.staticdata.StatsDto;
import com.ablanco.tonsofdamage.R;

import butterknife.Bind;

/**
 * Created by Álvaro Blanco on 04/04/2016.
 * TonsOfDamage
 */
public class ChampionOverviewFragment extends BaseFragment {

    @Bind(R.id.pg_attack)
    ProgressBar pgAttack;

    @Bind(R.id.pg_tank)
    ProgressBar pgTank;

    @Bind(R.id.pg_magic)
    ProgressBar pgMagic;

    @Bind(R.id.pg_difficult)
    ProgressBar pgDifficult;

    @Bind(R.id.tv_health) TextView tvHealth;
    @Bind(R.id.tv_health_regen) TextView tvHealthRegen;
    @Bind(R.id.tv_mana) TextView tvMana;
    @Bind(R.id.tv_mana_regen) TextView tvManaRegen;
    @Bind(R.id.tv_attack) TextView tvAttack;
    @Bind(R.id.tv_attack_speed) TextView tvAttackSpeed;
    @Bind(R.id.tv_armor) TextView tvArmor;
    @Bind(R.id.tv_magic_resist) TextView tvMagicResist;
    @Bind(R.id.tv_range) TextView tvRange;
    @Bind(R.id.tv_movement) TextView tvMovement;
    @Bind(R.id.tv_crit) TextView tvCrit;

    @Bind(R.id.tv_lore) TextView tvLore;

    private ChampionDto mChampion;

    public static Fragment newInstance(ChampionDto champion){
        ChampionOverviewFragment f = new ChampionOverviewFragment();
        f.setChampion(champion);
        return f;
    }

    public void setChampion(ChampionDto champion){
        this.mChampion = champion;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_champion_overview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fillAttributes();
        fillStats();
        fillLore();
    }

    private void fillLore() {
        if(mChampion.getLore() != null){
            tvLore.setText(Html.fromHtml(mChampion.getLore()));
        }
    }

    private void fillAttributes(){
        pgDifficult.getProgressDrawable().setColorFilter(
                ContextCompat.getColor(getActivity(), R.color.magenta), android.graphics.PorterDuff.Mode.SRC_IN);

        pgAttack.getProgressDrawable().setColorFilter(
                ContextCompat.getColor(getActivity(), R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);

        pgTank.getProgressDrawable().setColorFilter(
                ContextCompat.getColor(getActivity(), R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);

        pgMagic.getProgressDrawable().setColorFilter(
                ContextCompat.getColor(getActivity(), R.color.blue), android.graphics.PorterDuff.Mode.SRC_IN);

        if(mChampion.getInfo() != null){
            pgAttack.setProgress(mChampion.getInfo().getAttack());
            pgTank.setProgress(mChampion.getInfo().getDefense());
            pgMagic.setProgress(mChampion.getInfo().getMagic());
            pgDifficult.setProgress(mChampion.getInfo().getDifficulty());
        }
    }

    private void fillStats(){
        if(mChampion.getStats() != null){
            StatsDto stats = mChampion.getStats();
            StringBuilder mBuilder = new StringBuilder();
            if(mChampion.getStats().getHp() != null && mChampion.getStats().getHp() > 0){
                mBuilder.append(getString(R.string.health));
                mBuilder.append(" ");
                mBuilder.append(getFormattedValue(stats.getHp()));
                if(stats.getHpperlevel() != null){
                    mBuilder.append(" (+");
                    mBuilder.append(getFormattedValue(stats.getHpperlevel()));
                    mBuilder.append(")");
                }
                tvHealth.setText(mBuilder.toString());
            }else {
                tvHealth.setVisibility(View.GONE);
            }

            if(mChampion.getStats().getMp() != null && mChampion.getStats().getMp() > 0){
                mBuilder = new StringBuilder();
                mBuilder.append(getString(R.string.mana));
                mBuilder.append(" ");
                mBuilder.append(getFormattedValue(stats.getMp()));
                if(stats.getMpperlevel() != null){
                    mBuilder.append(" (+");
                    mBuilder.append(getFormattedValue(stats.getMpperlevel()));
                    mBuilder.append(")");
                }
                tvMana.setText(mBuilder.toString());
            }else {
                tvMana.setVisibility(View.GONE);
            }

            if(mChampion.getStats().getHpregen() != null && mChampion.getStats().getHpregen() > 0){
                mBuilder = new StringBuilder();
                mBuilder.append(getString(R.string.health_regen));
                mBuilder.append(" ");
                mBuilder.append(getFormattedValue(stats.getHpregen()));
                if(stats.getHpregenperlevel() != null){
                    mBuilder.append(" (+");
                    mBuilder.append(getFormattedValue(stats.getHpregenperlevel()));
                    mBuilder.append(")");
                }
                tvHealthRegen.setText(mBuilder.toString());
            }else {
                tvHealthRegen.setVisibility(View.GONE);
            }

            if(mChampion.getStats().getMpregen() != null && mChampion.getStats().getMpregen() > 0){
                mBuilder = new StringBuilder();
                mBuilder.append(getString(R.string.mana_regen));
                mBuilder.append(" ");
                mBuilder.append(getFormattedValue(stats.getMpregen()));
                if(stats.getMpregenperlevel() != null){
                    mBuilder.append(" (+");
                    mBuilder.append(getFormattedValue(stats.getMpregenperlevel()));
                    mBuilder.append(")");
                }
                tvManaRegen.setText(mBuilder.toString());
            }else {
                tvManaRegen.setVisibility(View.GONE);
            }

            if(mChampion.getStats().getAttackdamage() != null && mChampion.getStats().getAttackdamage() > 0){
                mBuilder = new StringBuilder();
                mBuilder.append(getString(R.string.attack));
                mBuilder.append(" ");
                mBuilder.append(getFormattedValue(stats.getAttackdamage()));
                if(stats.getAttackdamageperlevel() != null){
                    mBuilder.append(" (+");
                    mBuilder.append(getFormattedValue(stats.getAttackdamageperlevel()));
                    mBuilder.append(")");
                }
                tvAttack.setText(mBuilder.toString());
            }else {
                tvAttack.setVisibility(View.GONE);
            }

            if(mChampion.getStats().getAttackspeedoffset() != null && mChampion.getStats().getAttackspeedoffset() > 0){
                mBuilder = new StringBuilder();
                mBuilder.append(getString(R.string.attack_speed));
                mBuilder.append(" ");
                mBuilder.append(getFormattedValue(stats.getAttackspeedoffset()));
                if(stats.getAttackspeedperlevel() != null){
                    mBuilder.append(" (+");
                    mBuilder.append(getFormattedValue(stats.getAttackspeedperlevel()));
                    mBuilder.append(")");
                }
                tvAttackSpeed.setText(mBuilder.toString());
            }else {
                tvAttackSpeed.setVisibility(View.GONE);
            }

            if(mChampion.getStats().getArmor() != null && mChampion.getStats().getArmor() > 0){
                mBuilder = new StringBuilder();
                mBuilder.append(getString(R.string.armor));
                mBuilder.append(" ");
                mBuilder.append(getFormattedValue(stats.getArmor()));
                if(stats.getArmorperlevel() != null){
                    mBuilder.append(" (+");
                    mBuilder.append(getFormattedValue(stats.getArmorperlevel()));
                    mBuilder.append(")");
                }
                tvArmor.setText(mBuilder.toString());
            }else {
                tvArmor.setVisibility(View.GONE);
            }

            if(mChampion.getStats().getSpellblock() != null && mChampion.getStats().getSpellblock() > 0){
                mBuilder = new StringBuilder();
                mBuilder.append(getString(R.string.magic_resist));
                mBuilder.append(" ");
                mBuilder.append(getFormattedValue(stats.getSpellblock()));
                if(stats.getSpellblockperlevel() != null){
                    mBuilder.append(" (+");
                    mBuilder.append(getFormattedValue(stats.getSpellblockperlevel()));
                    mBuilder.append(")");
                }
                tvMagicResist.setText(mBuilder.toString());
            }else {
                tvMagicResist.setVisibility(View.GONE);
            }

            if(mChampion.getStats().getCrit() != null && mChampion.getStats().getCrit() > 0){
                mBuilder = new StringBuilder();
                mBuilder.append(getString(R.string.crit));
                mBuilder.append(" ");
                mBuilder.append(getFormattedValue(stats.getCrit()));
                if(stats.getCritperlevel() != null){
                    mBuilder.append(" (+");
                    mBuilder.append(getFormattedValue(stats.getCritperlevel()));
                    mBuilder.append(")");
                }
                tvCrit.setText(mBuilder.toString());
            }else {
                tvCrit.setVisibility(View.GONE);
            }

            if(mChampion.getStats().getAttackrange() != null && mChampion.getStats().getAttackrange() > 0){
                mBuilder = new StringBuilder();
                mBuilder.append(getString(R.string.range));
                mBuilder.append(" ");
                mBuilder.append(getFormattedValue(stats.getAttackrange()));
                tvRange.setText(mBuilder.toString());
            }

            if(mChampion.getStats().getMovespeed() != null && mChampion.getStats().getMovespeed() > 0){
                mBuilder = new StringBuilder();
                mBuilder.append(getString(R.string.movement));
                mBuilder.append(" ");
                mBuilder.append(getFormattedValue(stats.getMovespeed()));
                tvMovement.setText(mBuilder.toString());
            }else {
                tvMovement.setVisibility(View.GONE);
            }
        }
    }

    private String getFormattedValue(Double value){
        if (value == null) return "";

        return String.valueOf(value % 1 == 0 ? Math.round(value) : value);
    }
}
