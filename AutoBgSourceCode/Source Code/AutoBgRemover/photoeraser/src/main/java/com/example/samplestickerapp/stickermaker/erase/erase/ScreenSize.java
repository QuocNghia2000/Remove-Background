package com.example.samplestickerapp.stickermaker.erase.erase;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public final class ScreenSize
{
    public static class DisplayMatrixScreen {
        private int i;
        private int j;

        public DisplayMatrixScreen(int i, int i2) {
            this.i = i;
            this.j = i2;
        }

        public int widthScreen() {
            return this.j;
        }
        public int heightScreen() {
            return this.i;
        }
    }
    private ScreenSize() {
    }
    public static int screenVal(Context context) {
        return getSizeValue(context).widthScreen();
    }
    public static DisplayMatrixScreen getSizeValue(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        Display defaultDisplay = windowManager != null ? windowManager.getDefaultDisplay() : null;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (defaultDisplay != null) {
            defaultDisplay.getRealMetrics(displayMetrics);
        }
        return new DisplayMatrixScreen(displayMetrics.widthPixels, displayMetrics.heightPixels);
    }
    public static int ScreenSizeVal(Context context) {
        return getSizeValue(context).heightScreen();
    }
}
