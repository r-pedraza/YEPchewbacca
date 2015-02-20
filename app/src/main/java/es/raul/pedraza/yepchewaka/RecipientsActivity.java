package es.raul.pedraza.yepchewaka;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class RecipientsActivity extends ListActivity {

    private static final String TAG = RecipientsActivity.class.getName();

    List<ParseUser> mFriends;
    ArrayList<String> usernames;

    ArrayAdapter<String> adapter;

    ProgressBar spinner;

    ParseUser mCurrentUser;
    ParseRelation<ParseUser> mFriendsRelation;

    MenuItem mSendMenuItem;

    private Uri mMediaUri;
    private String mTipoArchivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipients);

        spinner =(ProgressBar)findViewById(R.id.progressBarRecipients);

        Intent intent = getIntent();
        //Cogemos archivo y tipo de archivo
        mMediaUri = intent.getData();
        mTipoArchivo = intent.getStringExtra(ParseConstants.CLAVE_TIPO_ARCHIVO);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipients, menu);

        mSendMenuItem = menu.getItem(0);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send) {
            ParseObject message = createMessage();

            if (message != null){
                enviar(message);
            }
            else{
                //CREAR VENTANA DE DIALOGO
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //metodo enviar mensaje
    private void enviar(ParseObject message) {
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if(e == null){
                    Toast.makeText(RecipientsActivity.this, "Enviado Yeahh", Toast.LENGTH_LONG).show();
                }
                else{
                    //HA IDO MAL
                    //CREAR VENTANA DE DIALOGO
                }
            }
        });

    }

    private ParseObject createMessage() {
        ParseObject message = new ParseObject(ParseConstants.CLASS_MESSAGE);

        //file name
        //file type, image or video
        //id recipient or recipients
        //name receiver

        message.put(ParseConstants.CLAVE_TIPO_ARCHIVO, mTipoArchivo);
        message.put(ParseConstants.CLAVE_ID_REMITENTE,
                ParseUser.getCurrentUser().getObjectId());
        message.put(ParseConstants.CLAVE_NOMBRE_REMITENTE,
                ParseUser.getCurrentUser().getUsername());
        message.put(ParseConstants.CLAVE_ID_DESTINATARIOS,
                ObtenerDestinatariosIds());

        byte[] ficheroBytes = FileHelper.getByteArrayFromFile(this, mMediaUri);

        if(ficheroBytes != null){
            //Si es una imagen, la reducimos
            if(mTipoArchivo.equals(ParseConstants.TIPO_IMAGEN)){
                ficheroBytes = FileHelper.reduceImageForUpload(ficheroBytes);
            }
            //Guardamos el nombre del archivo
            String nombreArchivo = FileHelper.getFileName(this, mMediaUri, mTipoArchivo);

            //Creamos objeto ParseFile con el nombre y el Array de Bytes
            ParseFile fichero = new ParseFile(nombreArchivo, ficheroBytes);
            //añadimos a la clase ParseObject(mensaje) el archivo
            message.put(ParseConstants.CLAVE_ARCHIVO, fichero);
        }
        else{
            //CREAR VENTANA DE DIALOGO
        }

        return message;

    }

    private ArrayList<String> ObtenerDestinatariosIds() {
        ArrayList<String> destinatarios = new ArrayList<>();

        for(int i = 0; i < getListView().getCount(); i++){
           //compruebo si un amigo esta seleccionado
            if (getListView().isItemChecked(i)){
                //añadimos y obtenemos el id del amigo
                destinatarios.add(mFriends.get(i).getObjectId());
            }
        }

        return destinatarios;
    }

    @Override
    public void onResume() {
        super.onResume();

        setListView();

        mCurrentUser = ParseUser.getCurrentUser();

        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.FRIENDS_RELATION);

        ParseQuery query = ParseUser.getQuery();
        query.orderByAscending(ParseConstants.USERNAME);


        spinner.setVisibility(View.VISIBLE);


        mFriendsRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {

            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if(e == null){
                //sucess
                    spinner.setVisibility(View.INVISIBLE);
                    mFriends = users;
                    for(ParseUser user:users){
                        adapter.add(user.getUsername());
                    }

                }
                else{
                    Log.e(TAG, "ParseException caught: ", e);
                }
            }
        });

    }

    private void setListView() {
        usernames= new ArrayList<String>();

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_checked,
                usernames);
        setListAdapter(adapter);
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        //El boton enviar sera visible si hay algun amigo seleccionado
        if(l.getCheckedItemCount() > 0) {
            mSendMenuItem.setVisible(true);
        //Sino sera invisible
        }else{
            mSendMenuItem.setVisible(false);
        }
    }
}