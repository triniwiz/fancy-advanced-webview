package co.fitcom.advancedwebview;

import android.content.ComponentName;
import android.support.customtabs.CustomTabsClient;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import co.fitcom.fancywebview.AdvancedWebView;
import co.fitcom.fancywebview.AdvancedWebViewListener;


public class MainActivity extends AppCompatActivity {
    private AdvancedWebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AdvancedWebView.init(this,false);
    }

    public void onTap(View view){
        AdvancedWebViewListener listener = new Listener();
        webView = new AdvancedWebView(this,listener);
        webView.loadUrl("http://google.com");
    }


    class Listener implements AdvancedWebViewListener{
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
            if(event == 6){
                System.out.println("Tabs closed");
            }
        }
    }
}