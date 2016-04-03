package com.ablanco.tonsofdamage.utils;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewAnimationUtils;

/**
 * Created by Álvaro Blanco on 30/03/2016.
 * TonsOfDamage
 */
public class Animationutils {

    public static void revealView(final View view){

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            int cx = view.getWidth() / 2;
            int cy = view.getWidth() / 2;

            int finalRadius = Math.max(view.getWidth(), view.getHeight()) / 2;

            Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
            view.setVisibility(View.VISIBLE);
            anim.start();
        }else {
            view.setVisibility(View.VISIBLE);
            view.setAlpha(0f);
            final ValueAnimator anim = ValueAnimator.ofFloat(0,1);
            anim.setDuration(600);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    view.setAlpha((Float) anim.getAnimatedValue());
                }
            });


            anim.start();
        }
    }

}
