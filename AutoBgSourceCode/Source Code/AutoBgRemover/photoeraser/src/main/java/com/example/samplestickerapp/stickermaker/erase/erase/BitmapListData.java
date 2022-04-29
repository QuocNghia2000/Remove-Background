package com.example.samplestickerapp.stickermaker.erase.erase;

import android.graphics.Bitmap;
import java.util.HashMap;

public class BitmapListData {

    public static final String bmp = "bitmap";
    private static BitmapListData _bitmapState;
    private HashMap<String, Bitmap> _listValue = new HashMap<>();

    private BitmapListData() { }

    public static BitmapListData m11922b() {
        if (_bitmapState == null) {
            _bitmapState = new BitmapListData();
        }
        return _bitmapState;
    }

    public void setListValue(Bitmap bitmap) {
        this._listValue.put(bmp, bitmap);
    }

    public Bitmap getListValue() {
        return (Bitmap) this._listValue.get(bmp);
    }
}
