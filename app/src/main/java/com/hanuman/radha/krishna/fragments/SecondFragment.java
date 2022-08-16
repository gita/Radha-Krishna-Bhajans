package com.hanuman.radha.krishna.fragments;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hanuman.radha.krishna.R;
import com.hanuman.radha.krishna.classes.MediaStatus;
import com.hanuman.radha.krishna.classes.NotificationService;
import com.hanuman.radha.krishna.classes.NotifyBuilder;
import com.hanuman.radha.krishna.classes.SharedPref;


public class SecondFragment extends Fragment {

    private TextView textView;

    private static final String MANTRA = "MANTRA";

    BroadcastReceiver broadcastReceiver;

    private SharedPref sharedPref;

    public static SecondFragment newInstance(String text) {
        SecondFragment fragment = new SecondFragment();
        Bundle args = new Bundle();
        args.putString(MANTRA, text);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        textView = (TextView) view.findViewById(R.id.textView);

        sharedPref = new SharedPref(requireContext());

        enableBroadcastReceiver();

       broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                select_lyrics(sharedPref.GET_MANTRA());

            }
        };

        requireActivity().registerReceiver(broadcastReceiver, new IntentFilter("Music"));

        String mantra = getArguments().getString(MANTRA);
        if(mantra !=null)

            {
                textView.setText(mantra);
            }
        return view;
        }



    public void enableBroadcastReceiver()
    {
        ComponentName receiver = new ComponentName(requireActivity(), NotificationService.class);
        PackageManager pm = requireContext().getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }



    @Override
    public void onDestroy() {
        requireActivity().unregisterReceiver(broadcastReceiver);
        disableBroadcastReceiver();
        super.onDestroy();
    }

    public void disableBroadcastReceiver(){
        ComponentName receiver = new ComponentName(requireActivity(), NotificationService.class);
        PackageManager pm = requireContext().getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    private void select_lyrics(String s){

        String lyrics =  requireContext().getResources().getString(getResources().getIdentifier(s, "string", requireContext().getPackageName())).trim();

        textView.setText("");
        textView.setText(lyrics);
    }
}

