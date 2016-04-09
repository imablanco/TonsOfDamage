package com.ablanco.tonsofdamage.ui.views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ablanco.teemo.model.staticdata.BlockDto;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.utils.SizeUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Álvaro Blanco on 09/04/2016.
 * TonsOfDamage
 */
public class RecommendedItemsView extends CardView {

    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.layout_blocks)
    LinearLayout mLayoutBlocks;

    public RecommendedItemsView(Context context) {
        this(context, null);
    }

    public RecommendedItemsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecommendedItemsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.view_recommended_items, this);

        this.setUseCompatPadding(true);
        this.setRadius(SizeUtils.convertDpToPixel(2));
        this.setCardElevation(SizeUtils.convertDpToPixel(4));
        this.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDarkAccent));
        ButterKnife.bind(this, this);
    }

    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    public void addItemsBlock(final BlockDto block) {
        View v = mLayoutBlocks.findViewWithTag(block.getType());
        if (v != null) {
            mLayoutBlocks.removeView(v);
        }

        ItemBlockView blockView = new ItemBlockView(getContext());
        blockView.setType(block.getType());
        blockView.addItems(block.getItems());
        blockView.setTag(block.getType());
        mLayoutBlocks.addView(blockView);

    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ButterKnife.unbind(this);
    }
}
