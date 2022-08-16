package com.hanuman.radha.krishna;


import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.tabs.TabLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.hanuman.radha.krishna.classes.MediaStatus;
import com.hanuman.radha.krishna.classes.NotifyBuilder;
import com.hanuman.radha.krishna.classes.SharedPref;
import com.hanuman.radha.krishna.fragments.MainFragment;
import com.hanuman.radha.krishna.fragments.SecondFragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class KrishnaBhajan extends BaseActivity implements MediaPlayer.OnPreparedListener, SharedPreferences.OnSharedPreferenceChangeListener, MediaStatus {
    private ImageButton playButton;
    private ImageButton bellButton;
    private ImageButton shankhButton;
    private ImageButton repeatButton;
    private ImageButton repeatButton1;
    private ImageButton repeatButton2;
    private FirebaseAnalytics mFirebaseAnalytics;

    private final int PAUSE = 1;
    private final int PLAY = 0;

//     private int POSITION;

    private SeekBar seekBar;

    private TabLayout tabs;
    private Toolbar toolbar;
    private ViewPager mViewPager;

    private EditText yourEditText;

    private static MediaPlayer player;
    static Handler handler;
    static Uri audio;
    static boolean canMakeCall = false;
    boolean flag = false;
    boolean flag1 = false;
    boolean flag3 = true;

    MediaPlayer bell;
    MediaPlayer shankh;

    private AdView mAdView;
    private Boolean mAdFree = false;
    private SharedPreferences mSharedPreferences;

    int count = 1;
    int maxCount = 1;
    int finalValue = 1;

    private com.hanuman.radha.krishna.classes.NotifyBuilder notifyBuilder;

    private static final String TEST_DEVICE ="85ECEB00A98B47FDFBFECC2639C994EF";
    private static final String RADHA = "C302756BCFE01E68DED07FA309375005";
    private static final String KRISHNA = "57F6141E3D949BD28C1E6144AE70F72E";

    String mantra = null;
    String filename = null;
    String mantra_name = null;
    private int filenameID;
    private SharedPref sharedPref;

    private final int ID = 1;

    private int array = 1;

    private int mantraID;

   private ArrayList<String> filename_list;
   private ArrayList<String> mantra_list;
   private  ArrayList<String> mantra_name_list;
   private ArrayList<Integer> image_id_list;

    public static final String CHANNEL_ID = "channel1";

   private int POSITION;

    BroadcastReceiver broadcastReceiver;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ganeshamantra_demo);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        View adContainer = findViewById(R.id.adMobView);


        final AdView mAdView = new AdView(this);
        mAdView.setAdSize(AdSize.BANNER);

        mAdFree = mSharedPreferences.getBoolean(getResources().getString(R.string.pref_remove_ads_key), false);


        if (BuildConfig.DEBUG) {
            mAdView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        } else {
            mAdView.setAdUnitId("ca-app-pub-4070209682123577/1447874236");
        }

        ((RelativeLayout)adContainer).addView(mAdView);

        if (!mAdFree) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        } else {
            mAdView.setVisibility(View.GONE);
        }

        filename_list = new ArrayList<>();
        mantra_list = new ArrayList<>();
        mantra_name_list = new ArrayList<>();
        image_id_list = new ArrayList<>();

        parse_arraylist();

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        sharedPref = new SharedPref(this);

        notifyBuilder = new NotifyBuilder(KrishnaBhajan.this);

        notifyBuilder.createNotificationChannel();


        drawableID = -1;
        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();
            if (getIntent().getStringExtra("mantra") != null && getIntent().getStringExtra("mantra") != null && getIntent().getStringExtra("mantra_name") != null && (getIntent().getIntExtra("image_id", 0) != 0)) {


                mantra= bundle.getString("mantra");
                filename = bundle.getString("filename");
                mantra_name = bundle.getString("mantra_name");
                drawableID = bundle.getInt("image_id");
                POSITION = bundle.getInt("track_number");
                sharedPref.PUT_MANTRA(mantra_list.get(POSITION));

            }
        } else {
            mantra = (String) savedInstanceState.getSerializable("mantra");
            filename = (String) savedInstanceState.getSerializable("filename");
            mantra_name = (String) savedInstanceState.getSerializable("mantra_name");
            drawableID = savedInstanceState.getInt("image_id");
            POSITION = savedInstanceState.getInt("track_number");

        }


        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String key =  intent.getStringExtra("action");

                switch (key)
                {

                    case NotifyBuilder.DISMISS_ACTION:
                        pause_2();
                        break;

                    case NotifyBuilder.ACTION_NEXT:
                        forward();
                        break;

                    case NotifyBuilder.ACTION_PLAY:
                        if (player.isPlaying()){

                            pause();
                        }else{
                            play();
                        }
                        break;

                    case NotifyBuilder.ACTION_PREVIUOS:
                        previous();
                        break;

                }

            }
        };

        registerReceiver(broadcastReceiver,new IntentFilter("Music"));

        mantraID = -1; filenameID = -1;

        if (mantra == null || filename == null || mantra_name == null || drawableID == -1) {
            filename = "mahamantra";
            mantra = "mahamantra_bhajan";
            mantra_name = "Hare Krishna Hare Rama";
            drawableID = 1;
        }

        mantraID = getResources().getIdentifier(mantra, "string", getPackageName());
        filenameID = getResources().getIdentifier(filename, "raw", getPackageName());

        mantraText = getResources().getString(mantraID);

        playButton = (ImageButton) findViewById(R.id.btn1);
        bellButton = (ImageButton) findViewById(R.id.btn2);
        shankhButton = (ImageButton) findViewById(R.id.btn3);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        repeatButton = (ImageButton) findViewById(R.id.btn4);
        repeatButton1 = (ImageButton) findViewById(R.id.btn5);
        repeatButton2 = (ImageButton) findViewById(R.id.btn6);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);

        flower = (ImageView) findViewById(R.id.flower);
        flower1 = (ImageView) findViewById(R.id.flower1);
        flower2 = (ImageView) findViewById(R.id.flower2);
        flower3 = (ImageView) findViewById(R.id.flower3);
        flower4 = (ImageView) findViewById(R.id.flower4);
        flower5 = (ImageView) findViewById(R.id.flower5);
        flower6 = (ImageView) findViewById(R.id.flower6);

        mSectionsPagerAdapter = new BaseActivity.SectionsPagerAdapter(getSupportFragmentManager());


        // Set up the ViewPager with the sections adapter.
        tabs = (TabLayout) findViewById(R.id.tabs);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(mantra_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabs.setupWithViewPager(mViewPager);
//        animat();

        final RelativeLayout playerLayout = (RelativeLayout) findViewById(R.id.player);

        try {
            player = MediaPlayer.create(KrishnaBhajan.this, filenameID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        seekBar.setMax(0);
        seekBar.setMax(player.getDuration());
        yourEditText = (EditText) findViewById(R.id.textView16);
        yourEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {


                try {
                    String value = yourEditText.getText().toString();
                    finalValue = new Integer(value).intValue();
                } catch (NumberFormatException e) {
                    finalValue = 1;
                }


                count = 1;
                maxCount = finalValue;

                TextView tr = (TextView) findViewById(R.id.textView1);
                tr.setText(" ");
                flag3 = true;
                yourEditText.setEnabled(true);

                repeatButton1.setEnabled(false);
                repeatButton.setEnabled(false);
                repeatButton2.setEnabled(false);

                try {
                    seekBar.setMax(player.getDuration());
                } catch(Exception e) { e.printStackTrace(); }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        mAdView.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                Resources r = getResources();
                int px = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        50,
                        r.getDisplayMetrics()
                );

                RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams) playerLayout.getLayoutParams();
                relativeParams.setMargins(0, 0, 0, px);  // left, top, right, bottom
                playerLayout.setLayoutParams(relativeParams);
                mAdView.setVisibility(View.VISIBLE);

                px = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        150,
                        r.getDisplayMetrics()
                );

                ViewPager pager = (ViewPager) findViewById(R.id.mViewPager);
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) pager.getLayoutParams();
                lp.setMargins(0, 0, 0, px);  // left, top, right, bottom
                pager.setLayoutParams(lp);
            }

        });


        bell = MediaPlayer.create(this, R.raw.bell);
        bellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (bell.isPlaying()) {
                        bell.stop();
                        bell.release();
                        bell = MediaPlayer.create(KrishnaBhajan.this, R.raw.bell);
                    } bell.start();
                } catch(Exception e) { e.printStackTrace(); }
            }
        });

        shankh = MediaPlayer.create(this, R.raw.shankh);
        shankhButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (shankh.isPlaying()) {
                        shankh.stop();
                        shankh.release();
                        shankh = MediaPlayer.create(KrishnaBhajan.this, R.raw.shankh);
                    } shankh.start();
                } catch(Exception e) { e.printStackTrace(); }
            }
        });


        if (canMakeCall = true) {
            player.setOnPreparedListener(this);

            handler = new Handler();

            seekBar.setMax(player.getDuration());

            playButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    try {
                        if (!player.isPlaying()) {

                            play();
                            player.start();
                            playButton.setImageResource(R.drawable.pause);
                            updateSeekBar();

                        } else if (player!=null && player.isPlaying()) {

                            pause();
                            notifyBuilder.cancel_notification();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });


            repeatButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    count = 1;
                    maxCount = 3;
                    TextView tr = (TextView) findViewById(R.id.textView1);
                    tr.setText(" ");
                    flag3 = true;
                    yourEditText.setEnabled(false);

                    repeatButton1.setEnabled(false);
                    repeatButton.setEnabled(false);
                    repeatButton2.setEnabled(false);


                    // Fix for IllegalStateException
                    // Original without try catch
                    try {
                        seekBar.setMax(player.getDuration());
                    } catch(Exception e) { e.printStackTrace(); }


                }

            });
            repeatButton1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    count = 1;
                    maxCount = 7;
                    TextView tr = (TextView) findViewById(R.id.textView1);
                    tr.setText(" ");
                    flag3 = true;
                    yourEditText.setEnabled(false);
                    repeatButton.setEnabled(false);

                    repeatButton1.setEnabled(false);
                    repeatButton2.setEnabled(false);


                    // Fix for IllegalStateException
                    // Original without try catch
                    try {
                        seekBar.setMax(player.getDuration());
                    } catch(Exception e) { e.printStackTrace(); }


                }

            });
            repeatButton2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    count = 1;
                    maxCount = 11;
                    TextView tr = (TextView) findViewById(R.id.textView1);
                    tr.setText(" ");
                    flag3 = true;
                    yourEditText.setEnabled(false);
                    repeatButton.setEnabled(false);
                    repeatButton1.setEnabled(false);

                    repeatButton2.setEnabled(false);


                    // Fix for IllegalStateException
                    // Original without try catch
                    try {
                        seekBar.setMax(player.getDuration());
                    } catch(Exception e) { e.printStackTrace(); }


                }

            });

//            stopButton.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View view) {
//                    if (player.isPlaying()) {
//                        player.stop();
//
//
//                        seekBar.setProgress(0);
//
//                        seekBar.invalidate();
//                        yourEditText.setText("");
//                        yourEditText.setEnabled(true);
//                        count = 1;
//                        TextView tr = (TextView) findViewById(R.id.textView1);
//                        tr.setText(" ");
//                        repeatButton.setEnabled(true);
//                        repeatButton1.setEnabled(true);
//                        repeatButton2.setEnabled(true);
//
//
//                        try {
//                            player.prepare();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        flag1 = false;
//                        seekBar.setProgress(0);
//
//                    }
//                }
//            });

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    if (b) {

                        seekChanged(seekBar, i, b);

                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {


                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    if (count < maxCount) {
                        TextView tr = (TextView) findViewById(R.id.textView1);
                        tr.setText(" " + count);
                        count++;
                        seekBar.setProgress(0);
                        player.seekTo(0);
                        player.start();
                    } else {
                        TextView tr = (TextView) findViewById(R.id.textView1);
                        tr.setText(" " + count);
                        flag3 = false;
                        //player.release();
                        seekBar.setProgress(0);
                        //player.prepareAsync();
                        seekBar.invalidate();

                        yourEditText.getText().clear();

                        yourEditText.setEnabled(true);

                        repeatButton.setEnabled(true);

                        repeatButton2.setEnabled(true);
                        repeatButton1.setEnabled(true);

                        playButton.setImageResource(R.drawable.play);

                    }
                    //player.stop();

                }
            });
        }

    }



    @Override
    protected void onDestroy() {


        if (broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver);
            Log.d("not null","mot null");
            notifyBuilder.cancel_notification();
            notifyBuilder.cancel_notification_and_channel();
        }

        super.onDestroy();

    }


    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        playButton.setEnabled(true);
        canMakeCall = true;
    }

    public void seekChanged(SeekBar seekBar, int progress, boolean fromUser) {
        try {
            player.seekTo(progress);
        } catch(Exception e) { e.printStackTrace(); }

    }

    public void updateSeekBar() {

        if (!flag) {
            if (flag3) {
                seekBar.setProgress(player.getCurrentPosition());
                if (!(player.isPlaying()) && !flag1) {
                    seekBar.setProgress(0);

                }
                if (player.isPlaying()) {
                    Runnable notification = new Runnable() {
                        public void run() {

                            updateSeekBar();
                        }
                    };
                    handler.postDelayed(notification, 1500);
                }
            }
        }
    }


    @Override
    public void onBackPressed() {
        notifyBuilder.cancel_notification();
        quitDialog();
    }

    public void quit() {
        quitDialog();
    }

    private void quitDialog() {

        player.stop();
        player.reset();
        player.release();
        flag = true;
        finish();

    }



    @Override
    public void play() {
        player.start();
        playButton.setImageResource(R.drawable.pause);
        updateSeekBar();
        notifyBuilder.create_notification(mantra_name_list.get(POSITION),image_id_list.get(POSITION),PAUSE,filename_list.size() - 1,POSITION);

    }

    private void pause_2(){
        flag1 = true;
        player.pause();
        playButton.setImageResource(R.drawable.play);
    }

    @Override
    public void pause() {
        flag1 = true;
        player.pause();
        playButton.setImageResource(R.drawable.play);
        notifyBuilder.create_notification(mantra_name_list.get(POSITION),image_id_list.get(POSITION),PLAY,filename_list.size() - 1,POSITION);

    }


    @Override
    public void forward() {

        try {


            if (POSITION != 23){
                POSITION += 1;
                player.stop();
                player.reset();
                sharedPref.PUT_MANTRA(mantra_list.get(POSITION));
                sharedPref.PUT_POSITION(POSITION);
                filenameID = getResources().getIdentifier(filename_list.get(POSITION), "raw", getPackageName());
                player = MediaPlayer.create(KrishnaBhajan.this, filenameID);
                player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        notifyBuilder.create_notification(mantra_name_list.get(POSITION),image_id_list.get(POSITION),PAUSE,filename_list.size() - 1,POSITION);
                        player.start();
                        playButton.setImageResource(R.drawable.pause);
                        updateSeekBar();
                        getSupportActionBar().setTitle(mantra_name_list.get(POSITION));
                    }
                });

            }

        }catch (Exception e){
            Log.d("Position",e.getMessage());
        }



    }

    @Override
    public void previous() {

        try {


            if (POSITION != 0) {
                POSITION -= 1;
                player.stop();
                player.reset();
                sharedPref.PUT_MANTRA(mantra_list.get(POSITION));
                sharedPref.PUT_POSITION(POSITION);
                filenameID = getResources().getIdentifier(filename_list.get(POSITION), "raw", getPackageName());
                player = MediaPlayer.create(KrishnaBhajan.this, filenameID);
                player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        notifyBuilder.create_notification(mantra_name_list.get(POSITION), image_id_list.get(POSITION), PAUSE, filename_list.size() - 1, POSITION);
                        player.start();
                        playButton.setImageResource(R.drawable.pause);
                        updateSeekBar();
                        getSupportActionBar().setTitle(mantra_name_list.get(POSITION));

                    }
                });

            }

        }catch (Exception e){
            Log.d("Position",e.getMessage());
        }

    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return MainFragment.newInstance(R.drawable.krishna_1);
            } else {
                return SecondFragment.newInstance(getResources().getString(R.string.maiya_mori_bhajan));
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Active";
                case 1:
                    return "All";
            }
            return null;
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mMenuInflater = getMenuInflater();
        mMenuInflater.inflate(R.menu.hanumanchalisa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_language) {
            showChangeLangDialog();
            return true;
        }
        if (item.getItemId() == R.id.action_share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Hey! Check out this awesome Krishna Bhajans app - https://play.google.com/store/apps/details?id=com.hanuman.radha.krishna";
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
        if (item.getItemId() == R.id.action_about) {
            String str;
            try {
                str = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                PackageManager.NameNotFoundException nameNotFoundException = e;
                str = "1.1";
                nameNotFoundException.printStackTrace();
            }
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.app_name) + " " + str)
                    .setCancelable(false)
                    .setPositiveButton("Try More Apps!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Uri uri = Uri.parse("https://play.google.com/store/apps/developer?id=Hanuman");
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("OK", null)
                    .show();

        }
        if (item.getItemId() == R.id.action_about_hanuman) {
            Uri uri = Uri.parse("https://radhakrishna.net");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.action_contact) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:" + "contact@bhagavadgita.io"));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Radha Krishna Bhajans App");
            try {
                startActivity(Intent.createChooser(emailIntent, "Send email using..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "No email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);

    }

    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.language, null);
        dialogBuilder.setView(dialogView);

        final Spinner spinner1 = (Spinner) dialogView.findViewById(R.id.spinner1);

        dialogBuilder.setTitle(getResources().getString(R.string.lang_dialog_title));
        dialogBuilder.setMessage(getResources().getString(R.string.lang_dialog_message));
        dialogBuilder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                int langpos = spinner1.getSelectedItemPosition();
                switch(langpos) {
                    case 0: //English
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "en").commit();
                        updateViews("en");
                        return;
                    case 1: //Hindi
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "hi").commit();
                        updateViews("hi");
                        return;
                    default: //By default set to english
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "en").commit();
                        updateViews("en");
                        return;
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void updateViews(String languageCode) {
        player.stop();
        Context context = LocaleHelper.setLocale(this, languageCode);
        Resources resources = context.getResources();
        ActivityRecreationHelper.recreate(this, true);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals((getString(R.string.pref_remove_ads_key)))){
            mAdFree = sharedPreferences.getBoolean(key, false);
            KrishnaBhajan.this.recreate();
        }
    }

    private void parse_arraylist(){



        // entering filename to arraylist
        filename_list.add("mahamantra");
        filename_list.add("maiyamorimain");
        filename_list.add("govindmerohai");
        filename_list.add("om");
        filename_list.add("aarti_kunj_bihari_ki");
        filename_list.add("badanatkhathaikrishnakanhaiya");
        filename_list.add("choti_choti_gaiya_krishna");
        filename_list.add("yashomatimaiyasebolenandlala");
        filename_list.add("mera_aap_ki_kripa");
        filename_list.add("achyutamkeshavam");
        filename_list.add("aredwarpalokanhaiya");
        filename_list.add("ek_baar_to_radha_bankar");
        filename_list.add("gopal_muraliya_wale");
        filename_list.add("govind_bolo_hari_gopal_bolo");
        filename_list.add("kabhi_ram_banke");
        filename_list.add("kisi_ne_mera_shyam_dekha");
        filename_list.add("mithe_ras_se_bharyo");
        filename_list.add("mohana_muralidhara");
        filename_list.add("om_jai_jagadish_hare");
        filename_list.add("radhe_radhe_japa_karo");
        filename_list.add("shri_krishna_govind_hare_murari");
        filename_list.add("shyam_teri_bansi_pukare_radha_naam");
        filename_list.add("shyama_aan_baso_vrindavan");

        mantra_name_list.add("Hare Krishna Hare Rama");
        mantra_name_list.add("Maiya Mori Main Nahi Maakhan Khayo");
        mantra_name_list.add("Govind Mero Hai Gopal Mero Hai");
        mantra_name_list.add("Om Mantra");
        mantra_name_list.add("Aarti Kunj Bihari Ki");
        mantra_name_list.add("Bada Natkhat Hai Krishna");
        mantra_name_list.add("Choti Choti Gaiya Chotay");
        mantra_name_list.add("Yashomati Maiya Se Bole Nandlala");
        mantra_name_list.add("Mera Aapki Kripa Se Sab Kaam");
        mantra_name_list.add("Achyutam Keshavam");
        mantra_name_list.add("Are Dwarpalo Kanhaiya Se Kehdo");
        mantra_name_list.add("Ek Baar To Radha bankar Dekho Mere Sawariya");
        mantra_name_list.add("Gopal Muraliya Wale");
        mantra_name_list.add("Govind Bolo hari Gopal Bolo");
        mantra_name_list.add("Kabhi Ram Banke Kabhi Shyam Banke");
        mantra_name_list.add("Kisi Ne Mera Shyam Dekha");
        mantra_name_list.add("Mithe Ras Se Bharyo Radha Rani");
        mantra_name_list.add("Mohana Muralidhara");
        mantra_name_list.add("Om Jai Jagdish Hare");
        mantra_name_list.add("Radhe Radhe Japo Chale Aayenge Bihari");
        mantra_name_list.add("Shri Krishna Govind Hare Murari");
        mantra_name_list.add("Shyam Teri Bansi");
        mantra_name_list.add("Shyama Aan Baso Vrindavan");

        mantra_list.add("mahamantra_bhajan");
        mantra_list.add("maiya_mori_bhajan");
        mantra_list.add("govind_mero_bhajan");
        mantra_list.add("om_mantra_bhajan");
        mantra_list.add("aarti_kunj_bihari_bhajan");
        mantra_list.add("bada_natkhat_bhajan");
        mantra_list.add("choti_choti_bhajan");
        mantra_list.add("yashomati_bhajan");
        mantra_list.add("mera_aapki_kripa_bhajan");
        mantra_list.add("achyutam_keshavam_bhajan");
        mantra_list.add("aredwarpalokanhaiya_bhajan");
        mantra_list.add("ek_baar_to_radha_bankar_bhajan");
        mantra_list.add("gopal_muraliya_wale_bhajan");
        mantra_list.add("govind_bolo_hari_gopal_bolo_bhajan");
        mantra_list.add("kabhi_ram_banke_bhajan");
        mantra_list.add("kisi_ne_mera_shyam_dekha_bhajan");
        mantra_list.add("mithe_ras_se_bharyo_bhajan");
        mantra_list.add("mohana_muralidhara_bhajan");
        mantra_list.add("om_jai_jagadish_hare_bhajan");
        mantra_list.add("radhe_radhe_japa_karo_bhajan");
        mantra_list.add("shri_krishna_govind_hare_murari_bhajan");
        mantra_list.add("shyam_teri_bansi_pukare_radha_naam_bhajan");
        mantra_list.add("shyama_aan_baso_vrindavan_bhajan");

        image_id_list.add(1);
        image_id_list.add(2);
        image_id_list.add(3);
        image_id_list.add(4);
        image_id_list.add(5);
        image_id_list.add(6);
        image_id_list.add(7);
        image_id_list.add(8);
        image_id_list.add(9);
        image_id_list.add(10);
        image_id_list.add(11);
        image_id_list.add(12);
        image_id_list.add(13);
        image_id_list.add(14);
        image_id_list.add(15);
        image_id_list.add(16);
        image_id_list.add(17);
        image_id_list.add(18);
        image_id_list.add(19);
        image_id_list.add(20);
        image_id_list.add(21);
        image_id_list.add(22);
        image_id_list.add(23);


    }


}


