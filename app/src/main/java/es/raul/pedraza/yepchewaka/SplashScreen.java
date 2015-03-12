package es.raul.pedraza.yepchewaka;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Timer;
import java.util.TimerTask;

import es.raul.pedraza.yepchewaka.activities.MainActivityTab;


public class SplashScreen extends ActionBarActivity {
    private static final long SPLASH_SCREEN_DELAY=3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //Hide title bar
        //requestWindowFeature(Window.FEA);

        setContentView(R.layout.activity_splash_screen);

        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                //Start new Activity
                Intent intent=new Intent().setClass(SplashScreen.this,MainActivityTab.class);
                startActivity(intent);
                //Close
                finish();
            }
        };

        Timer timer = new Timer();
        timer.schedule(timerTask,SPLASH_SCREEN_DELAY);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
