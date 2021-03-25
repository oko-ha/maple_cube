package com.example.maple_cube;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class IntroActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        final ImageView iv_intro = findViewById(R.id.iv_intro);
        Glide.with(this).asGif().load(R.drawable.intro1).listener(new RequestListener<GifDrawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                return false;
            }
            @Override
            public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                resource.setLoopCount(1);
                resource.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                    @Override
                    public void onAnimationEnd(Drawable drawable) {
                        finish();
                        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        startActivity(intent);
                    }
                });
                return false;
            }
        }).into(iv_intro);
    }
}
