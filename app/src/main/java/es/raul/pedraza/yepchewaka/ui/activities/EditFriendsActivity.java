package es.raul.pedraza.yepchewaka.ui.activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import es.raul.pedraza.yepchewaka.constants.ParseConstants;
import es.raul.pedraza.yepchewaka.R;


public class EditFriendsActivity extends ListActivity {

    private static final String TAG = EditFriendsActivity.class.getName();

    ProgressBar progressBar;
    List<ParseUser> mUsers;
    //Guardar Ids
    ArrayList<String> objectsIds;
    ArrayList<String> usernames;
    ArrayAdapter<String> adapter;

    //Usuario actual
    ParseUser mCurrentUser;
    //Guardar amigos
    ParseRelation<ParseUser> mFriendsRelation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friends);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        //Metodo para marcar elementos de la lista
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        //boolean pulsado = getListView().isItemChecked(position);
        //Comprobamos si el elemento esta pulsado
        //Si esta pulsado añade
        //Si no lo quita
        if (getListView().isItemChecked(position)){
            mFriendsRelation.add(mUsers.get(position));
        }
        else{
            mFriendsRelation.remove(mUsers.get(position));
        }

        //Guardar en la nube
        mCurrentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    //de pm
                }
                else{
                    Log.e(TAG, "Error al guardar relación", e);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Obtener usuario actual
        mCurrentUser = ParseUser.getCurrentUser();

        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.FRIENDS_RELATION);

        ParseQuery query = ParseUser.getQuery();
        query.orderByAscending(ParseConstants.USERNAME);
        query.setLimit(ParseConstants.MAX_USERS);

        usernames = new ArrayList<String>();
        objectsIds = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_checked, usernames);
        setListAdapter(adapter);

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if(e == null){
                    //de pm
                    mUsers = users;
                    for(ParseUser user:mUsers){
                        objectsIds.add(user.getObjectId());
                        adapter.add(user.getUsername());
                    }
                    addFriendCheckmarks();
                    progressBar.setVisibility(View.INVISIBLE);
                }
                else{
                    Log.e(TAG, "¡Ha ocurrido un error!", e);
                    //TODO show error message
                }
            }
        });
    }

    private void addFriendCheckmarks() {

        mFriendsRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                if(e == null){
                    for(ParseUser user:parseUsers){
                        String userId = user.getObjectId();
                        //Buscamos posicion del amigo
                        if(objectsIds.contains(userId)){
                            getListView().setItemChecked(objectsIds.indexOf(userId), true);
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_friends, menu);
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
