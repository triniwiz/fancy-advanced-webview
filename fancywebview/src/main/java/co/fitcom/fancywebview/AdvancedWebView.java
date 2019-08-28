package co.fitcom.fancywebview;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsCallback;
import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.customtabs.CustomTabsServiceConnection;
import androidx.browser.customtabs.CustomTabsSession;

/**
 * Created by triniwiz on 1/18/18.
 */

public class AdvancedWebView {
    private static CustomTabsServiceConnectionCallBack customTabsServiceConnection;
    private static CustomTabsCallbackListener customTabsCallbackListener;
    private static final String PACKAGE_NAME = "com.android.chrome";
    public static final int REQUEST_CODE = 1868;

    private AdvancedWebViewListener webViewListener;
    private CustomTabsClient customTabsClient;
    private boolean warmUpView = false;
    private CustomTabsSession customTabsSession;
    private CustomTabsIntent customTabsIntent;
    private CustomTabsIntent.Builder builder;
    private Context mContext;

    public AdvancedWebView(Context context, @Nullable AdvancedWebViewListener listener) {
        mContext = context;
        setWebViewListener(listener);
        setUp();
    }

    void setUp() {
        if (customTabsServiceConnection.getCustomTabsClient() != null) {
            customTabsClient = customTabsServiceConnection.getCustomTabsClient();
            customTabsSession = customTabsClient.newSession(customTabsCallbackListener);
            builder = new CustomTabsIntent.Builder(customTabsSession);
        }
    }

    public static void init(Context context, boolean warmUp) {
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
        PackageManager pm = mContext.getPackageManager();
        try {
            ApplicationInfo info = pm.getApplicationInfo(PACKAGE_NAME, 0);
            if (info.enabled) {
                setUp();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (builder != null) {
                    customTabsIntent = builder.build();
                    customTabsIntent.intent.setPackage(PACKAGE_NAME);
                }
            }
            if (customTabsIntent != null) {
                customTabsIntent.launchUrl(mContext, Uri.parse(string));
            } else {
                WebViewFallback fallback = new WebViewFallback();
                fallback.openUri((Activity) mContext, Uri.parse(string));
            }
        } catch (PackageManager.NameNotFoundException e) {
            WebViewFallback fallback = new WebViewFallback();
            fallback.openUri((Activity) mContext, Uri.parse(string));
        }
    }

    public CustomTabsSession getSession() {
        return customTabsSession;
    }

    public CustomTabsIntent.Builder getBuilder() {
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
            client.warmup(0L);
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

    public CustomTabsClient getCustomTabsClient() {
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
