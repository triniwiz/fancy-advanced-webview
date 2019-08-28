package co.fitcom.advancedwebview;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsClient;

import co.fitcom.fancywebview.AdvancedWebView;
import co.fitcom.fancywebview.AdvancedWebViewListener;


public class MainActivity extends AppCompatActivity {
    private AdvancedWebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AdvancedWebView.init(this, false);
    }

    public void onTap(View view) {
        AdvancedWebViewListener listener = new Listener();
        webView = new AdvancedWebView(this, listener);
        webView.loadUrl("https://google.com");
    }


    class Listener implements AdvancedWebViewListener {
        @Override
        public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
            System.out.println("Connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            System.out.println("Disconnected");
        }

        @Override
        public void onNavigationEvent(int event, Bundle extras) {
            if (event == 6) {
                System.out.println("Tabs closed");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AdvancedWebView.REQUEST_CODE) {
            if (resultCode == RESULT_CANCELED) {
                System.out.println("Tabs closed");
            }
        }
    }
}
