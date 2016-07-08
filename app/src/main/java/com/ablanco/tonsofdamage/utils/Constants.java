package com.ablanco.tonsofdamage.utils;

/**
 * Created by Álvaro Blanco on 04/04/2016.
 * TonsOfDamage
 */
public class Constants {

    public final static String CHAMPION_VIDEO_URI = "http://cdn.leagueoflegends.com/champion-abilities/videos/mp4/0{0}_0{1}.mp4";

    public static class ShowCase{
        public static final String SHOW_CASE_BOTTOM_BAR = "show_case_bottom_bar";
        public static final String SHOW_CASE_MENU = "show_case_menu";
        public static final String SHOW_CASE_PROFILE = "show_case_profile";
        public static final String SHOW_CASE_FAV = "show_case_fav";
    }

    public static class Maps{

        public final static String MAP_BACKGROUND_IMAGE_URL = "http://cdn.leagueoflegends.com/game-info/1.1.9/images/content/map-banner-{0}.jpg";
        public final static String SUMMONERS_RIFT_ACR = "SR";
        public final static String TWISTED_TREELINE_ACR = "TT";
        public final static String HOWLING_ABYSS_ACR = "HA";
        public final static String CRYSTAL_SCAR_ACR = "CS";
        public final static String HOWLING_ABYSS_BLOCK_ITEM_LITERAL = "Map12";

        public final static String SUMMONERS_RIFT = "Summoner's Rift";
        public final static String TWISTED_TREELINE = "Twisted Treeline";
        public final static String HOWLING_ABYSS = "Howling Abyss";
        public final static String CRYSTAL_SCAR = "Crystal Scar";

        public static String getMapBackgroundImageUrl(String mapName){
            return MAP_BACKGROUND_IMAGE_URL.replace("{0}", mapName.toLowerCase());
        }
        public static String getNameForAcronym(String acr){
            if (acr.equalsIgnoreCase(SUMMONERS_RIFT_ACR)){
                return SUMMONERS_RIFT;
            } else if (acr.equalsIgnoreCase(TWISTED_TREELINE_ACR)){
                return TWISTED_TREELINE;
            }else if (acr.equalsIgnoreCase(HOWLING_ABYSS_ACR)){
                return HOWLING_ABYSS;
            }else if (acr.equalsIgnoreCase(CRYSTAL_SCAR_ACR)){
                return CRYSTAL_SCAR;
            }

            return null;
        }
    }
}
