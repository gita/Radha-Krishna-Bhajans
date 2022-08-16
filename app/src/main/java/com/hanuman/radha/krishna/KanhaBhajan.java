package com.hanuman.radha.krishna;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.hanuman.radha.krishna.classes.MediaStatus;
import com.hanuman.radha.krishna.classes.NotifyBuilder;
import com.hanuman.radha.krishna.classes.SharedPref;
import com.hanuman.radha.krishna.fragments.MainFragment;
import com.hanuman.radha.krishna.fragments.SecondFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KanhaBhajan extends BaseActivity implements MediaPlayer.OnPreparedListener, SharedPreferences.OnSharedPreferenceChangeListener, MediaStatus {

    private ImageButton playButton;
    private ImageButton bellButton;
    private ImageButton shankhButton;
    private ImageButton repeatButton;
    private ImageButton repeatButton1;
    private ImageButton repeatButton2;
    private FirebaseAnalytics mFirebaseAnalytics;

    private ExecutorService executor;

    private SeekBar seekBar;

    private TabLayout tabs;
    private Toolbar toolbar;
    private ViewPager mViewPager;

    private final int ID = 1;

    private EditText yourEditText;

    private final int PAUSE = 1;
    private final int PLAY = 0;

    private static MediaPlayer player;
    static Handler handler;
    static Uri audio;
    static boolean canMakeCall = false;
    boolean flag = false;
    boolean flag1 = false;
    boolean flag3 = true;

    BroadcastReceiver broadcastReceiver;

    MediaPlayer bell;
    MediaPlayer shankh;

    private AdView mAdView;
    private Boolean mAdFree = false;
    private SharedPreferences mSharedPreferences;

    int count = 1;
    int maxCount = 1;
    int finalValue = 1;

    private ProgressDialog progressDialog;
    private boolean initialStage = true;

    private static final String TEST_DEVICE = "85ECEB00A98B47FDFBFECC2639C994EF";
    private static final String RADHA = "C302756BCFE01E68DED07FA309375005";
    private static final String KRISHNA = "57F6141E3D949BD28C1E6144AE70F72E";


    private int POSITION;

    String mantra = null;
    String filename = null;
    String mantra_name = null;
    private int mantraID;
    private int filenameID;


    private SharedPref sharedPref;

    ArrayList<String> filename_list;
    ArrayList<String> mantra_list;
    ArrayList<String> mantra_name_list;
    ArrayList<Integer> image_id_list;

    private com.hanuman.radha.krishna.classes.NotifyBuilder notifyBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ganeshamantra_demo);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        sharedPref = new SharedPref(KanhaBhajan.this);


        View adContainer = findViewById(R.id.adMobView);


        final AdView mAdView = new AdView(this);
        mAdView.setAdSize(AdSize.BANNER);

        mAdFree = mSharedPreferences.getBoolean(getResources().getString(R.string.pref_remove_ads_key), false);

        if (BuildConfig.DEBUG) {
            mAdView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        } else {
            mAdView.setAdUnitId("ca-app-pub-4070209682123577/1447874236");
        }

        ((RelativeLayout) adContainer).addView(mAdView);

        if (!mAdFree) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        } else {
            mAdView.setVisibility(View.GONE);
        }

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        notifyBuilder = new NotifyBuilder(KanhaBhajan.this);

        notifyBuilder.createNotificationChannel();

        drawableID = -1;
        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();
            if (getIntent().getStringExtra("mantra") != null && getIntent().getStringExtra("mantra") != null && getIntent().getStringExtra("mantra_name") != null && (getIntent().getIntExtra("image_id", 0) != 0)) {
                mantra = bundle.getString("mantra");
                filename = bundle.getString("filename");
                mantra_name = bundle.getString("mantra_name");
                drawableID = bundle.getInt("image_id");
                POSITION = bundle.getInt("track_number");

            }
        } else {
            mantra = (String) savedInstanceState.getSerializable("mantra");
            filename = (String) savedInstanceState.getSerializable("filename");
            mantra_name = (String) savedInstanceState.getSerializable("mantra_name");
            drawableID = savedInstanceState.getInt("image_id");
            POSITION = savedInstanceState.getInt("track_number");
        }

        mantraID = -1;
        filenameID = -1;

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


        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String key = intent.getStringExtra("action");


                switch (key) {

                    case NotifyBuilder.DISMISS_ACTION:
                        pause_2();
                        break;

                    case NotifyBuilder.ACTION_NEXT:
                        forward();
                        break;

                    case NotifyBuilder.ACTION_PLAY:
                        if (player.isPlaying()) {
                            pause();
                        } else {
                            play();
                        }
                        break;

                    case NotifyBuilder.ACTION_PREVIUOS:
                        previous();
                        break;

                }

            }
        };

        registerReceiver(broadcastReceiver, new IntentFilter("Music"));

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

//        player = new MediaPlayer();
//        player = MediaPlayer.create(KanhaBhajan.this, Uri.parse(filename));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Buffering...");

        parse_arraylist();


        if (isOnline()) {
            progressDialog.show();


            player = new MediaPlayer();

            try {
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                player.setDataSource(filename);
//                player.prepareAsync();
//                player.prepare();

//                Toast.makeText(getApplicationContext(), "Preparing Bhajan...", Toast.LENGTH_SHORT).show();
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

                    seekBar.setMax(player.getDuration());
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
                            bell = MediaPlayer.create(KanhaBhajan.this, R.raw.bell);
                        }
                        bell.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
                            shankh = MediaPlayer.create(KanhaBhajan.this, R.raw.shankh);
                        }
                        shankh.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


            if (canMakeCall = true) {
                player.setOnPreparedListener(this);

                try {
                    player.setDataSource(filename);
                    player.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                handler = new Handler();

                seekBar.setMax(player.getDuration());

                playButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        try {
                            if (!player.isPlaying()) {
                                play();
                            }
                            else if (player.isPlaying() && player != null) {
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


                        try {
                            seekBar.setMax(player.getDuration());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


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


                        try {
                            seekBar.setMax(player.getDuration());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


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


                        try {
                            seekBar.setMax(player.getDuration());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


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
                            yourEditText.setText("");
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
        } else {
            showDialogWifi();
        }
    }


    @Override
    protected void onDestroy() {
        if (broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver);
            notifyBuilder.cancel_notification_and_channel();

        }
        super.onDestroy();

    }


    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        progressDialog.dismiss();
        Toast.makeText(KanhaBhajan.this, "Bhajan Ready To Play", Toast.LENGTH_SHORT).show();
        playButton.setEnabled(true);
        canMakeCall = true;
    }

    public void seekChanged(SeekBar seekBar, int progress, boolean fromUser) {

        try {
            player.seekTo(progress);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        notifyBuilder.cancel_notification();
        flag = true;
        player.stop();
        player.reset();
        player.release();
        finish();

    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }


    public void showDialogWifi() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this).setCancelable(false);
        alertDialogBuilder.setMessage("Please Check Your Network Connection").setTitle("Connection Failed");

        alertDialogBuilder.setPositiveButton("Listen Offline Bhajans", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent();
                intent.setClass(KanhaBhajan.this, MantraList.class);
                startActivity(intent);
                finish();
            }
        });
        alertDialogBuilder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setClass(KanhaBhajan.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        alertDialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                playButton.setImageResource(R.drawable.play);
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void play() {

            player.start();
            playButton.setImageResource(R.drawable.pause);
            updateSeekBar();
            notifyBuilder.create_notification(mantra_name_list.get(POSITION), image_id_list.get(POSITION), PAUSE, filename_list.size() - 1, POSITION);

    }



    private void pause_2() {
        flag1 = true;
        player.pause();
        playButton.setImageResource(R.drawable.play);

    }


    @Override
    public void pause() {
            flag1 = true;
            player.pause();
            playButton.setImageResource(R.drawable.play);
            notifyBuilder.create_notification(mantra_name_list.get(POSITION), image_id_list.get(POSITION), PLAY, filename_list.size() - 1, POSITION);

    }



    @Override
    public void forward() {

        try {

            if (POSITION != 41) {

                POSITION += 1;

                sharedPref.PUT_MANTRA(mantra_list.get(POSITION));

                executor = Executors.newSingleThreadExecutor();

                executor.execute(new Runnable() {
                    @Override
                    public void run() {

                        Log.d("background","in background");

                        notifyBuilder.create_wait_notification();
                        player.stop();
                        player.reset();
                        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        try {
                            player.setDataSource(filename_list.get(POSITION));
                            player.prepareAsync();
                            Log.d("background",String.valueOf(POSITION));
                        } catch (IOException e) {
                            Log.d("background",e.getMessage());
                        }
                        sharedPref.PUT_POSITION(POSITION);

                    }
                });

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Log.d("background","in ui");

                        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                player.start();
                                playButton.setImageResource(R.drawable.pause);
                                updateSeekBar();
                                getSupportActionBar().setTitle(mantra_name_list.get(POSITION));
                                notifyBuilder.create_notification(mantra_name_list.get(POSITION), image_id_list.get(POSITION), PAUSE, filename_list.size() - 1, POSITION);

                            }
                        });
                    }
                });

                executor.shutdown();
            }

        } catch (Exception e) {}



    }

    @Override
    public void previous() {

                    try {

                        if (POSITION != 0) {
                            POSITION -= 1;

                            sharedPref.PUT_MANTRA(mantra_list.get(POSITION));

                            executor = Executors.newSingleThreadExecutor();

                            executor.execute(new Runnable() {
                                @Override
                                public void run() {
                                    notifyBuilder.create_wait_notification();
                                    player.stop();
                                    player.reset();
                                    player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                    try {
                                        player.setDataSource(filename_list.get(POSITION));
                                        player.prepareAsync();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    sharedPref.PUT_POSITION(POSITION);

                                }
                            });

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                        @Override
                                        public void onPrepared(MediaPlayer mediaPlayer) {

                                            player.start();
                                            playButton.setImageResource(R.drawable.pause);
                                            updateSeekBar();
                                            getSupportActionBar().setTitle(mantra_name_list.get(POSITION));
                                            notifyBuilder.create_notification(mantra_name_list.get(POSITION), image_id_list.get(POSITION), PAUSE, filename_list.size() - 1, POSITION);

                                        }
                                    });
                                }
                            });

                            executor.shutdown();
                        }

                    } catch (Exception e) {

                    }

    }



    private void parse_arraylist(){

        filename_list = new ArrayList<>();
        mantra_list = new ArrayList<>();
        mantra_name_list = new ArrayList<>();
        image_id_list = new ArrayList<Integer>();

        // entering filename to arraylist
        filename_list.add("https://media-audio.mio.to/by_artist/G/Gaurav%20Krishna%20Goswami/Radha%20Naam%20Sang%20Brij%2084%20Kos%20Yatra%20%28Radhe%20Radhe%20Barsane%20Wali%20Radhe%29%20%282009%29/1_1%20-%20Radha%20Naam%20Sang%20Brij%2084%20Kos%20Yatra%20%28Radhe%20Radhe%20Barsane%20Wali%20Radhe%29-vbr-V5.mp3");
        filename_list.add("https://media-audio.mio.to/various_artists/M/Maiya%20Mori%20By%20Anup%20Jalota%20%282016%29/1_1%20-%20Maiya%20Mori-vbr-V5.mp3");
        filename_list.add("https://media-audio.mio.to/by_artist/A/Anup%20Jalota/Aisi%20Laagi%20Lagan/1_1%20-%20Aisi%20Laagi%20Lagan-vbr-V5.mp3");
        filename_list.add("https://media-audio.mio.to/various_artists/B/Bhajans%20From%20Films-/1_1%20-%20O%20Paalanhaare-vbr-V5.mp3");
        filename_list.add( "https://media-audio.mio.to/by_artist/A/Anuradha%20Paudwal/Aarti/1_6%20-%20Aarti%20Kunj%20Bihari%20Ki-vbr-V5.mp3");
        filename_list.add("https://media-audio.mio.to/by_artist/L/Lata%20Mangeshkar/Bhajans%20by%20Lata%20Mangeshkar%20%282015%29/1_14%20-%20Bada%20Natkhat%20Hai%20Yeh%20-%20From%20Amar%20Prem%20-vbr-V5.mp3");
        filename_list.add("https://media-audio.mio.to/various_artists/H/Holy%20Chants%20On%20Lord%20Vishnu%20And%20Mahalakshmi/1_6%20-%20Achyutashtakam-vbr-V5.mp3");
        filename_list.add("https://media-audio.mio.to/by_artist/L/Lata%20Mangeshkar/Bhajans%20by%20Lata%20Mangeshkar%20%282015%29/1_12%20-%20Yashomati%20Maiya%20Se%20Bole%20Nandlala%20-%20From%20Satyam%20Shivam%20Sundaram%20-vbr-V5.mp3");
        filename_list.add("https://media-audio.mio.to/various_artists/H/Holy%20Chants%20On%20Lord%20Vishnu%20And%20Mahalakshmi/1_4%20-%20Shree%20Hari%20Stotram-vbr-V5.mp3");
        filename_list.add("https://media-audio.mio.to/by_artist/V/Vikram%20Hazra/Krishna%20-%20The%20Art%20Of%20Living%20%282015%29/1_1%20-%20Achutam%20Keshavam-vbr-V5.mp3");
        filename_list.add("https://media-audio.mio.to/various_artists/H/Holy%20Chants%20On%20Lord%20Vishnu%20And%20Mahalakshmi/1_8%20-%20Vishnu%20Shatanama%20Stotram-vbr-V5.mp3");
        filename_list.add("https://media-audio.mio.to/by_artist/J/Jagjit%20Singh/Jai%20Radha%20Madhav%20Shree%20Krishna%20Dhun%20%282000%29/1_2%20-%20Jai%20Radha%20Madhav%20%28From%20Hey%20Gobind%20Hey%20Gopal%20-%20Jagjit%20Singh%20And%20Chitra%20Singh%29-vbr-V5.mp3");
        filename_list.add("https://media-audio.mio.to/by_artist/H/Hariharan/Shree%20Ram%20-%20Shyam%20Dhun%20%282014%29/1_5%20-%20Radhe%20Radhe%20Govind%20Gopal%20Radhe-vbr-V5.mp3");
        filename_list.add("https://media-audio.mio.to/by_artist/J/Jagjit%20Singh/Radhe%20Krishan%20Radhe%20Shyam/1_2%20-%20Radhe%20Krishan-vbr-V5.mp3");
        filename_list.add("https://media-audio.mio.to/various_artists/S/Shri%20Krishna%20Govind%20Hare%20Murari%20%282013%29/1_1%20-%20Shri%20Krishna%20Govind-vbr-V5.mp3");
        filename_list.add("https://media-audio.mio.to/various_artists/G/Geet%20Gata%20Chal%20%281975%29/1_3%20-%20Shyam%20Teri%20Bansi-vbr-V5.mp3");
        filename_list.add("https://media-audio.mio.to/by_artist/G/Gaurav%20Krishan%20Goswami/Kishori%20Kuch%20Aisa%20Intejam%20Ho%20Jaye%20%282009%29/1_1%20-%20Kishori%20Kuch%20Aisa%20Intejam%20Ho%20Jaye-vbr-V5.mp3");
        filename_list.add("https://media-audio.mio.to/by_artist/S/Shradheya%20Gaurav%20Krishan%20Goswami%20Ji/Radha%20Sahastra%20Naam%20Yatra%20%282015%29/1_1%20-%20Radha%20Sahastra%20Naam%20Yatra-vbr-V5.mp3");
        filename_list.add("https://media-audio.mio.to/by_artist/S/Saket%20Kakkar/Giridhari%20-%20The%20Art%20Of%20Living%20%282015%29/1_3%20-%20Govinda%20Krishna%20Hari%20Om-vbr-V5.mp3");
        filename_list.add("https://media-audio.mio.to/various_artists/T/The%20Most%20Relaxing%20Devotional%20Album%20Ever%20%282012%29/1_13%20-%20Govind%20Bolo%20Hari%20Gopal%20Bolo-vbr-V5.mp3");
        filename_list.add("https://media-audio.mio.to/by_artist/S/Shradheya%20Gaurav%20Krishan%20Goswami%20Ji/Madhurashtakam%20Aivam%20Govind%20Damodar%20Stotra%20%282015%29/1_13%20-%20Hridyam%20Madhuram-vbr-V5.mp3");
        filename_list.add("https://media-audio.mio.to/by_artist/H/Hari%20Om%20Sharan/Kabhi%20Ram%20Banke%20Kabhi%20Shyam%20Banke%20%282000%29/1_1%20-%20Shyam%20Chudi%20Bechne%20Aaya-vbr-V5.mp3");
        filename_list.add("https://media-audio.mio.to/various_artists/B/Bhajans/1_4%20-%20Nand%20Ke%20Anand%20Bhayo-vbr-V5.mp3");
        filename_list.add("https://media-audio.mio.to/by_artist/J/Jagjit%20Singh/Saanwara%20-%20Krishna%20Bhajan%20N%20Kirtan%20-%20Jagjit%20Singh%20%281961%29/1_2%20-%20Hey%20Krishna%20Gopal%20Hari-vbr-V5.mp3");
        filename_list.add("https://media-audio.mio.to/by_artist/S/Sachin%20Limaye/Krishna%20Kanha%20-%20The%20Art%20Of%20Living%20%282015%29/1_1%20-%20Krishna%20Kanha-vbr-V5.mp3");
        filename_list.add("https://media-audio.mio.to/by_artist/S/Sachin%20Limaye/Krishna%20Kanha%20-%20The%20Art%20Of%20Living%20%282015%29/1_3%20-%20Gopala%20Gopala-vbr-V5.mp3");
        filename_list.add("https://media-audio.mio.to/by_artist/V/Vidhi%20Sharma/Ram%20Stuti%20%28Shri%20Ram%20Chandra%20Kripalu%20Bhajman%29%20%282013%29/1_1%20-%20Ram%20Stuti%20-%20Shri%20Ram%20Chandra%20Kripalu%20Bhajman-vbr-V5.mp3");
        filename_list.add("https://media-audio.mio.to/by_artist/B/Bhavesh%20Bhatt/Moolamantra%20Vol.%202%20%282004%29/1_1%20-%20Moolamantra%20Vol%202-vbr-V5.mp3");
        filename_list.add("https://media-audio.mio.to/by_artist/B/Bhanu%2C%20G%20Gayathri%20Devi%2C%20Gowri%2C%20Jaya%2C%20Krupa%2C%20Kruti%2C%20Ramya%2C%20Roopa%2C%20Saindhavi%2C%20Uma%20Mohan%2C%20Usha/Sacred%20Chants%20Vol%202%20%282002%29/1_8%20-%20Krishnashtakam-vbr-V5.mp3");
        filename_list.add("https://media-audio.mio.to/by_artist/M/Mridul%20Krishan%20Shastri/Best%20Of%20Krishna%20Bhajans%20%282014%29/1_7%20-%20Jape%20Ja%20Radhe%20Radhe-vbr-V5.mp3");
        filename_list.add("https://media-audio.mio.to/by_artist/M/Mridul%20Krishan%20Shastri/Best%20Of%20Krishna%20Bhajans%20%282014%29/1_6%20-%20Goverdhan%20Ko%20Jao-vbr-V5.mp3");
        filename_list.add("https://media-audio.mio.to/by_artist/M/Mridul%20Krishan%20Shastri/Best%20Of%20Krishna%20Bhajans%20%282014%29/1_10%20-%20Krishan%20Naam%20Ke%20Heere%20Moti-vbr-V5.mp3");
        filename_list.add("https://media-audio.mio.to/by_artist/M/Mridul%20Krishan%20Shastri/Best%20Of%20Krishna%20Bhajans%20%282014%29/1_9%20-%20Kishori%20Meri%20Kusum%20Kali-vbr-V5.mp3");
        filename_list.add("https://media-audio.mio.to/by_artist/M/Mridul%20Krishan%20Shastri/Best%20Of%20Krishna%20Bhajans%20%282014%29/1_13%20-%20Mere%20Shyam%20Murli%20Wale-vbr-V5.mp3");
        filename_list.add("https://media-audio.mio.to/by_artist/M/Mridul%20Krishna%20Shastri/Mridul%20Aarti%20Vandana%20%282013%29/1_1%20-%20Bankey%20Bihari%20Ji%20Ki%20Aarti-vbr-V5.mp3");
        filename_list.add("https://media-audio.mio.to/by_artist/V/Vinod%20Agarwal/Radhika%20Gori%20Se/1_2%20-%20Radhika%20Gori%20Se-vbr-V5.mp3");
        filename_list.add("https://media-audio.mio.to/by_artist/V/Vinod%20Agarwal/Duniya%20Kya%20Jaane/1_1%20-%20Mera%20Aapki%20Kripa%20Se-vbr-V5.mp3");
        filename_list.add("https://media-audio.mio.to/various_artists/J/Janmashtami%20Special/1_20%20-%20Radhe%20Radhe%20Radhe%20Shyam-vbr-V5.mp3");
        filename_list.add("https://media-audio.mio.to/by_artist/F/Falguni%20Pathak/Vandan%201/1_7%20-%20Radha%20Duhnd%20Rahi%20Kisene%20Mera%20Shyam%20Dekha-vbr-V5.mp3");
        filename_list.add("https://media-audio.mio.to/various_artists/B/Best%20of%20Laxmikant%20%26%20Pyarelal/1_8%20-%20Yashoda%20Ka%20Nandlala-vbr-V5.mp3");
        filename_list.add("https://media-audio.mio.to/by_artist/G/Gauthami%20S.%20Moorthy/Ba%20Ba%20Krishna%20%281970%29/1_5%20-%20Jayajanardana-vbr-V5.mp3");

        mantra_name_list.add("Radhe Radhe Barsane Wali Radhe");
        mantra_name_list.add("Maiya Mori Main Nahi Maakhan Khayo");
        mantra_name_list.add("Aisi Lagi Lagan");
        mantra_name_list.add("O Palanhaare");
        mantra_name_list.add("Aarti Kunj Bihari Ki");
        mantra_name_list.add("Bada Natkhat Hai Krishna");
        mantra_name_list.add("Achyuta Ashtakam");
        mantra_name_list.add("Yashomati Maiya Se Bole Nandlala");
        mantra_name_list.add("Shri Hari Stotram");
        mantra_name_list.add("Achyutam Keshavam");
        mantra_name_list.add("Vishnu Shatanama Stotram");
        mantra_name_list.add("Jai Radha Madhav");
        mantra_name_list.add("Radhe Radhe Govind Gopal Radhe");
        mantra_name_list.add("Radhe Krishna Radhe Shyam");
        mantra_name_list.add("Shri Krishna Govind Hare Murari");
        mantra_name_list.add("Shyam Teri Bansi");
        mantra_name_list.add("Kishori Kuch Aisa Intejam Ho Jaye");
        mantra_name_list.add("Shri Radha Sahastra Naam Yatra");
        mantra_name_list.add("Govinda Krishna Hari Om");
        mantra_name_list.add("Govind Bolo Hari Gopal Bolo");
        mantra_name_list.add("Madhurashtakam");
        mantra_name_list.add("Shyam Chudi Bechne Aaya");
        mantra_name_list.add("Nand Ke Anand Bhayo");
        mantra_name_list.add("Hey Krishna Gopal Hari");
        mantra_name_list.add("Krishna Kanha");
        mantra_name_list.add("Gopala Gopala");
        mantra_name_list.add("Shri Ram Stuti");
        mantra_name_list.add("Hari Om Tat Sat");
        mantra_name_list.add("Krishna Ashtakam");
        mantra_name_list.add("Jape Ja Radhe Radhe");
        mantra_name_list.add("Goverdhan Ko Jao");
        mantra_name_list.add("Krishna Naam Ke Heere Moti");
        mantra_name_list.add("Kishori Meri Kusum Kali");
        mantra_name_list.add("Mere Shyam Murli Wale");
        mantra_name_list.add("Bankey Bihari Ji Ki Aarti");
        mantra_name_list.add("Radhika Gori Se");
        mantra_name_list.add("Mera Aapki Kripa Se");
        mantra_name_list.add("Radhe Radhe Radhe Shyam");
        mantra_name_list.add("Radha Dhund Rahi");
        mantra_name_list.add("Yashoda Ka Nandlala");
        mantra_name_list.add("Jaya Janardhana");
        mantra_name_list.add("Radhe Radhe Barsane Wali Radhe");

        mantra_list.add("barsane_wali_radhe_bhajan");
        mantra_list.add("maiya_mori_bhajan");
        mantra_list.add("aisi_lagi_lagan_bhajan");
        mantra_list.add("o_palanhaare_bhajan");
        mantra_list.add("aarti_kunj_bihari_bhajan");
        mantra_list.add("bada_natkhat_bhajan");
        mantra_list.add("achyutashtakam_bhajan");
        mantra_list.add("yashomati_bhajan");
        mantra_list.add("shri_hari_stotram_bhajan");
        mantra_list.add("achyutam_keshavam_bhajan");
        mantra_list.add("vishnu_shatanama_bhajan");
        mantra_list.add("jai_radha_madhav_bhajan");
        mantra_list.add("radhe_radhe_govind_bhajan");
        mantra_list.add("radhe_krishna_radhe_shyam_bhajan");
        mantra_list.add("shri_krishna_govind_hare_murari_bhajan");
        mantra_list.add("shyam_teri_bansi_pukare_radha_naam_bhajan");
        mantra_list.add("kishori_kuch_aisa_intzam_ho_krishna_bhajan");
        mantra_list.add("radha_sahastra_naam_bhajan");
        mantra_list.add("govinda_krishna_hari_om_bhajan");
        mantra_list.add("govind_bolo_hari_gopal_bolo_bhajan");
        mantra_list.add("madhurashtakam_bhajan");
        mantra_list.add("shyam_chudi_bechne_bhajan");
        mantra_list.add("nand_ke_anand_bhayo_bhajan");
        mantra_list.add("hey_krishna_gopal_hari_bhajan");
        mantra_list.add("krishna_kanha_bhajan");
        mantra_list.add("gopala_gopala_bhajan");
        mantra_list.add("shri_ram_stuti_bhajan");
        mantra_list.add("hari_om_tat_sat_bhajan");
        mantra_list.add("krishnashtakham_bhajan");
        mantra_list.add("jape_ja_radhe_radhe_bhajan");
        mantra_list.add("goverdhan_ko_jao_bhajan");
        mantra_list.add("krishna_naam_ke_heere_moti_bhajan");
        mantra_list.add("kishori_meri_kusum_kali_bhajan");
        mantra_list.add("mere_shyam_murli_wale_bhajan");
        mantra_list.add("bankey_bihari_ji_ki_aarti_bhajan");
        mantra_list.add("radhika_gori_se_bhajan");
        mantra_list.add("mera_aapki_kripa_se_bhajan");
        mantra_list.add("radhe_radhe_radhe_shyam_bhajan");
        mantra_list.add("radha_dhund_rahi_bhajan");
        mantra_list.add("yashoda_ka_nandlala_bhajan");
        mantra_list.add("jaya_janardhana_bhajan");
        mantra_list.add("barsane_wali_radhe_bhajan");


        image_id_list.add(14);
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
        image_id_list.add(14);
        image_id_list.add(19);
        image_id_list.add(20);
        image_id_list.add(21);
        image_id_list.add(22);
        image_id_list.add(23);
        image_id_list.add(24);
        image_id_list.add(19);
        image_id_list.add(9);
        image_id_list.add(10);
        image_id_list.add(14);
        image_id_list.add(23);
        image_id_list.add(5);
        image_id_list.add(3);
        image_id_list.add(13);
        image_id_list.add(4);
        image_id_list.add(16);
        image_id_list.add(15);
        image_id_list.add(17);
        image_id_list.add(21);
        image_id_list.add(1);
        image_id_list.add(4);
        image_id_list.add(18);

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
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Hey! Check out this awesome Krishna Bhajans app - https://play.google.com/store/apps/details?id=com.hanuman.radha.krishna";
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
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
            KanhaBhajan.this.recreate();
        }
    }

}


