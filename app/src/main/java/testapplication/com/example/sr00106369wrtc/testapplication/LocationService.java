package testapplication.com.example.sr00106369wrtc.testapplication;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{

    private final IBinder mBinder = new LocalBinder();

    GoogleApiClient mGoogleApiClient;

    private boolean isConnected = false;

    String locationDetail;

    public class LocalBinder extends Binder{

        LocationService getservice(){
            return LocationService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
        isConnected = false;
    }

    @Override
    public void onConnected(Bundle bundle) {

        try{
            Location mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if(mLocation!= null){
                isConnected = true;
                locationDetail= String.valueOf(mLocation.getLatitude());
               // locationDetail[1]= String.valueOf(mLocation.getLongitude());
                Log.d("Location Service", "Obtained location");
            }
        }catch (Exception e){
            isConnected = false;
            Log.d("Location Service",e.getMessage());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("Location Service", "Connection to Play service suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        isConnected = false;
        Log.d("Location Service", "Connection to Play service failed");

    }


    public boolean isConnected(){
        return isConnected;
    }

    public void connectService(){
        mGoogleApiClient.connect();
    }

    public void disconnectService(){
        mGoogleApiClient.disconnect();
        isConnected = false;
        Log.d("Location Service", "Disconnected from Play service");
    }

    public String getValues(){
        return locationDetail;
    }
}
