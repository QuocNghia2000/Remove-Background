package com.example.samplestickerapp.stickermaker.erase.erase;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.appcompat.widget.AppCompatImageView;

public class ShaderView extends AppCompatImageView {

    int radius;
    int diameter;//=2R//duong kinh
    private int width;
    private int brushSize;
    private Paint _paint;
    private Bitmap _bitmap;
    private Context context;
    private boolean isRectBrushEnable = false;
    private boolean isTouch = false;
    private boolean drawOnScreenLeft = false;
    private Paint _paint1 = null;

    public ShaderView(Context context) {
        super(context);
        init(context);
    }

    public void init(Context context) {
        this.context = context;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.width = displayMetrics.widthPixels;
        diameter = ImageUtils.dpToPx(context, 150);
        radius = ImageUtils.dpToPx(context, 75);

        this._bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.circle1);
        Bitmap bitmap = this._bitmap;
        int i = diameter;
        this._bitmap = Bitmap.createScaledBitmap(bitmap, i, i, true);
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        StringBuilder sb = new StringBuilder();
        sb.append("onDraw:needToDraw ");
        sb.append(isTouch);
        sb.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("onDraw:onLeft ");
        sb2.append(drawOnScreenLeft);
        sb2.toString();
        if (isTouch && this._paint1 != null) {
            Bitmap bitmap = EraserActivity.bgCircleBit;
            Log.d("RUNNN",bitmap.getWidth()+"");
            if (bitmap != null) {
                if (drawOnScreenLeft) {
                    canvas.drawBitmap(bitmap, 0.0f, 0.0f, null);
                    int i = radius;
                    canvas.drawCircle((float) i, (float) i, (float) i, this._paint1);
                    if (this.isRectBrushEnable) {
                        int i2 = radius;
                        int i3 = brushSize;
                        canvas.drawRect((float) (i2 - i3), (float) (i2 - i3), (float) (i2 + i3), (float) (i2 + i3), this._paint);
                    } else {
                        int i4 = radius;
                        canvas.drawCircle((float) i4, (float) i4, (float) brushSize, this._paint);
                    }
                    canvas.drawBitmap(this._bitmap, 0.0f, 0.0f, null);
                    return;
                }
                canvas.drawBitmap(bitmap, (float) (this.width - diameter), 0.0f, null);
                int i5 = this.width;
                int i6 = radius;
                canvas.drawCircle((float) (i5 - i6), (float) i6, (float) i6, this._paint1);
                if (this.isRectBrushEnable) {
                    int i7 = this.width;
                    int i8 = radius;
                    int i9 = i7 - i8;
                    int i10 = brushSize;
                    canvas.drawRect((float) (i9 - i10), (float) (i8 - i10), (float) ((i7 - i8) + i10), (float) (i8 + i10), this._paint);
                } else {
                    int i11 = this.width;
                    int i12 = radius;
                    canvas.drawCircle((float) (i11 - i12), (float) i12, (float) brushSize, this._paint);
                }
                canvas.drawBitmap(this._bitmap, (float) (this.width - diameter), 0.0f, null);
            }
        }
    }

    public ShaderView(Context context,AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public void mo10677a(Paint paint, Paint paint2, int brushSize, boolean isTouched, boolean drawOnScreenLeft, boolean isRectBrushEnable) {
        StringBuilder sb = new StringBuilder();
        sb.append("updateShaderView: ");
        sb.append(isTouched);
        sb.toString();
        this.isTouch = isTouched;
        this.drawOnScreenLeft = drawOnScreenLeft;
        this.isRectBrushEnable = isRectBrushEnable;
        this._paint1 = paint;
        this._paint = paint2;
        this.brushSize = brushSize;
        Log.d("TUUU",drawOnScreenLeft+"");
        invalidate();
    }

    public ShaderView(Context context,AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }
}
