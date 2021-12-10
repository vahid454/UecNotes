package com.uec.notes.uecnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;

import android.media.tv.TvContract;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT=5000;
    private String email;

    Animation topAnim,bottomAnim;
    ImageView image;
    TextView t1,t2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);

        topAnim=AnimationUtils.loadAnimation(SplashActivity.this,R.anim.top_animation);
        bottomAnim=AnimationUtils.loadAnimation(SplashActivity.this,R.anim.bottom_animation);
        image=(ImageView) findViewById(R.id.imageView);
        t1=(TextView) findViewById(R.id.textView2);
        t2=(TextView) findViewById(R.id.textView3);
        image.setAnimation(topAnim);
        t1.setAnimation(bottomAnim);
        t2.setAnimation(bottomAnim);



        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {
                //CheckActiveUser();

                Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_TIME_OUT);


    }
}
