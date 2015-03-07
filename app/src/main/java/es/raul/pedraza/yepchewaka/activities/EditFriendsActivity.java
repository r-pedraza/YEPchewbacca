package es.raul.pedraza.yepchewaka.activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
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
    final static  String TAG=EditFriendsActivity.class.getName();
    ProgressBar progressBar;
    List<ParseUser> mUsers;
    List<String> objectsIds;
    ArrayList<String> usernames;
    ArrayAdapter<String> adapter;
    ParseUser mCurrentUser;
    ParseRelation<ParseUser> mFriendsRelation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friends);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);

        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
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
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        //Comprueba si el usuario esta selecionado y añade usuario.
        if(getListView().isItemChecked(position)){

            mFriendsRelation.add(mUsers.get(position));
        }else{

            mFriendsRelation.remove(mUsers.get(position));

        }


        //Guardado en Parse
        mCurrentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
            if(e==null){

                //puta madre
            }
                else{

                Log.e(TAG,"Error al guardar relación",e);
            }

            }
        });
    }

    @Override
    protected void onResume() {

        //Sacaramos todos los usuarios
        super.onResume();
        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.FRIENDS_RELATION);
        ParseQuery query = ParseUser.getQuery();
        query.orderByAscending(ParseConstants.USERNAME);
        query.setLimit(ParseConstants.MAX_USERS);
        usernames= new ArrayList<>();
        objectsIds=new ArrayList<String>();
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_checked,usernames);
        setListAdapter(adapter);

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List <ParseUser>users, ParseException e) {
                if(e==null){

                    mUsers=users;
                    for(ParseUser user:mUsers){
                        objectsIds.add(user.getObjectId());
                        adapter.add(user.getUsername());
                    }

                    addFriendCheckmarks();
                    progressBar.setVisibility(View.INVISIBLE);


                }else{
                    Log.e(TAG, "Mierda", e);
                }
            }
        });
    }

    private void addFriendCheckmarks() {

        mFriendsRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {

                if(e==null){

                    for(ParseUser user:parseUsers){
                        String userId=user.getObjectId();
                        //Comprobamos por id si es maigo mio o no es amigo mio, mio es el usuario con el que estemos registrado en yep.
                        if(objectsIds.contains(user.getObjectId())){

                            getListView().setItemChecked(objectsIds.indexOf(userId),true);

                        }
                    }
                }

            }
        });
    }
}
