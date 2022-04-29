package com.example.samplestickerapp.stickermaker.erase.erase;

import android.graphics.PointF;

public class Vector2D extends PointF {

    public Vector2D(float f, float f2) {
        super(f, f2);
    }

    public static float GestureScaleProc(Vector2D vector2D, Vector2D vector2D2) {
        vector2D.calculate_xy();
        vector2D2.calculate_xy();
        return (float) ((Math.atan2((double) vector2D2.y, (double) vector2D2.x) - Math.atan2((double) vector2D.y, (double) vector2D.x)) * 4.42745336E8d);
    }

    public Vector2D() {
    }

    public void calculate_xy() {
        float f = this.x;
        float f2 = f * f;
        float f3 = this.y;
        float sqrt = (float) Math.sqrt((double) (f2 + (f3 * f3)));
        this.x /= sqrt;
        this.y /= sqrt;
    }
}
