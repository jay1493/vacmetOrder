package com.imagesoftware.anubhav.vacmet.database.converters;

import android.arch.persistence.room.TypeConverter;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

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
                    s += token + ",";
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
            String[] seperatedTokens = result.split(",");
            tokenList = new ArrayList<>();
            Log.i("ArrayListConverter", "convertToArrayList: "+seperatedTokens[0]+" "+seperatedTokens[seperatedTokens.length-1]);
            for(int i=0; i< seperatedTokens.length; i++){
                tokenList.add(Double.parseDouble(seperatedTokens[i]));
            }

        }
        return tokenList;
    }
}
