package es.raul.pedraza.yepchewaka;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


public class LoginActivity extends ActionBarActivity implements View.OnClickListener {
    Button button;
    TextView tv;
    EditText aName, aPass, aEmail;

    private static String TAG = LoginActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activitie);
        button = (Button) findViewById(R.id.buttonLogin);
        button.setOnClickListener(this);
        tv = (TextView) findViewById(R.id.textView);
        tv.setOnClickListener(this);
        aName = (EditText) findViewById(R.id.nameLogin);
        aPass = (EditText) findViewById(R.id.passwordLogin);

        //quitar barra actionBar
        getSupportActionBar().hide();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_activitie, menu);
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


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case (R.id.textView):
                Intent intent = new Intent(LoginActivity.this, SingUpActivity.class);
                startActivity(intent);
                break;
            case (R.id.buttonLogin):

                Log.d(TAG, "Estoy en boton");

                String name = aName.getText().toString().trim();
                String pass = aPass.getText().toString().trim();

                if (name.isEmpty() || pass.isEmpty()) {

                    Toast.makeText(this, "No esta completado Alguno de los campos", Toast.LENGTH_SHORT).show();


                } else {

                    ParseUser.logInInBackground(name, pass, new LogInCallback() {
                        @Override
                        public void done(ParseUser parseUser, ParseException e) {
                            if (parseUser != null) {
                                Intent intent = new Intent(LoginActivity.this, MainActivityTab.class);
                                startActivity(intent);
                            } else {


                            }
                        }
                    });
                    break;

                }

        }
    }

}

