package testapplication.com.example.sr00106369wrtc.testapplication;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class LocationActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    GoogleApiClient mGoogleApiClient;
    Intent resultIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        resultIntent = getIntent();
        if (mLastLocation != null) {
            Log.d("location activity", "lastlocation found");
            resultIntent.putExtra("latitude", String.valueOf(mLastLocation.getLatitude()));
            resultIntent.putExtra("longitude", String.valueOf(mLastLocation.getLongitude()));
            setResult(RESULT_OK, resultIntent);
            finish();
        }
        else{
            setResult(RESULT_CANCELED,resultIntent);
            finish();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("Location Activity","Connection Suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("Location Activity","Connection Failed");
    }
}
