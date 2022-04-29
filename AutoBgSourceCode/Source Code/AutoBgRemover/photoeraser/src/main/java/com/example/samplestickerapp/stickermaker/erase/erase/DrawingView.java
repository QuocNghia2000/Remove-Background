package com.example.samplestickerapp.stickermaker.erase.erase;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;
import androidx.appcompat.widget.AppCompatImageView;
import com.example.samplestickerapp.stickermaker.erase.erase.ScaleGestureDetectorBg.SimpleOnScaleGestureListener;
import com.example.samplestickerapp.stickermaker.erase.erase.AutoRemovalModule.ModelBitmapProcessing;
import com.example.samplestickerapp.stickermaker.erase.erase.AutoRemovalModule.InterfaceAuto;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;

public class DrawingView extends AppCompatImageView implements OnTouchListener {

    public static final int colorCodeVal = -65536;
    private static final int INVALID_POINTER_ID = -1;
    private static int pc;
    private int ERASE = 1;
    private int LASSO = 3;
    public int MODE = 1;
    private int NONE = 0;
    public int REDRAW = 4;
    public int TARGET = 2;
    public int TOLERANCE = 30;
    public ArrayList<Integer> brushIndx = new ArrayList<>();
    public int brushSize = 18;
    private int brushSize1 = 18;
    public ArrayList<Boolean> brushTypeIndx = new ArrayList<>();
    public ArrayList<Path> changesIndx = new ArrayList<>();
    private boolean isAutoRunning = false;
    public boolean drawLasso = true;
    private boolean drawOnScreenLeft = true;
    private boolean isRotateEnabled = true;
    private boolean insidCutEnable = true;
    public boolean isMoved = false;
    private boolean isNewPath = false;
    public boolean isRectBrushEnable = false;
    private boolean isScaleEnabled = true;
    public boolean isSelected = true;
    private boolean isTouched = false;
    public boolean isTranslateEnabled = true;
    private ScaleGestureDetectorBg mScaleGestureDetector;
    public float maximumScale = 8.0f;
    private int offset = 200;
    public float minimumScale = 0.5f;
    private int offset1 = 200;
    public ProgressDialog progressDialog_ = null;
    public Point point;
    private int targetBrushSize = 18;
    float fX = 100.0f;
    private int targetBrushSize1 = 18;
    float fY = 100.0f;
    Canvas c2;
    public boolean updateOnly = false;
    Context ctx;
    Paint erPaint = new Paint();
    Paint erPaint1 = new Paint();
    int erps = ImageUtils.dpToPx(getContext(), 2);
    int height;
    boolean bn_state = false;
    Path tpath = new Path();
    Paint _paint = new Paint();
    Paint _paint1 = new Paint();
    public UndoRedoListener undoRedoListener;
    public ActionListener actionListener;
    private Bitmap orgBit;
    Bitmap bitmap2 = null;
    Bitmap bitmap3 = null;
    Bitmap bitmap4 = null;
    BitmapShader patternBMPshader;
    private ShaderView shaderView = null;
    public ArrayList<Integer> modeIndx = new ArrayList<>();
    float sX;
    float sY;
    float scale = 1.0f;
    private int screenWidth;
    int width;
    Path lPath = new Path();
    public int curIndx = -1;

    
    class AutoDetectionRemoval implements InterfaceAuto {

        final Activity activity_;
        final InterfaceBGI interfaceBgi_;

        AutoDetectionRemoval(Activity activity, InterfaceBGI eVar) {
            this.activity_ = activity;
            this.interfaceBgi_ = eVar;
        }

        public void stateAuto2(final Path path) {
            final Activity activity = this.activity_;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    processInterface1(activity, path, interfaceBgi_);
                }
            });
        }

        public void processInterface1(Activity activity, Path path, InterfaceBGI eVar) {
            DrawingView.this.ImageProcess_(activity);
            DrawingView drawingView = DrawingView.this;
            drawingView.setMODE(drawingView.REDRAW);
            DrawingView.this.invalidate();
            DrawingView drawingView2 = DrawingView.this;
            drawingView2.lPath = path;
            drawingView2.brushSize = ImageUtils.dpToPx(drawingView2.getContext(), 3);
            DrawingView.this.isRectBrushEnable = false;
            DrawingView.this.changesIndx.add(DrawingView.this.curIndx + 1, new Path(DrawingView.this.lPath));
            DrawingView.this.brushIndx.add(DrawingView.this.curIndx + 1, Integer.valueOf(DrawingView.this.brushSize));
            DrawingView.this.modeIndx.add(DrawingView.this.curIndx + 1, Integer.valueOf(DrawingView.this.MODE));
            DrawingView.this.brushTypeIndx.add(DrawingView.this.curIndx + 1, Boolean.valueOf(false));
            DrawingView drawingView3 = DrawingView.this;
            drawingView3._paint1 = drawingView3.getPaintByMode(((Integer) drawingView3.modeIndx.get(1)).intValue(), ((Integer) DrawingView.this.brushIndx.get(1)).intValue(), ((Boolean) DrawingView.this.brushTypeIndx.get(1)).booleanValue());
            DrawingView drawingView4 = DrawingView.this;
            drawingView4.c2.drawPath(drawingView4.lPath, drawingView4._paint1);
            DrawingView.this.curIndx = DrawingView.this.curIndx + 1;
            DrawingView.this.clearNextChanges();
            DrawingView.this.lPath.reset();
            if (eVar != null) {
                eVar.processD();
            }
        }

        public void stateAuto1() {
            final Activity activity = this.activity_;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Toast.makeText(activity, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e){}
                }
            });
        }
    }
    public interface ActionListener {
        void onAction(int i);
        void onActionCompleted(int i);
    }
    private class AsyncTaskRunner1 extends AsyncTask<Void, Void, Bitmap> {

        int _i;
        Vector<Point> _pointV;

        public AsyncTaskRunner1(int i) {
            this._i = i;
        }

        private void indexProc(Point point, int i, int i2) {
            int i3 = i;
            if (i3 != 0) {
                DrawingView drawingView = DrawingView.this;
                int i4 = drawingView.width;
                int i5 = drawingView.height;
                int[] iArr = new int[(i4 * i5)];
                drawingView.bitmap3.getPixels(iArr, 0, i4, 0, 0, i4, i5);
                LinkedList linkedList = new LinkedList();
                linkedList.add(point);
                while (linkedList.size() > 0) {
                    Point point2 = (Point) linkedList.poll();
                    DrawingView drawingView2 = DrawingView.this;
                    if (colorVal(iArr[drawingView2.getIndex(point2.x, point2.y, drawingView2.width)], i3)) {
                        Point point3 = new Point(point2.x + 1, point2.y);
                        while (true) {
                            int i6 = point2.x;
                            if (i6 <= 0) {
                                break;
                            }
                            DrawingView drawingView3 = DrawingView.this;
                            if (!colorVal(iArr[drawingView3.getIndex(i6, point2.y, drawingView3.width)], i3)) {
                                break;
                            }
                            DrawingView drawingView4 = DrawingView.this;
                            iArr[drawingView4.getIndex(point2.x, point2.y, drawingView4.width)] = i2;
                            this._pointV.add(new Point(point2.x, point2.y));
                            int i7 = point2.y;
                            if (i7 > 0) {
                                DrawingView drawingView5 = DrawingView.this;
                                if (colorVal(iArr[drawingView5.getIndex(point2.x, i7 - 1, drawingView5.width)], i3)) {
                                    linkedList.add(new Point(point2.x, point2.y - 1));
                                }
                            }
                            int i8 = point2.y;
                            DrawingView drawingView6 = DrawingView.this;
                            if (i8 < drawingView6.height && colorVal(iArr[drawingView6.getIndex(point2.x, i8 + 1, drawingView6.width)], i3)) {
                                linkedList.add(new Point(point2.x, point2.y + 1));
                            }
                            point2.x--;
                        }
                        int i9 = point2.y;
                        if (i9 > 0) {
                            DrawingView drawingView7 = DrawingView.this;
                            if (i9 < drawingView7.height) {
                                iArr[drawingView7.getIndex(point2.x, i9, drawingView7.width)] = i2;
                                this._pointV.add(new Point(point2.x, point2.y));
                            }
                        }
                        while (true) {
                            int i10 = point3.x;
                            DrawingView drawingView8 = DrawingView.this;
                            int i11 = drawingView8.width;
                            if (i10 >= i11 || !colorVal(iArr[drawingView8.getIndex(i10, point3.y, i11)], i3)) {
                                int i12 = point3.y;
                            } else {
                                DrawingView drawingView9 = DrawingView.this;
                                iArr[drawingView9.getIndex(point3.x, point3.y, drawingView9.width)] = i2;
                                this._pointV.add(new Point(point3.x, point3.y));
                                int i13 = point3.y;
                                if (i13 > 0) {
                                    DrawingView drawingView10 = DrawingView.this;
                                    if (colorVal(iArr[drawingView10.getIndex(point3.x, i13 - 1, drawingView10.width)], i3)) {
                                        linkedList.add(new Point(point3.x, point3.y - 1));
                                    }
                                }
                                int i14 = point3.y;
                                DrawingView drawingView11 = DrawingView.this;
                                if (i14 < drawingView11.height && colorVal(iArr[drawingView11.getIndex(point3.x, i14 + 1, drawingView11.width)], i3)) {
                                    linkedList.add(new Point(point3.x, point3.y + 1));
                                }
                                point3.x++;
                            }
                            int i122 = point3.y;
                            if (i122 > 0) {
                                DrawingView drawingView12 = DrawingView.this;
                                if (i122 < drawingView12.height) {
                                    iArr[drawingView12.getIndex(point3.x, i122, drawingView12.width)] = i2;
                                    this._pointV.add(new Point(point3.x, point3.y));
                                }
                            }

                        }
                    }
                }
                DrawingView drawingView13 = DrawingView.this;
                Bitmap bitmap = drawingView13.bitmap2;
                int i15 = drawingView13.width;
                bitmap.setPixels(iArr, 0, i15, 0, 0, i15, drawingView13.height);
            }
        }

        public void onPreExecute() {
            super.onPreExecute();
            DrawingView drawingView = DrawingView.this;
            ProgressDialog progressDialog = new ProgressDialog(drawingView.getContext());
            drawingView.progressDialog_ = progressDialog;
            StringBuilder sb = new StringBuilder();
            sb.append(DrawingView.this.ctx.getResources().getString(R.string.processing));
            sb.append("...");
            progressDialog.setMessage(sb.toString());
            DrawingView.this.progressDialog_.setCancelable(false);
            DrawingView.this.progressDialog_.show();
        }

        private void currIndx() {
            int size = DrawingView.this.changesIndx.size();
            StringBuilder sb = new StringBuilder();
            sb.append(" Curindx ");
            sb.append(DrawingView.this.curIndx);
            sb.append(" Size ");
            sb.append(size);
            String str = "testings";
            Log.i(str, sb.toString());
            int i = DrawingView.this.curIndx + 1;
            while (size > i) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(" indx ");
                sb2.append(i);
                Log.i(str, sb2.toString());
                DrawingView.this.changesIndx.remove(i);
                DrawingView.this.brushIndx.remove(i);
                DrawingView.this.modeIndx.remove(i);
                DrawingView.this.brushTypeIndx.remove(i);
                size = DrawingView.this.changesIndx.size();
            }
            if (DrawingView.this.undoRedoListener != null) {
                DrawingView.this.undoRedoListener.enableRedo(true, DrawingView.this.curIndx + 1);
                DrawingView.this.undoRedoListener.enableUndo(false, DrawingView.this.modeIndx.size() - (DrawingView.this.curIndx + 1));
            }
        }

        public boolean colorVal(int i, int i2) {
            boolean z = false;
            if (i != 0) {
                if (i2 == 0) {
                    return false;
                }
                if (i == i2) {
                    return true;
                }
                int abs = Math.abs(Color.red(i) - Color.red(i2));
                int abs2 = Math.abs(Color.green(i) - Color.green(i2));
                int abs3 = Math.abs(Color.blue(i) - Color.blue(i2));
                if (abs <= DrawingView.this.TOLERANCE && abs2 <= DrawingView.this.TOLERANCE && abs3 <= DrawingView.this.TOLERANCE) {
                    z = true;
                }
            }
            return z;
        }

        public Bitmap doInBackground(Void... voidArr) {
            if (this._i == 0) {
                return null;
            }
            this._pointV = new Vector<>();
            Point point = DrawingView.this.point;
            indexProc(new Point(point.x, point.y), this._i, 0);
            if (DrawingView.this.curIndx < 0) {
                DrawingView.this.changesIndx.add(DrawingView.this.curIndx + 1, new Path());
                DrawingView.this.brushIndx.add(DrawingView.this.curIndx + 1, Integer.valueOf(DrawingView.this.brushSize));
                DrawingView.this.modeIndx.add(DrawingView.this.curIndx + 1, Integer.valueOf(DrawingView.this.TARGET));
                DrawingView.this.brushTypeIndx.add(DrawingView.this.curIndx + 1, Boolean.valueOf(DrawingView.this.isRectBrushEnable));
                DrawingView.currentIndxState(DrawingView.this);
                currIndx();
            } else if (!(((Integer) DrawingView.this.modeIndx.get(DrawingView.this.curIndx)).intValue() == DrawingView.this.TARGET && DrawingView.this.curIndx == DrawingView.this.modeIndx.size() - 1)) {
                DrawingView.this.changesIndx.add(DrawingView.this.curIndx + 1, new Path());
                DrawingView.this.brushIndx.add(DrawingView.this.curIndx + 1, Integer.valueOf(DrawingView.this.brushSize));
                DrawingView.this.modeIndx.add(DrawingView.this.curIndx + 1, Integer.valueOf(DrawingView.this.TARGET));
                DrawingView.this.brushTypeIndx.add(DrawingView.this.curIndx + 1, Boolean.valueOf(DrawingView.this.isRectBrushEnable));
                DrawingView.currentIndxState(DrawingView.this);
                currIndx();
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Time : ");
            sb.append(this._i);
            sb.append("  ");
            sb.append(DrawingView.this.curIndx);
            sb.append("   ");
            sb.append(DrawingView.this.changesIndx.size());
            Log.i("testing", sb.toString());
            return null;
        }

        public void onPostExecute(Bitmap bitmap) {
            DrawingView.this.progressDialog_.dismiss();
            DrawingView.this.invalidate();
            DrawingView.this.isMoved = false;
        }
    }
    private class AsyncTaskRunner extends AsyncTask<Void, Void, Bitmap> {

        int _i;
        Vector<Point> _pointV;

        public AsyncTaskRunner(int i) {
            this._i = i;
        }
        private void m11677a(Bitmap bitmap, Point point, int i, int i2) {
            int i3 = i;
            if (i3 != 0) {
                DrawingView drawingView = DrawingView.this;
                int i4 = drawingView.width;
                int i5 = drawingView.height;
                int[] iArr = new int[(i4 * i5)];
                bitmap.getPixels(iArr, 0, i4, 0, 0, i4, i5);
                LinkedList linkedList = new LinkedList();
                linkedList.add(point);
                while (linkedList.size() > 0) {
                    Point point2 = (Point) linkedList.poll();
                    DrawingView drawingView2 = DrawingView.this;
                    if (_Is_indexStat(iArr[drawingView2.getIndex(point2.x, point2.y, drawingView2.width)], i3)) {

                        Point point3 = new Point(point2.x + 1, point2.y);
                        while (true) {
                            int i6 = point2.x;
                            if (i6 <= 0) {
                                break;
                            }
                            DrawingView drawingView3 = DrawingView.this;
                            if (!_Is_indexStat(iArr[drawingView3.getIndex(i6, point2.y, drawingView3.width)], i3)) {
                                break;
                            }
                            DrawingView drawingView4 = DrawingView.this;
                            iArr[drawingView4.getIndex(point2.x, point2.y, drawingView4.width)] = i2;
                            this._pointV.add(new Point(point2.x, point2.y));
                            int i7 = point2.y;
                            if (i7 > 0) {
                                DrawingView drawingView5 = DrawingView.this;
                                if (_Is_indexStat(iArr[drawingView5.getIndex(point2.x, i7 - 1, drawingView5.width)], i3)) {
                                    linkedList.add(new Point(point2.x, point2.y - 1));
                                }
                            }
                            int i8 = point2.y;
                            DrawingView drawingView6 = DrawingView.this;
                            if (i8 < drawingView6.height && _Is_indexStat(iArr[drawingView6.getIndex(point2.x, i8 + 1, drawingView6.width)], i3)) {
                                linkedList.add(new Point(point2.x, point2.y + 1));
                            }
                            point2.x--;
                        }
                        int i9 = point2.y;
                        if (i9 > 0) {
                            DrawingView drawingView7 = DrawingView.this;
                            if (i9 < drawingView7.height) {
                                iArr[drawingView7.getIndex(point2.x, i9, drawingView7.width)] = i2;
                                this._pointV.add(new Point(point2.x, point2.y));
                            }
                        }
                        while (true) {
                            int i10 = point3.x;
                            DrawingView drawingView8 = DrawingView.this;
                            int i11 = drawingView8.width;
                            if (i10 >= i11 || !_Is_indexStat(iArr[drawingView8.getIndex(i10, point3.y, i11)], i3)) {
                                int i12 = point3.y;
                            } else {
                                DrawingView drawingView9 = DrawingView.this;
                                iArr[drawingView9.getIndex(point3.x, point3.y, drawingView9.width)] = i2;
                                this._pointV.add(new Point(point3.x, point3.y));
                                int i13 = point3.y;
                                if (i13 > 0) {
                                    DrawingView drawingView10 = DrawingView.this;
                                    if (_Is_indexStat(iArr[drawingView10.getIndex(point3.x, i13 - 1, drawingView10.width)], i3)) {
                                        linkedList.add(new Point(point3.x, point3.y - 1));
                                    }
                                }
                                int i14 = point3.y;
                                DrawingView drawingView11 = DrawingView.this;
                                if (i14 < drawingView11.height && _Is_indexStat(iArr[drawingView11.getIndex(point3.x, i14 + 1, drawingView11.width)], i3)) {
                                    linkedList.add(new Point(point3.x, point3.y + 1));
                                }
                                point3.x++;

                                int i122 = point3.y;
                                if (i122 > 0) {
                                    DrawingView drawingView12 = DrawingView.this;
                                    if (i122 < drawingView12.height) {
                                        iArr[drawingView12.getIndex(point3.x, i122, drawingView12.width)] = i2;
                                        this._pointV.add(new Point(point3.x, point3.y));
                                    }
                                }

                            }

                        }

                    }
                }
                DrawingView drawingView13 = DrawingView.this;
                int i15 = drawingView13.width;
                bitmap.setPixels(iArr, 0, i15, 0, 0, i15, drawingView13.height);
            }
        }

        public void onPreExecute() {
            super.onPreExecute();
            DrawingView drawingView = DrawingView.this;
            ProgressDialog progressDialog = new ProgressDialog(drawingView.getContext());
            drawingView.progressDialog_ = progressDialog;
            StringBuilder sb = new StringBuilder();
            sb.append(DrawingView.this.ctx.getResources().getString(R.string.processing));
            sb.append("...");
            progressDialog.setMessage(sb.toString());
            DrawingView.this.progressDialog_.setCancelable(false);
            DrawingView.this.progressDialog_.show();
        }

        private void _indexStat() {
            int size = DrawingView.this.changesIndx.size();
            StringBuilder sb = new StringBuilder();
            sb.append(" Curindx ");
            sb.append(DrawingView.this.curIndx);
            sb.append(" Size ");
            sb.append(size);
            String str = "testings";
            Log.i(str, sb.toString());
            int i = DrawingView.this.curIndx + 1;
            while (size > i) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(" indx ");
                sb2.append(i);
                Log.i(str, sb2.toString());
                DrawingView.this.changesIndx.remove(i);
                DrawingView.this.brushIndx.remove(i);
                DrawingView.this.modeIndx.remove(i);
                DrawingView.this.brushTypeIndx.remove(i);
                size = DrawingView.this.changesIndx.size();
            }
            if (DrawingView.this.undoRedoListener != null) {
                DrawingView.this.undoRedoListener.enableRedo(true, DrawingView.this.curIndx + 1);
                DrawingView.this.undoRedoListener.enableUndo(false, DrawingView.this.modeIndx.size() - (DrawingView.this.curIndx + 1));
            }
            if (DrawingView.this.actionListener != null) {
                DrawingView.this.actionListener.onActionCompleted(DrawingView.this.MODE);
            }
        }

        public boolean _Is_indexStat(int i, int i2) {
            boolean z = false;
            if (i != 0) {
                if (i2 == 0) {
                    return false;
                }
                if (i == i2) {
                    return true;
                }
                int abs = Math.abs(Color.red(i) - Color.red(i2));
                int abs2 = Math.abs(Color.green(i) - Color.green(i2));
                int abs3 = Math.abs(Color.blue(i) - Color.blue(i2));
                if (abs <= DrawingView.this.TOLERANCE && abs2 <= DrawingView.this.TOLERANCE && abs3 <= DrawingView.this.TOLERANCE) {
                    z = true;
                }
            }
            return z;
        }

        public Bitmap doInBackground(Void... voidArr) {
            if (this._i == 0) {
                return null;
            }
            this._pointV = new Vector<>();
            DrawingView drawingView = DrawingView.this;
            Bitmap bitmap = drawingView.bitmap2;
            drawingView.bitmap3 = bitmap.copy(bitmap.getConfig(), true);
            DrawingView drawingView2 = DrawingView.this;
            Bitmap bitmap2 = drawingView2.bitmap2;
            Point point = drawingView2.point;
            m11677a(bitmap2, new Point(point.x, point.y), this._i, 0);
            DrawingView.this.changesIndx.add(DrawingView.this.curIndx + 1, new Path());
            DrawingView.this.brushIndx.add(DrawingView.this.curIndx + 1, Integer.valueOf(DrawingView.this.brushSize));
            DrawingView.this.modeIndx.add(DrawingView.this.curIndx + 1, Integer.valueOf(DrawingView.this.TARGET));
            DrawingView.this.brushTypeIndx.add(DrawingView.this.curIndx + 1, Boolean.valueOf(DrawingView.this.isRectBrushEnable));
            DrawingView.currentIndxState(DrawingView.this);
            _indexStat();
            DrawingView.this.updateOnly = true;
            StringBuilder sb = new StringBuilder();
            sb.append("Time : ");
            sb.append(this._i);
            sb.append("  ");
            sb.append(DrawingView.this.curIndx);
            sb.append("   ");
            sb.append(DrawingView.this.changesIndx.size());
            Log.i("testing", sb.toString());
            return null;
        }

        public void onPostExecute(Bitmap bitmap) {
            DrawingView.this.progressDialog_.dismiss();
            DrawingView drawingView = DrawingView.this;
            drawingView.progressDialog_ = null;
            drawingView.invalidate();
            DrawingView.this.isMoved = false;
        }
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

        TransformInfo(DrawingView drawingView, AutoDetectionRemoval aVar) {
            this();
        }

        private TransformInfo() {
        }
    }
    public interface UndoRedoListener {
        void enableRedo(boolean z, int i);
        void enableUndo(boolean z, int i);
    }
    private class ScaleGestureListener extends SimpleOnScaleGestureListener {

        private float mPivotX;
        private float mPivotY;
        private Vector2D mPrevSpanVector;

        ScaleGestureListener(DrawingView drawingView, AutoDetectionRemoval aVar) {
            this();
        }
        public boolean onScale(View view, ScaleGestureDetectorBg var) {
            TransformInfo gVar = new TransformInfo(DrawingView.this, null);
            gVar.deltaScale = DrawingView.this.isTranslateEnabled ? var.getPreviousSpan() : 1.0f;
            float f = 0.0f;
            gVar.deltaAngle = DrawingView.this.isSelected ? Vector2D.GestureScaleProc(this.mPrevSpanVector, var.getCurrentSpanVector()) : 0.0f;
            gVar.deltaX = DrawingView.this.drawLasso ? var.getFocusX() - this.mPivotX : 0.0f;
            if (DrawingView.this.drawLasso) {
                f = var.getFocusY() - this.mPivotY;
            }
            gVar.deltaY = f;
            gVar.pivotX = this.mPivotX;
            gVar.pivotY = this.mPivotY;
            DrawingView drawingView = DrawingView.this;
            gVar.minimumScale = drawingView.minimumScale;
            gVar.maximumScale = drawingView.maximumScale;
            drawingView.move(view, gVar);
            return false;
        }

        public boolean onScaleEnd(View view, ScaleGestureDetectorBg var) {
            this.mPivotX = var.getFocusX();
            this.mPivotY = var.getFocusY();
            this.mPrevSpanVector.set(var.getCurrentSpanVector());
            return true;
        }

        private ScaleGestureListener() {
            this.mPrevSpanVector = new Vector2D();
        }
    }
    public void setUndoRedoListener(UndoRedoListener hVar) {
        this.undoRedoListener = hVar;
    }
    public void setActionListener(ActionListener bVar) {
        this.actionListener = bVar;
    }
    public DrawingView(Context context) {
        super(context);
        initPaint(context);
    }
    public DrawingView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initPaint(context);
    }
    private void initPaint(Context context) {
        this.mScaleGestureDetector = new ScaleGestureDetectorBg(new ScaleGestureListener(this, null));
        this.ctx = context;
        setFocusable(true);
        setFocusableInTouchMode(true);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.screenWidth = displayMetrics.widthPixels;
        this.brushSize = ImageUtils.dpToPx(getContext(), this.brushSize);
        this.brushSize1 = ImageUtils.dpToPx(getContext(), this.brushSize);
        this.targetBrushSize = ImageUtils.dpToPx(getContext(), 50);
        this.targetBrushSize1 = ImageUtils.dpToPx(getContext(), 50);
        this._paint1.setAlpha(0);
        this._paint1.setColor(0);
        this._paint1.setStyle(Style.STROKE);
        this._paint1.setStrokeJoin(Join.ROUND);
        this._paint1.setStrokeCap(Cap.ROUND);
        this._paint1.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
        this._paint1.setAntiAlias(true);
        this._paint1.setStrokeWidth(updatebrushsize(this.brushSize1, this.scale));
//        this.erPaint = new Paint();
        this.erPaint.setAntiAlias(true);
        this.erPaint.setColor(colorCodeVal);
        this.erPaint.setStyle(Style.STROKE);
        this.erPaint.setStrokeJoin(Join.MITER);
        this.erPaint.setStrokeWidth(updatebrushsize(this.erps, this.scale));
        this.erPaint1 = new Paint();
        this.erPaint1.setAntiAlias(true);
        this.erPaint1.setColor(colorCodeVal);
        this.erPaint1.setAntiAlias(true);
        this.erPaint1.setStyle(Style.STROKE);
        this.erPaint1.setStrokeJoin(Join.MITER);
        this.erPaint1.setStrokeWidth(updatebrushsize(this.erps, this.scale));
        this.erPaint1.setPathEffect(new DashPathEffect(new float[]{10.0f, 20.0f}, 0.0f));
    }
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.c2 != null) {
            if (!this.updateOnly && this.isTouched) {
                this._paint1 = getPaintByMode(this.MODE, this.brushSize, this.isRectBrushEnable);
                Path path = this.lPath;
                if (path != null) {
                    Log.d("TAGG","ondraw");
                    this.c2.drawPath(path, this._paint1);
                }
                this.isTouched = false;
            }
            if (this.MODE == this.TARGET) {
                this._paint = new Paint();
                this._paint.setColor(colorCodeVal);
                this.erPaint.setStrokeWidth(updatebrushsize(this.erps, this.scale));
                canvas.drawCircle(this.fX, this.fY, (float) (this.targetBrushSize / 2), this.erPaint);
                canvas.drawCircle(this.fX, this.fY + ((float) this.offset), updatebrushsize(ImageUtils.dpToPx(getContext(), 7), this.scale), this._paint);
                this._paint.setStrokeWidth(updatebrushsize(ImageUtils.dpToPx(getContext(), 1), this.scale));
                float f = this.fX;
                int i = this.targetBrushSize;
                float f2 = f - ((float) (i / 2));
                float f3 = this.fY;
                canvas.drawLine(f2, f3, f + ((float) (i / 2)), f3, this._paint);
                float f4 = this.fX;
                float f5 = this.fY;
                int i2 = this.targetBrushSize;
                canvas.drawLine(f4, f5 - ((float) (i2 / 2)), f4, f5 + ((float) (i2 / 2)), this._paint);
                this.isRotateEnabled = true;
            }
            if (this.MODE == this.LASSO) {
                this._paint = new Paint();
                this._paint.setColor(colorCodeVal);
                this.erPaint.setStrokeWidth(updatebrushsize(this.erps, this.scale));
                canvas.drawCircle(this.fX, this.fY, (float) (this.targetBrushSize / 2), this.erPaint);
                canvas.drawCircle(this.fX, this.fY + ((float) this.offset), updatebrushsize(ImageUtils.dpToPx(getContext(), 7), this.scale), this._paint);
                this._paint.setStrokeWidth(updatebrushsize(ImageUtils.dpToPx(getContext(), 1), this.scale));
                float f6 = this.fX;
                int i3 = this.targetBrushSize;
                float f7 = f6 - ((float) (i3 / 2));
                float f8 = this.fY;
                canvas.drawLine(f7, f8, f6 + ((float) (i3 / 2)), f8, this._paint);
                float f9 = this.fX;
                float f10 = this.fY;
                int i4 = this.targetBrushSize;
                canvas.drawLine(f9, f10 - ((float) (i4 / 2)), f9, f10 + ((float) (i4 / 2)), this._paint);
                if (!this.isRotateEnabled) {
                    this.erPaint1.setStrokeWidth(updatebrushsize(this.erps, this.scale));
                    canvas.drawPath(this.tpath, this.erPaint1);
                }
            }
            int i5 = this.MODE;
            if (i5 == this.ERASE || i5 == this.REDRAW) {
                this._paint = new Paint();
                this._paint.setColor(colorCodeVal);
                this.erPaint.setStrokeWidth(updatebrushsize(this.erps, this.scale));
                if (this.isRectBrushEnable) {
                    Log.d("TAGG","iff");

                    int i6 = this.brushSize / 2;
                    float f11 = this.fX;
                    float f12 = (float) i6;
                    float f13 = f11 - f12;
                    float f14 = this.fY;
                    canvas.drawRect(f13, f14 - f12, f12 + f11, f12 + f14, this.erPaint);
                } else {
                    Log.d("TAGG","else");

                    canvas.drawCircle(this.fX, this.fY, (float) (this.brushSize / 2), this.erPaint);
                }
                canvas.drawCircle(this.fX, this.fY + ((float) this.offset), updatebrushsize(ImageUtils.dpToPx(getContext(), 7), this.scale), this._paint);
            }
            this.updateOnly = false;
        }
    }
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (motionEvent.getPointerCount() != 1) {
            if (this.lPath != null) {
                if (this.bn_state) {
                    int i = this.MODE;
                    if (i == this.ERASE || i == this.REDRAW) {
                        int i2 = this.brushSize / 2;
                        if (this.isRectBrushEnable) {
                            Path path = this.lPath;
                            float f = this.fX;
                            float f2 = (float) i2;
                            float f3 = f - f2;
                            float f4 = this.fY;
                            path.addRect(f3, f4 - f2, f2 + f, f2 + f4, Direction.CW);
                        } else {
                            this.lPath.lineTo(this.fX, this.fY);
                        }
                        invalidate();
                        this.changesIndx.add(this.curIndx + 1, new Path(this.lPath));
                        this.brushIndx.add(this.curIndx + 1, Integer.valueOf(this.brushSize));
                        this.modeIndx.add(this.curIndx + 1, Integer.valueOf(this.MODE));
                        this.brushTypeIndx.add(this.curIndx + 1, Boolean.valueOf(this.isRectBrushEnable));
                        this.lPath.reset();
                        this.curIndx++;
                        clearNextChanges();
                        this.lPath = null;
                        this.bn_state = false;
                    }
                }
                if(this.lPath!=null)
                {
                    this.lPath.reset();
                }
                invalidate();
                this.lPath = null;
            }
            this.mScaleGestureDetector.onTouchEvent((View) view.getParent(), motionEvent);
            invalidate();
            updateShader(this.fX, this.fY, motionEvent.getRawX(), motionEvent.getRawY(), this.erPaint, false);
        } else {
            MotionEvent motionEvent2 = motionEvent;

            ActionListener bVar = this.actionListener;
            if (bVar != null) {
                bVar.onAction(motionEvent.getAction());
            }
            if (this.MODE == this.TARGET) {
                this.isRotateEnabled = false;
                this.fX = motionEvent.getX();
                this.fY = motionEvent.getY() - ((float) this.offset);
                if (action == 0) {
                    invalidate();
                } else if (action == 1) {
                    float f5 = this.fX;
                    if (f5 >= 0.0f && this.fY >= 0.0f && f5 < ((float) this.bitmap2.getWidth()) && this.fY < ((float) this.bitmap2.getHeight())) {
                        this.point = new Point((int) this.fX, (int) this.fY);
                        pc = this.bitmap2.getPixel((int) this.fX, (int) this.fY);
                        if (!this.isMoved) {
                            this.isMoved = true;
//                            new AsyncTaskRunner(pc).execute(new Void[0]);
                        }
                    }
                    invalidate();
                } else if (action == 2) {
                    invalidate();
                }
            }
            if (this.MODE == this.LASSO) {
                this.fX = motionEvent.getX();
                this.fY = motionEvent.getY() - ((float) this.offset);
                if (action == 0) {
                    this.isNewPath = true;
                    this.isRotateEnabled = false;
                    this.sX = this.fX;
                    this.sY = this.fY;
                    this.tpath = new Path();
                    this.tpath.moveTo(this.fX, this.fY);
                    invalidate();
                } else if (action == 1) {
                    this.tpath.lineTo(this.fX, this.fY);
                    this.tpath.lineTo(this.sX, this.sY);
                    this.isAutoRunning = true;
                    invalidate();
                    ActionListener bVar2 = this.actionListener;
                    if (bVar2 != null) {
                        bVar2.onActionCompleted(5);
                    }
                } else if (action != 2) {
                    return false;
                } else {
                    this.tpath.lineTo(this.fX, this.fY);
                    invalidate();
                }
            }
            int i3 = this.MODE;
            if (i3 == this.ERASE || i3 == this.REDRAW) {
                Log.d("ONTOUCH","ERASE");

                int i4 = this.brushSize / 2;
                this.fX = motionEvent.getX();
                this.fY = motionEvent.getY() - ((float) this.offset);
                Log.d("TAGGGH",fX +" "+fY + "  "+ screenWidth);

                this.isTouched = true;
                this.erPaint.setStrokeWidth(updatebrushsize(this.erps, this.scale));
                updateShader(this.fX, this.fY, motionEvent.getRawX(), motionEvent.getRawY(), this.erPaint, true);
                if (action == 0) {
                    Log.d("ONTOUCH","action down");

                    this._paint1.setStrokeWidth((float) this.brushSize);
                    this.lPath = new Path();
                    if (this.isRectBrushEnable) {
                        Log.d("ONTOUCH","isRectBrushEnable");

                        Path path2 = this.lPath;
                        float f6 = this.fX;
                        float f7 = (float) i4;
                        float f8 = f6 - f7;
                        float f9 = this.fY;
                        path2.addRect(f8, f9 - f7, f6 + f7, f7 + f9, Direction.CW);
                    } else {
                        this.lPath.moveTo(this.fX, this.fY);
                    }
                    invalidate();
                    return true;
                } else if (action == 1) {
                    Log.d("ONTOUCH","action up");

                    updateShader(this.fX, this.fY, motionEvent.getRawX(), motionEvent.getRawY(), this.erPaint, false);
                    Path path3 = this.lPath;
                    if (path3 != null) {
                        if (this.isRectBrushEnable) {
                            float f10 = this.fX;
                            float f11 = (float) i4;
                            float f12 = f10 - f11;
                            float f13 = this.fY;
                            path3.addRect(f12, f13 - f11, f11 + f10, f11 + f13, Direction.CW);
                        } else {
                            path3.lineTo(this.fX, this.fY);
                        }
                        invalidate();
                        this.changesIndx.add(this.curIndx + 1, new Path(this.lPath));
                        this.brushIndx.add(this.curIndx + 1, Integer.valueOf(this.brushSize));
                        this.modeIndx.add(this.curIndx + 1, Integer.valueOf(this.MODE));
                        this.brushTypeIndx.add(this.curIndx + 1, Boolean.valueOf(this.isRectBrushEnable));
                        this.lPath.reset();
                        this.curIndx++;
                        clearNextChanges();
                        this.lPath = null;
                    }
                } else if (action != 2) {
                    return false;
                } else {
                    if (this.lPath != null) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(" In Action Move ");
                        sb.append(this.fX);
                        sb.append(" ");
                        sb.append(this.fY);
                        Log.e("movetest", sb.toString());
                        if (this.isRectBrushEnable) {
                            Path path4 = this.lPath;
                            float f14 = this.fX;
                            float f15 = (float) i4;
                            float f16 = f14 - f15;
                            float f17 = this.fY;
                            path4.addRect(f16, f17 - f15, f14 + f15, f15 + f17, Direction.CW);
                        } else {
                            this.lPath.lineTo(this.fX, this.fY);
                        }
                        invalidate();
                        this.bn_state = true;
                        return true;
                    }
                }
            }
        }
        return true;
    }
    private void updateShader(float f, float f2, float f3, float f4, Paint paint, boolean isTouched) {
        if (this.shaderView != null) {
            Paint paint2 = new Paint();
            if (f4 - ((float) this.offset) < ((float) ImageUtils.dpToPx(this.ctx, 300))) {
                if (f3 < ((float) ImageUtils.dpToPx(this.ctx, 180))) {
                    this.drawOnScreenLeft = false;
                }
                if (f3 > ((float) (this.screenWidth - ImageUtils.dpToPx(this.ctx, 180)))) {
                    this.drawOnScreenLeft = true;
                }
            }
            Bitmap bitmap = this.bitmap2;
            TileMode tileMode = TileMode.CLAMP;
            BitmapShader bitmapShader = new BitmapShader(bitmap, tileMode, tileMode);
            paint2.setShader(bitmapShader);
            Matrix matrix = new Matrix();
            float f5 = this.scale;
            matrix.postScale(f5 * 1.5f, f5 * 1.5f, f, f2);
            if (this.drawOnScreenLeft) {
                matrix.postTranslate(-(f - ((float) ImageUtils.dpToPx(this.ctx, 75))), -(f2 - ((float) ImageUtils.dpToPx(this.ctx, 75))));
            } else {
                matrix.postTranslate(-(f - ((float) (this.screenWidth - ImageUtils.dpToPx(this.ctx, 75)))), -(f2 - ((float) ImageUtils.dpToPx(this.ctx, 75))));
            }
            bitmapShader.setLocalMatrix(matrix);
            this._paint1.setShader(bitmapShader);
            paint.setStrokeWidth(updatebrushsize(this.erps, 1.5f) / 1.5f);
            ShaderView shaderView = this.shaderView;
            double d = (double) (this.brushSize1 / 2);
            shaderView.mo10677a(paint2, paint, (int) (d * 1.5d), isTouched, this.drawOnScreenLeft, this.isRectBrushEnable);
        }
    }
    public void move(View view, TransformInfo gVar) {
        computeRenderOffset(view, gVar.pivotX, gVar.pivotY);
        adjustTranslation(view, gVar.deltaX, gVar.deltaY);
        float max = Math.max(gVar.minimumScale, Math.min(gVar.maximumScale, view.getScaleX() * gVar.deltaScale));
        view.setScaleX(max);
        view.setScaleY(max);
        updateOnScale(max);
        invalidate();
    }
    private static void adjustTranslation(View view, float f, float f2) {
        float[] fArr = {f, f2};
        view.getMatrix().mapVectors(fArr);
        view.setTranslationX(view.getTranslationX() + fArr[0]);
        view.setTranslationY(view.getTranslationY() + fArr[1]);
    }
    private static void computeRenderOffset(View view, float f, float f2) {
        if (view.getPivotX() != f || view.getPivotY() != f2) {
            float[] fArr = {0.0f, 0.0f};
            view.getMatrix().mapPoints(fArr);
            view.setPivotX(f);
            view.setPivotY(f2);
            float[] fArr2 = {0.0f, 0.0f};
            view.getMatrix().mapPoints(fArr2);
            float f3 = fArr2[0] - fArr[0];
            float f4 = fArr2[1] - fArr[1];
            view.setTranslationX(view.getTranslationX() - f3);
            view.setTranslationY(view.getTranslationY() - f4);
        }
    }
    public void setMODE(int i) {
        this.MODE = i;
        StringBuilder sb = new StringBuilder();
        sb.append("setMODE: ");
        sb.append(i);
        sb.toString();
        if (i != this.TARGET) {
            Bitmap bitmap = this.bitmap3;
            if (bitmap != null) {
                try {
                    bitmap.recycle();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
                this.bitmap3 = null;
            }
        }
        if (i != this.LASSO) {
            this.isRotateEnabled = true;
            this.isAutoRunning = false;
            Bitmap bitmap2 = this.bitmap4;
            if (bitmap2 != null) {
                try {
                    bitmap2.recycle();
                } catch (RuntimeException e2) {
                    e2.printStackTrace();
                }
                this.bitmap4 = null;
            }
        }
    }
    public Paint getPaintByMode(int i, int i2, boolean z) {
        this._paint1 = new Paint();
        this._paint1.setAlpha(0);
        if (z) {
            this._paint1.setStyle(Style.FILL_AND_STROKE);
            this._paint1.setStrokeJoin(Join.MITER);
            this._paint1.setStrokeCap(Cap.SQUARE);
        } else {
            this._paint1.setStyle(Style.STROKE);
            this._paint1.setStrokeJoin(Join.ROUND);
            this._paint1.setStrokeCap(Cap.ROUND);
            this._paint1.setStrokeWidth((float) i2);
        }
        this._paint1.setAntiAlias(true);
        if (i == this.ERASE) {
            this._paint1.setColor(0);
            this._paint1.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
        }
        if (i == this.REDRAW) {
            this._paint1.setColor(-1);
            this.patternBMPshader = EraserActivity.patternBMPshader;
            this._paint1.setShader(this.patternBMPshader);
        }
        return this._paint1;
    }
    public void setOffset(int i) {
        this.offset1 = i;
        this.offset = (int) updatebrushsize(ImageUtils.dpToPx(this.ctx, i), this.scale);
        this.updateOnly = true;
    }
    public int getOffset() {
        return this.offset1;
    }
    public void setRadius(int i) {
        this.brushSize1 = ImageUtils.dpToPx(getContext(), i);
        this.brushSize = (int) updatebrushsize(this.brushSize1, this.scale);
        this.updateOnly = true;
    }
    public void setThreshold(int i) {
        this.TOLERANCE = i;
        int i2 = this.curIndx;
        if (i2 >= 0) {
            int intValue = ((Integer) this.modeIndx.get(i2)).intValue();
            StringBuilder sb = new StringBuilder();
            sb.append(" Threshold ");
            sb.append(i);
            sb.append("  ");
            sb.append(intValue == this.TARGET);
            Log.i("testings", sb.toString());
        }
    }
    public void updateOnScale(float f) {
        StringBuilder sb = new StringBuilder();
        sb.append("Scale ");
        sb.append(f);
        sb.append("  Brushsize  ");
        sb.append(this.brushSize);
        Log.i("testings", sb.toString());
        this.scale = f;
        this.brushSize = (int) updatebrushsize(this.brushSize1, f);
        this.targetBrushSize = (int) updatebrushsize(this.targetBrushSize1, f);
        this.offset = (int) updatebrushsize(ImageUtils.dpToPx(this.ctx, this.offset1), f);
    }
    public Bitmap getFinalBitmap() {
        Bitmap bitmap = this.bitmap2;
        return bitmap.copy(bitmap.getConfig(), true);
    }
    public int getLastChangeMode() {
        int i = this.curIndx;
        if (i < 0) {
            return this.NONE;
        }
        return ((Integer) this.modeIndx.get(i)).intValue();
    }
    public void setShaderView(ShaderView shaderView) {
        this.shaderView = shaderView;
    }
    public void setImageBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            if (this.orgBit == null) {
                this.orgBit = bitmap.copy(bitmap.getConfig(), true);
            }
            this.width = bitmap.getWidth();
            this.height = bitmap.getHeight();
            this.bitmap2 = Bitmap.createBitmap(this.width, this.height, bitmap.getConfig());
            this.c2 = new Canvas();
            this.c2.setBitmap(this.bitmap2);
//            this.c2.drawBitmap(bitmap, 0.0f, 0.0f, null);
            enableTouchClear(this.isScaleEnabled);
            super.setImageBitmap(this.bitmap2);
        }
    }
    public void enableTouchClear(boolean z) {
        this.isScaleEnabled = z;
        if (z) {
            setOnTouchListener(this);
        } else {
            setOnTouchListener(null);
        }
    }
    public void enableInsideCut(boolean z) {
        this.insidCutEnable = z;
        if (this.isAutoRunning) {
            String str = "testings";
            Log.i(str, " draw lassso   on up ");
            if (this.isNewPath) {
                Bitmap bitmap = this.bitmap2;
                this.bitmap4 = bitmap.copy(bitmap.getConfig(), true);
                drawLassoPath(this.tpath, this.insidCutEnable);
                this.changesIndx.add(this.curIndx + 1, new Path(this.tpath));
                this.brushIndx.add(this.curIndx + 1, Integer.valueOf(this.brushSize));
                this.modeIndx.add(this.curIndx + 1, Integer.valueOf(this.MODE));
                this.brushTypeIndx.add(this.curIndx + 1, Boolean.valueOf(this.isRectBrushEnable));
                this.curIndx++;
                clearNextChanges();
                invalidate();
                this.isNewPath = false;
                return;
            }
            Log.i(str, " New PAth false ");
            setImageBitmap(this.bitmap4);
            drawLassoPath(this.tpath, this.insidCutEnable);
            return;
        }
        Toast.makeText(this.ctx, "enableInsideCut", Toast.LENGTH_SHORT).show();
    }
    public interface InterfaceBGI {
        void processD();
    }
    public float updatebrushsize(int i, float f) {
        return ((float) i) / f;
    }
    public int getIndex(int i, int i2, int i3) {
        return i2 == 0 ? i : i + (i3 * (i2 - 1));
    }
    public void updateThreshHold() {
        if (this.bitmap3 != null && !this.isMoved) {
            this.isMoved = true;
            new AsyncTaskRunner1(pc).execute(new Void[0]);
        }
    }
    public void clearNextChanges() {
        int size = this.changesIndx.size();
        StringBuilder sb = new StringBuilder();
        sb.append("ClearNextChange Curindx ");
        sb.append(this.curIndx);
        sb.append(" Size ");
        sb.append(size);
        String str = "testings";
        Log.i(str, sb.toString());
        int i = this.curIndx + 1;
        while (size > i) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(" indx ");
            sb2.append(i);
            Log.i(str, sb2.toString());
            this.changesIndx.remove(i);
            this.brushIndx.remove(i);
            this.modeIndx.remove(i);
            this.brushTypeIndx.remove(i);
            size = this.changesIndx.size();
        }
        UndoRedoListener hVar = this.undoRedoListener;
        if (hVar != null) {
            hVar.enableRedo(true, this.curIndx + 1);
            this.undoRedoListener.enableUndo(false, this.modeIndx.size() - (this.curIndx + 1));
        }
        ActionListener bVar = this.actionListener;
        if (bVar != null) {
            bVar.onActionCompleted(this.MODE);
        }
    }
    private void redrawCanvas() {
        for (int i = 0; i <= this.curIndx; i++) {
            if (((Integer) this.modeIndx.get(i)).intValue() == this.ERASE || ((Integer) this.modeIndx.get(i)).intValue() == this.REDRAW) {
                this.lPath = new Path((Path) this.changesIndx.get(i));
                this._paint1 = getPaintByMode(((Integer) this.modeIndx.get(i)).intValue(), ((Integer) this.brushIndx.get(i)).intValue(), ((Boolean) this.brushTypeIndx.get(i)).booleanValue());
                this.c2.drawPath(this.lPath, this._paint1);
                this.lPath.reset();
            }
        }
    }
    public void redoChange() {
        this.isAutoRunning = false;
        StringBuilder sb = new StringBuilder();
        sb.append(this.curIndx + 1 >= this.changesIndx.size());
        sb.append(" Curindx ");
        sb.append(this.curIndx);
        sb.append(" ");
        sb.append(this.changesIndx.size());
        Log.i("testings", sb.toString());
        if (this.curIndx + 1 < this.changesIndx.size()) {
            setImageBitmap(this.orgBit);
            this.curIndx++;
            redrawCanvas();
            UndoRedoListener hVar = this.undoRedoListener;
            if (hVar != null) {
                hVar.enableRedo(true, this.curIndx + 1);
                this.undoRedoListener.enableUndo(true, this.modeIndx.size() - (this.curIndx + 1));
            }
            if (this.curIndx + 1 >= this.changesIndx.size()) {
                UndoRedoListener hVar2 = this.undoRedoListener;
                if (hVar2 != null) {
                    hVar2.enableUndo(false, this.modeIndx.size() - (this.curIndx + 1));
                }
            }
        }
    }
    public void undoChange() {
        this.isAutoRunning = false;
        setImageBitmap(this.orgBit);
        StringBuilder sb = new StringBuilder();
        sb.append("Performing UNDO Curindx ");
        sb.append(this.curIndx);
        String str = "  ";
        sb.append(str);
        sb.append(this.changesIndx.size());
        String str2 = "testings";
        Log.i(str2, sb.toString());
        int i = this.curIndx;
        if (i >= 0) {
            this.curIndx = i - 1;
            redrawCanvas();
            StringBuilder sb2 = new StringBuilder();
            sb2.append(" Curindx ");
            sb2.append(this.curIndx);
            sb2.append(str);
            sb2.append(this.changesIndx.size());
            Log.i(str2, sb2.toString());
            UndoRedoListener hVar = this.undoRedoListener;
            if (hVar != null) {
                hVar.enableRedo(true, this.curIndx + 1);
                this.undoRedoListener.enableUndo(true, this.modeIndx.size() - (this.curIndx + 1));
            }
            int i2 = this.curIndx;
            if (i2 < 0) {
                UndoRedoListener hVar2 = this.undoRedoListener;
                if (hVar2 != null) {
                    hVar2.enableRedo(false, i2 + 1);
                }
            }
        }
    }
    public boolean isRectBrushEnable() {
        return this.isRectBrushEnable;
    }
    public boolean isTouchEnable() {
        return this.isScaleEnabled;
    }
    private void drawLassoPath(Path path, boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append(z);
        sb.append(" New PAth false ");
        sb.append(path.isEmpty());
        Log.i("testings", sb.toString());
        if (z) {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(0);
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            this.c2.drawPath(path, paint);
        } else {
            Bitmap bitmap = this.bitmap2;
            Bitmap copy = bitmap.copy(bitmap.getConfig(), true);
            new Canvas(copy).drawBitmap(this.bitmap2, 0.0f, 0.0f, null);
            this.c2.drawColor(this.NONE, Mode.CLEAR);
            Paint paint2 = new Paint();
            paint2.setAntiAlias(true);
            this.c2.drawPath(path, paint2);
            paint2.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            this.c2.drawBitmap(copy, 0.0f, 0.0f, paint2);
            try {
                copy.recycle();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        this.isRotateEnabled = true;
    }
    public void enableRectBrush(boolean z) {
        this.isRectBrushEnable = z;
        this.updateOnly = true;
    }
    static int currentIndxState(DrawingView drawingView) {
        int i = drawingView.curIndx;
        drawingView.curIndx = i + 1;
        return i;
    }
    public void ImageProcess_(Activity activity) {
        if (this.lPath == null) {
            this.lPath = new Path();
        }
        for (int i = 0; i < ScreenSize.screenVal(getContext()) * 2; i += 100) {
            float f = (float) i;
            this.lPath.moveTo(f, 0.0f);
            this.lPath.lineTo(f, (float) (ScreenSize.ScreenSizeVal(getContext()) * 2));
        }
        setMODE(this.ERASE);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });
        this.brushSize = ImageUtils.dpToPx(getContext(), 100);
        this.isRectBrushEnable = false;
        this.changesIndx.add(this.curIndx + 1, new Path(this.lPath));
        this.brushIndx.add(this.curIndx + 1, Integer.valueOf(this.brushSize));
        this.modeIndx.add(this.curIndx + 1, Integer.valueOf(this.MODE));
        this.brushTypeIndx.add(this.curIndx + 1, Boolean.valueOf(false));
        this._paint1 = getPaintByMode(((Integer) this.modeIndx.get(0)).intValue(), ((Integer) this.brushIndx.get(0)).intValue(), ((Boolean) this.brushTypeIndx.get(0)).booleanValue());
        this.c2.drawPath(this.lPath, this._paint1);
        this.lPath.reset();
        this.curIndx++;

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                clearNextChanges();
            }
        });
        this.brushSize = ImageUtils.dpToPx(getContext(), 18);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });
    }
    public void AutoDetectionProcessingImage(Activity activity, InterfaceBGI eVar) {
        ModelBitmapProcessing.AutoRemovalThread(getContext(), this.orgBit, (InterfaceAuto) new AutoDetectionRemoval(activity, eVar));
    }

}
