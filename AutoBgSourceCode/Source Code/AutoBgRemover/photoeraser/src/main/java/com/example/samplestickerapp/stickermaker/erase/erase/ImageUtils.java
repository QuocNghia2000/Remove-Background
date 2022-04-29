package com.example.samplestickerapp.stickermaker.erase.erase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.util.Log;

public class ImageUtils {

    @SuppressLint({"UseValueOf"})
    public static Bitmap maskBitmap(Bitmap bitmap, Bitmap bitmap2) {
        if (bitmap == null || bitmap2 == null || bitmap.isRecycled() || bitmap2.isRecycled()) {
            return null;
        }
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint(1);
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, null);
        canvas.drawBitmap(bitmap2, 0.0f, 0.0f, paint);
        paint.setXfermode(null);
        return createBitmap;
    }
    public static Bitmap resizeBitmap(Bitmap bitmap, int i, int i2) {
        if (bitmap == null) {
            return null;
        }
        float f = (float) i;
        float f2 = (float) i2;
        float width = (float) bitmap.getWidth();
        float height = (float) bitmap.getHeight();
        StringBuilder sb = new StringBuilder();
        sb.append(f);
        String str = "  ";
        sb.append(str);
        sb.append(f2);
        sb.append("  and  ");
        sb.append(width);
        sb.append(str);
        sb.append(height);
        String str2 = "testings";
        Log.i(str2, sb.toString());
        float f3 = width / height;
        float f4 = height / width;
        String str3 = "  if (he > hr) ";
        String str4 = " in else ";
        if (width > f) {
            float f5 = f * f4;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("if (wd > wr) ");
            sb2.append(f);
            sb2.append(str);
            sb2.append(f5);
            Log.i(str2, sb2.toString());
            if (f5 > f2) {
                f = f2 * f3;
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str3);
                sb3.append(f);
                sb3.append(str);
                sb3.append(f2);
                Log.i(str2, sb3.toString());
            } else {
                StringBuilder sb4 = new StringBuilder();
                sb4.append(str4);
                sb4.append(f);
                sb4.append(str);
                sb4.append(f5);
                Log.i(str2, sb4.toString());
                f2 = f5;
            }
        } else if (height > f2) {
            float f6 = f2 * f3;
            StringBuilder sb5 = new StringBuilder();
            sb5.append(str3);
            sb5.append(f6);
            sb5.append(str);
            sb5.append(f2);
            Log.i(str2, sb5.toString());
            if (f6 > f) {
                f2 = f * f4;
            } else {
                StringBuilder sb6 = new StringBuilder();
                sb6.append(str4);
                sb6.append(f6);
                sb6.append(str);
                sb6.append(f2);
                Log.i(str2, sb6.toString());
                f = f6;
            }
        } else if (f3 > 0.75f) {
            f2 = f * f4;
            Log.i(str2, " if (rat1 > .75f) ");
        } else if (f4 > 1.5f) {
            f = f2 * f3;
            Log.i(str2, " if (rat2 > 1.5f) ");
        } else {
            f2 = f * f4;
            Log.i(str2, str4);
        }
        return Bitmap.createScaledBitmap(bitmap, (int) f, (int) f2, false);
    }
    public static Bitmap circularBitmap(Context context, int i) {
        int a = dpToPx(context, 150);
        return maskBitmap(tiltedBitmap(context, i, a, a), Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.circle), a, a, true));
    }
    public static int dpToPx(Context context, int i) {
        float f = (float) i;
        context.getResources();
        return (int) (f * Resources.getSystem().getDisplayMetrics().density);
    }
    public static Bitmap tiltedBitmap(Context context, int i, int i2, int i3) {
        Rect rect = new Rect(0, 0, i2, i3);
        Paint paint = new Paint();
        Options options = new Options();
        options.inScaled = false;
        Bitmap decodeResource = BitmapFactory.decodeResource(context.getResources(), i, options);
        TileMode tileMode = TileMode.REPEAT;
        paint.setShader(new BitmapShader(decodeResource, tileMode, tileMode));
        Bitmap createBitmap = Bitmap.createBitmap(i2, i3, Config.ARGB_8888);
        new Canvas(createBitmap).drawRect(rect, paint);
        return createBitmap;
    }

}
