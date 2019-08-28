package co.fitcom.fancywebview;

/*
  Created by triniwiz on 1/18/18.
 */

import android.content.ComponentName;
import android.os.Bundle;

import androidx.browser.customtabs.CustomTabsClient;

public interface AdvancedWebViewListener {
    void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client);

    void onServiceDisconnected(ComponentName name);

    void onNavigationEvent(int event, Bundle extras);
}

