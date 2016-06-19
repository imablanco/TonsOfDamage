package com.ablanco.tonsofdamage.items;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ablanco.teemo.Teemo;
import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.constants.StaticAPIQueryParams;
import com.ablanco.teemo.model.staticdata.ItemDto;
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.teemo.utils.ImageUris;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.base.BaseActivity;
import com.ablanco.tonsofdamage.handler.AnalyticsHandler;
import com.ablanco.tonsofdamage.handler.NavigationHandler;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.utils.SizeUtils;
import com.ablanco.tonsofdamage.utils.Utils;
import com.ablanco.tonsofdamage.views.ItemView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Álvaro Blanco on 14/04/2016.
 * TonsOfDamage
 */
public class ItemDetailDialogActivity extends BaseActivity {
    private final static int ITEM_VIEW_SIZE = 70;//dp
    private static final int ROW_SIZE = 3;
    public static final String EXTRA_ID_ITEM = "item_id";
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.item_img)
    ImageView mItemImg;
    @Bind(R.id.ic_coins)
    ImageView mIcCoins;
    @Bind(R.id.tv_price)
    TextView mTvPrice;
    @Bind(R.id.tv_item_description)
    TextView mTvItemDescription;
    @Bind(R.id.ll_from)
    LinearLayout mLlFrom;
    @Bind(R.id.ll_into)
    LinearLayout mLlInto;
    @Bind(R.id.tv_built_from)
    TextView mTvBuiltFrom;
    @Bind(R.id.tv_built_into)
    TextView mTvBuiltInto;

    private int dp5 = SizeUtils.convertDpToPixel(5);
    private LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(SizeUtils.convertDpToPixel(ITEM_VIEW_SIZE), SizeUtils.convertDpToPixel(ITEM_VIEW_SIZE));
    private int mId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_item_detail);
        ButterKnife.bind(this);
        mId = getIntent().getIntExtra(EXTRA_ID_ITEM, 0);

        Utils.setTransitionNameForView(mItemImg, String.valueOf(mId));
        Glide.with(ItemDetailDialogActivity.this).load(ImageUris.getItemIcon(SettingsHandler.getCDNVersion(ItemDetailDialogActivity.this), String.valueOf(mId))).into(mItemImg);

        Teemo.getInstance(this).getStaticDataHandler().getItemById(mId, SettingsHandler.getLanguage(this), null, StaticAPIQueryParams.Items.all, new ServiceResponseListener<ItemDto>() {
            @Override
            public void onResponse(ItemDto response) {
                if (!isFinishing()) {
                    mIcCoins.setImageResource(R.drawable.ic_gold);
                    mTvTitle.setText(response.getName());
                    mTvItemDescription.setText(Html.fromHtml(response.getDescription()));
                    if (response.getGold() != null) {
                        mTvPrice.setText(Utils.getItemPrice(response.getGold().getTotal(), response.getGold().getBase()));
                    }

                    if (response.getFrom() != null) {
                        mLlFrom.setVisibility(View.VISIBLE);
                        mTvBuiltFrom.setVisibility(View.VISIBLE);
                        buildItemBlock(mLlFrom, response.getFrom());
                    }

                    if (response.getInto() != null) {
                        mLlInto.setVisibility(View.VISIBLE);
                        mTvBuiltInto.setVisibility(View.VISIBLE);
                        buildItemBlock(mLlInto, response.getInto());
                    }
                }
            }

            @Override
            public void onError(TeemoException e) {
                onBackPressed();
            }
        });

    }

    @Override
    public String getClassName() {
        return AnalyticsHandler.CLASS_NAME_ITEM_DETAIL;
    }

    @Override
    public String getNavigationItemId() {
        return String.valueOf(mId);
    }


    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
    }

    private void buildItemBlock(LinearLayout linearLayout, List<String> items) {
        List<String> subList = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            if (i % ROW_SIZE == 0 && !subList.isEmpty()) {
                addRow(linearLayout, subList);
                subList = new ArrayList<>();
            }
            subList.add(items.get(i));
        }

        if (!subList.isEmpty()) {
            addRow(linearLayout, subList);
        }
    }

    private void addRow(LinearLayout linearLayout, List<String> subList) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(dp5, dp5, dp5, dp5);
        ItemView itemView;
        for (String item : subList) {
            itemView = new ItemView(this);
            itemView.setItemClickListener(new ItemClickListener(item));
            itemParams.leftMargin = dp5;
            itemParams.rightMargin = dp5;
            itemView.setLayoutParams(itemParams);
            itemView.setCollapsedPrice(true);
            itemView.setItemId(Integer.parseInt(item));
            row.addView(itemView);
        }

        linearLayout.addView(row);
    }

    public class ItemClickListener implements View.OnClickListener {

        private String id;

        public ItemClickListener(String id) {
            this.id = id;
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putInt(ItemDetailDialogActivity.EXTRA_ID_ITEM, Integer.parseInt(id));
            NavigationHandler.navigateTo(ItemDetailDialogActivity.this, NavigationHandler.ITEM_DETAIL, bundle);
            ItemDetailDialogActivity.this.finish();
        }
    }
}