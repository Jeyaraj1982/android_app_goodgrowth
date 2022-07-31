package com.imax.goodgrowth;

import android.app.Application;

/**
 * Created by Ranjith on 8/2/2018.
 */

public class MyApplication extends Application {

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectionReceiver.ConnectivityReceiverListener listener) {
        ConnectionReceiver.connectivityReceiverListener = listener;
    }

}
