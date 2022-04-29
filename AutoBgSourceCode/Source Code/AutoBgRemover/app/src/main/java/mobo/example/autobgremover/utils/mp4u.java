package mobo.example.autobgremover.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import mobo.example.autobgremover.R;


public class mp4u {
    public static String path_saved;
    public static int selected_frame = 0;
    public static float screenWidth, screenHeight;
    public static Bitmap backgroundBitmap = null;
    public static Bitmap altered_bitmap = null;
    public static ArrayList<Bitmap> collage_bitmap_array_list = new ArrayList<>();
    public static int camera_or_cameraCollage = 0;
    public static int cameraCollage_cat = 0;
    public static File filenew;
    public static Bitmap photoBitmap=null;
    public static ArrayList<String> IMAGEALLARY = new ArrayList();

    public static void listAllImages(File filepath) {
        File[] files = filepath.listFiles();
        if (files != null) {
            for (int j = files.length - 1; j >= 0; j--) {
                String ss = files[j].toString();
                File check = new File(ss);
                Log.d("" + check.length(), "" + check.length());
                if (check.length() <= PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) {
                    Log.i("Invalid Image", "Delete Image");
                } else if (check.toString().contains(".jpg") || check.toString().contains(".png") || check.toString().contains(".jpeg")) {
                    IMAGEALLARY.add(ss);
                }
                System.out.println(ss);
            }
            return;
        }
        System.out.println("Empty Folder");
    }


}
