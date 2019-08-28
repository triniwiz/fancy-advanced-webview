package co.fitcom.fancywebview

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.browser.customtabs.*

/**
 * Created by triniwiz on 1/18/18.
 */


object AdvancedWebViewStatics {
    var customTabsServiceConnection: CustomTabsServiceConnectionCallBack? = null
    var customTabsCallbackListener: CustomTabsCallbackListener? = null
    const val PACKAGE_NAME = "com.android.chrome"
    const val REQUEST_CODE = 1868

    fun init(context: Context, warmUp: Boolean) {
        if (customTabsServiceConnection == null) {
            customTabsServiceConnection = CustomTabsServiceConnectionCallBack(null, warmUp)
        }

        if (customTabsCallbackListener == null) {
            customTabsCallbackListener = CustomTabsCallbackListener(null)
        }

        CustomTabsClient.bindCustomTabsService(context, PACKAGE_NAME, customTabsServiceConnection)
    }
}

class AdvancedWebView(private val mContext: Context, listener: AdvancedWebViewListener?) {

    private var webViewListener: AdvancedWebViewListener? = null
    private var customTabsClient: CustomTabsClient? = null
    private var session: CustomTabsSession? = null
    private var customTabsIntent: CustomTabsIntent? = null
    private var builder: CustomTabsIntent.Builder? = null

    init {
        setWebViewListener(listener)
        setUp()
    }


    private fun setUp() {
        if (AdvancedWebViewStatics.customTabsServiceConnection!!.customTabsClient != null) {
            customTabsClient = AdvancedWebViewStatics.customTabsServiceConnection!!.customTabsClient
            session = customTabsClient!!.newSession(AdvancedWebViewStatics.customTabsCallbackListener)
            builder = CustomTabsIntent.Builder(session)
        }
    }

    private fun setWebViewListener(listener: AdvancedWebViewListener?) {
        webViewListener = listener
        AdvancedWebViewStatics.customTabsServiceConnection!!.setWebViewListener(listener)
        AdvancedWebViewStatics.customTabsCallbackListener!!.setWebViewListener(listener)
    }

    fun loadUrl(string: String) {
        val pm = mContext.packageManager
        try {
            val info = pm.getApplicationInfo(AdvancedWebViewStatics.PACKAGE_NAME, 0)

            if (info.enabled) {
                if (builder != null) {
                    customTabsIntent = builder!!.build()
                    customTabsIntent!!.intent.setPackage(AdvancedWebViewStatics.PACKAGE_NAME)
                }
            }
            if (customTabsIntent != null) {
                customTabsIntent!!.launchUrl(mContext, Uri.parse(string))
            } else {
                val fallback = WebViewFallback()
                fallback.openUri(mContext as Activity, Uri.parse(string))
            }
        } catch (e: PackageManager.NameNotFoundException) {
            val fallback = WebViewFallback()
            fallback.openUri(mContext as Activity, Uri.parse(string))
        }

    }

    fun getBuilder(): CustomTabsIntent.Builder? {
        return builder
    }

    fun setBuilder(builder: CustomTabsIntent.Builder?) {
        if (builder != null) this.builder = builder
    }

}


class CustomTabsServiceConnectionCallBack(private var webViewListener: AdvancedWebViewListener?, warmUp: Boolean) : CustomTabsServiceConnection() {
    private var warmUpView = false
    var customTabsClient: CustomTabsClient? = null
        private set

    init {
        warmUpView = warmUp
    }

    override fun onCustomTabsServiceConnected(name: ComponentName, client: CustomTabsClient) {
        if (warmUpView) {
            client.warmup(0L)
        }
        if (webViewListener != null) {
            webViewListener!!.onCustomTabsServiceConnected(name, client)
        }
        customTabsClient = client
    }

    override fun onServiceDisconnected(name: ComponentName) {
        if (webViewListener != null) {
            webViewListener!!.onServiceDisconnected(name)
        }
    }

    fun setWebViewListener(listener: AdvancedWebViewListener?) {
        webViewListener = listener
    }
}


class CustomTabsCallbackListener(private var webViewListener: AdvancedWebViewListener?) : CustomTabsCallback() {

    override fun onNavigationEvent(navigationEvent: Int, extras: Bundle?) {
        if (webViewListener != null) {
            webViewListener!!.onNavigationEvent(navigationEvent, extras)
        }
    }

    fun setWebViewListener(listener: AdvancedWebViewListener?) {
        webViewListener = listener
    }

}
