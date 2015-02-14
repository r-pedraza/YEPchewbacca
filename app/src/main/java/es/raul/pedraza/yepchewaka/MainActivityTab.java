package es.raul.pedraza.yepchewaka;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MainActivityTab extends ActionBarActivity implements ActionBar.TabListener {

    private static final String TAG = MainActivityTab.class.getName();
    private static final int TAKE_PHOTO_REQUEST = 0;
    private static final int TAKE_VIDEO_REQUEST = 1;
    private static final int PICK_PHOTO_REQUEST = 2;
    private static final int PICK_VIDEO_REQUEST = 3;
    private static final int VIDEO_LIMIT = 10;
    private static final int VIDEO_QUALITY = 0;

    //Variable para guardar la ruta de la imagen
    Uri mMediaUri;




    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_tab);
        ParseUser currenUser = ParseUser.getCurrentUser();
        if(currenUser==null){
            //Intent
            Intent intent= new Intent(this,LoginActivity.class);
            //Crea una bandera que le dioce al log que el va a ser la ultima actividad.
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //Borra la coleccion de avtividades superpuestas, es decir, por debajo.
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.ic_launcher);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),this);

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
                ParseUser.logOut();
                Intent intent=new Intent(MainActivityTab.this,LoginActivity.class);
                return true;
        }

        if(id==R.id.singOut){

            ParseUser.logOut();
            Intent intent=new Intent(MainActivityTab.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return  true;
        }
        //Accederemos a la calse para editar añadir un menu
        if(id==R.id.add_friend){
        Intent intent = new Intent(this,EditFriendsActivity.class);
            startActivity(intent);

        }

        if(id==R.id.cameraActionTab){

            dialogCameraChoices();
        }

        return super.onOptionsItemSelected(item);
    }

    private void dialogCameraChoices() {

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setItems(R.array.camera_choices,mDialogListener());
        //crear ventana
        AlertDialog dialog=builder.create();
        //Mostrar ventana
        dialog.show();
    }

    private DialogInterface.OnClickListener mDialogListener() {



        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                switch (which){
                    case 0:
                        //LLamada a intent implicit para la camara de fotos
                        Intent takePhotoIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePhotoIntent,TAKE_PHOTO_REQUEST);
                        takePhoto();

                        break;
                    case 1:
                        Intent takeVideoIntent=new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        startActivityForResult(takeVideoIntent, TAKE_VIDEO_REQUEST);
                        takeVideo();

                        break;
                    case 2:
                            pickPhoto();

                        break;
                    case 3:
                        pickVideo();

                        break;

                }
            }
        };
    }

    private void pickVideo() {
        Log.d(TAG,"ELige una video");

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Cuidadin");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent choosePhotoIntent=new Intent(Intent.ACTION_GET_CONTENT);
                //que me muestre de la galeria solo los videos.
                choosePhotoIntent.setType("video/*");
                startActivityForResult(choosePhotoIntent,PICK_VIDEO_REQUEST);

            }
        });
        builder.setTitle("Cuidadin");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        AlertDialog dialog=builder.create();
    }

    private void pickPhoto() {

        Log.d(TAG,"ELige un foto");
        Intent choosePhotoIntent=new Intent(Intent.ACTION_GET_CONTENT);
        //que me muestre de la galeria solo las fotos.
        choosePhotoIntent.setType("image/*");
        startActivityForResult(choosePhotoIntent,PICK_PHOTO_REQUEST);


    }

    private void takePhoto() {
        Intent takePhotoIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePhotoIntent,TAKE_PHOTO_REQUEST);
        mMediaUri=FileUtilities.getOutputMediaFile(FileUtilities.MEDIA_TYPE_IMAGE);

        if(mMediaUri==null){

            Toast.makeText(this,"Error en el almacenamiento externo Foto",Toast.LENGTH_LONG).show();


        }else {
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT,mMediaUri);
            startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);
        }



    }
    private void takeVideo() {
        Intent takeVideoIntent=new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(takeVideoIntent,TAKE_VIDEO_REQUEST);
        mMediaUri=FileUtilities.getOutputMediaFile(FileUtilities.MEDIA_TYPE_VIDEO);

        if(mMediaUri==null){

            Toast.makeText(this,"Error en el almacenamiento externo Video",Toast.LENGTH_LONG).show();


        }else {
            takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT,mMediaUri);
            takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,VIDEO_LIMIT);
            takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,VIDEO_QUALITY);
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
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_activity_tab, container, false);
            return rootView;
        }
    }
//Metodo que se ejecuta cuando vuelva de la camara de fotos
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==RESULT_OK){
            //Para que solo actualicemos la galeria solo si hemos hecho un videoo una galeria.
            if(requestCode==TAKE_PHOTO_REQUEST||requestCode==TAKE_VIDEO_REQUEST) {
                //Añadimos foto
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(mMediaUri);
                //Avisar a todo el mundo que se ha añadido una foto
                sendBroadcast(mediaScanIntent);
            }else{

                if(data!=null){
                    mMediaUri=data.getData();

                    if(requestCode==PICK_VIDEO_REQUEST){
                        //10mb
                        int fileSize=10*1024*1024;
                        int sizeMax=10*1024*1024;
                        InputStream inputStream=null;
                        try{
                            inputStream=getContentResolver().openInputStream(mMediaUri);
                            fileSize=inputStream.available();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            if(fileSize>sizeMax){
                                //TODO OTRO ERROR
                                return;
                            }
                        }finally {
                            if(inputStream!=null){

                                try {
                                    inputStream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    }

                }
            }

        }
        else if(resultCode!=RESULT_CANCELED){

          Log.d(TAG,"ERROR NE MÉTODO ONACTIVITYRESULT DE MAINACTIVITY");
        }

    }
}
