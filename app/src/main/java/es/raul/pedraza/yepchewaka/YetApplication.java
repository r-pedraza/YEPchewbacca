package es.raul.pedraza.yepchewaka;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by raulpedraza on 16/1/15.
 */
public class YetApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "wVrm5HZnLB49CAL9NIuYZ25sdBPuJkoQFWuTVup2", "kBx21HZKmUwfYvclgLCOu7JJ2qjhPHp3hFKkieQ1");
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();


    }
}
