package com.hanuman.radha.krishna.classes;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    private Context context;
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;

    private final String Shared_prefrence = "info";
    private final String POSITION = "position";
    private final String MANTRA = "mantra";


    public SharedPref(Context context) {
        this.context = context;
    }

    public void PUT_POSITION(int value){

        editor = context.getSharedPreferences(Shared_prefrence, MODE_PRIVATE).edit();
        editor.putInt(POSITION,value);
        editor.apply();
    }


    public void PUT_MANTRA(String value){

        editor = context.getSharedPreferences(Shared_prefrence, MODE_PRIVATE).edit();
        editor.putString(MANTRA,value);
        editor.apply();
    }


    public int GET_POSITION(){
        prefs = context.getSharedPreferences(Shared_prefrence, MODE_PRIVATE);
        return prefs.getInt(POSITION,0);
    }


    public String GET_MANTRA(){
        prefs = context.getSharedPreferences(Shared_prefrence, MODE_PRIVATE);
        return prefs.getString(MANTRA,"om_mantra_bhajan");
    }

}
