package com.imagesoftware.anubhav.vacmet.database.converters;

import android.arch.persistence.room.TypeConverter;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * Created by Anubhav-Singh on 12-01-2018.
 */

public class ArrayListDoubleConverter {
    @TypeConverter
    public String convertFromArrayList(ArrayList<Double> list){
        String s = "";
        if(list!=null && list.size()>0) {
            Iterator<Double> listIterator = list.iterator();
            while (listIterator.hasNext()) {
                String token = Double.toString(listIterator.next());
                if (token != null && listIterator.hasNext()) {
                    s += token + "$";
                } else if (token != null) {
                    s += token;
                }

            }
        }
        return s;
    }

    @TypeConverter
    public ArrayList<Double> convertToArrayList(String result){
        ArrayList<Double> tokenList = null;
        if(!TextUtils.isEmpty(result)) {
            if(result.contains("$")) {
                String[] seperatedTokens = result.split(Pattern.quote("$"));
                tokenList = new ArrayList<>();
                Log.i("ArrayListConverter", "convertToArrayList: " + seperatedTokens[0] + " " + seperatedTokens[seperatedTokens.length - 1]);
                for (int i = 0; i < seperatedTokens.length; i++) {
                    if (seperatedTokens[i].equalsIgnoreCase("0.0")) {
                        tokenList.add(0.0);
                    } else {
                        tokenList.add(Double.parseDouble(seperatedTokens[i].contains(",") ? seperatedTokens[i].replaceAll(",", "") : seperatedTokens[i]));
                    }
                }
            }else{
                tokenList = new ArrayList<>();
                tokenList.add(Double.parseDouble(result));
            }

        }
        return tokenList;
    }
}
