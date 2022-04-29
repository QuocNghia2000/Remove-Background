package mobo.example.autobgremover;

import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdRequest;

public class CustomAds
{
    public static com.google.android.gms.ads.InterstitialAd mInterstitialAd;
    public static InterstitialAd interstitialAdFacebook;
    public static void facebookAdBanner(final Context context, final LinearLayout adContainer) {
//
//        AudienceNetworkAds.initialize(context);
//        AudienceNetworkAds.isInAdsProcess(context);
//        AdSettings.addTestDevice("68abf7c6-a195-4a2a-9920-2c5a52bf1e7b");
//        AdView adView= new AdView(context, context.getString(R.string.id_facebook_banner), AdSize.BANNER_HEIGHT_50);
//        adContainer.addView(adView);
//        adView.setAdListener(new AdListener() {
//            @Override
//            public void onError(Ad ad, AdError adError) {
//                googleAdBanner(context,adContainer);
//            }
//            @Override
//            public void onAdLoaded(Ad ad) { }
//
//            @Override
//            public void onAdClicked(Ad ad) { }
//
//            @Override
//            public void onLoggingImpression(Ad ad) { }
//        });
//        adView.loadAd();
    }
    public static void facebookAdInterstitial(final Context context) {
//        AudienceNetworkAds.initialize(context);
//        AudienceNetworkAds.isInAdsProcess(context);
//        AdSettings.addTestDevice("68abf7c6-a195-4a2a-9920-2c5a52bf1e7b");
//        interstitialAdFacebook = new InterstitialAd(context, context.getString(R.string.id_facebook_fullscreen));
//        interstitialAdFacebook.setAdListener(new InterstitialAdListener() {
//            @Override
//            public void onInterstitialDisplayed(Ad ad) { }
//            @Override
//            public void onInterstitialDismissed(Ad ad) { }
//            @Override
//            public void onAdLoaded(Ad ad) {
//                interstitialAdFacebook.show();
//            }
//            @Override
//            public void onError(Ad ad, AdError adError) {
//                googleAdInterstitial(context);
//                Log.e("Errorfacebook", " : "+adError.getErrorMessage());
//            }
//
//            @Override
//            public void onAdClicked(Ad ad) { }
//            @Override
//            public void onLoggingImpression(Ad ad) { }
//        });
//        interstitialAdFacebook.loadAd();
}
    public static void googleAdBanner(Context context, LinearLayout adContainer){
        com.google.android.gms.ads.AdView mAdView = new com.google.android.gms.ads.AdView(context);
        mAdView.setAdSize(com.google.android.gms.ads.AdSize.SMART_BANNER);
        mAdView.setAdUnitId(context.getString(R.string.id_google_banner));
        adContainer.addView(mAdView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        mAdView.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdLoaded() { }
            @Override
            public void onAdClosed() { }
            @Override
            public void onAdFailedToLoad(int errorCode) { }
            @Override
            public void onAdLeftApplication() { }
            @Override
            public void onAdOpened() { super.onAdOpened(); }
        });
        mAdView.loadAd(adRequest);
    }
    public static void googleAdInterstitial(Context context) {
        mInterstitialAd = new com.google.android.gms.ads.InterstitialAd(context);
        mInterstitialAd.setAdUnitId(context.getString(R.string.id_google_fullscreen));
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new com.google.android.gms.ads.AdListener() {
            public void onAdLoaded() {
                if (mInterstitialAd.isLoaded())
                {
                    mInterstitialAd.show();
                }
            }
        });
    }
    public static void dismissInterstitialGoogle(Context context){
        if(mInterstitialAd!=null)
        {
            mInterstitialAd.setAdListener(null);
        }
    }
    public static void dismissInterstitialFacebook(Context context){
        if(interstitialAdFacebook!=null)
        {
            interstitialAdFacebook.destroy();
        }
    }

}
