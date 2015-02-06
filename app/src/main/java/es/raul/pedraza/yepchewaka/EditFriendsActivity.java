package es.raul.pedraza.yepchewaka;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class EditFriendsActivity extends ListActivity {

    private static final String TAG = EditFriendsActivity.class.getName();

    ProgressBar progressBar;
    List<ParseUser> mUsers;
    ArrayList<String> usernames;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friends);

        usernames = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_checked, usernames);
        setListAdapter(adapter);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        progressBar.setVisibility(View.INVISIBLE);

    }

    @Override
    protected void onResume() {
        super.onResume();

        ParseQuery query = ParseUser.getQuery();
        query.orderByAscending(ParseConstants.USERNAME);
        query.setLimit(ParseConstants.MAX_USERS);

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if(e == null){
                    //de pm
                    mUsers = users;
                    for(ParseUser user:mUsers){
                        adapter.add(user.getUsername());
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                }
                else{
                    Log.e(TAG, "¡Ha ocurrido un error!", e);
                    //TODO show error message
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
