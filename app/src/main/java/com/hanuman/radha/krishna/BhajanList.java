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
import android.widget.Toast;

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

public class BhajanList extends AppCompatActivity implements AdapterView.OnItemClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    ListView list;
    private FirebaseAnalytics mFirebaseAnalytics;
    private InterstitialAd mInterstitialAd;
    private Boolean mAdFree = false;
    private static final String TEST_DEVICE ="85ECEB00A98B47FDFBFECC2639C994EF";
    private static final String RADHA = "C302756BCFE01E68DED07FA309375005";
    private static final String KRISHNA = "57F6141E3D949BD28C1E6144AE70F72E";
    private SharedPreferences mSharedPreferences;

    String[] itemname = {
            "Radhe Radhe Barsane Wali Radhe\nराधे राधे बरसाने वाली राधे",
            "Maiya Mori Main Nahi Maakhan Khaayo\nमैया मोरी मैं नहीं माखन खायो",
            "Aisi Lagi Lagan\nऐसी लागी लगन",
            "O Palanhaare\nओ पालनहारे",
            "Aarti Kunj Bihari Ki\nआरती कुंज बिहारी की",
            "Bada Natkhat Hai Krishna Kanhaiya\nबड़ा नटखट है कृष्ण कन्हैया",
            "Achyuta Ashtakam\nअच्युता अष्टकम",
            "Yashomati Maiya Se Bole Nandlala\nयशोमति मैया से बोले नंदलाला",
            "Shri Hari Stotram\nश्री हरी स्तोत्रम",
            "Achyutam Keshavam\nअच्युतम केशवम",
            "Vishnu Shatanama Stotram\nविष्णु शतनाम स्तोत्रम",
            "Jai Radha Madhav\nजय राधा माधव",
            "Radhe Radhe Govind Gopal Radhe\nराधे राधे गोविन्द गोपाल राधे",
            "Radhe Krishna Radhe Shyam\nराधे कृष्ण राधे श्याम",
            "Shri Krishna Govind Hare Murari\nश्री कृष्ण गोविन्द हरे मुरारी",
            "Shyam Teri Bansi\nश्याम तेरी बंसी",
            "Kishori Kuch Aisa Intejam Ho Jaye\nकिशोरी कुछ ऐसा इंतजाम हो जाये",
            "Shri Radha Sahastra Naam Yatra\nश्री राधा सहस्त्र नाम यात्रा",
            "Govinda Krishna Hari Om\nगोविंदा कृष्णा हरी ॐ",
            "Govind Bolo Hari Gopal Bolo\nगोविन्द बोलो हरी गोपाल बोलो",
            "Madhurashtakam\nमधुराष्टकं",
            "Shyam Chudi Bechne Aaya\nश्याम चूड़ी बेचने आया",
            "Nand Ke Anand Bhayo\nनन्द के आनंद भयो",
            "Hey Krishna Gopal Hari\nहे कृष्ण गोपाल हरी",
            "Krishna Kanha\nकृष्ण कान्हा",
            "Gopala Gopala\nगोपाला गोपाला",
            "Shri Ram Stuti\nश्री राम स्तुति",
            "Hari Om Tat Sat\nहरी ॐ तत् सत्",
            "Krishna Ashtakam\nकृष्ण अष्टकम",
            "Jape Ja Radhe Radhe\nजपे जा राधे राधे",
            "Goverdhan Ko Jao\nगोवेर्धन को जाओ",
            "Krishna Naam Ke Heere Moti\nकृष्ण नाम के हीरे मोती",
            "Kishori Meri Kusum Kali\nकिशोरी मेरी कुसुम कली",
            "Mere Shyam Murli Wale\nमेरे श्याम मुरली वाले",
            "Bankey Bihari Ji Ki Aarti\nबांके बिहारी जी की आरती",
            "Radhika Gori Se\nराधिका गोरी से",
            "Mera Aapki Kripa Se\nमेरा आपकी कृपा से",
            "Radhe Radhe Radhe Shyam\nराधे राधे राधे श्याम",
            "Radha Dhund Rahi\nराधा ढूंढ रही",
            "Yashoda Ka Nandlala\nयशोदा का नंदलाला",
            "Jaya Janardhana\n"

    };
    Integer[] imgid = {
            R.drawable.krishna4,
            R.drawable.krishna3,
            R.drawable.krishna2,
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
            R.drawable.krishna4,
            R.drawable.krishna5,
            R.drawable.krishna8,
            R.drawable.krishna7,
            R.drawable.krishna9,
            R.drawable.krishna14,
            R.drawable.krishna4,
            R.drawable.krishna3,
            R.drawable.krishna2,
            R.drawable.krishna5,
            R.drawable.krishna4,
            R.drawable.ram1,
            R.drawable.krishna5,
            R.drawable.krishna11,
            R.drawable.krishna8,
            R.drawable.krishna12,
            R.drawable.krishna9,
            R.drawable.krishna14,
            R.drawable.krishna16,
            R.drawable.krishna1,
            R.drawable.krishna17,
            R.drawable.krishna18,
            R.drawable.krishna21,
            R.drawable.krishna7,
            R.drawable.krishna3,
            R.drawable.krishna1,
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
            InterstitialAd.load(BhajanList.this,interstitialId, adRequest, new InterstitialAdLoadCallback() {
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
            mInterstitialAd.show(BhajanList.this);
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

        Intent intent = new Intent();

        switch (position) {
            case 0:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/by_artist/G/Gaurav%20Krishna%20Goswami/Radha%20Naam%20Sang%20Brij%2084%20Kos%20Yatra%20%28Radhe%20Radhe%20Barsane%20Wali%20Radhe%29%20%282009%29/1_1%20-%20Radha%20Naam%20Sang%20Brij%2084%20Kos%20Yatra%20%28Radhe%20Radhe%20Barsane%20Wali%20Radhe%29-vbr-V5.mp3");
                intent.putExtra("mantra", "barsane_wali_radhe_bhajan");
                intent.putExtra("mantra_name", "Radhe Radhe Barsane Wali Radhe");
                intent.putExtra("image_id", 14);
                startActivity(intent);

                break;
            case 1:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/various_artists/M/Maiya%20Mori%20By%20Anup%20Jalota%20%282016%29/1_1%20-%20Maiya%20Mori-vbr-V5.mp3");
                intent.putExtra("mantra", "maiya_mori_bhajan");
                intent.putExtra("mantra_name", "Maiya Mori Main Nahi Maakhan Khayo");
                intent.putExtra("image_id", 1);
                startActivity(intent);

                break;
            case 2:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/by_artist/A/Anup%20Jalota/Aisi%20Laagi%20Lagan/1_1%20-%20Aisi%20Laagi%20Lagan-vbr-V5.mp3");
                intent.putExtra("mantra", "aisi_lagi_lagan_bhajan");
                intent.putExtra("mantra_name", "Aisi Lagi Lagan");
                intent.putExtra("image_id", 2);
                startActivity(intent);

                break;
            case 3:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/various_artists/B/Bhajans%20From%20Films-/1_1%20-%20O%20Paalanhaare-vbr-V5.mp3");
                intent.putExtra("mantra", "o_palanhaare_bhajan");
                intent.putExtra("mantra_name", "O Palanhaare");
                intent.putExtra("image_id", 3);
                startActivity(intent);

                break;
            case 4:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/by_artist/A/Anuradha%20Paudwal/Aarti/1_6%20-%20Aarti%20Kunj%20Bihari%20Ki-vbr-V5.mp3");
                intent.putExtra("mantra", "aarti_kunj_bihari_bhajan");
                intent.putExtra("mantra_name", "Aarti Kunj Bihari Ki");
                intent.putExtra("image_id", 4);
                startActivity(intent);

                break;
            case 5:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/by_artist/L/Lata%20Mangeshkar/Bhajans%20by%20Lata%20Mangeshkar%20%282015%29/1_14%20-%20Bada%20Natkhat%20Hai%20Yeh%20-%20From%20Amar%20Prem%20-vbr-V5.mp3");
                intent.putExtra("mantra", "bada_natkhat_bhajan");
                intent.putExtra("mantra_name", "Bada Natkhat Hai Krishna");
                intent.putExtra("image_id", 5);
                startActivity(intent);

                break;
            case 6:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/various_artists/H/Holy%20Chants%20On%20Lord%20Vishnu%20And%20Mahalakshmi/1_6%20-%20Achyutashtakam-vbr-V5.mp3");
                intent.putExtra("mantra", "achyutashtakam_bhajan");
                intent.putExtra("mantra_name", "Achyuta Ashtakam");
                intent.putExtra("image_id", 6);
                startActivity(intent);

                break;
            case 7:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/by_artist/L/Lata%20Mangeshkar/Bhajans%20by%20Lata%20Mangeshkar%20%282015%29/1_12%20-%20Yashomati%20Maiya%20Se%20Bole%20Nandlala%20-%20From%20Satyam%20Shivam%20Sundaram%20-vbr-V5.mp3");
                intent.putExtra("mantra", "yashomati_bhajan");
                intent.putExtra("mantra_name", "Yashomati Maiya Se Bole Nandlala");
                intent.putExtra("image_id", 7);
                startActivity(intent);

                break;
            case 8:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/various_artists/H/Holy%20Chants%20On%20Lord%20Vishnu%20And%20Mahalakshmi/1_4%20-%20Shree%20Hari%20Stotram-vbr-V5.mp3");
                intent.putExtra("mantra", "shri_hari_stotram_bhajan");
                intent.putExtra("mantra_name", "Shri Hari Stotram");
                intent.putExtra("image_id", 8);
                startActivity(intent);
                break;
            case 9:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/by_artist/V/Vikram%20Hazra/Krishna%20-%20The%20Art%20Of%20Living%20%282015%29/1_1%20-%20Achutam%20Keshavam-vbr-V5.mp3");
                intent.putExtra("mantra", "achyutam_keshavam_bhajan");
                intent.putExtra("mantra_name", "Achyutam Keshavam");
                intent.putExtra("image_id", 9);
                startActivity(intent);

                break;

            case 10:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/various_artists/H/Holy%20Chants%20On%20Lord%20Vishnu%20And%20Mahalakshmi/1_8%20-%20Vishnu%20Shatanama%20Stotram-vbr-V5.mp3");
                intent.putExtra("mantra", "vishnu_shatanama_bhajan");
                intent.putExtra("mantra_name", "Vishnu Shatanama Stotram");
                intent.putExtra("image_id", 10);
                startActivity(intent);

                break;

            case 11:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/by_artist/J/Jagjit%20Singh/Jai%20Radha%20Madhav%20Shree%20Krishna%20Dhun%20%282000%29/1_2%20-%20Jai%20Radha%20Madhav%20%28From%20Hey%20Gobind%20Hey%20Gopal%20-%20Jagjit%20Singh%20And%20Chitra%20Singh%29-vbr-V5.mp3");
                intent.putExtra("mantra", "jai_radha_madhav_bhajan");
                intent.putExtra("mantra_name", "Jai Radha Madhav");
                intent.putExtra("image_id", 11);
                startActivity(intent);

                break;

            case 12:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/by_artist/H/Hariharan/Shree%20Ram%20-%20Shyam%20Dhun%20%282014%29/1_5%20-%20Radhe%20Radhe%20Govind%20Gopal%20Radhe-vbr-V5.mp3");
                intent.putExtra("mantra", "radhe_radhe_govind_bhajan");
                intent.putExtra("mantra_name", "Radhe Radhe Govind Gopal Radhe");
                intent.putExtra("image_id", 12);
                startActivity(intent);

                break;

            case 13:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/by_artist/J/Jagjit%20Singh/Radhe%20Krishan%20Radhe%20Shyam/1_2%20-%20Radhe%20Krishan-vbr-V5.mp3");
                intent.putExtra("mantra", "radhe_krishna_radhe_shyam_bhajan");
                intent.putExtra("mantra_name", "Radhe Krishna Radhe Shyam");
                intent.putExtra("image_id", 13);
                startActivity(intent);

                break;


            case 14:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/various_artists/S/Shri%20Krishna%20Govind%20Hare%20Murari%20%282013%29/1_1%20-%20Shri%20Krishna%20Govind-vbr-V5.mp3");
                intent.putExtra("mantra", "shri_krishna_govind_hare_murari_bhajan");
                intent.putExtra("mantra_name", "Shri Krishna Govind Hare Murari");
                intent.putExtra("image_id", 14);
                startActivity(intent);
                break;

            case 15:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/various_artists/G/Geet%20Gata%20Chal%20%281975%29/1_3%20-%20Shyam%20Teri%20Bansi-vbr-V5.mp3");
                intent.putExtra("mantra", "shyam_teri_bansi_pukare_radha_naam_bhajan");
                intent.putExtra("mantra_name", "Shyam Teri Bansi");
                intent.putExtra("image_id", 15);
                startActivity(intent);
                break;

            case 16:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/by_artist/G/Gaurav%20Krishan%20Goswami/Kishori%20Kuch%20Aisa%20Intejam%20Ho%20Jaye%20%282009%29/1_1%20-%20Kishori%20Kuch%20Aisa%20Intejam%20Ho%20Jaye-vbr-V5.mp3");
                intent.putExtra("mantra", "kishori_kuch_aisa_intzam_ho_krishna_bhajan");
                intent.putExtra("mantra_name", "Kishori Kuch Aisa Intejam Ho Jaye");
                intent.putExtra("image_id", 16);
                startActivity(intent);
                break;

            case 17:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/by_artist/S/Shradheya%20Gaurav%20Krishan%20Goswami%20Ji/Radha%20Sahastra%20Naam%20Yatra%20%282015%29/1_1%20-%20Radha%20Sahastra%20Naam%20Yatra-vbr-V5.mp3");
                intent.putExtra("mantra", "radha_sahastra_naam_bhajan");
                intent.putExtra("mantra_name", "Shri Radha Sahastra Naam Yatra");
                intent.putExtra("image_id", 17);
                startActivity(intent);
                break;

            case 18:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/by_artist/S/Saket%20Kakkar/Giridhari%20-%20The%20Art%20Of%20Living%20%282015%29/1_3%20-%20Govinda%20Krishna%20Hari%20Om-vbr-V5.mp3");
                intent.putExtra("mantra", "govinda_krishna_hari_om_bhajan");
                intent.putExtra("mantra_name", "Govinda Krishna Hari Om");
                intent.putExtra("image_id", 18);
                startActivity(intent);
                break;

            case 19:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/various_artists/T/The%20Most%20Relaxing%20Devotional%20Album%20Ever%20%282012%29/1_13%20-%20Govind%20Bolo%20Hari%20Gopal%20Bolo-vbr-V5.mp3");
                intent.putExtra("mantra", "govind_bolo_hari_gopal_bolo_bhajan");
                intent.putExtra("mantra_name", "Govind Bolo Hari Gopal Bolo");
                intent.putExtra("image_id", 14);
                startActivity(intent);
                break;

            case 20:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/by_artist/S/Shradheya%20Gaurav%20Krishan%20Goswami%20Ji/Madhurashtakam%20Aivam%20Govind%20Damodar%20Stotra%20%282015%29/1_13%20-%20Hridyam%20Madhuram-vbr-V5.mp3");
                intent.putExtra("mantra", "madhurashtakam_bhajan");
                intent.putExtra("mantra_name", "Madhurashtakam");
                intent.putExtra("image_id", 19);
                startActivity(intent);
                break;

            case 21:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/by_artist/H/Hari%20Om%20Sharan/Kabhi%20Ram%20Banke%20Kabhi%20Shyam%20Banke%20%282000%29/1_1%20-%20Shyam%20Chudi%20Bechne%20Aaya-vbr-V5.mp3");
                intent.putExtra("mantra", "shyam_chudi_bechne_bhajan");
                intent.putExtra("mantra_name", "Shyam Chudi Bechne Aaya");
                intent.putExtra("image_id", 20);
                startActivity(intent);
                break;

            case 22:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/various_artists/B/Bhajans/1_4%20-%20Nand%20Ke%20Anand%20Bhayo-vbr-V5.mp3");
                intent.putExtra("mantra", "nand_ke_anand_bhayo_bhajan");
                intent.putExtra("mantra_name", "Nand Ke Anand Bhayo");
                intent.putExtra("image_id", 21);
                startActivity(intent);
                break;

            case 23:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/by_artist/J/Jagjit%20Singh/Saanwara%20-%20Krishna%20Bhajan%20N%20Kirtan%20-%20Jagjit%20Singh%20%281961%29/1_2%20-%20Hey%20Krishna%20Gopal%20Hari-vbr-V5.mp3");
                intent.putExtra("mantra", "hey_krishna_gopal_hari_bhajan");
                intent.putExtra("mantra_name", "Hey Krishna Gopal Hari");
                intent.putExtra("image_id", 22);
                startActivity(intent);
                break;

            case 24:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/by_artist/S/Sachin%20Limaye/Krishna%20Kanha%20-%20The%20Art%20Of%20Living%20%282015%29/1_1%20-%20Krishna%20Kanha-vbr-V5.mp3");
                intent.putExtra("mantra", "krishna_kanha_bhajan");
                intent.putExtra("mantra_name", "Krishna Kanha");
                intent.putExtra("image_id", 23);
                startActivity(intent);
                break;

            case 25:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/by_artist/S/Sachin%20Limaye/Krishna%20Kanha%20-%20The%20Art%20Of%20Living%20%282015%29/1_3%20-%20Gopala%20Gopala-vbr-V5.mp3");
                intent.putExtra("mantra", "gopala_gopala_bhajan");
                intent.putExtra("mantra_name", "Gopala Gopala");
                intent.putExtra("image_id", 24);
                startActivity(intent);
                break;

            case 26:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/by_artist/V/Vidhi%20Sharma/Ram%20Stuti%20%28Shri%20Ram%20Chandra%20Kripalu%20Bhajman%29%20%282013%29/1_1%20-%20Ram%20Stuti%20-%20Shri%20Ram%20Chandra%20Kripalu%20Bhajman-vbr-V5.mp3");
                intent.putExtra("mantra", "shri_ram_stuti_bhajan");
                intent.putExtra("mantra_name", "Shri Ram Stuti");
                intent.putExtra("image_id", 19);
                startActivity(intent);
                break;

            case 27:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/by_artist/B/Bhavesh%20Bhatt/Moolamantra%20Vol.%202%20%282004%29/1_1%20-%20Moolamantra%20Vol%202-vbr-V5.mp3");
                intent.putExtra("mantra", "hari_om_tat_sat_bhajan");
                intent.putExtra("mantra_name", "Hari Om Tat Sat");
                intent.putExtra("image_id", 9);
                startActivity(intent);
                break;

            case 28:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/by_artist/B/Bhanu%2C%20G%20Gayathri%20Devi%2C%20Gowri%2C%20Jaya%2C%20Krupa%2C%20Kruti%2C%20Ramya%2C%20Roopa%2C%20Saindhavi%2C%20Uma%20Mohan%2C%20Usha/Sacred%20Chants%20Vol%202%20%282002%29/1_8%20-%20Krishnashtakam-vbr-V5.mp3");
                intent.putExtra("mantra", "krishnashtakham_bhajan");
                intent.putExtra("mantra_name", "Krishna Ashtakam");
                intent.putExtra("image_id", 10);
                startActivity(intent);
                break;

            case 29:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/by_artist/M/Mridul%20Krishan%20Shastri/Best%20Of%20Krishna%20Bhajans%20%282014%29/1_7%20-%20Jape%20Ja%20Radhe%20Radhe-vbr-V5.mp3");
                intent.putExtra("mantra", "jape_ja_radhe_radhe_bhajan");
                intent.putExtra("mantra_name", "Jape Ja Radhe Radhe");
                intent.putExtra("image_id", 14);
                startActivity(intent);
                break;

            case 30:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/by_artist/M/Mridul%20Krishan%20Shastri/Best%20Of%20Krishna%20Bhajans%20%282014%29/1_6%20-%20Goverdhan%20Ko%20Jao-vbr-V5.mp3");
                intent.putExtra("mantra", "goverdhan_ko_jao_bhajan");
                intent.putExtra("mantra_name", "Goverdhan Ko Jao");
                intent.putExtra("image_id", 23);
                startActivity(intent);
                break;

            case 31:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/by_artist/M/Mridul%20Krishan%20Shastri/Best%20Of%20Krishna%20Bhajans%20%282014%29/1_10%20-%20Krishan%20Naam%20Ke%20Heere%20Moti-vbr-V5.mp3");
                intent.putExtra("mantra", "krishna_naam_ke_heere_moti_bhajan");
                intent.putExtra("mantra_name", "Krishna Naam Ke Heere Moti");
                intent.putExtra("image_id", 5);
                startActivity(intent);
                break;

            case 32:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/by_artist/M/Mridul%20Krishan%20Shastri/Best%20Of%20Krishna%20Bhajans%20%282014%29/1_9%20-%20Kishori%20Meri%20Kusum%20Kali-vbr-V5.mp3");
                intent.putExtra("mantra", "kishori_meri_kusum_kali_bhajan");
                intent.putExtra("mantra_name", "Kishori Meri Kusum Kali");
                intent.putExtra("image_id", 3);
                startActivity(intent);
                break;

            case 33:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/by_artist/M/Mridul%20Krishan%20Shastri/Best%20Of%20Krishna%20Bhajans%20%282014%29/1_13%20-%20Mere%20Shyam%20Murli%20Wale-vbr-V5.mp3");
                intent.putExtra("mantra", "mere_shyam_murli_wale_bhajan");
                intent.putExtra("mantra_name", "Mere Shyam Murli Wale");
                intent.putExtra("image_id", 13);
                startActivity(intent);
                break;

            case 34:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/by_artist/M/Mridul%20Krishna%20Shastri/Mridul%20Aarti%20Vandana%20%282013%29/1_1%20-%20Bankey%20Bihari%20Ji%20Ki%20Aarti-vbr-V5.mp3");
                intent.putExtra("mantra", "bankey_bihari_ji_ki_aarti_bhajan");
                intent.putExtra("mantra_name", "Bankey Bihari Ji Ki Aarti");
                intent.putExtra("image_id", 4);
                startActivity(intent);
                break;

            case 35:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/by_artist/V/Vinod%20Agarwal/Radhika%20Gori%20Se/1_2%20-%20Radhika%20Gori%20Se-vbr-V5.mp3");
                intent.putExtra("mantra", "radhika_gori_se_bhajan");
                intent.putExtra("mantra_name", "Radhika Gori Se");
                intent.putExtra("image_id", 16);
                startActivity(intent);
                break;

            case 36:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/by_artist/V/Vinod%20Agarwal/Duniya%20Kya%20Jaane/1_1%20-%20Mera%20Aapki%20Kripa%20Se-vbr-V5.mp3");
                intent.putExtra("mantra", "mera_aapki_kripa_se_bhajan");
                intent.putExtra("mantra_name", "Mera Aapki Kripa Se");
                intent.putExtra("image_id", 15);
                startActivity(intent);
                break;

            case 37:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/various_artists/J/Janmashtami%20Special/1_20%20-%20Radhe%20Radhe%20Radhe%20Shyam-vbr-V5.mp3");
                intent.putExtra("mantra", "radhe_radhe_radhe_shyam_bhajan");
                intent.putExtra("mantra_name", "Radhe Radhe Radhe Shyam");
                intent.putExtra("image_id", 17);
                startActivity(intent);
                break;

            case 38:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/by_artist/F/Falguni%20Pathak/Vandan%201/1_7%20-%20Radha%20Duhnd%20Rahi%20Kisene%20Mera%20Shyam%20Dekha-vbr-V5.mp3");
                intent.putExtra("mantra", "radha_dhund_rahi_bhajan");
                intent.putExtra("mantra_name", "Radha Dhund Rahi");
                intent.putExtra("image_id", 21);
                startActivity(intent);
                break;

            case 39:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/various_artists/B/Best%20of%20Laxmikant%20%26%20Pyarelal/1_8%20-%20Yashoda%20Ka%20Nandlala-vbr-V5.mp3");
                intent.putExtra("mantra", "yashoda_ka_nandlala_bhajan");
                intent.putExtra("mantra_name", "Yashoda Ka Nandlala");
                intent.putExtra("image_id", 1);
                startActivity(intent);
                break;

            case 40:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/by_artist/G/Gauthami%20S.%20Moorthy/Ba%20Ba%20Krishna%20%281970%29/1_5%20-%20Jayajanardana-vbr-V5.mp3");
                intent.putExtra("mantra", "jaya_janardhana_bhajan");
                intent.putExtra("mantra_name", "Jaya Janardhana");
                intent.putExtra("image_id", 4);
                startActivity(intent);
                break;


            default:
                intent.setClass(this, KanhaBhajan.class);
                intent.putExtra("filename", "https://media-audio.mio.to/by_artist/G/Gaurav%20Krishna%20Goswami/Radha%20Naam%20Sang%20Brij%2084%20Kos%20Yatra%20%28Radhe%20Radhe%20Barsane%20Wali%20Radhe%29%20%282009%29/1_1%20-%20Radha%20Naam%20Sang%20Brij%2084%20Kos%20Yatra%20%28Radhe%20Radhe%20Barsane%20Wali%20Radhe%29-vbr-V5.mp3");
                intent.putExtra("mantra", "barsane_wali_radhe_bhajan");
                intent.putExtra("mantra_name", "Radhe Radhe Barsane Wali Radhe");
                intent.putExtra("image_id", 18);
                startActivity(intent);

                break;
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals((getString(R.string.pref_remove_ads_key)))){
            mAdFree = sharedPreferences.getBoolean(key, false);
            BhajanList.this.recreate();
        }
    }

}
