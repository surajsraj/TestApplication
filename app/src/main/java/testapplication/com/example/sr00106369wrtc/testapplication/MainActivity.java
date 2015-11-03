package testapplication.com.example.sr00106369wrtc.testapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    protected void onResume() {
        super.onResume();

        Intent i = new Intent(getApplicationContext(),NetworkActivity.class);
        startActivity(i);
    }

}
