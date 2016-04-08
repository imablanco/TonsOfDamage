package com.ablanco.tonsofdamage.utils;

import com.ablanco.teemo.model.staticdata.ChampionSpellDto;
import com.ablanco.teemo.model.staticdata.SpellVarsDto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Álvaro Blanco on 06/04/2016.
 * TonsOfDamage
 */
public class ChampionSpellParser {

    private final static String PATTERN_EFFECT = "\\{\\{\\se[0-9]\\s\\}\\}";
    private final static String PATTERN_VAR = "\\{\\{\\sa[0-9]\\s\\}\\}|\\{\\{\\sf[0-9]\\s\\}\\}";
    private final static String PATTERN_DIGIT = "\\d";
    private final static String PATTERN_WORD_DIGIT = "\\w\\d";
    private final static String REPLACEMENT_COST = "{{ cost }}";

    private static Pattern patternEffect = Pattern.compile(PATTERN_EFFECT);
    private static Pattern patternVar = Pattern.compile(PATTERN_VAR);
    private static Pattern patternDigit = Pattern.compile(PATTERN_DIGIT);
    private static Pattern patternWordDigit = Pattern.compile(PATTERN_WORD_DIGIT);

    public static String buildChampionSpellText(ChampionSpellDto championSpellDto) {

        String championSpellText = championSpellDto.getTooltip();
        Matcher effectMatcher = patternEffect.matcher(championSpellText);
        while (effectMatcher.find()) {
            Matcher matcherDigit = patternDigit.matcher(effectMatcher.group());
            while (matcherDigit.find()) {
                String value = "0";
                if(championSpellDto.getEffectBurn() != null && championSpellDto.getEffectBurn().get(Integer.parseInt(matcherDigit.group())) != null){
                    value = championSpellDto.getEffectBurn().get(Integer.parseInt(matcherDigit.group()));
                }
                championSpellText = championSpellText.replace(effectMatcher.group(), value);
            }
        }

        Matcher varMatcher = patternVar.matcher(championSpellText);
        while (varMatcher.find()) {
            Matcher matcherWordDigit = patternWordDigit.matcher(varMatcher.group());
            while (matcherWordDigit.find()) {
                SpellVarsDto matchingSpellVar = null;
                if(championSpellDto.getVars() != null){
                    for (SpellVarsDto spellVar : championSpellDto.getVars()){
                        if(spellVar.getKey().equals(matcherWordDigit.group())){
                            matchingSpellVar = spellVar;
                            break;
                        }
                    }
                }
                StringBuilder builder = new StringBuilder();
                if(matchingSpellVar != null){
                    if(matchingSpellVar.getCoeff() != null){
                        for (Double coeff : matchingSpellVar.getCoeff()){
                            if(!builder.toString().isEmpty()){
                                builder.append("/");
                            }

                            builder.append(String.valueOf(coeff));
                        }
                    }
                }
                championSpellText = championSpellText.replace(varMatcher.group(), builder.toString());

            }
        }

        return championSpellText;
    }


    public static String getChampionSpellCost(ChampionSpellDto championSpellDto){
        String cost = championSpellDto.getResource().replace(REPLACEMENT_COST, championSpellDto.getCostBurn());

        Matcher effectMatcher = patternEffect.matcher(cost);
        while (effectMatcher.find()) {
            Matcher matcherDigit = patternDigit.matcher(effectMatcher.group());
            while (matcherDigit.find()) {
                String value = "0";
                if(championSpellDto.getEffectBurn() != null && championSpellDto.getEffectBurn().get(Integer.parseInt(matcherDigit.group())) != null){
                    value = championSpellDto.getEffectBurn().get(Integer.parseInt(matcherDigit.group()));
                }
                cost = cost.replace(effectMatcher.group(), value);
            }
        }

        return cost;
    }
}
