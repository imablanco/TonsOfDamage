package com.ablanco.tonsofdamage.counters;

import android.content.Context;
import android.os.AsyncTask;

import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.model.staticdata.ChampionDto;
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.tonsofdamage.handler.StaticDataHandler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Álvaro Blanco Cabrero on 17/6/16
 * TonsOfDamage
 */
public class CounterPickTask extends AsyncTask<String, Void, Elements>{

    public final static int COUNTERS_WEAK = 0;
    public final static int COUNTERS_STRONG = 1;

    private final static String URL = "http://www.championcounter.es/";
    private final static String MODE_WEAK = "weakAgainst";
    private final static String MODE_STRONG = "strongAgainst";
    private final static String CLASS_ENTITY = "entity";
    private final static String ATTR_DATA_NAME= "data-name";

    private int counterMode;
    private Context context;
    private final ServiceResponseListener<List<ChampionDto>> listener;


    public CounterPickTask(Context context, int mode, ServiceResponseListener<List<ChampionDto>> listener){
        this.counterMode = mode;
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected Elements doInBackground(String... params) {

        try {
            Document doc = Jsoup.connect(URL + params[0].toLowerCase())
                    .timeout(3000)
                    .get();

            String mode;
            switch (this.counterMode){
                case COUNTERS_WEAK:default:
                    mode = MODE_WEAK;
                    break;
                case COUNTERS_STRONG:
                    mode = MODE_STRONG;
            }
            return doc.getElementById(mode).getElementsByClass(CLASS_ENTITY);


        } catch (IOException e) {
            return null;
        }

    }

    @Override
    protected void onPostExecute(final Elements elements) {
        if(elements != null){
            StaticDataHandler.getInstance().getChampions(context, new StaticDataHandler.ResponseListener<List<ChampionDto>>() {
                @Override
                public void onResponse(List<ChampionDto> response) {
                    List<ChampionDto> champions = new ArrayList<ChampionDto>();
                    for (ChampionDto championDto : response) {
                        for (Element element : elements) {
                            if(element.attr(ATTR_DATA_NAME).toLowerCase().equals(championDto.getKey().toLowerCase())){
                                champions.add(championDto);
                                break;
                            }
                        }
                    }


                    if(listener != null){
                        listener.onResponse(champions);
                    }
                }

                @Override
                public void onError(TeemoException e) {
                    if(listener != null){
                        listener.onError(e);
                    }
                }
            });
        }
    }
}
