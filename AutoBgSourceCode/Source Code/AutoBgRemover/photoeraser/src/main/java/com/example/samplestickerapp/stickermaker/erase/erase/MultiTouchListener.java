package com.example.samplestickerapp.stickermaker.erase.erase;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import com.example.samplestickerapp.stickermaker.erase.erase.ScaleGestureDetectorBg.SimpleOnScaleGestureListener;

public class MultiTouchListener implements OnTouchListener {

    private static final int INVALID_POINTER_ID = -1;
    Bitmap bitmap_;
    boolean bt = false;
    GestureDetector gesture_detector = null;
    private boolean handleTransparecy = false;
    public boolean isRotateEnabled = true;
    public boolean isRotationEnabled = false;
    public boolean isScaleEnabled = true;
    public boolean isTranslateEnabled = true;
    private TouchCallbackListener listener = null;
    private int mActivePointerId = -1;
    private float mPrevX;
    private float mPrevY;
    private ScaleGestureDetectorBg mScaleGestureDetector = new ScaleGestureDetectorBg(new ScaleGestureListener());
    public float maximumScale = 8.0f;
    public float minimumScale = 0.5f;

    private class ScaleGestureListener extends SimpleOnScaleGestureListener {
        private float valX;
        private float valY;
        private Vector2D vtr;
        public boolean onScale(View view, ScaleGestureDetectorBg var) {
            TransformInfo dVar = new TransformInfo();
            dVar.deltaScale = MultiTouchListener.this.isScaleEnabled ? var.getPreviousSpan() : 1.0f;
            float f = 0.0f;
            dVar.deltaAngle = MultiTouchListener.this.isRotateEnabled ? Vector2D.GestureScaleProc(this.vtr, var.getCurrentSpanVector()) : 0.0f;
            dVar.deltaX = MultiTouchListener.this.isTranslateEnabled ? var.getFocusX() - this.valX : 0.0f;
            if (MultiTouchListener.this.isTranslateEnabled) {
                f = var.getFocusY() - this.valY;
            }
            dVar.deltaY = f;
            dVar.pivotX = this.valX;
            dVar.pivotY = this.valY;
            MultiTouchListener c0Var = MultiTouchListener.this;
            dVar.minimumScale = c0Var.minimumScale;
            dVar.maximumScale = c0Var.maximumScale;
            c0Var.move(view, dVar);
            return false;
        }
        public boolean onScaleEnd(View view, ScaleGestureDetectorBg var) {
            this.valX = var.getFocusX();
            this.valY = var.getFocusY();
            this.vtr.set(var.getCurrentSpanVector());
            return true;
        }

        private ScaleGestureListener() {
            this.vtr = new Vector2D();
        }
    }
    public interface TouchCallbackListener {
        void onTouchCallback(View view);
        void onTouchUpCallback(View view);
    }
    private class TransformInfo {
        public float deltaAngle;
        public float deltaScale;
        public float deltaX;
        public float deltaY;
        public float maximumScale;
        public float minimumScale;
        public float pivotX;
        public float pivotY;
        private TransformInfo() {
        }
    }
    private static float adjustAngle(float f) {
        if (f > 180.0f) {
            return f - 360.0f;
        }
        if (f < -180.0f) {
            f += 360.0f;
        }
        return f;
    }
    private static void adjustTranslation(View view, float f, float f2) {
        if (view.getPivotX() != f || view.getPivotY() != f2) {
            float[] fArr = {0.0f, 0.0f};
            view.getMatrix().mapPoints(fArr);
            view.setPivotX(f);
            view.setPivotY(f2);
            float[] fArr2 = {0.0f, 0.0f};
            view.getMatrix().mapPoints(fArr2);
            float f3 = fArr2[1] - fArr[1];
            view.setTranslationX(view.getTranslationX() - (fArr2[0] - fArr[0]));
            view.setTranslationY(view.getTranslationY() - f3);
        }
    }
    public MultiTouchListener setHandleTransparecy(boolean z) {
        this.handleTransparecy = z;
        return this;
    }
    public MultiTouchListener setGestureListener(GestureDetector gestureDetector) {
        this.gesture_detector = gestureDetector;
        return this;
    }
    public MultiTouchListener setOnTouchCallbackListener(TouchCallbackListener cVar) {
        this.listener = cVar;
        return this;
    }
    public MultiTouchListener enableRotation(boolean z) {
        this.isRotationEnabled = z;
        return this;
    }
    public MultiTouchListener setMinScale(float f) {
        this.minimumScale = f;
        return this;
    }
    public void move(View view, TransformInfo dVar) {
        adjustTranslation(view, dVar.pivotX, dVar.pivotY);
        computeRenderOffset(view, dVar.deltaX, dVar.deltaY);
        float max = Math.max(dVar.minimumScale, Math.min(dVar.maximumScale, view.getScaleX() * dVar.deltaScale));
        view.setScaleX(max);
        view.setScaleY(max);
        if (this.isRotationEnabled) {
            view.setRotation(adjustAngle(view.getRotation() + dVar.deltaAngle));
        }
    }
    private static void computeRenderOffset(View view, float f, float f2) {
        float[] fArr = {f, f2};
        view.getMatrix().mapVectors(fArr);
        view.setTranslationX(view.getTranslationX() + fArr[0]);
        view.setTranslationY(view.getTranslationY() + fArr[1]);
    }
    public boolean handleTransparency(View view, MotionEvent motionEvent) {
        boolean z;
        String str = "  ";
        try {
            if (motionEvent.getAction() == 2 && !this.bt) {
                return false;
            }
            String str2 = "MOVE_TESTs";
            boolean z2 = true;
            if (motionEvent.getAction() == 1) {
                if (!this.bt) {
                    Log.i(str2, "Action UP");
                    this.bt = true;
                    if (this.bitmap_ != null) {
                        this.bitmap_.recycle();
                    }
                    return false;
                }
            }
            int[] iArr = new int[2];
            view.getLocationOnScreen(iArr);
            int rawX = (int) (motionEvent.getRawX() - ((float) iArr[0]));
            int rawY = (int) (motionEvent.getRawY() - ((float) iArr[1]));
            float rotation = view.getRotation();
            Matrix matrix = new Matrix();
            matrix.postRotate(-rotation);
            float[] fArr = {(float) rawX, (float) rawY};
            matrix.mapPoints(fArr);
            int i = (int) fArr[0];
            int i2 = (int) fArr[1];
            if (motionEvent.getAction() == 0) {
                Log.i(str2, "Action DOWN");
                this.bt = true;
                view.setDrawingCacheEnabled(true);
                this.bitmap_ = Bitmap.createBitmap(view.getDrawingCache());
                i = (int) (((float) i) * (((float) this.bitmap_.getWidth()) / (((float) this.bitmap_.getWidth()) * view.getScaleX())));
                i2 = (int) (((float) i2) * (((float) this.bitmap_.getHeight()) / (((float) this.bitmap_.getHeight()) * view.getScaleX())));
                view.setDrawingCacheEnabled(false);
            }
            if (i < 0 || i2 < 0 || i > this.bitmap_.getWidth() || i2 > this.bitmap_.getHeight()) {
                z = false;
            } else {
                z = this.bitmap_.getPixel(i, i2) == 0;
                if (motionEvent.getAction() == 0) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Action DOWN ");
                    sb.append(z);
                    Log.i(str2, sb.toString());
                    this.bt = z;
                }
            }
            StringBuilder sb2 = new StringBuilder();
            if (i < 0 || i2 < 0 || i > this.bitmap_.getWidth() || i2 > this.bitmap_.getHeight()) {
                z2 = false;
            }
            sb2.append(z2);
            sb2.append(" Color  ");
            sb2.append(z);
            sb2.append(str);
            sb2.append(this.bitmap_.getWidth());
            sb2.append(str);
            sb2.append(this.bitmap_.getHeight());
            Log.i(str2, sb2.toString());
            return z;
        } catch (Exception unused) {
            return false;
        }
    }
    public boolean onTouch(View view, MotionEvent motionEvent) {
        this.mScaleGestureDetector.onTouchEvent(view, motionEvent);
        int i = 0;
        if (this.handleTransparecy && handleTransparency(view, motionEvent)) {
            return false;
        }
        GestureDetector gestureDetector = this.gesture_detector;
        if (gestureDetector != null) {
            gestureDetector.onTouchEvent(motionEvent);
        }
        if (!this.isTranslateEnabled) {
            return true;
        }
        int action = motionEvent.getAction();
        int actionMasked = motionEvent.getActionMasked() & action;
        if (actionMasked == 0) {
            TouchCallbackListener cVar = this.listener;
            if (cVar != null) {
                cVar.onTouchCallback(view);
            }
            this.mPrevX = motionEvent.getX();
            this.mPrevY = motionEvent.getY();
            this.mActivePointerId = motionEvent.getPointerId(0);
            return true;
        } else if (actionMasked == 1) {
            this.mActivePointerId = -1;
            TouchCallbackListener cVar2 = this.listener;
            if (cVar2 == null) {
                return true;
            }
            cVar2.onTouchUpCallback(view);
            return true;
        } else if (actionMasked == 2) {
            int findPointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
            if (findPointerIndex == -1) {
                return true;
            }
            float x = motionEvent.getX(findPointerIndex);
            float y = motionEvent.getY(findPointerIndex);
            if (this.mScaleGestureDetector.isInProgress()) {
                return true;
            }
            computeRenderOffset(view, x - this.mPrevX, y - this.mPrevY);
            return true;
        } else if (actionMasked == 3) {
            this.mActivePointerId = -1;
            return true;
        } else if (actionMasked != 6) {
            return true;
        } else {
            int i2 = (65280 & action) >> 8;
            if (motionEvent.getPointerId(i2) != this.mActivePointerId) {
                return true;
            }
            if (i2 == 0) {
                i = 1;
            }
            this.mPrevX = motionEvent.getX(i);
            this.mPrevY = motionEvent.getY(i);
            this.mActivePointerId = motionEvent.getPointerId(i);
            return true;
        }
    }
}
