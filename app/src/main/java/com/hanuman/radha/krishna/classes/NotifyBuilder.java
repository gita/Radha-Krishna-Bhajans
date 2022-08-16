package com.hanuman.radha.krishna.classes;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.hanuman.radha.krishna.R;

public class NotifyBuilder {

    private final int PAUSE = 1;
    private final int PLAY = 0;
    public static final String CHANNEL_ID = "channel1";
    public static final String CHANNEL_ID_2 = "channel2";
    public static final String ACTION_PREVIUOS = "actionprevious";
    public static final String ACTION_PLAY = "actionplay";
    public static final String ACTION_NEXT = "actionnext";
    public static final String DISMISS_ACTION = "dismiss";
    private Context context;


    public NotifyBuilder(Context context) {
        this.context = context;
    }


    private Bitmap check_which_icon(int image_id){

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.krishna_yashoda_balaram);;

                switch (image_id){

                    case 1:
                        icon = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.krishna_yashoda_balaram);
                        return icon;

                    case 2:
                        icon = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.baby_krishna);
                        return icon;

                    case 3:
                        icon = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.krishna_radha);
                        return icon;

                    case 4:
                        icon = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.krishna_5);
                        return icon;

                    case 5:
                        icon = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.krishna_1);
                        return icon;

                    case 6:
                        icon = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.krishna_2);
                        return icon;

                    case 7:
                        icon = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.krishna_3);
                        return icon;

                    case 8:
                        icon = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.krishna_vishnu);
                        return icon;

                    case 9:
                        icon = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.krishna_brahma);
                        return icon;

                    case 10:
                        icon = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.krishna_kaliya);
                        return icon;

                    case 11:
                        icon = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.are_dwarpalo);
                        return icon;

                    case 12:
                        icon = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.ek_baar_to_radha);
                        return icon;

                    case 13:
                        icon = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.gopal_muraliya_wale);
                        return icon;

                    case 14:
                        icon = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.govind_bolo_hari_gopal);
                        return icon;

                    case 15:
                        icon = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.kabhi_ram_banke_kabhi_shyam);
                        return icon;

                    case 16:
                        icon = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.radha_dhoond_rahi);
                        return icon;

                    case 17:
                        icon = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.mithe_ras_se_bharyo);
                        return icon;

                    case 18:
                        icon = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.mohana_muralidhara);
                        return icon;

                    case 19:
                        icon = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.om_jai_jagdish_hare);
                        return icon;


                    case 20:
                        icon = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.radhe_radhe_japo);
                        return icon;

                    case 21:
                        icon = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.shri_krishna_goving_hare_murari);
                        return icon;

                    case 22:
                        icon = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.shyam_teri_bansi);
                        return icon;

                    case 23:
                        icon = BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.shyama_aan_baso_vrindavan);
                        return icon;

                }

        return icon;

    }


    @SuppressLint("UnspecifiedImmutableFlag")
    public void create_notification(String mantra_name, int image_id, int PLAY_PAUSE,int TRACK_SIZE, int POS) {


        int previos = R.drawable.previous;
        int next = R.drawable.forward;

        PendingIntent PREVIOUS_INTENT;

        if (PLAY_PAUSE == PAUSE){
            PLAY_PAUSE = R.drawable.pause;
        }else if(PLAY_PAUSE == PLAY){
            PLAY_PAUSE = R.drawable.play;
        }

        if (POS == 0){
            previos = 0;
            PREVIOUS_INTENT = null;
        }else{
            Intent PREVIOUS = new Intent(context,NotificationService.class).setAction(ACTION_PREVIUOS);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                PREVIOUS_INTENT = PendingIntent.getBroadcast(context,4,PREVIOUS,PendingIntent.FLAG_IMMUTABLE);
            }else
            {
                PREVIOUS_INTENT = PendingIntent.getBroadcast(context,4,PREVIOUS,PendingIntent.FLAG_UPDATE_CURRENT);
            }
        }


        PendingIntent NEXT_INTENT;

        if (POS == TRACK_SIZE){
            next = 0;
            NEXT_INTENT = null;
        }else{

            Intent NEXT = new Intent(context,NotificationService.class).setAction(ACTION_NEXT);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                NEXT_INTENT = PendingIntent.getBroadcast(context,3,NEXT,PendingIntent.FLAG_IMMUTABLE);
            }else
            {
                NEXT_INTENT = PendingIntent.getBroadcast(context,3,NEXT,PendingIntent.FLAG_UPDATE_CURRENT);
            }
        }

        Bitmap bitmap = check_which_icon(image_id);

        Intent PLAY = new Intent(context,NotificationService.class).setAction(ACTION_PLAY);

        PendingIntent PLAY_INTENT = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            PLAY_INTENT = PendingIntent.getBroadcast(context,2,PLAY,PendingIntent.FLAG_IMMUTABLE);
        }else
        {
            PLAY_INTENT = PendingIntent.getBroadcast(context,2,PLAY,PendingIntent.FLAG_UPDATE_CURRENT);
        }

        Intent intent = new Intent(context, NotificationService.class).setAction(DISMISS_ACTION);
        PendingIntent DISMISS = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
             DISMISS = PendingIntent.getBroadcast(context,
                            5, intent, PendingIntent.FLAG_IMMUTABLE);
        }else{
            DISMISS = PendingIntent.getBroadcast(context.getApplicationContext(),
                    5, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.music)
                .setContentTitle(mantra_name)
                .setLargeIcon(bitmap)
                .setOngoing(false)
                .setAutoCancel(false)
                .setDeleteIntent(DISMISS)
                .setPriority(Notification.PRIORITY_LOW)
                .addAction(previos,"Previous",PREVIOUS_INTENT)
                .addAction(PLAY_PAUSE,"Pause",PLAY_INTENT)
                .addAction(next,"Next",NEXT_INTENT)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle());


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder.build());

    }


    public void create_wait_notification() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.music)
                .setContentTitle("Please Wait")
                .setContentText("Loading Next Bhajan...")
                .setOngoing(false)
                .setAutoCancel(false)
                .setPriority(Notification.PRIORITY_DEFAULT);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder.build());

    }


    public void createNotificationChannel() {

        NotificationChannel channel;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;channel = new NotificationChannel(CHANNEL_ID, "Music Player", importance);
            channel.setDescription("This is description");
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void cancel_notification(){
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(1);

    }

    public void cancel_notification_and_channel(){
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.deleteNotificationChannel(CHANNEL_ID);
        }
        manager.cancel(1);

    }




}
