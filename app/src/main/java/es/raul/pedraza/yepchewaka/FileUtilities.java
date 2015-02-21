package es.raul.pedraza.yepchewaka;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by raulpedraza on 13/2/15.
 */
public class FileUtilities {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final String TAG = FileUtilities.class.getName();
    public static String APP_NAME = "YEP";

    public static Uri getOutputMediaFile(int mediaType) {

        if (isExternalStorage()) {

            //Obtener el directorio del almacenamiento interno
            File mediaStorageId = null;
            switch (mediaType) {
                case MEDIA_TYPE_IMAGE:
                    //Carpeta por defecto donde estan las imagenes
                    mediaStorageId = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    break;

                case MEDIA_TYPE_VIDEO:
                    mediaStorageId = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
                    break;
            }


            //Crear subdirectorio
            File appDir = new File(mediaStorageId, APP_NAME);

            if (!appDir.exists()) {
                if (!appDir.mkdirs()) {
                    Log.d(TAG, "Directory" + appDir.getAbsolutePath() + "notcreated");
                    return null;

                }
                Log.d(TAG, "Directory" + appDir.getAbsolutePath());

            }

            //crear nombre del fichero

            String fileName = "";
            Date now = new Date();
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", new Locale("es", "ES")).format(now);
            switch (mediaType) {
                case MEDIA_TYPE_IMAGE:
                    fileName = "IMG_" + timestamp + ".jpg";
                    break;
                case MEDIA_TYPE_VIDEO:
                    fileName = "VID_" + timestamp + ".mp4";
                    break;
            }

            //Crear un objeto FIle con el nombre del fichero
            String pathFile = appDir.getAbsolutePath() + File.separator + fileName;
            File mediaFile = new File(pathFile);
            Log.d(TAG, "File:" + mediaFile.getAbsolutePath());


            //Devolver el URI del fichero
            return Uri.fromFile(mediaFile);


        }


        return null;
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorage() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }


}
