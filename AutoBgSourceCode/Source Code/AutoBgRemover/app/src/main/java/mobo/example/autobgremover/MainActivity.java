package mobo.example.autobgremover;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.samplestickerapp.stickermaker.erase.erase.EraserActivity;

import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_GALLERY = 764;
    private static final int REQUEST_GALLERY_EDIT = 765;
    private boolean isOpenFisrtTime = false;
    public static String mPath="";

    int currentPage = 0;
    Integer[] imageId = new Integer[]{
            Integer.valueOf(R.drawable.slider_1),
            Integer.valueOf(R.drawable.slider_2),
            Integer.valueOf(R.drawable.slider_3)
    };
    Timer timer;
    private ViewPager viewPager;
    class C02512 implements Runnable {
        C02512() {
        }

        public void run() {
            if (MainActivity.this.currentPage == 3) {
                MainActivity.this.currentPage = 0;
            }
            ViewPager access$100 = MainActivity.this.viewPager;
            MainActivity mainActivity = MainActivity.this;
            int i = mainActivity.currentPage;
            mainActivity.currentPage = i + 1;
            access$100.setCurrentItem(i, true);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(1024, 1024);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;

        LinearLayout adContainer=(LinearLayout) findViewById(R.id.adContainer);
//        CustomAds.facebookAdBanner(MainActivity.this,adContainer);
//        CustomAds.facebookAdInterstitial(MainActivity.this);


        CardView cardView=(CardView)findViewById(R.id.cardView);
        LinearLayout.LayoutParams params_cardView = new LinearLayout.LayoutParams((width),((width)/3)*2);
        cardView.setLayoutParams(params_cardView);
        this.viewPager = (ViewPager) findViewById(R.id.imageviewPager);

        LinearLayout.LayoutParams params_view = new LinearLayout.LayoutParams((width),((width)/3)*2);
        this.viewPager.setLayoutParams(params_view);
        this.viewPager.setAdapter(new CustomAdapter(this, this.imageId));
        final Handler handler = new Handler();
        final Runnable Update = new C02512();
        this.timer = new Timer();
        this.timer.schedule(new TimerTask() {public void run(){handler.post(Update);}}, 500, 3000);


        ImageView eraser=(ImageView)findViewById(R.id.start);
        ImageView editor=(ImageView)findViewById(R.id.editor);
        ImageView folder=(ImageView)findViewById(R.id.folder);
        ImageView share_app =(ImageView)findViewById(R.id.share_app);
        ImageView rate_app=(ImageView)findViewById(R.id.rate_app);


        eraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 23 && !(checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED)) {
                    permissionDialog();
                }
                else
                {

                    onGalleryButtonClick();
                }
            }
        });

        editor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (Build.VERSION.SDK_INT >= 23 && !(checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED)) {
                    permissionDialog();
                }
                else
                {
                    onGalleryButtonClick1();
                }
            }
        });

        folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (Build.VERSION.SDK_INT >= 23 && !(checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED)) {
                    permissionDialog();
                }
                else
                {
                    startActivity(new Intent(MainActivity.this,MyCreationActivity.class));
                }
            }
        });

        share_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Background Eraser Photo Editor");
                    String shareMessage= "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                }
            }
        });

        rate_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String package_name="Enter your package name";
                Uri uri = Uri.parse("market://details?id=" + package_name);
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }            }
        });
    }

    private void permissionDialog() {
        final Dialog dialog = new Dialog(this, 16974128);
        dialog.setContentView(R.layout.permissionsdialog);
        dialog.setTitle(getResources().getString(R.string.permission).toString());
        dialog.setCancelable(false);
        ((Button) dialog.findViewById(R.id.ok)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    MainActivity.this.requestPermissions(new String[]{"android.permission.CAMERA", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 922);
                }
                dialog.dismiss();
            }
        });
        if (this.isOpenFisrtTime) {
            Button setting = (Button) dialog.findViewById(R.id.settings);
            setting.setVisibility(View.VISIBLE);
            setting.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.fromParts("package", MainActivity.this.getPackageName(), null));
                    intent.addFlags(268435456);
                    MainActivity.this.startActivityForResult(intent, 922);
                    dialog.dismiss();
                }
            });
        } else {
            this.isOpenFisrtTime = true;
        }
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_;
        dialog.show();
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != 922) {
            return;
        }
        if (grantResults[0] == 0) {
            if (Build.VERSION.SDK_INT < 23) {
                return;
            }
            if (checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                permissionDialog();
            }
        } else if (Build.VERSION.SDK_INT < 23) {
        }
        else
        {
            if (checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                permissionDialog();
            }
        }
    }
    private void onGalleryButtonClick() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.PICK");
        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_picture)), REQUEST_GALLERY);
    }
    private void onGalleryButtonClick1() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.PICK");
        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_picture)), REQUEST_GALLERY_EDIT);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            if (requestCode == REQUEST_GALLERY) {
                Uri selectedImageUri = data.getData();
                Intent intent = new Intent(this, EraserActivity.class);
                intent.setData(selectedImageUri);
                startActivity(intent);
            }
            if (requestCode == REQUEST_GALLERY_EDIT)
            {
                Uri selectedImageUr = data.getData();
                try {
                    int size=1;
                    mPath=getFilePath(MainActivity.this,selectedImageUr);
                    Log.d("TAGG",selectedImageUr.toString());
                    String[] jArr = new String[size];
                    for (int i = 0; i < size; i++) {
                        jArr[i] = mPath;
                    }
                    int[] iArr = new int[size];
                    for (int i2 = 0; i2 < size; i2++) {
                        iArr[i2] = 0;
                    }
                    Intent intent=new Intent(MainActivity.this,CreateCollageActivity.class);
                    intent.putExtra("photo_id_list", jArr);
                    intent.putExtra("photo_orientation_list", iArr);
                    intent.putExtra("is_scrap_book", false);
                    intent.putExtra("is_shape", false);
                    startActivity(intent);
                }
                catch (Exception e){ }
            }
        }
    }
    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {


            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }

            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

}
