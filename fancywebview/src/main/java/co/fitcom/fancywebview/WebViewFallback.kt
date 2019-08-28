package co.fitcom.fancywebview

/*
  Created by triniwiz on 2/28/18
 */

import android.app.Activity
import android.content.Intent
import android.net.Uri


class WebViewFallback : CustomTabFallback {
    override fun openUri(activity: Activity, uri: Uri) {
        val intent = Intent(activity, WebViewActivity::class.java)
        intent.putExtra(WebViewActivityConstants.EXTRAL_URL, uri.toString())
        activity.startActivityForResult(intent, 1868)
    }
}