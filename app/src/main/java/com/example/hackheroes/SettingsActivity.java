package com.example.hackheroes;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class SettingsActivity extends AppCompatActivity {

    String username ="";
    String profileImg = "";
    EditText usernameE;
    SeekBar master;
    SeekBar music;
    SeekBar effects;
    private ActivityResultLauncher<String> pickImageLauncher;

    Button goback;
    Button reser;
    ImageView img;
    int[] volumeSettings = {100,100,100};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Intent musicIntent = new Intent(this, BackgroundMusicService.class);
        startService(musicIntent);
        bindService(musicIntent, serviceConnection, BIND_AUTO_CREATE);


        goback = findViewById(R.id.gobackB);
        reser = findViewById(R.id.reset);
        img = findViewById(R.id.image);

        master = findViewById(R.id.seekBar);
        music = findViewById(R.id.seekBar1);
        effects = findViewById(R.id.seekBar2);




        usernameE = findViewById(R.id.editText);

        master.setProgress(volumeSettings[0]);
        music.setProgress(volumeSettings[1]);
        effects.setProgress(volumeSettings[2]);

        if (isBound) {
            master.setProgress(musicService.masterVol);
            music.setProgress(musicService.musicVol);
            effects.setProgress(musicService.effectVol);
            System.out.println(musicService.masterVol);
            System.out.println(musicService.musicVol);
            System.out.println(musicService.effectVol);
        }


        if ((int[]) getIntent().getSerializableExtra("volset")!=null) {
            int[] volset = (int[]) getIntent().getSerializableExtra("volset");
            assert volset != null;
            volumeSettings[0]=volset[0];
            volumeSettings[1]=volset[1];
            volumeSettings[2]=volset[2];

        }

        SeekBar.OnSeekBarChangeListener volumeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int id = seekBar.getId();
                if (id == R.id.seekBar) {
                    volumeSettings[0] = progress;
                } else if (id == R.id.seekBar1) {
                    volumeSettings[1] = progress;
                } else if (id == R.id.seekBar2) {
                    volumeSettings[2] = progress;
                }
                if (isBound) {
                    musicService.setMusicVolume(((float) volumeSettings[0] /100)*((float) volumeSettings[1] /100));
                    musicService.masterVol = volumeSettings[0];
                    musicService.musicVol = volumeSettings[1];
                    musicService.effectVol = volumeSettings[2];
                    System.out.println(musicService.masterVol);
                    System.out.println(musicService.musicVol);
                    System.out.println(musicService.effectVol);
                }
                SoundEffects.setVolume(((float) volumeSettings[0] /100)*((float) volumeSettings[2] /100));
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        };

        master.setOnSeekBarChangeListener(volumeListener);
        music.setOnSeekBarChangeListener(volumeListener);
        effects.setOnSeekBarChangeListener(volumeListener);

        SettingsActivity context = this;

        reser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundEffects.playButton();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("are you sure?");
                builder.setMessage("Are you sure delete all custom star collections and revert default ones?");

                builder.setPositiveButton("delete", ((dialog, which) -> {
                    Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                    intent.putExtra("order66", true);
                    startActivity(intent);
                }));
                builder.setNegativeButton("leave it", (dialog, which) -> {
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setTextColor(Color.RED);
            }
        });
        goback = findViewById(R.id.gobackB);

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundEffects.playButton();
                username = String.valueOf(usernameE.getText());
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.putExtra("VolSet", volumeSettings);
                if (username!=null && !username.isEmpty()) {
                    intent.putExtra("username", username);
                }
                if (profileImg!=null && !profileImg.isEmpty()) {
                    intent.putExtra("profileIco", profileImg);
                }
                startActivity(intent);
            }
        });

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        saveImageToExternal(uri);
                    }
                }
        );

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundEffects.playButton();
                openGallery();
            }
        });

        if (isBound) {
            master.setProgress(volumeSettings[0]);
            music.setProgress(volumeSettings[1]);
            effects.setProgress(volumeSettings[2]);
        }
    }


    BackgroundMusicService musicService;
    boolean isBound = false;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BackgroundMusicService.MusicBinder binder =
                    (BackgroundMusicService.MusicBinder) service;
            musicService = binder.getService();
            master.setProgress(musicService.masterVol);
            music.setProgress(musicService.musicVol);
            effects.setProgress(musicService.effectVol);
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

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


    private void openGallery() {
        pickImageLauncher.launch("image/*");
    }


    private void saveImageToExternal(Uri uri) {
        try {
            InputStream is = getContentResolver().openInputStream(uri);
            if (is == null) return;

            String filename = "bg_" + System.currentTimeMillis() + ".png";
            File outFile = new File(getExternalFilesDir(null), filename);

            try (OutputStream os = new FileOutputStream(outFile)) {
                byte[] buffer = new byte[4096];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
            }

            Bitmap bitmap = BitmapFactory.decodeFile(outFile.getAbsolutePath());
            img.setImageBitmap(cropToCircle(bitmap));

            profileImg = filename;

            Toast.makeText(this, "profile picture selected", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
        }
    }

}
