package co.fitcom.fancywebview

import android.app.Activity
import android.net.Uri

/**
 * Created by triniwiz on 2/28/18
 */

interface CustomTabFallback {
    fun openUri(activity: Activity, uri: Uri)
}