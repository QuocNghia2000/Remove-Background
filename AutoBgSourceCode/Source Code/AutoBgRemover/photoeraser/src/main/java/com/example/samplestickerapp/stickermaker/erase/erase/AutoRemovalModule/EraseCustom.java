package com.example.samplestickerapp.stickermaker.erase.erase.AutoRemovalModule;

import android.content.Context;
import android.graphics.Bitmap;

public final class EraseCustom implements Runnable
{
    private final Context context;
    private final InterfaceAuto interfaceAuto;
    private final Bitmap bmp;

    public EraseCustom(Context context, InterfaceAuto dVar, Bitmap bitmap) {
        this.context = context;
        this.interfaceAuto = dVar;
        this.bmp = bitmap;
    }
    public final void run() {
        ModelBitmapProcessing.initilizeAutoRemoval(this.context, this.interfaceAuto, this.bmp);
    }

}
