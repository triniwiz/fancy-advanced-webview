package co.fitcom.advancedwebview

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsClient
import co.fitcom.fancywebview.AdvancedWebView
import co.fitcom.fancywebview.AdvancedWebViewListener
import co.fitcom.fancywebview.AdvancedWebViewStatics


class MainActivity : AppCompatActivity() {
    private var webView: AdvancedWebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AdvancedWebViewStatics.init(this, false)
    }

    fun onTap(view: View) {
        val listener = Listener()
        webView = AdvancedWebView(this, listener)
        val b = webView!!.getBuilder()
        b!!.addDefaultShareMenuItem()
                .enableUrlBarHiding()
                .setToolbarColor(resources.getColor(android.R.color.holo_green_dark)
                ).setShowTitle(true)
                .setSecondaryToolbarColor(resources.getColor(android.R.color.holo_purple))
                .setInstantAppsEnabled(true)
                .build()
        webView!!.setBuilder(b)
        webView!!.loadUrl("https://google.com")
    }


    internal inner class Listener : AdvancedWebViewListener {
        override fun onCustomTabsServiceConnected(name: ComponentName, client: CustomTabsClient) {
            println("Connected")
        }

        override fun onServiceDisconnected(name: ComponentName) {
            println("Disconnected")
        }

        override fun onNavigationEvent(event: Int, extras: Bundle?) {
            if (event == 6) {
                println("Tabs closed")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AdvancedWebViewStatics.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_CANCELED) {
                println("Tabs closed")
            }
        }
    }
}
