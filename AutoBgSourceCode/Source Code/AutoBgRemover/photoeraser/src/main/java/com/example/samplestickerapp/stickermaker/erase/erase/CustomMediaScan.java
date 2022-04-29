package com.example.samplestickerapp.stickermaker.erase.erase;

import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;
import java.io.File;

class CustomMediaScan implements InterfaceI {

    final String pathImage;
    final AdvanceEditActivity activity;

    CustomMediaScan(AdvanceEditActivity advanceEditActivity, String str) {
        this.activity = advanceEditActivity;
        this.pathImage = str;
    }

    public void model_1() {
        this.activity.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(new File(this.pathImage))));
        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SavedMsg(pathImage);
            }
        });
    }

    public void model_2() {
        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                errorMsg();
            }
        });
    }

    public void errorMsg() {
        AdvanceEditActivity advanceEditActivity = this.activity;
        Toast.makeText(advanceEditActivity, advanceEditActivity.getString(R.string.error), Toast.LENGTH_SHORT).show();
    }

    public void SavedMsg(String str) {
        AdvanceEditActivity advanceEditActivity = this.activity;
        StringBuilder sb = new StringBuilder();
        sb.append(this.activity.getString(R.string.image_saved));
        sb.append(str);
        Toast.makeText(advanceEditActivity, sb.toString(), Toast.LENGTH_SHORT).show();
    }
}
