package com.imagesoftware.anubhav.vacmet.database.converters;

import android.arch.persistence.room.TypeConverter;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * Created by Anubhav-Singh on 09-01-2018.
 */

public class ArrayListConverter {

    @TypeConverter
    public String convertFromArrayList(ArrayList<String> list){
        String s = "";
        if(list!=null && list.size()>0) {
            Iterator<String> listIterator = list.iterator();
            while (listIterator.hasNext()) {
                String token = listIterator.next();
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
    public ArrayList<String> convertToArrayList(String result){
        ArrayList<String> tokenList = null;
        if(!TextUtils.isEmpty(result)) {
            if(result.contains("$")) {
                String[] seperatedTokens = result.split(Pattern.quote("$"));
                Log.i("ArrayListConverter", "convertToArrayList: " + seperatedTokens[0] + " " + seperatedTokens[seperatedTokens.length - 1]);

                tokenList = new ArrayList<>(Arrays.asList(seperatedTokens));
            }else{
                tokenList = new ArrayList<>();
                tokenList.add(result);
            }
        }
        return tokenList;
    }

}
