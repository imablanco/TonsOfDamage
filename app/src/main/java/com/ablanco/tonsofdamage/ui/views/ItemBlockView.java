package com.ablanco.tonsofdamage.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ablanco.teemo.model.staticdata.BlockItemDto;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.utils.SizeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Álvaro Blanco on 09/04/2016.
 * TonsOfDamage
 */
public class ItemBlockView extends LinearLayout {

    private final static int ITEM_VIEW_SIZE = 75;//dp
    private static final int ROW_SIZE = 4;
    @Bind(R.id.tv_type)
    TextView mTvType;
    @Bind(R.id.layout_items)
    LinearLayout mLayoutItems;

    private int dp5 = SizeUtils.convertDpToPixel(5);
    private LayoutParams itemParams = new LayoutParams(SizeUtils.convertDpToPixel(ITEM_VIEW_SIZE), SizeUtils.convertDpToPixel(ITEM_VIEW_SIZE));

    public ItemBlockView(Context context) {
        this(context, null);
    }

    public ItemBlockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemBlockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.view_item_block, this);
        ButterKnife.bind(this);

        setOrientation(VERTICAL);
    }

    public void setType(String type) {
        String fixedType = type;
        if (fixedType != null && !fixedType.isEmpty()) {
            fixedType = fixedType.substring(0, 1).toUpperCase().concat(fixedType.substring(1, fixedType.length()));
        }

        this.mTvType.setText(fixedType);
    }

    public void addItems(final List<BlockItemDto> items) {

        List<BlockItemDto> subList = new ArrayList<>();
        for (int i = 0; i < items.size(); i++){
            if(i % ROW_SIZE == 0 && !subList.isEmpty()){
                addRow(subList);
                subList = new ArrayList<>();
            }
            subList.add(items.get(i));
        }

        if(!subList.isEmpty()){
            addRow(subList);
        }

    }

    private void addRow(List<BlockItemDto> subList){
        LinearLayout row = new LinearLayout(getContext());
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(dp5, dp5, dp5, dp5);
        ItemView itemView;
        for (BlockItemDto item : subList) {
            itemView = new ItemView(getContext());
            itemParams.leftMargin = dp5;
            itemParams.rightMargin = dp5;
            itemView.setLayoutParams(itemParams);
            itemView.setItemId(item.getId());
            row.addView(itemView);
        }

        mLayoutItems.addView(row);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ButterKnife.unbind(this);
    }
}
