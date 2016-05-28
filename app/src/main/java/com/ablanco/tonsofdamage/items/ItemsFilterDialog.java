package com.ablanco.tonsofdamage.items;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.adapter.ItemFilterAdapter;
import com.ablanco.tonsofdamage.base.BaseAnimatedDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Álvaro Blanco on 03/04/2016.
 * TonsOfDamage
 */
public class ItemsFilterDialog extends BaseAnimatedDialog {

    private final static String ARG_TAGS = "tags";
    private final static String ARG_SELECTED_TAGS = "selected_tags";

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private List<String> mTags = new ArrayList<>();
    private List<String> mSelectedTags = new ArrayList<>();
    private ItemFilterAdapter adapter;

    public interface ItemsFilterDialogListener{
        void onTagsSelected(List<String> tags);
    }

    private ItemsFilterDialogListener listener;

    public static ItemsFilterDialog newInstance(ArrayList<String> tags, ArrayList<String> selectedTags) {
        ItemsFilterDialog f = new ItemsFilterDialog();
        if (tags != null) {
            Bundle b = new Bundle();
            b.putStringArrayList(ARG_TAGS, tags);
            b.putStringArrayList(ARG_SELECTED_TAGS, selectedTags);
            f.setArguments(b);
        }

        return f;
    }

    public void setTags(List<String> tags){
        this.mTags = tags;
        adapter.notifyDataSetChanged();
    }

    public void setListener(ItemsFilterDialogListener listener){
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mTags = getArguments().getStringArrayList(ARG_TAGS);
            mSelectedTags = getArguments().getStringArrayList(ARG_SELECTED_TAGS);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_filter_items, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ItemFilterAdapter(getActivity(), mTags, mSelectedTags);
        mRecyclerView.setAdapter(adapter);

    }

    @OnClick(R.id.bt_ok)
    public void onOkButtonClied(){
        if(listener != null && adapter != null){
            listener.onTagsSelected(adapter.getSelectedTags());
        }

        dismiss();
    }

    @OnClick(R.id.bt_cancel)
    public void onCancelButtonClicked(){
        dismiss();
    }
}
