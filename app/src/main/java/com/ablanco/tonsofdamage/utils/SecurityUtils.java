package com.ablanco.tonsofdamage.utils;

import android.content.Context;
import android.util.Base64;

import com.ablanco.tonsofdamage.R;
import com.scottyab.aescrypt.AESCrypt;

/**
 * Created by Álvaro Blanco Cabrero on 16/6/16
 * TonsOfDamage
 */
public class SecurityUtils {

    public static String getWhatIsMine(Context context){
        String myTreasure = null;
        try {
            myTreasure = AESCrypt.decrypt(String.valueOf(context.getResources().getInteger(R.integer.the_key)), context.getResources().getString(R.string.the_door));

            for (int i = 0; i < 0x9C40/0x2710; i++) {
                myTreasure = new String(Base64.decode(myTreasure.getBytes(), Base64.DEFAULT), "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return myTreasure;
    }

}
