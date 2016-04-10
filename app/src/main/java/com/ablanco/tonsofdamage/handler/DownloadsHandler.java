package com.ablanco.tonsofdamage.handler;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

/**
 * Created by Álvaro Blanco on 10/04/2016.
 * TonsOfDamage
 */
public class DownloadsHandler {


    private static DownloadsHandler mInstance;
    private final DownloadManager mDownloadManager;

    public static DownloadsHandler getInstance(Context context){
        if(mInstance == null){
            mInstance = new DownloadsHandler(context);
        }
        return mInstance;
    }

    private DownloadsHandler(Context context){
        this.mDownloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    public long requestDownLoad(String title, String description, String url, String filename){
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(url));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,filename);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setVisibleInDownloadsUi(true);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        if(title != null){
            request.setTitle(title);
        }
        if(description != null){
            request.setDescription(description);
        }
        return mDownloadManager.enqueue(request);
    }
}
