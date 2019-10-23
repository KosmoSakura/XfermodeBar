package cos.mos.xfermodebar.example;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import java.util.Random;

import cos.mos.xfermodebar.R;
import cos.mos.xfermodebar.widget.LineBar;
import cos.mos.xfermodebar.widget.WaveShapeBar;

public class MainActivity extends AppCompatActivity {
    private WaveShapeBar wpv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LineBar lneb = findViewById(R.id.st3_line);
        lneb.setOnClickListener(v -> lneb.start());
        lneb.setCompleteListener(() -> Toast.makeText(MainActivity.this, "进度完成", Toast.LENGTH_SHORT).show());

        wpv = findViewById(R.id.st3_wpv);
        wpv.setText(Color.parseColor("#FFFFFF"), 120)
            .setWaveColor(Color.parseColor("#FF0000"))
            .setSpeed(10)//8
            .setMaxProgress(100)
            .build();
        wpv.setOnClickListener(v -> {
            wpv.clear();
            startAnim();
        });
    }

    private void startAnim() {
        ValueAnimator animator = ValueAnimator.ofInt(0, new Random().nextInt(100));
        animator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            wpv.setProgress(value, String.valueOf(value));
        });
        animator.setDuration(3000);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
    }
}
