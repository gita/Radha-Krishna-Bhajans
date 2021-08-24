package com.hanuman.radha.krishna;


public final class PreferenceHelper {

    private static boolean mAdFree;

    private PreferenceHelper() {
    }

    public static void setAdFree(Boolean adFreePaidFor) {
        //Track if user has purchased "ad removal" for the app.  This check is used throughout the app
        //to determine whether or not to show ads.
        if (adFreePaidFor){
            mAdFree = true;
        } else {
            mAdFree = false;
        }
    }


    public static boolean getAdFree(){
        return mAdFree;
    }



}
