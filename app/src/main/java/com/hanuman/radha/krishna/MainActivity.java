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

import androidx.appcompat.app.AppCompatActivity;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.onesignal.OneSignal;

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

        MobileAds.initialize(this, "ca-app-pub-4070209682123577~6974698932");


        // Establish connection to billing client
        mBillingClient = BillingClient.newBuilder(MainActivity.this).setListener(this).build();
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
                if (billingResponseCode == BillingClient.BillingResponse.OK) {
                    // The billing client is ready. You can query purchases here.

                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                //TODO implement your own retry policy
                Toast.makeText(MainActivity.this,  getResources().getString(R.string.billing_connection_failure), Toast.LENGTH_SHORT);
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });

        mBuyButton = (Button) findViewById(R.id.btnRemoveAds);

        mBuyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If user clicks the buy button, launch the billing flow for an ad removal  purchase
                // Response is handled using onPurchasesUpdated listener
                BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                        .setSku(ITEM_SKU_ADREMOVAL)
                        .setType(BillingClient.SkuType.INAPP)
                        .build();
                int responseCode = mBillingClient.launchBillingFlow(MainActivity.this, flowParams);
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
                    if (purchase.getSku().equals(ITEM_SKU_ADREMOVAL)) {
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
        if (purchase.getSku().equals(ITEM_SKU_ADREMOVAL)) {
            mSharedPreferences.edit().putBoolean(getResources().getString(R.string.pref_remove_ads_key), true).apply();
            setAdFree(true);
            mBuyButton.setText(getResources().getString(R.string.pref_ad_removal_purchased));
            mBuyButton.setEnabled(false);
        }
    }

    @Override
    public void onPurchasesUpdated(int responseCode, @androidx.annotation.Nullable List<com.android.billingclient.api.Purchase> purchases) {

        //Handle the responseCode for the purchase
        //If response code is OK,  handle the purchase
        //If user already owns the item, then indicate in the shared prefs that item is owned
        //If cancelled/other code, log the error

        if (responseCode == BillingClient.BillingResponse.OK
                && purchases != null) {
            for (Purchase purchase : purchases) {
                handlePurchase(purchase);
            }
        } else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            Log.d(TAG, "User Canceled" + responseCode);
        } else if (responseCode == BillingClient.BillingResponse.ITEM_ALREADY_OWNED) {
            mSharedPreferences.edit().putBoolean(getResources().getString(R.string.pref_remove_ads_key), true).apply();
            setAdFree(true);
            mBuyButton.setText(getResources().getString(R.string.pref_ad_removal_purchased));
            mBuyButton.setEnabled(false);
        } else {
            Log.d(TAG, "Other code" + responseCode);
            // Handle any other error codes.
        }
    }


}
