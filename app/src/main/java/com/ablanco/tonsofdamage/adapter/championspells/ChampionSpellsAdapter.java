package com.ablanco.tonsofdamage.adapter.championspells;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ablanco.teemo.model.staticdata.ChampionSpellDto;
import com.ablanco.teemo.model.staticdata.PassiveDto;
import com.ablanco.teemo.utils.ImageUris;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.utils.ChampionSpellParser;
import com.ablanco.tonsofdamage.utils.Utils;
import com.bumptech.glide.Glide;

import java.util.List;

import im.ene.lab.toro.ToroAdapter;
import im.ene.lab.toro.ToroVideoViewHolder;

/**
 * Created by Álvaro Blanco on 04/04/2016.
 * TonsOfDamage
 */
public class ChampionSpellsAdapter extends ToroAdapter<ToroVideoViewHolder> {

    private final static int PASSIVE = 0;
    private final static int SPELL = 1;

    private List<ChampionSpellDto> spells;
    private Context context;
    private int championId;
    private PassiveDto passive;

    public ChampionSpellsAdapter(Context context, int championId, PassiveDto passive, List<ChampionSpellDto> spells){
        this.context = context;
        this.spells = spells;
        this.championId = championId;
        this.passive = passive;
    }

    @Nullable
    @Override
    protected Object getItem(int position) {
        return position == 0 ? passive : spells.get(position-1);
    }

    @Override
    public ToroVideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case SPELL:default:
                return new ChampionSpellViewHolder(LayoutInflater.from(context).inflate(R.layout.item_champion_spell, parent, false));
            case PASSIVE:
                return new ChampionPassiveSpellViewHolder(LayoutInflater.from(context).inflate(R.layout.item_champion_spell_passive, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? PASSIVE : SPELL;
    }

    @Override
    public int getItemCount() {
        return spells.size()+1;
    }

    class ChampionPassiveSpellViewHolder extends ChampionSpellBaseViewHolder{

        public ChampionPassiveSpellViewHolder(View itemView) {
            super(context, itemView);
        }

        @Override
        protected String getVideoUri() {
            return Utils.getChampionAbilityVideoUrl(championId, getAdapterPosition() + 1);
        }

        @Override
        public void bind(@Nullable Object object) {
            super.bind(object);
            Glide.with(mContext).load(ImageUris.getPassiveAbilityIcon(passive.getImage().getFull())).into(imgSpell);

            tvAbilityName.setText(passive.getName());
            tvAbilityDescription.setText(Html.fromHtml(passive.getDescription()));

        }
    }

    class ChampionSpellViewHolder extends ChampionSpellBaseViewHolder {

        private TextView tvAbilityCost;
        private TextView tvAbilityRange;
        private TextView tvAbilityCooldown;

        public ChampionSpellViewHolder(View itemView) {
            super(context, itemView);
            tvAbilityCost = (TextView) itemView.findViewById(R.id.tv_spell_cost);
            tvAbilityRange = (TextView) itemView.findViewById(R.id.tv_spell_range);
            tvAbilityCooldown = (TextView) itemView.findViewById(R.id.tv_spell_cooldown);
        }

        @Override
        protected String getVideoUri() {
            return Utils.getChampionAbilityVideoUrl(championId, getAdapterPosition() + 1);
        }

        @Override
        public void bind(@Nullable Object object) {
            super.bind(object);
            int position = getAdapterPosition();
            ChampionSpellDto spell = spells.get(position-1);
            Glide.with(mContext).load(ImageUris.getChampionAbilityIcon(spell.getImage().getFull())).into(imgSpell);

            ChampionSpellParser.buildChampionSpellText(spell);
            tvAbilityName.setText(spell.getName());
            tvAbilityCooldown.setText(context.getString(R.string.cooldown).concat(" ").concat(spell.getCooldownBurn()).concat(" s"));

            String cost = ChampionSpellParser.getChampionSpellCost(spell);
            if(cost != null){
                tvAbilityCost.setVisibility(View.VISIBLE);
                tvAbilityCost.setText(context.getString(R.string.cost).concat(" ").concat(ChampionSpellParser.getChampionSpellCost(spell)));
            }else {
                tvAbilityCost.setVisibility(View.GONE);
            }

            tvAbilityRange.setText(context.getString(R.string.range).concat(" ").concat(spell.getRangeBurn()));
            tvAbilityDescription.setText(Html.fromHtml(ChampionSpellParser.buildChampionSpellText(spell)));

        }
    }
}
