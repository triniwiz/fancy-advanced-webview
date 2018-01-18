package co.fitcom.fancywebview;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;

/**
 * Created by triniwiz on 1/18/18.
 */

public class AdvancedWebView {
    private static CustomTabsServiceConnectionCallBack customTabsServiceConnection;
    private AdvancedWebViewListener webViewListener;
    private CustomTabsClient customTabsClient;
    private static CustomTabsCallbackListener customTabsCallbackListener;
    private boolean warmUpView = false;
    private CustomTabsSession customTabsSession;
    private CustomTabsIntent customTabsIntent;
    private CustomTabsIntent.Builder builder;
    private Context mContext;
    public static final String PACKAGE_NAME = "com.android.chrome";
    public AdvancedWebView(Context context, @Nullable AdvancedWebViewListener listener) {
        mContext = context;
        setWebViewListener(listener);
        customTabsClient = customTabsServiceConnection.getCustomTabsClient();
        customTabsSession = customTabsClient.newSession(customTabsCallbackListener);
        builder = new CustomTabsIntent.Builder(customTabsSession);
    }

    public static void init(Context context,boolean warmUp) {
        if (customTabsServiceConnection == null) {
            customTabsServiceConnection = new CustomTabsServiceConnectionCallBack(null, warmUp);
        }

        if (customTabsCallbackListener == null) {
            customTabsCallbackListener = new CustomTabsCallbackListener(null);
        }

        CustomTabsClient.bindCustomTabsService(context, PACKAGE_NAME, customTabsServiceConnection);
    }

    public void setWebViewListener(AdvancedWebViewListener listener) {
        webViewListener = listener;
        customTabsServiceConnection.setWebViewListener(listener);
        customTabsCallbackListener.setWebViewListener(listener);
    }

    public void setCustomTabsClient(CustomTabsClient client) {
        customTabsClient = client;
    }

    public CustomTabsClient getCustomTabsClient() {
        return customTabsClient;
    }

    public void loadUrl(String string) {
        customTabsIntent = builder.build();
        PackageManager pm = mContext.getPackageManager();
        try {
            ApplicationInfo info = pm.getApplicationInfo(PACKAGE_NAME, 0);
            if (info.enabled) {
                customTabsIntent.intent.setPackage(PACKAGE_NAME);
            }
            customTabsIntent.launchUrl(mContext, Uri.parse(string));
        } catch (PackageManager.NameNotFoundException e) {
            customTabsIntent.launchUrl(mContext, Uri.parse(string));
        }
    }

    public CustomTabsSession getSession(){
        return customTabsSession;
    }

    public CustomTabsIntent.Builder getBuilder(){
        return builder;
    }

}


class CustomTabsServiceConnectionCallBack extends CustomTabsServiceConnection {
    private AdvancedWebViewListener webViewListener;
    private boolean warmUpView = false;
    private CustomTabsClient mClient;
    public CustomTabsServiceConnectionCallBack(@Nullable AdvancedWebViewListener listener, boolean warmUp) {
        webViewListener = listener;
        warmUpView = warmUp;
    }

    @Override
    public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
        if (warmUpView) {
            client.warmup(0);
        }
        if (webViewListener != null) {
            webViewListener.onCustomTabsServiceConnected(name, client);
        }
        mClient = client;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        if (webViewListener != null) {
            webViewListener.onServiceDisconnected(name);
        }
    }

    public void setWebViewListener(AdvancedWebViewListener listener) {
        webViewListener = listener;
    }

    public CustomTabsClient getCustomTabsClient(){
        return mClient;
    }
}


class CustomTabsCallbackListener extends CustomTabsCallback {
    private AdvancedWebViewListener webViewListener;

    public CustomTabsCallbackListener(@Nullable AdvancedWebViewListener listener) {
        webViewListener = listener;
    }

    @Override
    public void onNavigationEvent(int navigationEvent, Bundle extras) {
        if (webViewListener != null) {
            webViewListener.onNavigationEvent(navigationEvent, extras);
        }
    }

    public void setWebViewListener(AdvancedWebViewListener listener) {
        webViewListener = listener;
    }
}