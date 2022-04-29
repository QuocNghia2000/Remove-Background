package com.example.samplestickerapp.stickermaker.erase.erase;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.samplestickerapp.stickermaker.erase.erase.MultiTouchListener.TouchCallbackListener;
import com.example.samplestickerapp.stickermaker.erase.erase.DrawingView.ActionListener;
import com.example.samplestickerapp.stickermaker.erase.erase.DrawingView.InterfaceBGI;
import com.example.samplestickerapp.stickermaker.erase.erase.DrawingView.UndoRedoListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EraserActivity extends AppCompatActivity implements OnClickListener {

    public static final String _pathV = "image_path";
    public static final int inV = 21211;
    private static final String MyPrefs = "MyPrefs";
    private static final int REQUEST_FEATHER_ACTIVITY = 224;
    public static final String _nm = EraserActivity.class.getSimpleName();
    public static Bitmap bgCircleBit = null;
    public static Bitmap bitmap = null;
    public static int curBgType = 1;
    public static boolean isTutOpen = false;
    public static int orgBitHeight;
    public static int orgBitWidth;
    public static Bitmap orgBitmap;
    public static BitmapShader patternBMPshader;
    private LinearLayout layout_offset_Seekbar;
    private LinearLayout layout_threshold_Seekbar;
    private RelativeLayout main_rel;
    private SeekBar offsetSeekbar;
    private SeekBar offsetSeekbar1;
    private SeekBar offsetSeekbar2;
    public RelativeLayout offsetSeekbarLayout;
    private RelativeLayout outSideCutLayout;
    private SeekBar RadiusSeekbar;
    public ImageButton btn_redo;
    public RelativeLayout rel_arrow_up;
    private RelativeLayout rel_below;
    public RelativeLayout rel_color;
    public RelativeLayout rel_desc;
    private RelativeLayout rel_down;
    public RelativeLayout rel_down_btns;
    private LinearLayout rel_instrs;
    private RelativeLayout rel_instrs1;
    private RelativeLayout rel_instrs2;
    public RelativeLayout fseek_container;
    public RelativeLayout rel_up_btns;
    private RelativeLayout rel_zoom;
    private ImageButton restore_btn;
    private RelativeLayout restore_btn_layout;
    public ImageView btn_doneImg;
    public Animation scale_anim;
    private ShaderView _shaderView;
    private boolean _bnVt = false;
    public Uri _uriValue;
    public ImageView tbg_img;
    SharedPreferences _sp;
    private SeekBar threshold_seekbar;
    Typeface typeFaceFont;
    private TextView txt_desc;
    private Animation animSlideDown;
    public TextView txt_redo;
    private Animation animSlideUp;
    public TextView txt_undo;
    private ImageButton btn_auto;
    public ImageButton btn_undo;
    private RelativeLayout auto_btn_rel;
    public int width;
    private Bitmap _bmp1 = null;
    private ImageButton zoom_back;
    private ImageButton btn_back;
    private RelativeLayout zoom_btn_rel;
    private View[] view_1 = new View[5];
    public ProgressDialog _progressDialog1;
    private View[] view_2 = new View[5];
    public ProgressDialog _progressDialog;
    private ImageButton btn_bg;
    private ImageButton btn_brush;
    private ImageButton btn_up;
    public DrawingView drawingView;
    private ImageView dv1;
    public ImageButton erase_btn;
    public RelativeLayout erase_btn_rel;
    private TextView header_text;
    public int height;
    private RelativeLayout inside_cut_lay;
    private ImageButton lasso_btn;
    private RelativeLayout lasso_btn_rel;
    private LinearLayout lay_lasso_cut;

    class AnimListener_1 implements AnimationListener {
        AnimListener_1() {
        }

        public void onAnimationEnd(Animation animation) {
            EraserActivity.this.rel_down_btns.setVisibility(View.INVISIBLE);
            if (EraserActivity.isTutOpen) {
                EraserActivity.this.rel_arrow_up.setVisibility(View.GONE);
                EraserActivity.this.rel_arrow_up.clearAnimation();
                EraserActivity.this.rel_color.setVisibility(View.VISIBLE);
                EraserActivity.this.rel_color.startAnimation(EraserActivity.this.scale_anim);
                EraserActivity.this.erase_btn.setEnabled(true);
                EraserActivity.this.erase_btn_rel.startAnimation(EraserActivity.this.scale_anim);
            }
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
        }
    }
    class AnimListener_2 implements AnimationListener {
        AnimListener_2() {
        }

        public void onAnimationEnd(Animation animation) {
            EraserActivity.this.rel_up_btns.setVisibility(View.GONE);
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
            EraserActivity.this.rel_down_btns.setVisibility(View.VISIBLE);
        }
    }
    class UndoRedoProcess implements UndoRedoListener {
        UndoRedoProcess() { }
        public void enableRedo(boolean z, int i) {
            if (EraserActivity.this._progressDialog1 != null && EraserActivity.this._progressDialog1.isShowing()) {
                EraserActivity.this._progressDialog1.dismiss();
            }
            if (z) {
                EraserActivity eraserActivity = EraserActivity.this;
                eraserActivity.settingBgDrawable(eraserActivity.txt_undo, i, (ImageView) EraserActivity.this.btn_undo, R.drawable.ic_undo, z);
                return;
            }
            EraserActivity eraserActivity2 = EraserActivity.this;
            eraserActivity2.settingBgDrawable(eraserActivity2.txt_undo, i, (ImageView) EraserActivity.this.btn_undo, R.drawable.ic_undo_1, z);
        }
        public void enableUndo(boolean z, int i) {
            if (EraserActivity.this._progressDialog != null && EraserActivity.this._progressDialog.isShowing()) {
                EraserActivity.this._progressDialog.dismiss();
            }
            if (z) {
                EraserActivity eraserActivity = EraserActivity.this;
                eraserActivity.settingBgDrawable(eraserActivity.txt_redo, i, (ImageView) EraserActivity.this.btn_redo, R.drawable.ic_redo, z);
                return;
            }
            EraserActivity eraserActivity2 = EraserActivity.this;
            eraserActivity2.settingBgDrawable(eraserActivity2.txt_redo, i, (ImageView) EraserActivity.this.btn_redo, R.drawable.ic_redo_1, z);
        }
    }
    class bitmap_proc implements Runnable {
        bitmap_proc() {
        }

        public void run() {
            Log.d("RUNNN","ok");
            ImageView j = EraserActivity.this.tbg_img;
            EraserActivity eraserActivity = EraserActivity.this;
            j.setImageBitmap(ImageUtils.tiltedBitmap(eraserActivity, R.drawable.tbg1, eraserActivity.width, EraserActivity.this.height));
            EraserActivity.bgCircleBit = ImageUtils.circularBitmap((Context) EraserActivity.this, R.drawable.tbg1);
            EraserActivity eraserActivity2 = EraserActivity.this;
            eraserActivity2.ImportImageViaUri(eraserActivity2.getIntent().getData());
        }
    }
    class SeekbarOffset implements OnSeekBarChangeListener {
        SeekbarOffset() {
        }

        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            if (EraserActivity.this.drawingView != null) {
                EraserActivity.this.drawingView.setOffset(i - 150);
                EraserActivity.this.drawingView.invalidate();
            }
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    }
    class SeekbarRadius implements OnSeekBarChangeListener {
        SeekbarRadius() {
        }

        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            if (EraserActivity.this.drawingView != null) {
                EraserActivity.this.drawingView.setRadius(i + 2);
                EraserActivity.this.drawingView.invalidate();
            }
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    }
    class SeekbarTrashold implements OnSeekBarChangeListener {
        SeekbarTrashold() {
        }

        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            if (EraserActivity.this.drawingView != null) {
                EraserActivity.this.drawingView.setThreshold(seekBar.getProgress() + 10);
                EraserActivity.this.drawingView.updateThreshHold();
            }
        }
    }
    class TouchClickProcess implements OnTouchListener {
        TouchClickProcess() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            return true;
        }
    }
    class DialogClick implements DialogInterface.OnClickListener {
        DialogClick() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            Editor edit = EraserActivity.this._sp.edit();
            edit.putBoolean("needForTut", false);
            edit.commit();
            Log.d("THHH","okee");
            EraserActivity.isTutOpen = false;
            EraserActivity.this.reImportImage();
            EraserActivity.this.btn_doneImg.setImageResource(R.drawable.ic_done);
            ((TextView) EraserActivity.this.findViewById(R.id.txt_done)).setText(EraserActivity.this.getString(R.string.done));
        }
    }
    class TouchCallback implements TouchCallbackListener {
        class ValClick implements OnClickListener {
            final Dialog _dialog;

            ValClick(Dialog dialog) {
                this._dialog = dialog;
            }

            public void onClick(View view) {
                Editor edit = EraserActivity.this._sp.edit();
                edit.putBoolean("needForTut", false);
                edit.commit();
                this._dialog.dismiss();
                EraserActivity.isTutOpen = false;
                EraserActivity.this.reImportImage();
                EraserActivity.this.btn_doneImg.setImageResource(R.drawable.ic_done);
                ((TextView) EraserActivity.this.findViewById(R.id.txt_done)).setText(EraserActivity.this.getString(R.string.done));
            }
        }

        TouchCallback() {
        }

        public void onTouchCallback(View view) {
        }

        public void onTouchUpCallback(View view) {
            EraserActivity.this.rel_down_btns.setVisibility(View.VISIBLE);
            EraserActivity.this.rel_up_btns.setVisibility(View.GONE);
            EraserActivity.this.rel_desc.setVisibility(View.GONE);
            EraserActivity.this.rel_desc.clearAnimation();
            Dialog dialog = new Dialog(EraserActivity.this, VERSION.SDK_INT >= 14 ? 16974126 : 16973835);
            dialog.getWindow().requestFeature(1);
            dialog.setContentView(R.layout.instructiondialog);
            dialog.setCancelable(false);
            dialog.findViewById(R.id.gotit).setOnClickListener(new ValClick(dialog));
            dialog.show();
        }
    }
    private void changeBG() {
        int i = curBgType;
        if (i == 1) {
            curBgType = 2;
            this.tbg_img.setImageBitmap(null);
            this.tbg_img.setImageBitmap(ImageUtils.tiltedBitmap(this, R.drawable.tbg1, this.width, this.height));
            this.btn_bg.setBackgroundResource(R.drawable.tbg2);
            bgCircleBit = ImageUtils.circularBitmap((Context) this, R.drawable.tbg1);
        } else if (i == 2) {
            curBgType = 3;
            this.tbg_img.setImageBitmap(null);
            this.tbg_img.setImageBitmap(ImageUtils.tiltedBitmap(this, R.drawable.tbg2, this.width, this.height));
            this.btn_bg.setBackgroundResource(R.drawable.tbg3);
            bgCircleBit = ImageUtils.circularBitmap((Context) this, R.drawable.tbg2);
        } else if (i == 3) {
            curBgType = 4;
            this.tbg_img.setImageBitmap(null);
            this.tbg_img.setImageBitmap(ImageUtils.tiltedBitmap(this, R.drawable.tbg3, this.width, this.height));
            this.btn_bg.setBackgroundResource(R.drawable.tbg4);
            bgCircleBit = ImageUtils.circularBitmap((Context) this, R.drawable.tbg3);
        } else if (i == 4) {
            curBgType = 5;
            this.tbg_img.setImageBitmap(null);
            this.tbg_img.setImageBitmap(ImageUtils.tiltedBitmap(this, R.drawable.tbg4, this.width, this.height));
            this.btn_bg.setBackgroundResource(R.drawable.tbg5);
            bgCircleBit = ImageUtils.circularBitmap((Context) this, R.drawable.tbg4);
        } else if (i == 5) {
            curBgType = 6;
            this.tbg_img.setImageBitmap(null);
            this.tbg_img.setImageBitmap(ImageUtils.tiltedBitmap(this, R.drawable.tbg5, this.width, this.height));
            this.btn_bg.setBackgroundResource(R.drawable.tbg);
            bgCircleBit = ImageUtils.circularBitmap((Context) this, R.drawable.tbg5);
        } else if (i == 6) {
            curBgType = 1;
            this.tbg_img.setImageBitmap(null);
            this.tbg_img.setImageBitmap(ImageUtils.tiltedBitmap(this, R.drawable.tbg, this.width, this.height));
            this.btn_bg.setBackgroundResource(R.drawable.tbg1);
            bgCircleBit = ImageUtils.circularBitmap((Context) this, R.drawable.tbg);
        }
    }
    private void initUI() {
        this.rel_arrow_up = (RelativeLayout) findViewById(R.id.rel_arrow_up);
        this.rel_color = (RelativeLayout) findViewById(R.id.rel_color);
        this.rel_below = (RelativeLayout) findViewById(R.id.rel_bw);
        this.rel_zoom = (RelativeLayout) findViewById(R.id.rel_zoom);
        this.rel_desc = (RelativeLayout) findViewById(R.id.rel_desc);
        this.rel_instrs = (LinearLayout) findViewById(R.id.rel_instr);
        this.rel_instrs1 = (RelativeLayout) findViewById(R.id.rel_instr1);
        this.rel_instrs2 = (RelativeLayout) findViewById(R.id.rel_instr2);
        this.offsetSeekbarLayout = (RelativeLayout) findViewById(R.id.offset_seekbar_lay);
        this.fseek_container = (RelativeLayout) findViewById(R.id.rel_seek_container);
        this.auto_btn_rel = (RelativeLayout) findViewById(R.id.auto_btn_rel);
        this.erase_btn_rel = (RelativeLayout) findViewById(R.id.erase_btn_rel);
        this.restore_btn_layout = (RelativeLayout) findViewById(R.id.restore_btn_rel);
        this.lasso_btn_rel = (RelativeLayout) findViewById(R.id.lasso_btn_rel);
        this.zoom_btn_rel = (RelativeLayout) findViewById(R.id.zoom_btn_rel);
        this.header_text = (TextView) findViewById(R.id.headertext);
        this.txt_desc = (TextView) findViewById(R.id.txt_desc);
        this.main_rel = (RelativeLayout) findViewById(R.id.main_rel);
        this.layout_threshold_Seekbar = (LinearLayout) findViewById(R.id.lay_threshold_seek);
        this.layout_offset_Seekbar = (LinearLayout) findViewById(R.id.lay_offset_seek);
        this.lay_lasso_cut = (LinearLayout) findViewById(R.id.lay_lasso_cut);
        this.inside_cut_lay = (RelativeLayout) findViewById(R.id.inside_cut_lay);
        this.outSideCutLayout = (RelativeLayout) findViewById(R.id.outside_cut_lay);
        this.btn_undo = (ImageButton) findViewById(R.id.btn_undo);
        this.btn_redo = (ImageButton) findViewById(R.id.btn_redo);
        this.btn_up = (ImageButton) findViewById(R.id.btn_up);
        this.rel_up_btns = (RelativeLayout) findViewById(R.id.rel_up_btns);
        this.rel_down_btns = (RelativeLayout) findViewById(R.id.rel_down_btns);
        this.rel_down = (RelativeLayout) findViewById(R.id.rel_down);
        this.btn_auto = (ImageButton) findViewById(R.id.auto_btn);
        this.erase_btn = (ImageButton) findViewById(R.id.erase_btn);
        this.restore_btn = (ImageButton) findViewById(R.id.restore_btn);
        this.lasso_btn = (ImageButton) findViewById(R.id.lasso_btn);
        this.zoom_back = (ImageButton) findViewById(R.id.zoom_btn);
        this.btn_back = (ImageButton) findViewById(R.id.btn_back);
        this.btn_doneImg = (ImageView) findViewById(R.id.btn_done);
        this.btn_bg = (ImageButton) findViewById(R.id.btn_bg);
        this.btn_brush = (ImageButton) findViewById(R.id.btn_brush);
        this.tbg_img = (ImageView) findViewById(R.id.tbg_img);
        this.txt_undo = (TextView) findViewById(R.id.txt_undo);
        this.txt_redo = (TextView) findViewById(R.id.txt_redo);
        this.btn_up.setOnClickListener(this);
        this.rel_down.setOnClickListener(this);
        this.btn_back.setOnClickListener(this);
        this.btn_undo.setOnClickListener(this);
        this.btn_redo.setOnClickListener(this);
        this.btn_undo.setEnabled(false);
        this.btn_redo.setEnabled(false);
        this.btn_doneImg.setOnClickListener(this);
        this.btn_bg.setOnClickListener(this);
        this.erase_btn.setOnClickListener(this);
        this.btn_auto.setOnClickListener(this);
        this.lasso_btn.setOnClickListener(this);
        this.restore_btn.setOnClickListener(this);
        this.zoom_back.setOnClickListener(this);
        this.inside_cut_lay.setOnClickListener(this);
        this.outSideCutLayout.setOnClickListener(this);
        this.view_1[0] = findViewById(R.id.auto_btn_lay);
        this.view_1[1] = findViewById(R.id.erase_btn_lay);
        this.view_1[2] = findViewById(R.id.restore_btn_lay);
        this.view_1[3] = findViewById(R.id.lasso_btn_lay);
        this.view_1[4] = findViewById(R.id.zoom_btn_lay);
        this.view_2[0] = findViewById(R.id.auto_btn_lay1);
        this.view_2[1] = findViewById(R.id.erase_btn_lay1);
        this.view_2[2] = findViewById(R.id.restore_btn_lay1);
        this.view_2[3] = findViewById(R.id.lasso_btn_lay1);
        this.view_2[4] = findViewById(R.id.zoom_btn_lay1);
        this.offsetSeekbar = (SeekBar) findViewById(R.id.offset_seekbar);
        this.offsetSeekbar1 = (SeekBar) findViewById(R.id.offset_seekbar1);
        this.offsetSeekbar2 = (SeekBar) findViewById(R.id.offset_seekbar2);
        SeekbarOffset gVar = new SeekbarOffset();
        this.offsetSeekbar.setOnSeekBarChangeListener(gVar);
        this.offsetSeekbar1.setOnSeekBarChangeListener(gVar);
        this.offsetSeekbar2.setOnSeekBarChangeListener(gVar);
        this.RadiusSeekbar = (SeekBar) findViewById(R.id.radius_seekbar);
        this.RadiusSeekbar.setOnSeekBarChangeListener(new SeekbarRadius());
        this.threshold_seekbar = (SeekBar) findViewById(R.id.threshold_seekbar);
        this.threshold_seekbar.setOnSeekBarChangeListener(new SeekbarTrashold());
        this.layout_offset_Seekbar.setOnTouchListener(new TouchClickProcess());

        findViewById(R.id.llTutorial);
    }
    public void reImportImage() {
        this.rel_arrow_up.clearAnimation();
        this.rel_color.clearAnimation();
        this.rel_below.clearAnimation();
        this.rel_zoom.clearAnimation();
        this.erase_btn_rel.clearAnimation();
        this.restore_btn_layout.clearAnimation();
        this.zoom_btn_rel.clearAnimation();
        this.rel_desc.clearAnimation();
        this.rel_arrow_up.setVisibility(View.GONE);
        this.rel_color.setVisibility(View.GONE);
        this.rel_below.setVisibility(View.GONE);
        this.rel_zoom.setVisibility(View.GONE);
        this.rel_desc.setVisibility(View.GONE);
        this.layout_offset_Seekbar.setVisibility(View.GONE);
        this.btn_auto.setEnabled(true);
        this.erase_btn.setEnabled(true);
        this.restore_btn.setEnabled(true);
        this.lasso_btn.setEnabled(true);
        this.zoom_back.setEnabled(true);
        this.rel_up_btns.setEnabled(true);
        this.btn_back.setEnabled(true);
        this.btn_doneImg.setEnabled(true);
        findViewById(R.id.erase_btn_lay1).setEnabled(true);
        findViewById(R.id.auto_btn_lay1).setEnabled(true);
        findViewById(R.id.lasso_btn_lay1).setEnabled(true);
        findViewById(R.id.restore_btn_lay1).setEnabled(true);
        findViewById(R.id.zoom_btn_lay1).setEnabled(true);
        this.txt_undo.setText(String.valueOf(0));
        this.txt_redo.setText(String.valueOf(0));
        this.btn_undo.setBackgroundResource(R.drawable.ic_undo_1);
        this.btn_redo.setBackgroundResource(R.drawable.ic_redo_1);
        showButtonsLayout(true);
        setSelected(0);
        this.main_rel.setOnTouchListener(null);
        ImportImageViaUri(getIntent().getData());
    }
    public Bitmap getGreenLayerBitmap(Bitmap bitmap) {
        Paint paint = new Paint();
        paint.setColor(-16711936);
        paint.setAlpha(80);
        int a = ImageUtils.dpToPx((Context) this, 42);
        Bitmap createBitmap = Bitmap.createBitmap(orgBitWidth + a + a, orgBitHeight + a + a, bitmap.getConfig());
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawColor(0);
        float f = (float) a;
        canvas.drawBitmap(orgBitmap, f, f, null);
        canvas.drawRect(f, f, (float) (orgBitWidth + a), (float) (orgBitHeight + a), paint);
        Bitmap createBitmap2 = Bitmap.createBitmap(orgBitWidth + a + a, orgBitHeight + a + a, bitmap.getConfig());
        Canvas canvas2 = new Canvas(createBitmap2);
        canvas2.drawColor(0);
        canvas2.drawBitmap(orgBitmap, f, f, null);
        Bitmap a2 = ImageUtils.resizeBitmap(createBitmap2, this.width, this.height);
        Shader.TileMode tileMode = Shader.TileMode.REPEAT;
        patternBMPshader = new BitmapShader(a2, tileMode, tileMode);
        return ImageUtils.resizeBitmap(createBitmap, this.width, this.height);
    }
    private void settingImageBitmap() {
        this.drawingView = new DrawingView(this);
        this.dv1 = new ImageView(this);
        this.drawingView.setImageBitmap(this._bmp1);
        this.dv1.setImageBitmap(getGreenLayerBitmap(this._bmp1));
        this.offsetSeekbar.setProgress(225);
        this.RadiusSeekbar.setProgress(18);
        this.threshold_seekbar.setProgress(20);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.main_rel_parent);
        this._shaderView = new ShaderView(this);
        this._shaderView.setLayoutParams(new LayoutParams(-1, -1));
        relativeLayout.addView(this._shaderView);
        this.drawingView.setShaderView(this._shaderView);
        this.main_rel.setScaleX(1.0f);
        this.main_rel.setScaleY(1.0f);
        this.main_rel.setTranslationX(0.0f);
        this.main_rel.setTranslationY(0.0f);
        this.main_rel.removeAllViews();
        this.main_rel.addView(this.dv1);
        this.main_rel.addView(this.drawingView);
        this.drawingView.invalidate();
        this.dv1.setVisibility(View.GONE);
        this.drawingView.setUndoRedoListener(new UndoRedoProcess());
        this._bmp1.recycle();
        if (isTutOpen) {
            StringBuilder sb = new StringBuilder();
            sb.append("setImageBitmap: isTutOpen=");
            sb.append(isTutOpen);
            sb.toString();
            this.drawingView.setActionListener(new ActionListener() {
                @Override
                public void onAction(final int i) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("onAction: ");
                    sb.append(i);
                    sb.toString();
                    EraserActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            _seekVisi(i);
                        }
                    });
                }

                @Override
                public void onActionCompleted(int i) {
                    EraserActivity.this.modeChangingLayout(i);
                }

                public void _seekVisi(int i) {
                    if (i == 0) {
                        EraserActivity.this.fseek_container.setVisibility(View.GONE);
                    }
                    if (i == 1) {
                        EraserActivity.this.fseek_container.setVisibility(View.VISIBLE);
                    }
                }
            });
            return;
        }
        this.drawingView.setActionListener(new ActionListener() {
            @Override
            public void onAction(final int i) {
                StringBuilder sb = new StringBuilder();
                sb.append("onAction: ");
                sb.append(i);
                sb.toString();
                EraserActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        _seekVisi2(i);
                    }
                });
            }

            @Override
            public void onActionCompleted(final int i) {
                StringBuilder sb = new StringBuilder();
                sb.append("onActionCompleted: ");
                sb.append(i);
                sb.toString();
                EraserActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        _seekVisi1(i);
                    }
                });
            }
            public void _seekVisi2(int i) {
                if (i == 0) {
                    EraserActivity.this.fseek_container.setVisibility(View.GONE);
                }
                if (i == 1) {
                    EraserActivity.this.fseek_container.setVisibility(View.VISIBLE);
                }
            }
            public void _seekVisi1(int i) {
                if (i == 5) {
                    EraserActivity.this.offsetSeekbarLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
    }
    public void onClick(View view) {
        if (this.drawingView != null || view.getId() == R.id.btn_back) {
            int id = view.getId();
            if (id == R.id.btn_back || id == R.id.txt_back) {
                onBackPressed();
            } else if (id == R.id.btn_done || id == R.id.txt_done) {
                if (isTutOpen) {
                    new Builder(this, VERSION.SDK_INT >= 14 ? 16974126 : 16973835).setTitle(getResources().getString(R.string.tut_ext_title)).setMessage(getResources().getString(R.string.tut_ext_msg)).setPositiveButton(getResources().getString(R.string.skip), new DialogClick()).setNegativeButton(getResources().getString(R.string.cont), null).create().show();
                    return;
                }
                bitmap = this.drawingView.getFinalBitmap();
                if (bitmap != null) {
                    try {
                        int a = ImageUtils.dpToPx((Context) this, 42);
                        bitmap = ImageUtils.resizeBitmap(bitmap, orgBitWidth + a + a, orgBitHeight + a + a);
                        int i = a + a;
                        bitmap = Bitmap.createBitmap(bitmap, a, a, bitmap.getWidth() - i, bitmap.getHeight() - i);
                        bitmap = Bitmap.createScaledBitmap(bitmap, orgBitWidth, orgBitHeight, true);
                        bitmap = ImageUtils.maskBitmap(orgBitmap, bitmap);
                        if (bitmap == null) {
                            Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show();
                        } else {
                            BitmapListData.m11922b().setListValue(bitmap.copy(bitmap.getConfig(), true));
                            startActivityForResult(new Intent(this, AdvanceEditActivity.class),1111);
                        }
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }
                } else {
                    finish();
                }
            } else if (id == R.id.outside_cut_lay) {
                this.offsetSeekbarLayout.setVisibility(View.VISIBLE);
                this.drawingView.enableInsideCut(false);
                this.inside_cut_lay.clearAnimation();
                this.outSideCutLayout.clearAnimation();
            } else if (id == R.id.inside_cut_lay) {
                this.offsetSeekbarLayout.setVisibility(View.VISIBLE);
                this.drawingView.enableInsideCut(true);
                this.inside_cut_lay.clearAnimation();
                this.outSideCutLayout.clearAnimation();
            } else if (id == R.id.btn_undo) {
                this._progressDialog1.show();
                this.drawingView.undoChange();
            } else if (id == R.id.btn_bg) {
                changeBG();
            } else if (id == R.id.btn_up) {
                showButtonsLayout(true);
            } else if (id == R.id.btn_brush) {
                DrawingView drawingView = this.drawingView;
                if (drawingView != null) {
                    if (drawingView.isRectBrushEnable()) {
                        this.drawingView.enableRectBrush(false);
                        this.drawingView.invalidate();
                        this.btn_brush.setBackgroundResource(R.drawable.ic_square);
                        return;
                    }
                    this.drawingView.enableRectBrush(true);
                    this.btn_brush.setBackgroundResource(R.drawable.ic_circle);
                    this.drawingView.invalidate();
                }
            } else if (id == R.id.btn_redo) {
                this._progressDialog.show();
                this.drawingView.redoChange();
            } else if (id == R.id.auto_btn_lay1 || id == R.id.auto_btn_rel || id == R.id.auto_btn) {
                setSelected(R.id.auto_btn_lay);
                this.drawingView.enableTouchClear(true);
                this.main_rel.setOnTouchListener(null);
                this.drawingView.setMODE(2);
                this.drawingView.invalidate();
                if (view.getId() != R.id.auto_btn_lay1) {
                    showButtonsLayout(false);
                }
                if (isTutOpen) {
                    this.txt_desc.setText(getResources().getString(R.string.targate_drag));
                    this.rel_desc.setVisibility(View.VISIBLE);
                    this.rel_desc.startAnimation(this.scale_anim);
                    this.rel_instrs.setVisibility(View.GONE);
                    this.auto_btn_rel.clearAnimation();
                }
            } else if (id == R.id.lasso_btn_lay1 || id == R.id.lasso_btn_rel || id == R.id.lasso_btn) {
                this.offsetSeekbarLayout.setVisibility(View.VISIBLE);
                setSelected(R.id.lasso_btn_lay);
                this.drawingView.enableTouchClear(true);
                this.main_rel.setOnTouchListener(null);
                this.drawingView.setMODE(3);
                this.drawingView.invalidate();
                if (view.getId() != R.id.lasso_btn_lay1) {
                    showButtonsLayout(false);
                }
                if (isTutOpen) {
                    this.txt_desc.setText(getResources().getString(R.string.draw_lasso));
                    this.rel_desc.setVisibility(View.VISIBLE);
                    this.rel_desc.startAnimation(this.scale_anim);
                    this.lasso_btn_rel.clearAnimation();
                }
            } else if (id == R.id.erase_btn_lay1 || id == R.id.erase_btn_rel || id == R.id.erase_btn) {
                setSelected(R.id.erase_btn_lay);
                this.drawingView.enableTouchClear(true);
                this.main_rel.setOnTouchListener(null);
                this.drawingView.setMODE(1);
                this.drawingView.invalidate();
                if (view.getId() != R.id.erase_btn_lay1) {
                    showButtonsLayout(false);
                }
                if (isTutOpen) {
                    this.rel_color.setVisibility(View.GONE);
                    this.rel_color.clearAnimation();
                    this.txt_desc.setText(getResources().getString(R.string.drag_color));
                    this.rel_desc.setVisibility(View.VISIBLE);
                    this.rel_desc.startAnimation(this.scale_anim);
                    this.erase_btn_rel.clearAnimation();
                }
            } else if (id == R.id.restore_btn_lay1 || id == R.id.restore_btn_rel || id == R.id.restore_btn) {
                setSelected(R.id.restore_btn_lay);
                this.drawingView.enableTouchClear(true);
                this.main_rel.setOnTouchListener(null);
                this.drawingView.setMODE(4);
                this.drawingView.invalidate();
                if (view.getId() != R.id.restore_btn_lay1) {
                    showButtonsLayout(false);
                }
                if (isTutOpen) {
                    this.rel_below.setVisibility(View.GONE);
                    this.rel_below.clearAnimation();
                    this.txt_desc.setText(getResources().getString(R.string.drag_bw));
                    this.rel_desc.setVisibility(View.VISIBLE);
                    this.rel_desc.startAnimation(this.scale_anim);
                    this.restore_btn_layout.clearAnimation();
                }
            } else if (id == R.id.zoom_btn_lay1 || id == R.id.zoom_btn_rel || id == R.id.zoom_btn) {
                this.drawingView.enableTouchClear(false);
                MultiTouchListener c0Var = new MultiTouchListener();
                this.main_rel.setOnTouchListener(c0Var);
                setSelected(R.id.zoom_btn_lay);
                this.drawingView.setMODE(0);
                this.drawingView.invalidate();
                if (view.getId() != R.id.zoom_btn_lay1) {
                    showButtonsLayout(false);
                }
                if (isTutOpen) {
                    this.rel_zoom.setVisibility(View.GONE);
                    this.rel_zoom.clearAnimation();
                    this.txt_desc.setText(getResources().getString(R.string.zoom_pinch));
                    this.rel_desc.setVisibility(View.VISIBLE);
                    this.rel_desc.startAnimation(this.scale_anim);
                    this.zoom_btn_rel.clearAnimation();
                    c0Var.setOnTouchCallbackListener((TouchCallbackListener) new TouchCallback());
                }
            } else {
                if (id == R.id.rel_down) {
                    showButtonsLayout(false);
                }
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.import_img_warning), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1111) {
            if (AdvanceEditActivity.isConfirm) {
                finish();
            }
        }
    }



    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_eraser);
        this.animSlideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        this.animSlideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        this.scale_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_anim);
        this._progressDialog1 = new ProgressDialog(this);
        ProgressDialog progressDialog = this._progressDialog1;
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.undoing));
        String str = " ...";
        sb.append(str);
        progressDialog.setMessage(sb.toString());
        this._progressDialog1.setCancelable(false);
        this._progressDialog = new ProgressDialog(this);
        ProgressDialog progressDialog2 = this._progressDialog;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(getString(R.string.redoing));
        sb2.append(str);
        progressDialog2.setMessage(sb2.toString());
        this._progressDialog.setCancelable(false);
        initUI();
        ((TextView) findViewById(R.id.headertext)).setTypeface(this.typeFaceFont);
        ((TextView) findViewById(R.id.txt_offset)).setTypeface(this.typeFaceFont);
        ((TextView) findViewById(R.id.txt_offset1)).setTypeface(this.typeFaceFont);
        ((TextView) findViewById(R.id.txt_offset2)).setTypeface(this.typeFaceFont);
        ((TextView) findViewById(R.id.txt_radius)).setTypeface(this.typeFaceFont);
        ((TextView) findViewById(R.id.txt_threshold)).setTypeface(this.typeFaceFont);
        ((TextView) findViewById(R.id.txt_inside)).setTypeface(this.typeFaceFont);
        ((TextView) findViewById(R.id.txt_outside)).setTypeface(this.typeFaceFont);
        ((TextView) findViewById(R.id.txt_erase)).setTypeface(this.typeFaceFont);
        ((TextView) findViewById(R.id.txt_auto)).setTypeface(this.typeFaceFont);
        ((TextView) findViewById(R.id.txt_lasso)).setTypeface(this.typeFaceFont);
        ((TextView) findViewById(R.id.txt_restore)).setTypeface(this.typeFaceFont);
        ((TextView) findViewById(R.id.txt_zoom)).setTypeface(this.typeFaceFont);
        ((TextView) findViewById(R.id.txt_erase1)).setTypeface(this.typeFaceFont);
        ((TextView) findViewById(R.id.txt_auto1)).setTypeface(this.typeFaceFont);
        ((TextView) findViewById(R.id.txt_lasso1)).setTypeface(this.typeFaceFont);
        ((TextView) findViewById(R.id.txt_restore1)).setTypeface(this.typeFaceFont);
        ((TextView) findViewById(R.id.txt_zoom1)).setTypeface(this.typeFaceFont);
        this._sp = getSharedPreferences(MyPrefs, 0);
        isTutOpen = false;
        AdvanceEditActivity.isConfirm=false;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int i = displayMetrics.heightPixels;
        this.width = displayMetrics.widthPixels;
        this.height = i - ImageUtils.dpToPx((Context) this, 120);
        curBgType = 2;
        this.main_rel.postDelayed(new bitmap_proc(), 1000);
        if (isTutOpen) {
            this.btn_doneImg.setImageResource(R.drawable.ic_skip_next_black_24dp);
            ((TextView) findViewById(R.id.txt_done)).setText(getString(R.string.skip));
            this.rel_up_btns.setVisibility(View.GONE);
            this.rel_down_btns.setVisibility(View.VISIBLE);
            this.rel_arrow_up.setVisibility(View.VISIBLE);
            this.rel_arrow_up.startAnimation(this.scale_anim);
            this.btn_auto.setEnabled(false);
            this.erase_btn.setEnabled(false);
            this.restore_btn.setEnabled(false);
            this.lasso_btn.setEnabled(false);
            this.zoom_back.setEnabled(false);
            this.rel_up_btns.setEnabled(false);
            this.btn_back.setEnabled(false);
            findViewById(R.id.erase_btn_lay1).setEnabled(false);
            findViewById(R.id.auto_btn_lay1).setEnabled(false);
            findViewById(R.id.lasso_btn_lay1).setEnabled(false);
            findViewById(R.id.restore_btn_lay1).setEnabled(false);
            findViewById(R.id.zoom_btn_lay1).setEnabled(false);
        }
    }
    public void onDestroy() {
        if (!isFinishing()) {
            DrawingView drawingView = this.drawingView;
            if (drawingView != null) {
                ProgressDialog progressDialog = drawingView.progressDialog_;
                if (progressDialog != null && progressDialog.isShowing()) {
                    this.drawingView.progressDialog_.dismiss();
                }
            }
        }
        super.onDestroy();
    }
    public final class DialogDrawing_ implements InterfaceBGI {

        private final EraserActivity eraserActivity;
        private final ProgressDialog progressDialog;

        public DialogDrawing_(EraserActivity eraserActivity, ProgressDialog progressDialog) {
            this.eraserActivity = eraserActivity;
            this.progressDialog = progressDialog;
        }

        public final void processD() {
            this.eraserActivity.DialogProcessingD(this.progressDialog);
        }
    }
    public void DialogProcessingD(ProgressDialog progressDialog) {
        setSelected(R.id.restore_btn_lay);
        this.drawingView.enableTouchClear(true);
        this.main_rel.setOnTouchListener(null);
        this.drawingView.setMODE(4);
        this.drawingView.invalidate();
        try {
            if (!isFinishing() && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
    public static class DialogDismiss_ implements DialogInterface.OnClickListener {
        public static DialogDismiss_ dialog_dismiss = new DialogDismiss_();

        private DialogDismiss_() {
        }

        public final void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    }
    public void ImportImageViaUri(Uri uri) {
        final ProgressDialog progressDialog;
        this._uriValue = uri;
        StringBuilder sb = new StringBuilder();
        sb.append("import image uri : ");
        sb.append(this._uriValue);
        Log.i("texting", sb.toString());
        this._bnVt = false;
        if (!isFinishing()) {
            progressDialog = ProgressDialog.show(this, "", getResources().getString(R.string.importing_image), true);
            progressDialog.setCancelable(false);
            progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    dialogProcessAuto(dialog);
                }
            });
        } else {
            progressDialog = null;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadingImageBitmap(progressDialog);
            }
        }).start();
    }
    public void settingBgDrawable(TextView textView, int i, ImageView imageView, int i2, boolean z) {
        setBgDrawable tVar = new setBgDrawable(imageView, i2, z, textView, i);
        runOnUiThread(tVar);
    }
    public final class setBgDrawable implements Runnable {

        private final ImageView imageView;
        private final int i;
        private final boolean state;
        private final TextView tv;
        private final int j;

        public setBgDrawable(ImageView imageView, int i, boolean z, TextView textView, int i2) {
            this.imageView = imageView;
            this.i = i;
            this.state = z;
            this.tv = textView;
            this.j = i2;
        }

        public final void run() {
            setVal(this.imageView, this.i, this.state, this.tv, this.j);
        }


        void setVal(ImageView imageView, int i, boolean z, TextView textView, int i2) {
            imageView.setBackgroundResource(i);
            imageView.setEnabled(z);
            textView.setText(String.valueOf(i2));
        }

    }
    public void modeChangingLayout(final int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("actionCompleted: ");
        sb.append(i);
        sb.toString();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                modeChangeAction(i);
            }
        });
    }
    public void modeChangeAction(int i) {
        if (i == 2) {
            this.rel_desc.setVisibility(View.GONE);
            this.rel_desc.clearAnimation();
            this.lasso_btn_rel.startAnimation(this.scale_anim);
            this.lasso_btn.setEnabled(true);
            this.btn_auto.setEnabled(false);
        }
        if (i == 1) {
            this.rel_desc.setVisibility(View.GONE);
            this.rel_desc.clearAnimation();
            this.rel_below.setVisibility(View.VISIBLE);
            this.rel_below.startAnimation(this.scale_anim);
            this.restore_btn_layout.startAnimation(this.scale_anim);
            this.restore_btn.setEnabled(true);
            this.erase_btn.setEnabled(false);
        }
        if (i == 4) {
            this.rel_desc.setVisibility(View.GONE);
            this.rel_desc.clearAnimation();
            this.rel_zoom.setVisibility(View.VISIBLE);
            this.rel_zoom.startAnimation(this.scale_anim);
            this.zoom_btn_rel.startAnimation(this.scale_anim);
            this.zoom_back.setEnabled(true);
            this.restore_btn.setEnabled(false);
        }
        if (i == 3) {
            this.rel_instrs.setVisibility(View.GONE);
            this.rel_instrs1.clearAnimation();
            this.rel_instrs2.clearAnimation();
            this.outSideCutLayout.clearAnimation();
            this.inside_cut_lay.clearAnimation();
            this.rel_color.setVisibility(View.VISIBLE);
            this.rel_color.startAnimation(this.scale_anim);
            this.erase_btn_rel.startAnimation(this.scale_anim);
            this.erase_btn.setEnabled(true);
            this.lasso_btn.setEnabled(false);
        }
        if (i == 5) {
            this.rel_desc.setVisibility(View.GONE);
            this.rel_desc.clearAnimation();
            this.rel_instrs.setVisibility(View.VISIBLE);
            this.rel_instrs1.startAnimation(this.scale_anim);
            this.rel_instrs2.startAnimation(this.scale_anim);
            this.outSideCutLayout.startAnimation(this.scale_anim);
            this.inside_cut_lay.startAnimation(this.scale_anim);
            this.offsetSeekbarLayout.setVisibility(View.GONE);
            this.rel_zoom.setVisibility(View.GONE);
            this.zoom_back.clearAnimation();
        }
    }
    public void setSelected(int i) {
        int i2 = 0;
        while (true) {
            View[] viewArr = this.view_1;
            if (i2 >= viewArr.length) {
                break;
            }
            if (viewArr[i2].getId() == i) {
                this.view_1[i2].setBackgroundResource(R.drawable.crop_buttons1);
                this.view_2[i2].setBackgroundResource(R.drawable.crop_buttons1);
            } else {
                this.view_1[i2].setBackgroundResource(R.drawable.crop_buttons);
                this.view_2[i2].setBackgroundColor(0);
            }
            i2++;
        }
        if (i == R.id.erase_btn_lay) {
            this.layout_offset_Seekbar.setVisibility(View.VISIBLE);
            this.layout_threshold_Seekbar.setVisibility(View.GONE);
            this.lay_lasso_cut.setVisibility(View.GONE);
            this.header_text.setText(getResources().getString(R.string.erase));
        }
        if (i == R.id.auto_btn_lay) {
            this.offsetSeekbar1.setProgress(this.drawingView.getOffset() + 150);
            this.layout_offset_Seekbar.setVisibility(View.GONE);
            this.layout_threshold_Seekbar.setVisibility(View.VISIBLE);
            this.lay_lasso_cut.setVisibility(View.GONE);
            this.header_text.setText(getResources().getString(R.string.auto));
        }
        if (i == R.id.lasso_btn_lay) {
            this.offsetSeekbar2.setProgress(this.drawingView.getOffset() + 150);
            this.layout_offset_Seekbar.setVisibility(View.GONE);
            this.layout_threshold_Seekbar.setVisibility(View.GONE);
            this.lay_lasso_cut.setVisibility(View.VISIBLE);
            this.header_text.setText(getResources().getString(R.string.lasso));
        }
        if (i == R.id.restore_btn_lay) {
            this.layout_offset_Seekbar.setVisibility(View.VISIBLE);
            this.layout_threshold_Seekbar.setVisibility(View.GONE);
            this.lay_lasso_cut.setVisibility(View.GONE);
            this.header_text.setText(getResources().getString(R.string.restore));
        }
        if (i == R.id.zoom_btn_lay) {
            this.layout_offset_Seekbar.setVisibility(View.GONE);
            this.layout_threshold_Seekbar.setVisibility(View.GONE);
            this.lay_lasso_cut.setVisibility(View.GONE);
            this.header_text.setText(getResources().getString(R.string.zoom));
        }
        if (i == R.id.restore_btn_lay) {
            this.dv1.setVisibility(View.VISIBLE);
        } else {
            this.dv1.setVisibility(View.GONE);
        }
        if (i != R.id.zoom_btn_lay) {
            this.drawingView.updateOnScale(this.main_rel.getScaleX());
        }
    }
    private void showButtonsLayout(boolean z) {
        if (z) {
            if (this.rel_up_btns.getVisibility() != View.VISIBLE) {
                this.rel_up_btns.setVisibility(View.VISIBLE);
                this.rel_up_btns.startAnimation(this.animSlideUp);
                this.animSlideUp.setAnimationListener(new AnimListener_1());
            }
        } else if (!isTutOpen && this.rel_up_btns.getVisibility() == View.VISIBLE) {
            this.rel_up_btns.startAnimation(this.animSlideDown);
            this.animSlideDown.setAnimationListener(new AnimListener_2());
        }
    }
    public void dialogProcessAuto(DialogInterface dialogInterface) {
        if (this._bnVt) {
            Toast.makeText(this, getResources().getString(R.string.import_error), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            settingImageBitmap();
        }
        if (isTutOpen) {
            this.drawingView.enableTouchClear(false);
            this.drawingView.setMODE(1);
            this.drawingView.invalidate();
            new Builder(this, VERSION.SDK_INT >= 14 ? 16974126 : 16973835).setTitle(getResources().getString(R.string.tut_title)).setMessage(getResources().getString(R.string.tut_msg)).setCancelable(false).setPositiveButton(getResources().getString(R.string.ok), DialogDismiss_.dialog_dismiss).create().show();
        } else {
            ProgressDialog progressDialog = new ProgressDialog(this);
            StringBuilder sb = new StringBuilder();
            sb.append(getString(R.string.removing));
            sb.append(" ...");
            progressDialog.setMessage(sb.toString());
            progressDialog.setCancelable(false);
            progressDialog.show();
            DrawingView drawingView = this.drawingView;
            if (drawingView != null) {
                drawingView.AutoDetectionProcessingImage((Activity) this, (InterfaceBGI) new DialogDrawing_(this, progressDialog));
            }
        }
        DrawingView drawingView2 = this.drawingView;
        if (drawingView2 != null) {
            drawingView2.setRadius(20);
            this.drawingView.setOffset(75);
            this.drawingView.invalidate();
        }
    }
    public void loadingImageBitmap(ProgressDialog progressDialog) {
        boolean z;
        String str = " ";
        Log.d("TAGGG","loadingImageBitmap");

        try {
            if (isTutOpen) {
                this._bmp1 = BitmapFactory.decodeResource(getResources(), R.drawable.demo_img);
                this._bmp1 = ImageUtils.resizeBitmap(this._bmp1, this.width, this.height);
            } else {


                this._bmp1 = Glide.with(this).asBitmap().load(this._uriValue).into(this.width, this.height).get();

//                this._bmp1 = (Bitmap) Glide.with((FragmentActivity) this).load(this._uriValue).m9604a(((C2573h) new C2573h().mo10303a(C2215j.f8618b)).mo10321b(true)).mo9486e(this.width, this.height).get();
//                this._bmp1 = (Bitmap) C2107b.m9493a((FragmentActivity) this).mo9494b().m9609a(this._uriValue).m9604a(((C2573h) new C2573h().mo10303a(C2215j.f8618b)).mo10321b(true)).mo9486e(this.width, this.height).get();

                String str2 = "texting";
                StringBuilder sb = new StringBuilder();
                sb.append(this._bmp1.getWidth());
                sb.append(str);
                sb.append(this.width);
                sb.append(" Resizing Image ");
                if (this._bmp1.getWidth() <= this.width) {
                    if (this._bmp1.getHeight() <= this.height) {
                        Log.d("TAGGG","ds");
                        z = false;
                        sb.append(z);
                        sb.append(str);
                        sb.append(this._bmp1.getHeight());
                        sb.append(str);
                        sb.append(this.height);
                        Log.i(str2, sb.toString());
                        if (this._bmp1.getWidth() > this.width || this._bmp1.getHeight() > this.height || (this._bmp1.getWidth() < this.width && this._bmp1.getHeight() < this.height)) {
                            this._bmp1 = ImageUtils.resizeBitmap(this._bmp1, this.width, this.height);
                        }
                    }
                }
                z = true;
                sb.append(z);
                sb.append(str);
                sb.append(this._bmp1.getHeight());
                sb.append(str);
                sb.append(this.height);
                Log.i(str2, sb.toString());
                this._bmp1 = ImageUtils.resizeBitmap(this._bmp1, this.width, this.height);
            }
            if (this._bmp1 == null) {
                this._bnVt = true;
            } else {
                Log.d("TAGGG","ok");
                orgBitmap = this._bmp1.copy(this._bmp1.getConfig(), true);
                int a = ImageUtils.dpToPx((Context) this, 42);
                orgBitWidth = this._bmp1.getWidth();
                orgBitHeight = this._bmp1.getHeight();
                Bitmap createBitmap = Bitmap.createBitmap(this._bmp1.getWidth() + a + a, this._bmp1.getHeight() + a + a, this._bmp1.getConfig());
                Canvas canvas = new Canvas(createBitmap);
                canvas.drawColor(0);
                float f = (float) a;
                //canvas.drawBitmap(this._bmp1, f, f, null);
                Log.d("TAGGG","w: "+_bmp1.getWidth() + a + a+"h:"+this._bmp1.getHeight() + a + a);

                this._bmp1 = createBitmap;
                if (this._bmp1.getWidth() > this.width || this._bmp1.getHeight() > this.height || (this._bmp1.getWidth() < this.width && this._bmp1.getHeight() < this.height)) {
                    this._bmp1 = ImageUtils.resizeBitmap(this._bmp1, this.width, this.height);
                    Log.d("TAGGG","w"+_bmp1.getWidth()+"h:"+_bmp1.getHeight());
                }
            }
            Thread.sleep(1000);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            this._bnVt = true;
            if (progressDialog != null) {
                try {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                } catch (IllegalArgumentException e2) {
                    e2.printStackTrace();
                }
            }
        } catch (Exception e3) {
            e3.printStackTrace();
            this._bnVt = true;
            if (progressDialog != null) {
                try {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                } catch (IllegalArgumentException e4) {
                    e4.printStackTrace();
                }
            }
        }
        if (progressDialog != null) {
            try {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            } catch (IllegalArgumentException e5) {
                e5.printStackTrace();
            }
        }
    }

}