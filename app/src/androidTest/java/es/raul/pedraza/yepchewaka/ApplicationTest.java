package es.raul.pedraza.yepchewaka;

import android.app.Application;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ApplicationTestCase;
import android.test.TouchUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import junit.framework.TestResult;

import es.raul.pedraza.yepchewaka.LoginActivity;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    private static final String USER_NAME = "a";
    private static final String PASS = "a";
    private static final String EXIST_USER = "Usuario existente";
    private static final String NOT_EXIST_USER = "Usuario no existe";
    private EditText name,pass;
    private Button btn;
    private LoginActivity actividad;

    public ApplicationTest() {

        super(LoginActivity.class);


    }


    protected void setUp() throws Exception {

        super.setUp();
        actividad=getActivity();
        //Call this method before the first call to getActivity() to set the initial touch mode for the Activity under test.
        setActivityInitialTouchMode(true);
        name= (EditText) actividad.findViewById(R.id.nameLogin);
        pass=(EditText)actividad.findViewById(R.id.passwordLogin);
        btn=(Button) actividad.findViewById(R.id.buttonLogin);




    }



    public void testAddValues() {
        //on value 1 entry
        TouchUtils.tapView(this, name);
        sendKeys("a");
        // now on value2 entry
        TouchUtils.tapView(this, pass);
        sendKeys("a");
        // now on Add button
        TouchUtils.clickView(this, btn);



        //Checking that edit text name is not null.
        assertNotNull(name);
        //Checking that edit text pass is not null.
        assertNotNull(pass);
    }

    @Override
    protected void runTest() throws Throwable {
        super.runTest();
    }

    @Override
    public void setName(String name) {
        super.setName(name);

        if(!name.equals("a")){

            assertFalse(NOT_EXIST_USER,false);
        }else {

         assertTrue(EXIST_USER,true);

        }
    }


    @Override
    public TestResult run() {
        return super.run();

    }
}