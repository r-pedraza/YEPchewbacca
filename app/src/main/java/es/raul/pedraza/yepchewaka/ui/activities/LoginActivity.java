package es.raul.pedraza.yepchewaka.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import es.raul.pedraza.yepchewaka.R;


public class LoginActivity extends ActionBarActivity {

    protected TextView mSignUpTextView;

    private EditText usernameField;
    private EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Quita el Action Bar
        getSupportActionBar().hide();

        usernameField = (EditText) findViewById(R.id.usernameField);
        passwordField = (EditText) findViewById(R.id.passwordField);

        mSignUpTextView = (TextView) findViewById(R.id.signUpText);
        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    public void entrarLogin (View view){
        //con el trim quita los espacios entre las palabras
        String sUsername = usernameField.getText().toString().trim();
        String spassword = passwordField.getText().toString().trim();
        /*if (ParseUser.getCurrentUser()!=null){
            ParseUser.logOut();
        }*/

        //Ventana de progreso
        final ProgressDialog dialog = ProgressDialog.show(this,
                getString(R.string.login_message),
                getString(R.string.waiting_message), true);

        ParseUser.logInInBackground(sUsername, spassword, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser!=null){
                    Intent intent = new Intent(LoginActivity.this,MainActivityTab.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                    dialog.dismiss(); //oculto en ventana
                }else{
                    //Arreglar esto
                    Toast.makeText(LoginActivity.this, "Complete los campos",
                            Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
