package org.apache.taverna.mobile.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by Sagar on 03/06/16.
 */
public class ConnectionInfo {
    private static String TAG;
    private static Context context;

    public ConnectionInfo(Context context) {
        TAG = this.getClass().getName();
        Log.i(TAG, "Utils: ");
        ConnectionInfo.context = context;
    }


    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

}
