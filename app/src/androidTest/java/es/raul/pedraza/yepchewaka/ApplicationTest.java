package es.raul.pedraza.yepchewaka;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseUser;

import es.raul.pedraza.yepchewaka.ui.activities.LoginActivity;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    protected TextView etiqueta;
    private EditText user1;
    private EditText pass2;
    private LoginActivity loginActivity;
    private Button signIn;

    public ApplicationTest() {

        super(LoginActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        loginActivity = getActivity();
        etiqueta = (TextView) loginActivity.findViewById(R.id.signUpText);
        user1 = (EditText) loginActivity.findViewById(R.id.usernameField);
        pass2 = (EditText) loginActivity.findViewById(R.id.passwordField);
        signIn = (Button) loginActivity.findViewById(R.id.buttonLogin);
    }

    protected void tearDown() throws Exception {

        super.tearDown();
    }

    private static final String USER_YEP = "antonioo";
    private static final String PASS_YEP = "toli";

    public void testAddValues() {
        //on value 1 entry
        TouchUtils.tapView(this, user1);
        getInstrumentation().sendStringSync(USER_YEP);
        // now on value2 entry
        TouchUtils.tapView(this, pass2);
        getInstrumentation().sendStringSync(PASS_YEP);
        // now on Add button
        TouchUtils.clickView(this, signIn);

        if(ParseUser.getCurrentUser()!=null)
            ParseUser.logOut();

    }




}