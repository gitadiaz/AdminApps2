package inggitsemut.adminapps2.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import inggitsemut.adminapps2.R;

public class SplashScreenActivity extends AppCompatActivity {

    TextView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        logo = findViewById(R.id.logo);
        AlphaAnimation animation = new AlphaAnimation(0f, 1f); //tipe data float. opacity dari angka berapa ke angka berapa
        animation.setFillAfter(true); // ngikutin animasi terakhirnya. untuk menghindari looping
        animation.setDuration(750); // mili sec
        logo.startAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);

    }
}
