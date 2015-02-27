package es.raul.pedraza.yepchewaka;

import android.app.Application;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ApplicationTestCase;
import android.test.TouchUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseUser;

import junit.framework.TestResult;

import es.raul.pedraza.yepchewaka.LoginActivity;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    private static final String USER_NAME = "a";
    private static final String PASS = "a";
    private EditText name, pass;
    private Button btn;
    private LoginActivity actividad;

    public ApplicationTest() {

        super(LoginActivity.class);


    }


    protected void setUp() throws Exception {

        super.setUp();
        actividad = getActivity();
        name = (EditText) actividad.findViewById(R.id.nameLogin);
        pass = (EditText) actividad.findViewById(R.id.passwordLogin);
        btn = (Button) actividad.findViewById(R.id.buttonLogin);


    }


    public void testAddValues() {

        TouchUtils.tapView(this, name);
        getInstrumentation().sendStringSync(USER_NAME);
        // now on value2 entry
        TouchUtils.tapView(this, pass);
        getInstrumentation().sendStringSync(PASS);
        // now on Add button
        TouchUtils.clickView(this, btn);

        if (ParseUser.getCurrentUser() != null) {
            ParseUser.logOut();
        }


    }
}