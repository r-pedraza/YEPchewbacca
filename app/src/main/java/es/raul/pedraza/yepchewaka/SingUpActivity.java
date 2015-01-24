package es.raul.pedraza.yepchewaka;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.text.ParseException;


public class SingUpActivity extends ActionBarActivity implements View.OnClickListener {


    EditText name, pass, email;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        name = (EditText) findViewById(R.id.ETName);
        pass = (EditText) findViewById(R.id.ETName);
        email = (EditText) findViewById(R.id.ETName);
        btn = (Button) findViewById(R.id.buttonSing);
        btn.setOnClickListener(this);
        getSupportActionBar().hide();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sing_up, menu);
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


        String aName = name.getText().toString().trim();
        String aPass = pass.getText().toString().trim();
        String aMail = email.getText().toString().trim();

        if (aName.isEmpty() || aMail.isEmpty() || aPass.isEmpty()) {

            Toast.makeText(this, "No esta completado el campo usuario", Toast.LENGTH_SHORT).show();


        } else {

            ParseUser user = new ParseUser();
            user.setUsername(aName);
            user.setPassword(aPass);
            user.setEmail(aMail);


            user.signUpInBackground(new SignUpCallback() {
                                        @Override
                                        public void done(com.parse.ParseException e) {


                                            // Hooray! Let them use the app now.
                                            // Sign up didn't succeed. Look at the ParseException
                                            if (e == null) {

                                                Intent intent = new Intent(SingUpActivity.this, MainActivityTab.class);
                                                startActivity(intent);
                                                //No se guarde en el historial para que al volver atras mencionemos al registro.
                                                intent.addFlags((intent.FLAG_ACTIVITY_NEW_TASK));
                                                intent.addFlags((intent.FLAG_ACTIVITY_CLEAR_TASK));
                                                startActivity(intent);

                                            } else {
                                                Toast.makeText(SingUpActivity.this, "Error Singup()", Toast.LENGTH_SHORT).show();


                                            }
                                        }


                                    }

            );


        }
    }
}

