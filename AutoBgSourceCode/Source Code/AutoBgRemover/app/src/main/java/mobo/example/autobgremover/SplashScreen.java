package mobo.example.autobgremover;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends Activity
{
    private static int SPLASH_TIME_OUT = 1500;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView((int) R.layout.activity_splash_screen);
        ImageView icon_app=(ImageView)findViewById(R.id.icon_app);
        Animation loadAnimation = AnimationUtils.loadAnimation(this, R.anim.heartpulseanimation);
        icon_app.startAnimation(loadAnimation);
        new Handler().postDelayed(new StartingApp(), (long) SPLASH_TIME_OUT);
    }

    class StartingApp implements Runnable
    {
        public void run() {
            startActivity(new Intent(SplashScreen.this, MainActivity.class));
            finish();
        }
    }

}
