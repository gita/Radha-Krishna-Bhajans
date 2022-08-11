package com.hanuman.radha.krishna;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetailsParams;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.onesignal.OneSignal;

import java.util.ArrayList;
import java.util.List;

import static com.hanuman.radha.krishna.PreferenceHelper.setAdFree;

/**
 * RadhaKrishna
 */
public class MainActivity extends AppCompatActivity implements PurchasesUpdatedListener {
    static Button sharingButton;
    private FirebaseAnalytics mFirebaseAnalytics;

    private static final String TAG = "InAppBilling";

    static final String ITEM_SKU_ADREMOVAL = "radhakrishna.remove_ads";

    private Button mBuyButton;
    private String mAdRemovalPrice;
    private SharedPreferences mSharedPreferences;

    private BillingClient mBillingClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        sharingButton= (Button) findViewById(R.id.btn2);
        sharingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shareIt();
            }
        });

        sharingButton= (Button) findViewById(R.id.btn5);
        sharingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonClickPlay();
            }
        });

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();


        // Establish connection to billing client
        mBillingClient = BillingClient.newBuilder(MainActivity.this).setListener(this).enablePendingPurchases().build();
        mBillingClient.startConnection(new BillingClientStateListener() {

            @Override
            public void onBillingServiceDisconnected() {
                //TODO implement your own retry policy
                Toast.makeText(MainActivity.this,  getResources().getString(R.string.billing_connection_failure), Toast.LENGTH_SHORT);
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }

            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {

            }
        });

        mBuyButton = (Button) findViewById(R.id.btnRemoveAds);

        mBuyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If user clicks the buy button, launch the billing flow for an ad removal  purchase
                // Response is handled using onPurchasesUpdated listener
                List<String> skuList = new ArrayList<>();
                skuList.add(ITEM_SKU_ADREMOVAL);
                SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
                mBillingClient.querySkuDetailsAsync(params.build(),
                        (billingResult, skuDetailsList) -> {
                            if (skuDetailsList != null && skuDetailsList.size() > 0) {
                                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                                        .setSkuDetails(skuDetailsList.get(0))
                                        .build();
                                mBillingClient.launchBillingFlow(MainActivity.this, billingFlowParams).getResponseCode();
                            }
                        });
            }
        });

        // Query purchases incase a user is connecting from a different device and they've already purchased the app
        queryPurchases();
        queryPrefPurchases();

    }
    public void buttonClickMantras(View v)
    {
        Intent intent3 = new Intent(getApplicationContext(), MantraList.class);
        startActivity(intent3);
    }

    public void buttonClickBhajans(View v)
    {
        Intent intent3 = new Intent(getApplicationContext(), BhajanList.class);
        startActivity(intent3);
    }

    public void buttonBhagavadGita(View v)
    {
        Uri uri = Uri.parse("market://dev?id=5163542502248614028");
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/dev?id=5163542502248614028")));
        }
    }

    private void shareIt() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Hey! Check out this awesome Radha Krishna bhajans app - https://play.google.com/store/apps/details?id=com.hanuman.radha.krishna";
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private void buttonClickPlay() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    private void queryPrefPurchases() {
        Boolean adFree = mSharedPreferences.getBoolean(getResources().getString(R.string.pref_remove_ads_key), false);
        if (adFree) {
            mBuyButton.setText(getResources().getString(R.string.pref_ad_removal_purchased));
            mBuyButton.setEnabled(false);
        }
    }

    private void queryPurchases() {

        //Method not being used for now, but can be used if purchases ever need to be queried in the future

        Purchase.PurchasesResult purchasesResult = mBillingClient.queryPurchases(BillingClient.SkuType.INAPP);
        if (purchasesResult != null) {
            List<Purchase> purchasesList = purchasesResult.getPurchasesList();
            if (purchasesList == null) {
                return;
            }
            if (!purchasesList.isEmpty()) {
                for (Purchase purchase : purchasesList) {
                    if (purchase.getSkus().get(0).equals(ITEM_SKU_ADREMOVAL)) {
                        mSharedPreferences.edit().putBoolean(getResources().getString(R.string.pref_remove_ads_key), true).apply();
                        setAdFree(true);
                        mBuyButton.setText(getResources().getString(R.string.pref_ad_removal_purchased));
                        mBuyButton.setEnabled(false);
                    }
                }
            }
        }

    }

    private void handlePurchase(Purchase purchase) {
        if (purchase.getSkus().get(0).equals(ITEM_SKU_ADREMOVAL)) {
            mSharedPreferences.edit().putBoolean(getResources().getString(R.string.pref_remove_ads_key), true).apply();
            setAdFree(true);
            mBuyButton.setText(getResources().getString(R.string.pref_ad_removal_purchased));
            mBuyButton.setEnabled(false);
        }
    }



    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                && list != null) {
            for (Purchase purchase : list) {
                handlePurchase(purchase);
            }
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            Log.d(TAG, "User Canceled" + billingResult.getResponseCode());
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            mSharedPreferences.edit().putBoolean(getResources().getString(R.string.pref_remove_ads_key), true).apply();
            setAdFree(true);
            mBuyButton.setText(getResources().getString(R.string.pref_ad_removal_purchased));
            mBuyButton.setEnabled(false);
        } else {
            Log.d(TAG, "Other code" + billingResult.getResponseCode());
            // Handle any other error codes.
        }
    }
}
