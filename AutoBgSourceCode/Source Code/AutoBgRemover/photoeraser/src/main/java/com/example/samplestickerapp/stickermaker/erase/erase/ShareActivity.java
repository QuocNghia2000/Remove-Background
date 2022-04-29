package com.example.samplestickerapp.stickermaker.erase.erase;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;

public class ShareActivity extends Activity implements View.OnClickListener
{
    int width,height;
    TextView textView;
    ImageView imageView;
    public static String saved_image;
    public void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        getWindow().setFlags(1024, 1024);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);


        int sh = displaymetrics.heightPixels;
        this.width = displaymetrics.widthPixels;
        this.height = sh - ImageUtils.dpToPx(this, 105);

        textView=(TextView)findViewById(R.id.tvFilePath);
        imageView=(ImageView) findViewById(R.id.img);

        if(saved_image==null)
        {
            Toast.makeText(this, "Something went wrong try again !", Toast.LENGTH_SHORT).show();
            finish();
        }
        else
        {
            textView.setText(saved_image);
            Glide.with(ShareActivity.this).load(saved_image).into(imageView);
        }


    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_back || id == R.id.txt_back) {
            finish();
        }
    }

}
