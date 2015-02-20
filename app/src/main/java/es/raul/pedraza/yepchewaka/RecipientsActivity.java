package es.raul.pedraza.yepchewaka;

import android.app.AlertDialog;
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
import com.parse.Parse;
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
    final static String TAG = FriendsFragment.class.getName();

    List<ParseUser> mFriends;
    ArrayList<String> usernames;
    ArrayAdapter<String> adapter;
    ProgressBar spinner;
    ParseUser mCurrentUser;
    ParseRelation<ParseUser> mFriendsRelation;
    MenuItem mSendMenuItem;
    private Uri mMediaUri;
    private String mTypeFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipients);
        spinner=(ProgressBar)findViewById(R.id.progressBarRecipientsActivity);
        Intent intent=getIntent();
        mMediaUri=intent.getData();
        mTypeFile=intent.getStringExtra(ParseConstants.KEY_TYPE_FILE);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipients, menu);
        mSendMenuItem=menu.getItem(0);
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



            ParseObject message=cretaeMessage();

            if (message!=null){

                send(message);
            }else {

                //Mensaje de error de que no se ha podido crear mensaje
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void send(ParseObject message) {


        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null){

                    Toast.makeText(RecipientsActivity.this,"Ok",Toast.LENGTH_LONG).show();

                }else {

                    //Mensaje de mensaje no enviado
                }
            }
        });
    }

    private ParseObject cretaeMessage() {

        ParseObject message= new ParseObject(ParseConstants.CLASS_MESSAGE);

        //Name file
         message.put(ParseConstants.KEY_TYPE_FILE,mTypeFile);
        message.put(ParseConstants.KEY_ID_SENDER,ParseUser.getCurrentUser());
        message.put(ParseConstants.KEY_NAME_SENDER,ParseUser.getCurrentUser().getUsername());
        message.put(ParseConstants.KEY_RECIPIENT_IDS,getRecipientsId());
        byte fileBytes[]=FileHelper.getByteArrayFromFile(this,mMediaUri);

        if(fileBytes!=null){

            if(mTypeFile.equals(ParseConstants.TYPE_IMAGE)){

                //IMAGEN



            }
            fileBytes=FileHelper.reduceImageForUpload(fileBytes);
            String nameFile=FileHelper.getFileName(this,mMediaUri,mTypeFile);
            //Create object ParseFIle with name and array
            ParseFile file=new ParseFile(nameFile,fileBytes);
            //Add to the class ParseObject and file
            message.put(ParseConstants.KEY_FILE,file);


        }
        else {

        //AlerDialog
        }


        //Tipe file, image or video

        //id sender or receipent

        //name sender

        return message;
    }

    private ArrayList<String> getRecipientsId() {

        ArrayList<String> recipient = new ArrayList<>();

        for (int i =0;i<getListView().getCount();i++){

            if(getListView().isItemChecked(i)){

                recipient.add(mFriends.get(i).getObjectId());
            }
        }




        return recipient;
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
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_checked,usernames);
        setListAdapter(adapter);
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (l.getCheckedItemCount()>0) {

            mSendMenuItem.setVisible(true);
        }else{
            mSendMenuItem.setVisible(false);

        }


    }
}
