package com.ablanco.tonsofdamage.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.ablanco.tonsofdamage.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Álvaro Blanco on 17/04/2016.
 * TonsOfDamage
 */
public class HomeErrorUtils {

    public final static int HOME = 0;
    public final static int CHAMPIONS = 1;
    public final static int ITEMS = 2;
    public final static int SUMMONERS = 3;

    private int mCurrentPage;

    private static HomeErrorUtils mInstance;

    public static HomeErrorUtils getInstance(){
        if(mInstance == null){
            mInstance = new HomeErrorUtils();
        }

        return mInstance;
    }

    public static void init(){
        mInstance = new HomeErrorUtils();
    }

    private HomeErrorUtils(){
        mSnackbarMap = new HashMap<>();
    }
    private Map<Integer, Snackbar> mSnackbarMap;


    private void showUnhandledError(){
        if(mSnackbarMap.containsKey(mCurrentPage)){
            mSnackbarMap.get(mCurrentPage).show();
        }
    }

    public void setCurrentPage(int page){
        hideSnackBar();
        mCurrentPage = page;
        showUnhandledError();
    }

    private void hideSnackBar(){
        if(mSnackbarMap.containsKey(mCurrentPage)){
            mSnackbarMap.get(mCurrentPage).dismiss();
        }
    }

    public void showPersistentError(final int page, View v, final View.OnClickListener onClickListener){
        if(v != null){
            Snackbar snackbar = Snackbar.make(v, R.string.error_text, Snackbar.LENGTH_INDEFINITE).setAction(R.string.reload, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSnackbarMap.get(page).dismiss();
                    mSnackbarMap.remove(page);
                    onClickListener.onClick(v);

                }
            });
            mSnackbarMap.put(page,snackbar);
            if(mCurrentPage == page){
                snackbar.show();
            }
        }
    }

}
