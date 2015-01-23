package es.raul.pedraza.yepchewaka;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class SignUpActivity extends ActionBarActivity {

    private EditText usernameField;
    private EditText passwordField;
    private EditText emailField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameField = (EditText) findViewById(R.id.usernameField);
        passwordField = (EditText) findViewById(R.id.passwordField);
        emailField = (EditText) findViewById(R.id.emailField);

    }

    public void signUp(View v){

        String sUserName = usernameField.getText().toString().trim(); //trim quita espacios a izquierda y derecha
        String sPassword = passwordField.getText().toString().trim();
        String sEmailAddress = emailField.getText().toString().trim();

        if(sUserName.isEmpty() | sPassword.isEmpty() | sEmailAddress.isEmpty()){
            Toast.makeText(this, "Falta por completar un campo", Toast.LENGTH_SHORT).show();
        }
        else {
            //Crea objeto vacio para un usuario
            ParseUser newUser = new ParseUser();

            newUser.setUsername(sUserName);
            newUser.setPassword(sPassword);
            newUser.setEmail(sEmailAddress);

            newUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Intent intent = new Intent(SignUpActivity.this, MainActivityTab.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        //Creamos un Toast llamando a la clase Superior
                        Toast.makeText(SignUpActivity.this, "No se ha podido crear el usuario" +
                                "", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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
