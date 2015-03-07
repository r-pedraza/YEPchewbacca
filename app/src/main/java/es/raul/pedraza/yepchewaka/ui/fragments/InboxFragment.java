package es.raul.pedraza.yepchewaka.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import es.raul.pedraza.yepchewaka.ui.activities.ViewPhoto;
import es.raul.pedraza.yepchewaka.adapters.MessageAdapter;
import es.raul.pedraza.yepchewaka.constants.ParseConstants;
import es.raul.pedraza.yepchewaka.R;

/**
 * Created by Victor on 06/02/2015.
 */
public class InboxFragment extends ListFragment {

    ProgressBar progressBar;
    List<ParseObject> mMessages;
    private ArrayList<String> mensajes;
    //private ArrayAdapter adaptador;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.fragment_inbox, container, false);

       progressBar = (ProgressBar)
               rootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        //Al ir cambiando de fragmento los mensajes se vuelven a actualizar
        //Por eso es mejor ponerlo en onResume que en onCreate
        mensajes = new ArrayList<>();
        //adaptador = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, mensajes);
        //MessageAdapter adaptador = new MessageAdapter(getListView().getContext(), mMessages);
        //setListAdapter(adaptador);

        //Sacariamos todos los mensajes
        ParseQuery<ParseObject> consulta = ParseQuery.getQuery(ParseConstants.CLASS_MESSAGE);

        //Filtraria los mensajes por el usuario que esta conectado
        consulta.whereEqualTo(ParseConstants.CLAVE_ID_DESTINATARIOS,
                ParseUser.getCurrentUser().getObjectId());

        //Ordenamos los mensajes de forma descendiente
        consulta.addDescendingOrder(ParseConstants.CLAVE_CREADO_EN);

        consulta.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    mMessages = parseObjects;

                    //Vamos a recorrer todos los mensajes
                    //for(ParseObject mensaje: mMessages){
                        //adaptador.add(mensaje.getString(ParseConstants.CLAVE_NOMBRE_REMITENTE));
                        MessageAdapter adaptador = new MessageAdapter(getListView().getContext(), mMessages);
                        setListAdapter(adaptador);
                    //}

                    //Cuando se carguen todos los mensajes desaparece
                    progressBar.setVisibility(View.INVISIBLE);
                }
                else{
                    //CREAR VENTANA DE DIALOGO
                }
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        ParseObject message = mMessages.get(position);
        String tipoArchivo = message.getString(ParseConstants.CLAVE_TIPO_ARCHIVO);

        if(tipoArchivo.equals(ParseConstants.TIPO_IMAGEN)){
            ParseFile archivo = message.getParseFile(ParseConstants.CLAVE_ARCHIVO);

            Uri ficheroUri = Uri.parse(archivo.getUrl());
            Intent intent = new Intent(getActivity(), ViewPhoto.class);
            intent.setData(ficheroUri);
            startActivity(intent);
        }
        else{
            //VIDEO
        }
    }
}
