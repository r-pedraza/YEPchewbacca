package es.raul.pedraza.yepchewaka;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

/**
 * Created by Victor on 06/02/2015.
 */
public class FriendsFragment extends ListFragment {

    ProgressBar progressBar;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.fragment_friends, container, false);

       progressBar = (ProgressBar)
               rootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        return rootView;
    }
}
