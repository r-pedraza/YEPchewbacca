package es.raul.pedraza.yepchewaka.ui.activities;

import android.app.AlertDialog;
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

import es.raul.pedraza.yepchewaka.R;


public class SignUpActivity extends ActionBarActivity {

    private EditText usernameField;
    private EditText passwordField;
    private EditText emailField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Quitar actionBar
        getSupportActionBar().hide();

        usernameField = (EditText) findViewById(R.id.usernameField);
        passwordField = (EditText) findViewById(R.id.passwordField);
        emailField = (EditText) findViewById(R.id.emailField);

    }

    public void signUp(View v) {

        String sUserName = usernameField.getText().toString().trim(); //trim quita espacios a izquierda y derecha
        String sPassword = passwordField.getText().toString().trim();
        String sEmailAddress = emailField.getText().toString().trim();

        if (sUserName.isEmpty() | sPassword.isEmpty() | sEmailAddress.isEmpty()) {
            //Toast.makeText(this, "Falta por completar algun campo", Toast.LENGTH_SHORT).show();

            AlertDialog dialog = createErrorDialog(getString(R.string.empty_field_message));

            dialog.show();

        } else {
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

    private AlertDialog createErrorDialog(String message) {
        //ventana de dialogo
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
        builder.setMessage(getString(R.string.empty_field_message));
        builder.setTitle(getString(R.string.dialog_error_title));
        builder.setPositiveButton(android.R.string.ok, null);
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        return builder.create();
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

    //onClick del boton cancelar
    public void cancelSignUp(View view) {

        finish();
    }
}
