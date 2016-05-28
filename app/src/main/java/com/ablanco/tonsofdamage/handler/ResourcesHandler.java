package com.ablanco.tonsofdamage.handler;

import android.content.Context;

import com.ablanco.teemo.Teemo;
import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.model.staticdata.LanguageStringsDto;
import com.ablanco.teemo.service.base.ServiceResponseListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Álvaro Blanco on 10/04/2016.
 * TonsOfDamage
 */
public class ResourcesHandler {

    private Map<String, String> mResources;

    private static ResourcesHandler mInstance;

    private ResourcesHandler(Context context) {
        mResources = new HashMap<>();
        if(context != null){
            Teemo.getInstance(context).getStaticDataHandler().getStringsLanguages(SettingsHandler.getLanguage(context), null, new ServiceResponseListener<LanguageStringsDto>() {
                @Override
                public void onResponse(LanguageStringsDto response) {
                    mResources.putAll(response.getData());
                }

                @Override
                public void onError(TeemoException e) {

                }
            });
        }

    }

    public static void init(Context context){
        mInstance = new ResourcesHandler(context);
    }

    public static ResourcesHandler getInstance(Context context){
        if(mInstance == null){
            mInstance = new ResourcesHandler(context);
        }
        return mInstance;
    }

    public String getResourceForKey(String key){
        return mResources.get(key) == null ? key : mResources.get(key);
    }
}
