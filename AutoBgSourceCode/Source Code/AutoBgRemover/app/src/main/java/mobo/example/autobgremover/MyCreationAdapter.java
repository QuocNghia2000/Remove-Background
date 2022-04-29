package mobo.example.autobgremover;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import java.io.File;
import java.util.ArrayList;

import static mobo.example.autobgremover.MainActivity.getFilePath;
import static mobo.example.autobgremover.MainActivity.mPath;
import static mobo.example.autobgremover.MyCreationActivity.IMAGEALLARY;


public class MyCreationAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private Activity dactivity;
    private int imageSize;
    ArrayList<String> imagegallary = new ArrayList();
    SparseBooleanArray mSparseBooleanArray;
    MediaMetadataRetriever metaRetriever;
    View vi;

    static class ViewHolder {
        public FrameLayout frm;
        ImageView imgIcon;

        ViewHolder() {
        }
    }

    public MyCreationAdapter(Activity dAct, ArrayList<String> dUrl) {
        this.dactivity = dAct;
        this.imagegallary = dUrl;
        inflater = (LayoutInflater) this.dactivity.getSystemService("layout_inflater");
        this.mSparseBooleanArray = new SparseBooleanArray(this.imagegallary.size());
    }

    public int getCount() {
        return this.imagegallary.size();
    }

    public Object getItem(int position) {
        return Integer.valueOf(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View row = convertView;
        int width = this.dactivity.getResources().getDisplayMetrics().widthPixels;
        if (row == null) {
            row = LayoutInflater.from(this.dactivity).inflate(R.layout.list_gallary, parent, false);
            holder = new ViewHolder();
            holder.frm = (FrameLayout) row.findViewById(R.id.frm);
            holder.imgIcon = (ImageView) row.findViewById(R.id.imgIcon);
            holder.imgIcon.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    final Dialog dialog = new Dialog(MyCreationAdapter.this.dactivity, 16973839);
                    DisplayMetrics metrics = new DisplayMetrics();
                    MyCreationAdapter.this.dactivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    int height = (int) (((double) metrics.heightPixels) * 1.0d);
                    int width = (int) (((double) metrics.widthPixels) * 1.0d);
                    dialog.requestWindowFeature(1);
                    dialog.getWindow().setFlags(1024, 1024);
                    dialog.setContentView(R.layout.layout_fullscreen_image);
                    dialog.getWindow().setLayout(width, height);
                    dialog.setCanceledOnTouchOutside(true);
                    ImageView imgShare = (ImageView) dialog.findViewById(R.id.imgShare);

                    ((ImageView) dialog.findViewById(R.id.imgDisplay)).setImageURI(Uri.parse((String) IMAGEALLARY.get(position)));

                    imgShare.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            MyCreationActivity.shareApplication(MyCreationAdapter.this.dactivity);
                        }
                    });
                    dialog.show();
                }
            });
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        Glide.with(this.dactivity).load((String) this.imagegallary.get(position)).into(holder.imgIcon);
        System.gc();
        return row;
    }

}
