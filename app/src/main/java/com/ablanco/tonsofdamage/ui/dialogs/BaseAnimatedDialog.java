package com.ablanco.tonsofdamage.ui.dialogs;

import android.support.v4.app.DialogFragment;

import com.ablanco.tonsofdamage.R;

/**
 * Created by Álvaro Blanco Cabrero on 27/3/16
 * TonsOfDamage
 */
public class BaseAnimatedDialog extends DialogFragment {

    @Override
    public void onStart() {
        super.onStart();

        // safety check
        if (getDialog() == null) {
            return;
        }
        // set the animations to use on showing and hiding the dialog
        getDialog().getWindow().setWindowAnimations(
                R.style.DialogAnimationFade);

    }
}
