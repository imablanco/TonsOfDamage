package com.ablanco.tonsofdamage.summoner;

import android.animation.ValueAnimator;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.transition.Transition;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ablanco.teemo.model.staticdata.MasteryDto;
import com.ablanco.teemo.utils.ImageUris;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.utils.Utils;
import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Álvaro Blanco on 28/05/2016.
 * TonsOfDamage
 */
public class MasteryDetailActivityDialog extends AppCompatActivity {

    public static final String EXTRA_MASTERY = "mastery_obj";
    public static final String EXTRA_RANK = "extra_rank";

    @Bind(R.id.img_mastery)
    CircleImageView mImgMastery;
    @Bind(R.id.tv_mastery_description)
    TextView mTvMasteryDescription;
    @Bind(R.id.root)
    RelativeLayout mRoot;
    private ColorMatrix matrix;
    private boolean isGray;
    private ValueAnimator animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_mastery_detail);
        ButterKnife.bind(this);
        MasteryDto mMastery = (MasteryDto) getIntent().getSerializableExtra(EXTRA_MASTERY);
        if (mMastery != null) {

            Utils.setTransitionNameForView(mImgMastery, getString(R.string.shared_transition, mMastery.getId()));
            Glide.with(this).load(ImageUris.getMasteryIcon(SettingsHandler.getCDNVersion(this), mMastery.getImage().getFull())).into(mImgMastery);

            int rank = getIntent().getIntExtra(EXTRA_RANK, 0);
            if(mMastery.getDescription() != null && !mMastery.getDescription().isEmpty()){
                mTvMasteryDescription.setText(Html.fromHtml(mMastery.getDescription().get(rank == 0 ? rank : rank - 1)));
            }

            //check if image was in grayscale
            isGray = rank == 0;
            if (isGray) {
                matrix = new ColorMatrix();
                Utils.applyGrayScaleFilter(mImgMastery);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Transition sharedElementEnterTransition = getWindow().getSharedElementEnterTransition();
                    sharedElementEnterTransition.addListener(new SimpleTransitionListener() {
                        @Override
                        public void onTransitionEnd(Transition transition) {
                            animateImageSaturationToIdentity();
                        }
                    });
                } else {
                    animateImageSaturationToIdentity();
                }

            }
        }

        mRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void animateImageSaturationToIdentity() {
        animation = ValueAnimator.ofFloat(0f, 1f);
        animation.setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                matrix.setSaturation(animation.getAnimatedFraction());
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                mImgMastery.setColorFilter(filter);
            }
        });
        animation.start();
    }


    @Override
    public void onBackPressed() {
        if (isGray) {
            if (animation != null && animation.isRunning()) {
                animation.cancel();
            }

            matrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            mImgMastery.setColorFilter(filter);
        }
        supportFinishAfterTransition();
    }


    private static class SimpleTransitionListener implements Transition.TransitionListener {

        @Override
        public void onTransitionStart(Transition transition) {

        }

        @Override
        public void onTransitionEnd(Transition transition) {

        }

        @Override
        public void onTransitionCancel(Transition transition) {

        }

        @Override
        public void onTransitionPause(Transition transition) {

        }

        @Override
        public void onTransitionResume(Transition transition) {

        }
    }

}
