package com.hanuman.radha.krishna.review;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.suddenh4x.ratingdialog.AppRating;
import com.suddenh4x.ratingdialog.preferences.RatingThreshold;

public class ReviewLifecycleCallback implements Application.ActivityLifecycleCallbacks {
    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
        if (bundle == null) {
            try {
                AppCompatActivity activity1 = (AppCompatActivity) activity;
                new AppRating.Builder(activity1)
                        .setMinimumLaunchTimes(5)
                        .setMinimumDays(2)
                        .setMinimumLaunchTimesToShowAgain(5)
                        .setMinimumDaysToShowAgain(10)
                        .setRatingThreshold(RatingThreshold.FOUR)
                        .useGoogleInAppReview()
                        .showIfMeetsConditions();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }
}
