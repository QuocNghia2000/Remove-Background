package com.example.samplestickerapp.stickermaker.erase.erase.AutoRemovalModule;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

public class AutoRemoveModel {

    private static final String auto_detection_model_path = "file:///android_asset/data_autocut_cute_wallpapers_studio.pb";
    private static final String imageTensor = "ImageTensor";
    private static final String semanticPredictions = "SemanticPredictions";
    private static TensorFlowInferenceInterface tensorFlowInferenceInterface;
    private static Context contextAuto;

    public static synchronized boolean loadingModel(Context context) {
        FileInputStream fileInputStream;
        synchronized (AutoRemoveModel.class) {
            contextAuto = context;
            try {
                fileInputStream = new FileInputStream(new File(auto_detection_model_path));
            } catch (FileNotFoundException e) {
                Log.e("Input", e.getMessage());
                fileInputStream = null;
            }
            tensorFlowInferenceInterface = new TensorFlowInferenceInterface(context.getAssets(), auto_detection_model_path);
            if (tensorFlowInferenceInterface == null) {
                Log.w("init model failed", auto_detection_model_path);
                return false;
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException unused) {
                }
            }
            return true;
        }
    }

    public static synchronized boolean loadingState() {
        boolean z;
        synchronized (AutoRemoveModel.class) {
            z = tensorFlowInferenceInterface != null;
        }
        return z;
    }

    public static synchronized int[] processAutoRemoveModel(Bitmap bitmap) {
        synchronized (AutoRemoveModel.class) {
            if (tensorFlowInferenceInterface == null) {
                Log.e("tfmodel not init", "");
                return null;
            } else if (bitmap == null) {
                Log.e("bitmap null ", "");
                return null;
            } else {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                StringBuilder sb = new StringBuilder();
                sb.append("bitmap: ");
                sb.append(width);
                sb.append("x");
                sb.append(height);
                Log.e(sb.toString(), "");
                if (width <= 513) {
                    if (height <= 513) {
                        int i = width * height;
                        int[] iArr = new int[i];
                        byte[] bArr = new byte[(i * 3)];
                        int[] iArr2 = new int[i];
                        bitmap.getPixels(iArr, 0, width, 0, 0, width, height);
                        for (int i2 = 0; i2 < iArr.length; i2++) {
                            int i3 = iArr[i2];
                            int i4 = i2 * 3;
                            bArr[i4 + 0] = (byte) ((i3 >> 16) & 255);
                            bArr[i4 + 1] = (byte) ((i3 >> 8) & 255);
                            bArr[i4 + 2] = (byte) (i3 & 255);
                        }
                        System.currentTimeMillis();
                        tensorFlowInferenceInterface.feed(imageTensor, bArr, 1, (long) height, (long) width, 3);
                        tensorFlowInferenceInterface.run(new String[]{semanticPredictions}, true);
                        tensorFlowInferenceInterface.fetch(semanticPredictions, iArr2);
                        double[] dArr = new double[256];
                        for (int i5 = 0; i5 < height; i5++) {
                            for (int i6 = 0; i6 < width; i6++) {
                                int i7 = (i5 * width) + i6;
                                if (iArr2[i7] != 0) {
                                    int i8 = iArr2[i7];
                                    dArr[i8] = dArr[i8] + 1.0d;
                                }
                            }
                        }
                        for (int i9 = (height / 2) - 10; i9 < (height / 2) + 10; i9++) {
                            for (int i10 = (width / 2) - 10; i10 < (width / 2) + 10; i10++) {
                                int i11 = (i9 * width) + i10;
                                dArr[iArr2[i11]] = dArr[iArr2[i11]] * 1.04d;
                            }
                        }
                        double d = 0.0d;
                        int i12 = 0;
                        for (int i13 = 1; i13 < 256; i13++) {
                            if (d < dArr[i13]) {
                                d = dArr[i13];
                                i12 = i13;
                            }
                        }
                        for (int i14 = 0; i14 < height; i14++) {
                            for (int i15 = 0; i15 < width; i15++) {
                                int i16 = (i14 * width) + i15;
                                iArr2[i16] = iArr2[i16] == i12 ? 1 : 0;
                            }
                        }
                        return iArr2;
                    }
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append("bitmap: ");
                sb2.append(width);
                sb2.append("x");
                sb2.append(height);
                Log.e(sb2.toString(), "");
                return null;
            }
        }
    }

}
