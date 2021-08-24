package com.hanuman.radha.krishna;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
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
import com.kobakei.ratethisapp.RateThisApp;
import com.hanuman.radha.krishna.fragments.MainFragment;
import com.hanuman.radha.krishna.fragments.SecondFragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class KrishnaBhajan extends BaseActivity implements MediaPlayer.OnPreparedListener, SharedPreferences.OnSharedPreferenceChangeListener {
    private ImageButton playButton;
    private ImageButton bellButton;
    private ImageButton shankhButton;
    private ImageButton repeatButton;
    private ImageButton repeatButton1;
    private ImageButton repeatButton2;
    private FirebaseAnalytics mFirebaseAnalytics;

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

    private static final String TEST_DEVICE ="85ECEB00A98B47FDFBFECC2639C994EF";
    private static final String RADHA = "C302756BCFE01E68DED07FA309375005";
    private static final String KRISHNA = "57F6141E3D949BD28C1E6144AE70F72E";


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

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);


        String mantra = null;
        String filename = null;
        String mantra_name = null;
        drawableID = -1;
        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();
            if (getIntent().getStringExtra("mantra") != null && getIntent().getStringExtra("mantra") != null && getIntent().getStringExtra("mantra_name") != null && (getIntent().getIntExtra("image_id", 0) != 0)) {
                mantra= bundle.getString("mantra");
                filename = bundle.getString("filename");
                mantra_name = bundle.getString("mantra_name");
                drawableID = bundle.getInt("image_id");
            }
        } else {
            mantra = (String) savedInstanceState.getSerializable("mantra");
            filename = (String) savedInstanceState.getSerializable("filename");
            mantra_name = (String) savedInstanceState.getSerializable("mantra_name");
            drawableID = savedInstanceState.getInt("image_id");
        }

        int mantraID = -1; int filenameID = -1;

        if (mantra == null || filename == null || mantra_name == null || drawableID == -1) {
            filename = "mahamantra";
            mantra = "mahamantra_bhajan";
            mantra_name = "Hare Krishna Hare Rama";
            drawableID = 1;
        }

        mantraID = getResources().getIdentifier(mantra, "string", getPackageName());
        filenameID = getResources().getIdentifier(filename, "raw", getPackageName());

        mantraText = getResources().getString(mantraID);

        RateThisApp.onCreate(this);
        RateThisApp.showRateDialogIfNeeded(this);

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
                            player.start();
                            playButton.setImageResource(R.drawable.pause);
                            updateSeekBar();
                        } else if (player!=null && player.isPlaying()) {
                            flag1 = true;
                            player.pause();
                            playButton.setImageResource(R.drawable.play);
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
        quitDialog();
    }

    public void quit() {
        quitDialog();
    }

    private void quitDialog() {

        flag = true;
        player.release();
        finish();

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

}


