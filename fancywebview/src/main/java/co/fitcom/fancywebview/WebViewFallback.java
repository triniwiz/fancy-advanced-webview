package co.fitcom.fancywebview;

/**
 * Created by triniwiz on 2/28/18
 */
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;


public class WebViewFallback implements CustomTabFallback {
    @Override
    public void openUri(Activity activity, Uri uri) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra(WebViewActivity.EXTRA_URL, uri.toString());
        activity.startActivityForResult(intent,1868);
    }
}