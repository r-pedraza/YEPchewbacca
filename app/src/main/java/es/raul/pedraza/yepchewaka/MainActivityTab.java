package es.raul.pedraza.yepchewaka;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.ParseUser;


public class MainActivityTab extends ActionBarActivity implements ActionBar.TabListener {

    private static final String TAG = MainActivityTab.class.getName();
    private final static int TAKE_PHOTO_REQUEST = 0;
    private final static int TAKE_VIDEO_REQUEST = 1;
    private final static int PICK_PHOTO_REQUEST = 2;
    private final static int PICK_VIDEO_REQUEST = 3;

    Uri mMediaUri;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    //private String[] camera_choices = {"Haz una foto", "Haz un vídeo", "Elige una foto", "Elige un vídeo"};
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_tab);

        if (ParseUser.getCurrentUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
        /*Bandera que le dice a Login que Login será
        el va a ser la ultima actividad*/
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //Limpia coleccion de actividades superpuestas
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();

        //Ponemos icono
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.ic_launcher);

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity_tab, menu);
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
        } else if (id == R.id.sign_out) {
            ParseUser.logOut();
            Intent i = new Intent(
                    MainActivityTab.this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);

            return true;
        } else if (id == R.id.action_edit_friends){
            Intent intent = new
                    Intent(this, EditFriendsActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_camera){
            dialogCameraChoices();
        }

        return super.onOptionsItemSelected(item);
    }

    //Ventana dialogo para pedir que elegir al hacer foto
    private void dialogCameraChoices() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setItems(R.array.camera_choices, mDialogListener());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private DialogInterface.OnClickListener mDialogListener() {

        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case 0: 
                        takePhoto();
                        break;
                    case 1: 
                        
                        takeVideo();
                        break;
                    case 2: 
                        
                        Log.d(TAG, "Elige una foto");
                        break;
                    case 3: 
                        
                        Log.d(TAG, "Elige un vídeo");
                        break;
                }
            }
        };
    }

    //Método que sacara la camara de fotos
    private void takePhoto() {
        Log.d(TAG, "Haz una foto");
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        mMediaUri = FileUtilities.getOutputMediaFileUri(FileUtilities.MEDIA_TYPE_IMAGE);

        if (mMediaUri == null){
            Toast.makeText(this, "Error en el almacenamiento externo", Toast.LENGTH_LONG).show();
        }
        else{
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
            startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);
        }

    }

    //Método que sacara la camara de video
    private void takeVideo() {
        Log.d(TAG, "Haz un vídeo");
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        mMediaUri = FileUtilities.getOutputMediaFileUri(FileUtilities.MEDIA_TYPE_VIDEO);

        if (mMediaUri == null){
            Toast.makeText(this, "Error en el almacenamiento externo", Toast.LENGTH_LONG).show();
        }
        else{
        startActivityForResult(takeVideoIntent, TAKE_VIDEO_REQUEST);
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class InboxFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static InboxFragment newInstance(int sectionNumber) {
            InboxFragment fragment = new InboxFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public InboxFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_activity_tab, container, false);
            return rootView;
        }
    }

    //Metodo que se va a ejecutar cuando vuelva de la camara de fotos
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Si es igual de abrir la camara y haces la foto
        if(resultCode == RESULT_OK){

            //Intent para avisar a galeria de que hay una imagen nueva
            Intent mediaScanIntent = new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

            mediaScanIntent.setData(mMediaUri);

            //al que le interese hay una nueva foto
            sendBroadcast(mediaScanIntent);
        }
        //Si es distinto de abrir la camara y no haces la foto
        else if(resultCode != RESULT_CANCELED){
            Toast.makeText(this, "Foto Cancelada", Toast.LENGTH_LONG).show();
        }
    }
}
