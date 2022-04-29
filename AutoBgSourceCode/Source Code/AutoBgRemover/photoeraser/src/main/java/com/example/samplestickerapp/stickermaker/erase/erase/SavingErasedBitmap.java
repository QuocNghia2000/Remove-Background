package com.example.samplestickerapp.stickermaker.erase.erase;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SavingErasedBitmap {

    public static File file_bitmap;
    public static String file_bitmap_path(Context context) {
        File file = file_bitmap;
        if (file == null || !file.exists()) {
            create_folder(context);
        }
        File file2 = file_bitmap;
        StringBuilder sb = new StringBuilder();
        sb.append("_Cut_");
        sb.append(System.currentTimeMillis());
        sb.append(".png");
        return new File(file2, sb.toString()).getAbsolutePath();
    }
    public static void create_folder(Context context) {
        File file = file_bitmap;
        if (file == null || !file.exists()) {
            File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (externalStoragePublicDirectory == null) {
                externalStoragePublicDirectory = Environment.getExternalStorageDirectory();
            }
            file_bitmap = new File(externalStoragePublicDirectory, "Background_Eraser");
            if (!file_bitmap.exists()) {
                file_bitmap.mkdirs();
            }
        }

    }

    public static void saveImageToStorage(String str, Bitmap bitmap, InterfaceI d0Var) {
        if (bitmap == null || bitmap.isRecycled()) {
            if (d0Var != null) {
                d0Var.model_2();
            }
            return;
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(str);
            bitmap.compress(CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            if (d0Var != null) {
                d0Var.model_1();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            StringBuilder sb = new StringBuilder();
            sb.append("saveToFile: ");
            sb.append(str);
            sb.toString();
            if (d0Var != null) {
                d0Var.model_2();
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            if (d0Var != null) {
                d0Var.model_2();
            }
        } catch (IllegalStateException e3) {
            e3.printStackTrace();
            if (d0Var != null) {
                d0Var.model_2();
            }
        }
    }
}
