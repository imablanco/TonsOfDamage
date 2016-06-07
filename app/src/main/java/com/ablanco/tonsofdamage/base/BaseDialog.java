package com.ablanco.tonsofdamage.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ablanco.tonsofdamage.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Álvaro Blanco Cabrero on 7/6/16
 * TonsOfDamage
 */
public class BaseDialog extends BaseAnimatedDialog {

    private final static String ARG_TITLE = "arg_title";
    private final static String ARG_MESSAGE = "arg_message";

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_message)
    TextView tvMessage;

    private String title;
    private String message;


    public static DialogFragment newInstance(String title, String message) {
        DialogFragment f = new BaseDialog();
        Bundle b = new Bundle();
        b.putString(ARG_TITLE, title);
        b.putString(ARG_MESSAGE, message);
        f.setArguments(b);
        return f;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            title = getArguments().getString(ARG_TITLE);
            message = getArguments().getString(ARG_MESSAGE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_dialog, container, false);
        ButterKnife.bind(this, view);

        tvTitle.setText(title);
        tvMessage.setText(message);
        return view;
    }

    @OnClick(R.id.bt_ok)
    public void dismissDialog() {
        dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
