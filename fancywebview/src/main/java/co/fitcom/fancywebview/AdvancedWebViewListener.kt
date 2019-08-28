package co.fitcom.fancywebview

/*
  Created by triniwiz on 1/18/18.
 */

import android.content.ComponentName
import android.os.Bundle

import androidx.browser.customtabs.CustomTabsClient

interface AdvancedWebViewListener {
    fun onCustomTabsServiceConnected(name: ComponentName, client: CustomTabsClient)

    fun onServiceDisconnected(name: ComponentName)

    fun onNavigationEvent(event: Int, extras: Bundle?)
}

