package es.raul.pedraza.yepchewaka;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class YepApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "HFNVi1CMUhTNEQ5CINAQzcSuINJf7mNri3ArXkk3", "8NrqYa92kmXqNd7bUJp3ddi0it6sqD2eLDydjAMT");

        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();

    }
}






