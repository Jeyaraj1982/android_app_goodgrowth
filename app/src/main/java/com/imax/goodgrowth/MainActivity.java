package com.imax.goodgrowth;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.net.NetworkInfo;


public class MainActivity extends Activity {

    private static final String LOG_TAG = "CheckNetworkStatus";
    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;
    public WebView webView;
    Button retry,close;
    LinearLayout visib;
    ConnectivityManager ConnectionManager;
    NetworkInfo networkInfo;
    public String WebUrl = "https://goodgrowth.in/app2/index.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo=ConnectionManager.getActiveNetworkInfo();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);

        init();
        retry();

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retry();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmExit();
            }
        });
    }
    @Override
    public void onBackPressed() {
        ConfirmExit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //  getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        Log.v(LOG_TAG, "onDestory");
        super.onDestroy();
        unregisterReceiver(receiver);
    }


    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {

            Log.v(LOG_TAG, "Receieved notification about network status");
            isNetworkAvailable(context);
        }


        private boolean isNetworkAvailable(Context context) {
            ConnectivityManager connectivity = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            if(!isConnected){
                                //Log.v(LOG_TAG, "Now you are connected to Internet!");
                                //Toast.makeText(getApplicationContext(),"Now you are connected to Internet !",Toast.LENGTH_LONG).show();
                                webView.setVisibility(View.VISIBLE);
                                visib.setVisibility(View.GONE);
                                //webView.loadUrl("https://www.srihariomhindusevasangam.com/");
                                webView.loadUrl(WebUrl);
                                // networkStatus.setText("Now you are connected to Internet!");
                                isConnected = true;
                                //do your processing here ---
                                //if you need to post any data to the server or get status
                                //update from the server
                            }
                            return true;
                        }
                    }
                }
            }
            //Log.v(LOG_TAG, "You are not connected to Internet!");
            // networkStatus.setText("You are not connected to Internet!");
            Toast.makeText(getApplicationContext(), "You are not connected to Internet !", Toast.LENGTH_LONG).show();
            webView.setVisibility(View.GONE);
            visib.setVisibility(View.VISIBLE);
            isConnected = false;
            return false;
        }

    }
    public void retry(){
        ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo=ConnectionManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()==true )
        {
            webView.setVisibility(View.VISIBLE);
            visib.setVisibility(View.GONE);
            //webView.loadUrl("https://www.srihariomhindusevasangam.com/");
            webView.loadUrl(WebUrl);

        }
        else
        {
            Toast.makeText(getApplicationContext(), "Network Not Available", Toast.LENGTH_LONG).show();
            webView.setVisibility(View.GONE);
            visib.setVisibility(View.VISIBLE);

        }
    }
    public void init() {
        // networkStatus = (TextView) findViewById(R.id.networkStatus);
        webView = (WebView) findViewById(R.id.wv);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        visib =(LinearLayout)findViewById(R.id.lay_vis);
        retry=(Button)findViewById(R.id.but_retry);
        close=(Button)findViewById(R.id.but_close);
        //webview.setScrollBarStyle(0);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

    }

    public void ConfirmExit() {
        AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
        ad.setTitle("Close!");
        ad.setMessage("Are you sure want to close?");
        ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                MainActivity.this.finish();

            }
        });
        ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

            }
        });
        ad.show();
    }
}