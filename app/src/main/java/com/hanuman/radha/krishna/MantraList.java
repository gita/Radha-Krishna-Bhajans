package com.hanuman.radha.krishna;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * RadhaKrishna
 */

public class MantraList extends AppCompatActivity implements AdapterView.OnItemClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    ListView list;
    private FirebaseAnalytics mFirebaseAnalytics;
    private InterstitialAd mInterstitialAd;
    private Boolean mAdFree = false;
    private static final String TEST_DEVICE ="85ECEB00A98B47FDFBFECC2639C994EF";
    private static final String RADHA = "C302756BCFE01E68DED07FA309375005";
    private static final String KRISHNA = "57F6141E3D949BD28C1E6144AE70F72E";
    private SharedPreferences mSharedPreferences;

    String[] itemname = {
            "Hare Krishna Hare Rama Mahamantra\nहरे कृष्णा हरे रामा महामंत्र",
            "Maiya Mori Main Nahi Maakhan Khaayo\nमैया मोरी मैं नहीं माखन खायो",
            "Govind Mero Hai Gopal Mero Hai\nगोविन्द मेरो है गोपाल मेरो है",
            "Om Mantra\nॐ मंत्र",
            "Aarti Kunj Bihari Ki\nआरती कुंज बिहारी की",
            "Bada Natkhat Hai Krishna Kanhaiya\nबड़ा नटखट है कृष्ण कन्हैया",
            "Choti Choti Gaiya Chotay Chotay Gwaal\nछोटी छोटी गैया छोटे छोटे ग्वाल",
            "Yashomati Maiya Se Bole Nandlala\nयशोमति मैया से बोले नंदलाला",
            "Mera Aapki Kripa Se Sab Kaam Ho Raha Hai\nमेरा आपकी कृपा से सब काम हो रहा है",
            "Achyutam Keshavam\nअच्युतम केशवम",
            "Are Dwarpalo Kanhaiya Se Kehdo\nअरे द्वारपालों कन्हैया से कहदो",
            "Ek Baar To Radha bankar Dekho Mere Sawariya\nएक बार तो राधा बनकर देखो मेरे सावरिया",
            "Gopal Muraliya Wale\nगोपाल मुरलिया वाले",
            "Govind Bolo hari Gopal Bolo\nगोविन्द बोलो हरी गोपाल बोलो",
            "Kabhi Ram Banke Kabhi Shyam Banke\nकभी राम बनके कभी श्याम बनके",
            "Kisi Ne Mera Shyam Dekha\nकिसी ने मेरा श्याम देखा",
            "Mithe Ras Se Bharyo Radha Rani\nमीठे रस से भर्यो राधा रानी",
            "Mohana Muralidhara\nमोहना मुरलीधरा",
            "Om Jai Jagdish Hare\nॐ जय जगदीश हरे",
            "Radhe Radhe Japa Karo\nराधे राधे जपा करो",
            "Shri Krishna Govind Hare Murari\nश्री कृष्ण गोविन्द हरे मुरारी",
            "Shyam Teri Bansi\nश्याम तेरी बंसी",
            "Shyama Aan Baso Vrindavan\nश्यामा आन बसों वृन्दावन",

    };
    Integer[] imgid = {
            R.drawable.krishna4,
            R.drawable.krishna3,
            R.drawable.krishna2,
            R.drawable.om,
            R.drawable.krishna5,
            R.drawable.krishna4,
            R.drawable.krishna1,
            R.drawable.krishna7,
            R.drawable.krishna8,
            R.drawable.krishna9,
            R.drawable.krishna11,
            R.drawable.krishna12,
            R.drawable.krishna2,
            R.drawable.krishna14,
            R.drawable.krishna3,
            R.drawable.krishna16,
            R.drawable.krishna17,
            R.drawable.krishna18,
            R.drawable.krishna19,
            R.drawable.krishna20,
            R.drawable.krishna10,
            R.drawable.krishna21,
            R.drawable.krishna4,
    };

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.mantra_list);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mAdFree = mSharedPreferences.getBoolean(getResources().getString(R.string.pref_remove_ads_key), false);


        //ListView listView = (ListView) findViewById(R.id.sites_list);
        //listView.setOnItemClickListener(this);
        CustomListAdapter adapter = new CustomListAdapter(this, itemname, imgid);
        list = (ListView) findViewById(R.id.mantras_list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(this);

        loadInterstitial();

    }

    private void loadInterstitial(){
        String interstitialId = "";
        if (BuildConfig.DEBUG) {
            interstitialId = "ca-app-pub-3940256099942544/1033173712";
        } else {
            interstitialId = "ca-app-pub-4070209682123577/7601384893";
        }

        if (!mAdFree) {
            AdRequest adRequest = new AdRequest.Builder().build();
            InterstitialAd.load(MantraList.this,interstitialId, adRequest, new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                    super.onAdLoaded(interstitialAd);
                    mInterstitialAd = interstitialAd;
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdShowedFullScreenContent() {
                            super.onAdShowedFullScreenContent();
                            mInterstitialAd = null;
                            loadInterstitial();
                        }
                    });
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    // Handle the error
                    Log.i("Ads", loadAdError.getMessage());
                    mInterstitialAd = null;
                }
            });
        }
    }

    @Override
    public void onItemClick(AdapterView<?> l, View v, int position, long id) {

        if (mInterstitialAd!=null) {
            mInterstitialAd.show(MantraList.this);
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

        Intent intent = new Intent();

        switch (position) {
            case 0:
                intent.setClass(this, KrishnaBhajan.class);
                intent.putExtra("filename", "mahamantra");
                intent.putExtra("mantra", "mahamantra_bhajan");
                intent.putExtra("mantra_name", "Hare Krishna Hare Rama");
                intent.putExtra("image_id", 1);

                startActivity(intent);

                break;
            case 1:
                intent.setClass(this, KrishnaBhajan.class);
                intent.putExtra("filename", "maiyamorimain");
                intent.putExtra("mantra", "maiya_mori_bhajan");
                intent.putExtra("mantra_name", "Maiya Mori Main Nahi Maakhan Khayo");
                intent.putExtra("image_id", 2);
                startActivity(intent);

                break;
            case 2:
                intent.setClass(this, KrishnaBhajan.class);
                intent.putExtra("filename", "govindmerohai");
                intent.putExtra("mantra", "govind_mero_bhajan");
                intent.putExtra("mantra_name", "Govind Mero Hai Gopal Mero Hai");
                intent.putExtra("image_id", 3);
                startActivity(intent);

                break;
            case 3:
                intent.setClass(this, KrishnaBhajan.class);
                intent.putExtra("filename", "om");
                intent.putExtra("mantra", "om_mantra_bhajan");
                intent.putExtra("mantra_name", "Om Mantra");
                intent.putExtra("image_id", 4);
                startActivity(intent);

                break;
            case 4:
                intent.setClass(this, KrishnaBhajan.class);
                intent.putExtra("filename", "aarti_kunj_bihari_ki");
                intent.putExtra("mantra", "aarti_kunj_bihari_bhajan");
                intent.putExtra("mantra_name", "Aarti Kunj Bihari Ki");
                intent.putExtra("image_id", 5);
                startActivity(intent);

                break;
            case 5:
                intent.setClass(this, KrishnaBhajan.class);
                intent.putExtra("filename", "badanatkhathaikrishnakanhaiya");
                intent.putExtra("mantra", "bada_natkhat_bhajan");
                intent.putExtra("mantra_name", "Bada Natkhat Hai Krishna");
                intent.putExtra("image_id", 6);
                startActivity(intent);

                break;
            case 6:
                intent.setClass(this, KrishnaBhajan.class);
                intent.putExtra("filename", "choti_choti_gaiya_krishna");
                intent.putExtra("mantra", "choti_choti_bhajan");
                intent.putExtra("mantra_name", "Choti Choti Gaiya Chotay");
                intent.putExtra("image_id", 7);
                startActivity(intent);

                break;
            case 7:
                intent.setClass(this, KrishnaBhajan.class);
                intent.putExtra("filename", "yashomatimaiyasebolenandlala");
                intent.putExtra("mantra", "yashomati_bhajan");
                intent.putExtra("mantra_name", "Yashomati Maiya Se Bole Nandlala");
                intent.putExtra("image_id", 8);
                startActivity(intent);

                break;
            case 8:
                intent.setClass(this, KrishnaBhajan.class);
                intent.putExtra("filename", "mera_aap_ki_kripa");
                intent.putExtra("mantra", "mera_aapki_kripa_bhajan");
                intent.putExtra("mantra_name", "Mera Aapki Kripa Se Sab Kaam");
                intent.putExtra("image_id", 9);
                startActivity(intent);
                break;
            case 9:
                intent.setClass(this, KrishnaBhajan.class);
                intent.putExtra("filename", "achyutamkeshavam");
                intent.putExtra("mantra", "achyutam_keshavam_bhajan");
                intent.putExtra("mantra_name", "Achyutam Keshavam");
                intent.putExtra("image_id", 10);
                startActivity(intent);

                break;

            case 10:
                intent.setClass(this, KrishnaBhajan.class);
                intent.putExtra("filename", "aredwarpalokanhaiya");
                intent.putExtra("mantra", "aredwarpalokanhaiya_bhajan");
                intent.putExtra("mantra_name", "Are Dwarpalo Kanhaiya Se Kehdo");
                intent.putExtra("image_id", 11);
                startActivity(intent);

                break;

            case 11:
                intent.setClass(this, KrishnaBhajan.class);
                intent.putExtra("filename", "ek_baar_to_radha_bankar");
                intent.putExtra("mantra", "ek_baar_to_radha_bankar_bhajan");
                intent.putExtra("mantra_name", "Ek Baar To Radha bankar Dekho Mere Sawariya");
                intent.putExtra("image_id", 12);
                startActivity(intent);

                break;

            case 12:
                intent.setClass(this, KrishnaBhajan.class);
                intent.putExtra("filename", "gopal_muraliya_wale");
                intent.putExtra("mantra", "gopal_muraliya_wale_bhajan");
                intent.putExtra("mantra_name", "Gopal Muraliya Wale");
                intent.putExtra("image_id", 13);
                startActivity(intent);

                break;

            case 13:
                intent.setClass(this, KrishnaBhajan.class);
                intent.putExtra("filename", "govind_bolo_hari_gopal_bolo");
                intent.putExtra("mantra", "govind_bolo_hari_gopal_bolo_bhajan");
                intent.putExtra("mantra_name", "Govind Bolo hari Gopal Bolo");
                intent.putExtra("image_id", 14);
                startActivity(intent);

                break;

            case 14:
                intent.setClass(this, KrishnaBhajan.class);
                intent.putExtra("filename", "kabhi_ram_banke");
                intent.putExtra("mantra", "kabhi_ram_banke_bhajan");
                intent.putExtra("mantra_name", "Kabhi Ram Banke Kabhi Shyam Banke");
                intent.putExtra("image_id", 15);
                startActivity(intent);
                break;

            case 15:
                intent.setClass(this, KrishnaBhajan.class);
                intent.putExtra("filename", "kisi_ne_mera_shyam_dekha");
                intent.putExtra("mantra", "kisi_ne_mera_shyam_dekha_bhajan");
                intent.putExtra("mantra_name", "Kisi Ne Mera Shyam Dekha");
                intent.putExtra("image_id", 16);
                startActivity(intent);
                break;

            case 16:
                intent.setClass(this, KrishnaBhajan.class);
                intent.putExtra("filename", "mithe_ras_se_bharyo");
                intent.putExtra("mantra", "mithe_ras_se_bharyo_bhajan");
                intent.putExtra("mantra_name", "Mithe Ras Se Bharyo Radha Rani");
                intent.putExtra("image_id", 17);
                startActivity(intent);
                break;

            case 17:
                intent.setClass(this, KrishnaBhajan.class);
                intent.putExtra("filename", "mohana_muralidhara");
                intent.putExtra("mantra", "mohana_muralidhara_bhajan");
                intent.putExtra("mantra_name", "Mohana Muralidhara");
                intent.putExtra("image_id", 18);
                startActivity(intent);
                break;

            case 18:
                intent.setClass(this, KrishnaBhajan.class);
                intent.putExtra("filename", "om_jai_jagadish_hare");
                intent.putExtra("mantra", "om_jai_jagadish_hare_bhajan");
                intent.putExtra("mantra_name", "Om Jai Jagdish Hare");
                intent.putExtra("image_id", 19);
                startActivity(intent);
                break;

            case 19:
                intent.setClass(this, KrishnaBhajan.class);
                intent.putExtra("filename", "radhe_radhe_japa_karo");
                intent.putExtra("mantra", "radhe_radhe_japa_karo_bhajan");
                intent.putExtra("mantra_name", "Radhe Radhe Japo Chale Aayenge Bihari");
                intent.putExtra("image_id", 20);
                startActivity(intent);
                break;

            case 20:
                intent.setClass(this, KrishnaBhajan.class);
                intent.putExtra("filename", "shri_krishna_govind_hare_murari");
                intent.putExtra("mantra", "shri_krishna_govind_hare_murari_bhajan");
                intent.putExtra("mantra_name", "Shri Krishna Govind Hare Murari");
                intent.putExtra("image_id", 21);
                startActivity(intent);
                break;

            case 21:
                intent.setClass(this, KrishnaBhajan.class);
                intent.putExtra("filename", "shyam_teri_bansi_pukare_radha_naam");
                intent.putExtra("mantra", "shyam_teri_bansi_pukare_radha_naam_bhajan");
                intent.putExtra("mantra_name", "Shyam Teri Bansi");
                intent.putExtra("image_id", 22);
                startActivity(intent);
                break;

            case 22:
                intent.setClass(this, KrishnaBhajan.class);
                intent.putExtra("filename", "shyama_aan_baso_vrindavan");
                intent.putExtra("mantra", "shyama_aan_baso_vrindavan_bhajan");
                intent.putExtra("mantra_name", "Shyama Aan Baso Vrindavan");
                intent.putExtra("image_id", 23);
                startActivity(intent);
                break;

            default:
                intent.setClass(this, KrishnaBhajan.class);
                intent.putExtra("filename", "mahamantra");
                intent.putExtra("mantra", "mahamantra_bhajan");
                intent.putExtra("mantra_name", "Hare Krishna Hare Rama");
                intent.putExtra("image_id", 1);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals((getString(R.string.pref_remove_ads_key)))){
            mAdFree = sharedPreferences.getBoolean(key, false);
            MantraList.this.recreate();
        }
    }

}
