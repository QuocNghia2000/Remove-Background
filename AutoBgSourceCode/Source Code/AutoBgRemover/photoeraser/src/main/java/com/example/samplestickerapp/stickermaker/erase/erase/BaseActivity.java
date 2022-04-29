package com.example.samplestickerapp.stickermaker.erase.erase;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    public void runInit() {
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setFlags(1024, 1024);
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
