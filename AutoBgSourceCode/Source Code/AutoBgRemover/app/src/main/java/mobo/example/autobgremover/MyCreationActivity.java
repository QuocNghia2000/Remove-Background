package mobo.example.autobgremover;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;


public class MyCreationActivity extends Activity {
    
    private MyCreationAdapter galleryAdapter;
    private GridView lstList;
    private ImageView noImage;
    public static ArrayList<String> IMAGEALLARY=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_creation);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        LinearLayout adContainer=(LinearLayout) findViewById(R.id.adContainer);
        CustomAds.facebookAdBanner(MyCreationActivity.this,adContainer);
        CustomAds.facebookAdInterstitial(MyCreationActivity.this);


    }

    public static void shareApplication(Context context) {
        //share a url
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_TEXT, "Hey, I'm using this poster maker app) and it is awesome. :- Add ypur app link");
        context.startActivity(Intent.createChooser(share, "Share With Your friends!"));

    }

    protected void onResume() {
        super.onResume();
        bindView();
    }
    private void bindView() {
        noImage = (ImageView) findViewById(R.id.novideoimg);
        lstList = (GridView) findViewById(R.id.lstList);
        getImages();
        if (IMAGEALLARY.size() <= 0) {
            this.noImage.setVisibility(View.VISIBLE);
            lstList.setVisibility(View.GONE);
        } else {
            this.noImage.setVisibility(View.GONE);
            lstList.setVisibility(View.VISIBLE);
        }
        Collections.sort(IMAGEALLARY);
        Collections.reverse(IMAGEALLARY);
        Log.e("list size", String.valueOf(IMAGEALLARY.size()));
        this.galleryAdapter = new MyCreationAdapter(this, IMAGEALLARY);
        lstList.setAdapter(galleryAdapter);
    }
    private void getImages() {
        if (Build.VERSION.SDK_INT < 23) {
            fetchImage();
        } else if (checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED) {
            fetchImage();
        } else if (checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 5);
        }
    }
    private void fetchImage() {
        IMAGEALLARY.clear();
        listAllImages(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Background_Eraser"));
    }
    @TargetApi(23)
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 5:
                if (grantResults[0] == 0) {
                    fetchImage();
                    return;
                } else if (checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != 0) {
                    requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 5);
                    return;
                } else {
                    return;
                }
            default:
                return;
        }
    }
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
    @Override
    protected void onPause() {
        super.onPause();
    }
}
