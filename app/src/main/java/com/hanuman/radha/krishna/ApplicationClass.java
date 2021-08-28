package com.hanuman.radha.krishna;

import android.app.Application;

import com.hanuman.radha.krishna.review.ReviewLifecycleCallback;
import com.onesignal.OneSignal;

public class ApplicationClass extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        registerActivityLifecycleCallbacks(new ReviewLifecycleCallback());
    }
}


