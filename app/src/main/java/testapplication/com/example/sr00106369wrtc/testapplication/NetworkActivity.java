package testapplication.com.example.sr00106369wrtc.testapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class NetworkActivity extends AppCompatActivity {

    public static final String WIFI = "Wi-Fi";
    public static final String ANY = "Any";

    // Whether there is a Wi-Fi connection.
    private static boolean wifiConnected = false;
    // Whether there is a mobile connection.
    private static boolean mobileConnected = false;
    // Whether the display should be refreshed.
    public static boolean refreshDisplay = true;

   // public static int location_requestCode = 1;

    // The user's current network preference setting.
    public static String sPref = null;

    public Boolean pageLoad;

    public final int locationRequestCode = 1;

    Handler handler = new Handler(Looper.getMainLooper());

    // The BroadcastReceiver that tracks network connectivity changes.
    private NetworkReceiver receiver;

    public WebView mWebView;
    private static final String URL = "https://apprtc.appspot.com/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Registers BroadcastReceiver to track network connection changes.
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkReceiver();
        this.registerReceiver(receiver, filter);
        setContentView(R.layout.activity_network);
        mWebView = (WebView)findViewById(R.id.myWebView);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_network, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(getApplicationContext(),Connection_Settings.class);
            startActivity(i);
        }
        if (id == R.id.action_dial) {
            Intent i = new Intent(Intent.ACTION_DIAL);
            i.setData(Uri.parse("tel:"));
            startActivity(i);
        }
        if (id == R.id.action_contacts) {
            Intent i = new Intent();
            i.setComponent(new ComponentName("com.android.contacts", "com.android.contacts.DialtactsContactsEntryActivity"));
            i.setAction("android.intent.action.MAIN");
            i.addCategory("android.intent.category.LAUNCHER");
            i.addCategory("android.intent.category.DEFAULT");
            startActivity(i);
        }
        if (id == R.id.action_sms) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("sms:"));
            startActivity(i);
        }
        if (id == R.id.action_showLocation) {

            handler.post(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(NetworkActivity.this, LocationActivity.class);
                    startActivityForResult(i, locationRequestCode);
                }
            });
        }

        if (id == R.id.action_showAccelerometer) {

            Intent i = new Intent(this,AccelerometerActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == locationRequestCode){
            if(resultCode == RESULT_OK){
                String latitude = data.getStringExtra("latitude");
                String longitude = data.getStringExtra("longitude");
                Toast.makeText(NetworkActivity.this, "Latitude:"+latitude+"\n"+"Longitude:"+longitude, Toast.LENGTH_LONG).show();
            }else if(resultCode == RESULT_CANCELED){
                Toast.makeText(NetworkActivity.this, "Could not find a last location, please ensure location service is turned on", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unregisters BroadcastReceiver when app is destroyed.
        if (receiver != null) {
            this.unregisterReceiver(receiver);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    // Refreshes the display if the network connection and the
    // pref settings allow it.

    @Override
    public void onStart () {
        super.onStart();

        // Gets the user's network preference settings
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Retrieves a string value for the preferences. The second parameter
        // is the default value to use if a preference value is not found.
        sPref = sharedPrefs.getString("pref_syncConnectionType", "Wi-Fi");

        updateConnectedFlags();

        if(refreshDisplay){
            loadPage();
        }
    }

    // Checks the network connection and sets the wifiConnected and mobileConnected
    // variables accordingly.
    public void updateConnectedFlags() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        } else {
            wifiConnected = false;
            mobileConnected = false;
        }
    }

    // Uses AsyncTask subclass to download the XML feed from stackoverflow.com.
    public void loadPage() {
        if (((sPref.equals(ANY)) && (wifiConnected || mobileConnected))
                || ((sPref.equals(WIFI)) && (wifiConnected))) {
            pageLoad = true;
        } else {
          pageLoad = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpWebViewDefaults(mWebView);
        if(pageLoad) {
            Toast.makeText(NetworkActivity.this, "Connecting to the internet", Toast.LENGTH_LONG).show();
            mWebView.loadUrl(URL);
        }
        else {
            Toast.makeText(NetworkActivity.this, "Could not connect to the internet", Toast.LENGTH_LONG).show();
        }
    }

    private void setUpWebViewDefaults(WebView webView) {
        WebSettings settings = webView.getSettings();

        // Enable Javascript
        settings.setJavaScriptEnabled(true);

        // Use WideViewport and Zoom out if there is no viewport defined
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        // Enable pinch to zoom without the zoom buttons
        settings.setBuiltInZoomControls(true);

        // Allow use of Local Storage
        settings.setDomStorageEnabled(true);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            // Hide the zoom controls for HONEYCOMB+
            settings.setDisplayZoomControls(false);
        }

        // Enable remote debugging via chrome://inspect
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                request.grant(request.getResources());
            }
        });
        webView.setWebViewClient(new WebViewClient());

        // AppRTC requires third party cookies to work
        CookieManager cookieManager = CookieManager.getInstance();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(mWebView, true);
        }
    }

    public void reloadPage(){
        mWebView.loadUrl("javascript:window.location.reload( true )");
    }
}
