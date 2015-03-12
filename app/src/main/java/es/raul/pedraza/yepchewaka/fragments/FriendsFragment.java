
package es.raul.pedraza.yepchewaka.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import es.raul.pedraza.yepchewaka.constants.ParseConstants;
import es.raul.pedraza.yepchewaka.R;

/**
 * Created by raulpedrazaleon on 30/12/14.
 */
public class FriendsFragment extends ListFragment {

    final static String TAG = FriendsFragment.class.getName();

    List<ParseUser> mUsers;
    ArrayList<String> usernames;
    ArrayAdapter<String> adapter;
    ProgressBar spinner;
    ParseUser mCurrentUser;
    ParseRelation<ParseUser> mFriendsRelation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);

        spinner = (ProgressBar)
                rootView.findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);
        return rootView;

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
                //success
                    spinner.setVisibility(View.INVISIBLE);
                    mUsers = users;
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
        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,usernames);
        setListAdapter(adapter);
    }
}