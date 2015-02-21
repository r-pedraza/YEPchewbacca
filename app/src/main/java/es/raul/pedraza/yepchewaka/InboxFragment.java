package es.raul.pedraza.yepchewaka;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raulpedraza on 6/2/15.
 */

//Extendemos de list fragment por que va a ser una lista de fragmentos.
public class InboxFragment extends ListFragment {

    View progressBar;
    List<ParseObject> mMessages;
    ArrayList<String> messages;
    ArrayAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);
        progressBar = rootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        messages = new ArrayList<>();
        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, messages);
        setListAdapter(adapter);

        //Consulta de los mensajes
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstants.CLASS_MESSAGE);

        query.whereEqualTo(ParseConstants.KEY_RECIPIENT_IDS, ParseUser.getCurrentUser().getObjectId());

        query.orderByDescending(ParseConstants.CREATED_AT);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {

                if (e == null) {
                    mMessages = parseObjects;

                    for (ParseObject message : mMessages) {

                        adapter.add(message.getString(ParseConstants.KEY_NAME_SENDER));
                    }

                    progressBar.setVisibility(View.INVISIBLE);
                } else {

                    //Error de envio a parse
                }

            }
        });

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        //Conseguimos el mensaje del elemento
        ParseObject message = mMessages.get(position);

        String typeFile = message.getString(ParseConstants.KEY_TYPE_FILE);

        if (typeFile.equals(ParseConstants.TYPE_IMAGE)) {

            //Cogeos el archivpo
            ParseFile file = message.getParseFile(ParseConstants.KEY_FILE);
            Uri fileUri = Uri.parse(file.getUrl());
            Intent intent = new Intent(getActivity(), ViewPhoto.class);
            intent.setData(fileUri);
            startActivity(intent);

        } else {

            //video
        }


    }
}

