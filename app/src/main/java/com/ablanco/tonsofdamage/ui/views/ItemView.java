package com.ablanco.tonsofdamage.ui.views;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ablanco.teemo.Teemo;
import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.constants.StaticAPIQueryParams;
import com.ablanco.teemo.model.staticdata.ItemDto;
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.teemo.utils.ImageUris;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.ui.dialogs.ItemDetailDialogFragment;
import com.ablanco.tonsofdamage.utils.Utils;
import com.bumptech.glide.Glide;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Álvaro Blanco on 09/04/2016.
 * TonsOfDamage
 */
public class ItemView extends SquareRelativeLayout implements View.OnClickListener{

    @Bind(R.id.img_item)
    ImageView mImgItem;
    @Bind(R.id.ic_coins)
    ImageView mIcCoins;
    @Bind(R.id.tv_price)
    TextView mTvPrice;

    private int mId;
    private boolean mCollapsedPrice = false;

    public ItemView(Context context) {
        this(context, null);
    }

    public ItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.view_item, this);
        ButterKnife.bind(this, this);
        this.setOnClickListener(this);
    }

    public void setItemId(int id) {
        this.mId = id;
        Teemo.getInstance(getContext()).getStaticDataHandler().getItemById(id, Locale.getDefault().toString(), null, StaticAPIQueryParams.Items.gold, new ServiceResponseListener<ItemDto>() {
            @Override
            public void onResponse(ItemDto response) {
                if(response.getGold() != null && mImgItem != null){
                    Glide.with(getContext()).load(ImageUris.getItemIcon(SettingsHandler.getCDNVersion(getContext()), String.valueOf(mId))).into(mImgItem);
                    if(!mCollapsedPrice){
                        mTvPrice.setText(Utils.getItemPrice(response.getGold().getTotal(), response.getGold().getBase()));
                    }else {
                        mTvPrice.setText(String.valueOf(response.getGold().getBase()));
                    }

                    Glide.with(getContext()).load(ImageUris.SCORE_BOARD_GOLD_URL).into(mIcCoins);
                }
            }

            @Override
            public void onError(TeemoException e) {

            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ButterKnife.unbind(this);
    }

    public void setItemClickListener(OnClickListener clickListener){
        this.setOnClickListener(clickListener);
    }

    public void setCollapsedPrice(boolean collapsedPrice){
        this.mCollapsedPrice = collapsedPrice;
    }

    @Override
    public void onClick(View v) {
        if(getContext() instanceof FragmentActivity){
            ItemDetailDialogFragment.newInstance(mId).show(((FragmentActivity) getContext()).getSupportFragmentManager(), "item_detail");
        }
    }
}
