package com.example.hackheroes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;

public class ProfileActivity extends AppCompatActivity {
    Button gobackBtn;
    ImageView img;
    TextView username;
    TextView stars;
    TextView starColl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.profil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profile), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        img = findViewById(R.id.imageView);
        username = findViewById(R.id.textView2);
        stars = findViewById(R.id.textView6);
        starColl = findViewById(R.id.textView7);

        if (getIntent().getSerializableExtra("name")!=null) {
            username.setText((CharSequence) getIntent().getSerializableExtra("name"));
        }
        if ((String) getIntent().getSerializableExtra("img")!=null) {
            ImageView bg = new ImageView(this);

            File imgFile = new File(getExternalFilesDir(null), (String) getIntent().getSerializableExtra("img"));

            if (imgFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                img.setImageBitmap(cropToCircle(bitmap));
            } else {
                img.setImageResource(R.drawable.duches_ability);
            }
        }
        if (getIntent().getSerializableExtra("stars")!=null) {
            stars.setText("Complete stars: "+ getIntent().getSerializableExtra("stars"));
        }
        if (getIntent().getSerializableExtra("scs")!=null) {
            starColl.setText("Complete star collections: "+ getIntent().getSerializableExtra("scs"));
        }

            gobackBtn = findViewById(R.id.gobackB);
        gobackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundEffects.playButton();
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                if (getIntent().getSerializableExtra("name")!=null) {
                    intent.putExtra("username",getIntent().getSerializableExtra("name"));
                }
                if (getIntent().getSerializableExtra("img")!=null) {
                    intent.putExtra("profileIco",getIntent().getSerializableExtra("img"));
                }
                startActivity(intent);
            }
        });

    }
    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }
    private Bitmap cropToCircle(Bitmap srcBitmap) {

        int size = dpToPx(200);

        Bitmap scaled = Bitmap.createScaledBitmap(srcBitmap, size, size, true);

        Bitmap output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        float radius = size / 2f;


        canvas.drawCircle(radius, radius, radius, paint);


        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(scaled, 0, 0, paint);

        return output;
    }


}
