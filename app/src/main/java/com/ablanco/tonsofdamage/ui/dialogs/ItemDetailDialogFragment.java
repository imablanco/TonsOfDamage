package com.ablanco.tonsofdamage.ui.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.ui.views.ItemView;
import com.ablanco.tonsofdamage.utils.SizeUtils;
import com.ablanco.tonsofdamage.utils.Utils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.tvbarthel.lib.blurdialogfragment.SupportBlurDialogFragment;

/**
 * Created by Álvaro Blanco on 14/04/2016.
 * TonsOfDamage
 */
public class ItemDetailDialogFragment extends SupportBlurDialogFragment {
    private final static int ITEM_VIEW_SIZE = 70;//dp
    private static final int ROW_SIZE = 3;
    private static final String ARG_ID_ITEM = "item_id";
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

    public static DialogFragment newInstance(int itemId) {
        DialogFragment fragment = new ItemDetailDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID_ITEM, itemId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(STYLE_NORMAL, R.style.DialogTheme);

        if (getArguments() != null) {
            mId = getArguments().getInt(ARG_ID_ITEM);
        }
    }

    // implement either onCreateView or onCreateDialog
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_item_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Teemo.getInstance(getActivity()).getStaticDataHandler().getItemById(mId, SettingsHandler.getLanguage(getActivity()), null, StaticAPIQueryParams.Items.all, new ServiceResponseListener<ItemDto>() {
            @Override
            public void onResponse(ItemDto response) {
                if (getActivity() != null) {
                    Glide.with(getActivity()).load(ImageUris.getItemIcon(SettingsHandler.getCDNVersion(getActivity()), String.valueOf(mId))).into(mItemImg);
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
                dismiss();
            }
        });
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
        LinearLayout row = new LinearLayout(getContext());
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(dp5, dp5, dp5, dp5);
        ItemView itemView;
        for (String item : subList) {
            itemView = new ItemView(getContext());
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

    @Override
    protected float getDownScaleFactor() {
        // Allow to customize the down scale factor.
        return 8.0f;
    }

    @Override
    protected boolean isActionBarBlurred() {
        // Enable or disable the blur effect on the action bar.
        // Disabled by default.
        return true;
    }

    @Override
    protected boolean isDimmingEnable() {
        // Enable or disable the dimming effect.
        // Disabled by default.
        return true;
    }

    @Override
    protected boolean isRenderScriptEnable() {
        // Enable or disable the use of RenderScript for blurring effect
        // Disabled by default.
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public class ItemClickListener implements View.OnClickListener {

        private String id;

        public ItemClickListener(String id) {
            this.id = id;
        }

        @Override
        public void onClick(View v) {
            ItemDetailDialogFragment.newInstance(Integer.valueOf(id)).show(getActivity().getSupportFragmentManager(), "item_detail");
            dismiss();
        }
    }
}