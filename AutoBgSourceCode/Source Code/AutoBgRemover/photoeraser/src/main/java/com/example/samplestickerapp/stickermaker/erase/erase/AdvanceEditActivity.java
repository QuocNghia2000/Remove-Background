
package com.example.samplestickerapp.stickermaker.erase.erase;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AdvanceEditActivity extends AppCompatActivity implements OnClickListener
{
    private static final String myPrefs = "MyPrefs";
    public static Bitmap bitmap;
    public static boolean isConfirm;
    SharedPreferences sharedPreferences;
    public Bitmap bitmap_track;
    private boolean sp_state = true;
    public ImageView main_img;
    private ImageView tbg_img;
    private SeekBar seekbar;

    private Bitmap processBlurEffect(Bitmap bitmap, int i) {
        try {
            Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
            Bitmap createScaledBitmap = Bitmap.createScaledBitmap(EraserActivity.orgBitmap, bitmap.getWidth(), bitmap.getHeight(), false);
            Bitmap[] a = getBlurAlpha(bitmap, i);
            Canvas canvas = new Canvas();
            canvas.setBitmap(createBitmap);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            canvas.drawBitmap(a[1], 0.0f, 0.0f, paint);
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(createScaledBitmap, 0.0f, 0.0f, paint);
            Bitmap createBitmap2 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
            Canvas canvas2 = new Canvas();
            canvas2.setBitmap(createBitmap2);
            Paint paint2 = new Paint();
            paint2.setAntiAlias(true);
            canvas2.drawBitmap(a[0], 0.0f, 0.0f, paint2);
            paint2.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas2.drawBitmap(createBitmap, 0.0f, 0.0f, paint2);
            a[0].recycle();
            a[1].recycle();
            return createBitmap2;
        } catch (Exception | OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }
    public Bitmap getresizedAlpha(Bitmap bitmap, int i) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        int i2 = i * 2;
        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() - i2, bitmap.getHeight() - i2, true);
        Canvas canvas = new Canvas();
        canvas.setBitmap(createBitmap);
        float f = (float) i;
        canvas.drawBitmap(createScaledBitmap, f, f, null);
        return createBitmap;
    }
    public Bitmap getresizedAlphaInc(Bitmap bitmap, int i) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        int i2 = i * 2;
        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() + i2, bitmap.getHeight() + i2, true);
        Canvas canvas = new Canvas();
        canvas.setBitmap(createBitmap);
        float f = (float) (-i);
        canvas.drawBitmap(createScaledBitmap, f, f, null);
        return createBitmap;
    }
    public void processingBitmap(Bitmap bitmap, int i) {
        if (bitmap != null && !bitmap.isRecycled()) {
            ProgressDialog show = ProgressDialog.show(this, "", getString(R.string.processing_image), true);
            show.setCancelable(false);
            final Bitmap[] bitmapArr = new Bitmap[1];
            ProcessBit dVar = new ProcessBit(this, bitmapArr, bitmap.copy(bitmap.getConfig(), true), i, show);

            new Thread(dVar).start();
            show.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    setBitmap(bitmapArr, dialog);
                }
            });
        }
    }
    public void imageSaving(String str) {
        SavingErasedBitmap.saveImageToStorage(str, bitmap, new CustomMediaScan(this, str));
    }
    public void onBackPressed() {
        super.onBackPressed();
    }
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_back || id == R.id.txt_back) {
            finish();
        } else if (id == R.id.btn_done || id == R.id.txt_done)
        {
//            setBitmapResultAndFinish();

/*
            new Handler().post(new Runnable() {
                @Override
                public void run()
                {
                    imageSaving(SavingErasedBitmap.file_bitmap_path());
                }
            });*/
            saveNewBitmap(AdvanceEditActivity.this);

        } else {
            if (id == R.id.btn_tut) {
            }
        }
    }

    private void saveBitmap(Bitmap tbitmap,String file_name) {

        try {
            if(tbitmap==null)
            {
                Toast.makeText(this, "bitmap is null", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "bitmap is not null", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){ }

        try {
            FileOutputStream ostream = new FileOutputStream(file_name);
            tbitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
            ostream.flush();
            ostream.close();
//                tbitmap.recycle();
            sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(new File(file_name))));
            return;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error !"+e, Toast.LENGTH_SHORT).show();
            return;
        }
    }
    public void saveNewBitmap(final Context context) {
        final ProgressDialog ringProgressDialog = ProgressDialog.show(this, "", getString(R.string.processing_image), true);
        ringProgressDialog.setCancelable(false);
        new Thread(new Runnable() {
            public void run() {
                try {
                    String str=SavingErasedBitmap.file_bitmap_path(context);
                    ShareActivity.saved_image=str;
                    saveBitmap(AdvanceEditActivity.bitmap,str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ringProgressDialog.dismiss();
            }
        }).start();

        ringProgressDialog.setOnDismissListener(new DismissDialog());

    }
    class DismissDialog implements OnDismissListener {
        DismissDialog() { }
        public void onDismiss(DialogInterface dialog)
        {
            startActivity(new Intent(AdvanceEditActivity.this, ShareActivity.class));
        }
    }


    public void setBitmapResultAndFinish() {
        if(bitmap==null)
        {
            isConfirm=false;
            Toast.makeText(this, "Error! Something went wrong.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            isConfirm=true;
        }
        finish();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_advance_edit);
        this.sharedPreferences = getSharedPreferences(myPrefs, 0);
        this.sp_state = this.sharedPreferences.getBoolean("advanceedit", true);
        this.sp_state = false;
        isConfirm=false;
        this.main_img = (ImageView) findViewById(R.id.main_img);
        this.tbg_img = (ImageView) findViewById(R.id.tbg_img);
        this.tbg_img.setImageBitmap(bitmapRecunst(ScreenSize.ScreenSizeVal(this), ScreenSize.screenVal(this), 12));
        this.bitmap_track = BitmapListData.m11922b().getListValue();
        bitmap = this.bitmap_track;
        this.main_img.setImageBitmap(bitmap);
        this.seekbar = (SeekBar) findViewById(R.id.seekbar);
        this.seekbar.setProgress(0);
        seekbar.setMax(10);
        this.seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                if (seekBar.getProgress() == 0) {
                    AdvanceEditActivity.this.main_img.setImageBitmap(AdvanceEditActivity.this.bitmap_track);
                    AdvanceEditActivity.bitmap = AdvanceEditActivity.this.bitmap_track;
                    return;
                }
                Log.d("TKKK",seekBar.getProgress()+"");
                AdvanceEditActivity advanceEditActivity = AdvanceEditActivity.this;
                advanceEditActivity.processingBitmap(advanceEditActivity.bitmap_track, seekBar.getProgress());

            }
        });
        SavingErasedBitmap.create_folder(AdvanceEditActivity.this);
    }
    public static Bitmap bitmapRecunst(int i, int i2, int i3) {
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint(4);
        canvas.drawColor(Color.argb(255, 248, 248, 248));
        paint.setColor(Color.argb(255, 216, 216, 216));
        int i4 = 0;
        while (i4 < createBitmap.getWidth()) {
            int i5 = 0;
            while (i5 < createBitmap.getHeight()) {
                float f = (float) (i4 + i3);
                float f2 = (float) (i5 + i3);
                Paint paint2 = paint;
                canvas.drawRect((float) i4, (float) i5, f, f2, paint2);
                float f3 = (float) i3;
                canvas.drawRect(f, f2, f + f3, f2 + f3, paint2);
                i5 += i3 * 2;
            }
            i4 += i3 * 2;
        }
        return createBitmap;
    }
    public void onDestroy() {
        super.onDestroy();
    }
    public void progressBlurBitmap(Bitmap[] bitmapArr, Bitmap bitmap, int i, ProgressDialog progressDialog) {
        bitmapArr[0] = processBlurEffect(bitmap, i);
        progressDialog.dismiss();
    }
    public void setBitmap(Bitmap[] bitmapArr, DialogInterface dialogInterface) {
        ImageView imageView = this.main_img;
        bitmap = bitmapArr[0];
        imageView.setImageBitmap(bitmap);
    }
    public Bitmap[] getBlurAlpha(Bitmap bitmap, int i) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Bitmap extractAlpha = bitmap.extractAlpha();
        Canvas canvas = new Canvas();
        canvas.setBitmap(createBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setMaskFilter(new BlurMaskFilter((float) i, Blur.NORMAL));
        canvas.drawBitmap(extractAlpha, 0.0f, 0.0f, paint);
        return new Bitmap[]{getresizedAlpha(createBitmap, i), getresizedAlphaInc(createBitmap, i)};
    }
    class ProcessBit implements Runnable {

        private final AdvanceEditActivity activity;
        private final Bitmap[] bitmap_arr;
        private final Bitmap bitmap;
        private final int i;
        private final ProgressDialog progressDialog;

        public ProcessBit(AdvanceEditActivity advanceEditActivity, Bitmap[] bitmapArr, Bitmap bitmap, int i, ProgressDialog progressDialog) {
            this.activity = advanceEditActivity;
            this.bitmap_arr = bitmapArr;
            this.bitmap = bitmap;
            this.i = i;
            this.progressDialog = progressDialog;
        }

        public final void run() {
            this.activity.progressBlurBitmap(this.bitmap_arr, this.bitmap, this.i, this.progressDialog);
        }
    }

}
