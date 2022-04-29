package com.example.samplestickerapp.stickermaker.erase.erase.AutoRemovalModule;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Log;
import java.lang.reflect.Array;

public class ModelBitmapProcessing {

    public static Bitmap m11912a(Bitmap bitmap, int i, int i2) {
        if (bitmap == null) {
            return null;
        }
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
        new Canvas(createBitmap).drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), new Rect(0, 0, i, i2), null);
        return createBitmap;
    }

    static int[][] cordinateCal(int[][] iArr, int i) {
        int[][] a = cordinateCal1(iArr);
        for (int i2 = 0; i2 < a.length; i2++) {
            for (int i3 = 0; i3 < a[i2].length; i3++) {
                a[i2][i3] = a[i2][i3] <= i ? 1 : 0;
            }
        }
        return a;
    }

    static int[][] cordinateCal1(int[][] iArr) {
        for (int i = 0; i < iArr.length; i++) {
            for (int i2 = 0; i2 < iArr[i].length; i2++) {
                if (iArr[i][i2] == 1) {
                    iArr[i][i2] = 0;
                } else {
                    iArr[i][i2] = iArr.length + iArr[i].length;
                    if (i > 0) {
                        iArr[i][i2] = Math.min(iArr[i][i2], iArr[i - 1][i2] + 1);
                    }
                    if (i2 > 0) {
                        iArr[i][i2] = Math.min(iArr[i][i2], iArr[i][i2 - 1] + 1);
                    }
                }
            }
        }
        for (int length = iArr.length - 1; length >= 0; length--) {
            for (int length2 = iArr[length].length - 1; length2 >= 0; length2--) {
                int i3 = length + 1;
                if (i3 < iArr.length) {
                    iArr[length][length2] = Math.min(iArr[length][length2], iArr[i3][length2] + 1);
                }
                int i4 = length2 + 1;
                if (i4 < iArr[length].length) {
                    iArr[length][length2] = Math.min(iArr[length][length2], iArr[length][i4] + 1);
                }
            }
        }
        return iArr;
    }

    public static void AutoRemovalThread(Context context, Bitmap bitmap, InterfaceAuto dVar) {
        new Thread(new EraseCustom(context, dVar, bitmap)).start();
    }

    static void initilizeAutoRemoval(Context context, InterfaceAuto dVar, Bitmap bitmap) {
        if (!AutoRemoveModel.loadingState()) {
            try {
                AutoRemoveModel.loadingModel(context);
            } catch (RuntimeException e) {
                StringBuilder sb = new StringBuilder();
                sb.append("autoRemoveBackground: ");
                sb.append(e.toString());
                Log.e("initialize: ", sb.toString());
                dVar.stateAuto1();
            }
        }
        if (bitmap == null) {
            Log.e("PotraitBlur", "Null Bitmap");
            dVar.stateAuto1();
            return;
        }
        float max = 513.0f / ((float) Math.max(bitmap.getWidth(), bitmap.getHeight()));
        Bitmap a = m11912a(bitmap, Math.round(((float) bitmap.getWidth()) * max), Math.round(((float) bitmap.getHeight()) * max));
        Path a2 = pathCalculation(AutoRemoveModel.processAutoRemoveModel(a), a, max);
        if (dVar != null) {
            if (a2 != null) {
                dVar.stateAuto2(a2);
            } else {
                dVar.stateAuto1();
            }
        }
    }

    private static Path pathCalculation(int[] iArr, Bitmap bitmap, float f) {
        if (iArr == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[][] iArr2 = (int[][]) Array.newInstance(int.class, new int[]{width, height});
        for (int i = 0; i < height; i++) {
            for (int i2 = 0; i2 < width; i2++) {
                iArr2[i2][i] = iArr[(i * width) + i2];
            }
        }
        int[][] a = cordinateCal(iArr2, 1);
        Path path = new Path();
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        while (i3 < a.length) {
            int i6 = i5;
            int i7 = i4;
            for (int i8 = 0; i8 < a[i3].length; i8++) {
                if (a[i3][i8] == 1) {
                    if (i3 == i7 && i8 - i6 == 1) {
                        path.lineTo(((float) i3) / f, ((float) i8) / f);
                    } else {
                        path.moveTo(((float) i3) / f, ((float) i8) / f);
                    }
                    i7 = i3;
                    i6 = i8;
                }
            }
            i3++;
            i4 = i7;
            i5 = i6;
        }
        return path;
    }
}
