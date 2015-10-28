package testapplication.com.example.sr00106369wrtc.testapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Locale;

public class NetworkReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager conn = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();

        // Checks the user prefs and the network connection. Based on the result, decides whether
        // to refresh the display or keep the current display.
        // If the userpref is Wi-Fi only, checks to see if the device has a Wi-Fi connection.
        if (NetworkActivity.WIFI.equals(NetworkActivity.sPref) && networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            // If device has its Wi-Fi connection, sets refreshDisplay
            // to true. This causes the display to be refreshed when the user
            // returns to the app.
            NetworkActivity.refreshDisplay = true;

            showWifiIpAddress(context);

            // If the setting is ANY network and there is a network connection
            // (which by process of elimination would be mobile), sets refreshDisplay to true.
        } else if (NetworkActivity.ANY.equals(NetworkActivity.sPref) && networkInfo != null) {
            NetworkActivity.refreshDisplay = true;

            if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                showWifiIpAddress(context);
            }else {
                showMobileIpAddress(context);
            }


            // Otherwise, the app can't download content--either because there is no network
            // connection (mobile or Wi-Fi), or because the pref setting is WIFI, and there
            // is no Wi-Fi connection.
            // Sets refreshDisplay to false.
        } else {
            NetworkActivity.refreshDisplay = false;
            Toast.makeText(context, "Connection lost", Toast.LENGTH_SHORT).show();
        }
    }

    public void showWifiIpAddress(Context myContext){

        WifiManager wifiManager = (WifiManager)myContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String formatIpAddress = String.format(Locale.getDefault(),"%d.%d.%d.%d",(ipAddress &0xff),(ipAddress>>8 &0xff),(ipAddress>>16 &0xff),(ipAddress>>24 &0xff));
        Toast.makeText(myContext, "Wifi Connected with IP address:"+formatIpAddress, Toast.LENGTH_LONG).show();
    }

    public void showMobileIpAddress(Context myContext)  {

        try{

            for(Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();){
                NetworkInterface intr = en.nextElement();
                for (Enumeration<InetAddress> enumIpAdrs = intr.getInetAddresses(); enumIpAdrs.hasMoreElements();){
                    InetAddress inetIpAdrs = enumIpAdrs.nextElement();
                    if(!inetIpAdrs.isLoopbackAddress()&& inetIpAdrs instanceof Inet4Address){
                        String ipAddress = inetIpAdrs.getHostAddress().toString();
                        Toast.makeText(myContext, "Mobile Connected with IP address:"+ipAddress, Toast.LENGTH_LONG).show();
                    }
                }
            }

        }catch (Exception e){
            Log.e("Network Receiver","Could not get Network Interface");
        }


    }
}