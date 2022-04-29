package mobo.example.autobgremover;


import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class CustomAdapter extends PagerAdapter {
    private Activity activity;
    private Integer[] imagesArray;

    public CustomAdapter(Activity activity, Integer[] imagesArray) {
        this.activity = activity;
        this.imagesArray = imagesArray;
    }

    public Object instantiateItem(ViewGroup container, int position) {
        View viewItem = this.activity.getLayoutInflater().inflate(R.layout.item_snap, container, false);
        ((ImageView) viewItem.findViewById(R.id.thumbnail_image)).setBackgroundResource(this.imagesArray[position].intValue());
        ((ViewPager) container).addView(viewItem);
        return viewItem;
    }

    public int getCount() {
        return this.imagesArray.length;
    }

    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}
