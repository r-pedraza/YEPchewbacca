package es.raul.pedraza.yepchewaka;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by raulpedraza on 6/2/15.
 */

//Extendemos de list fragment por que va a ser una lista de fragmentos.
public class InboxFragment extends ListFragment {

    View progressBar;
    Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_inbox,container,false);
        progressBar = rootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);


        return rootView;
    }
}

