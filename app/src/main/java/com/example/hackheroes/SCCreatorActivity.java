package com.example.hackheroes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class SCCreatorActivity extends AppCompatActivity {

    Button goBack;
    Button createStar;
    Button hideB;
    ConstraintLayout buttonL;
    ConstraintLayout footer;

    RecyclerView recyclerView;

    ArrayList<Star> starsCreator = new ArrayList<>(Arrays.asList(new Star("","",false,0.5f)));
    private String selectedBgFilename = "";
    private StarCollection currentStarCollection;
    private EditText scNameInput;
    private Button selectBgBtn;
    private ImageView bgPreview;
    Button comeback;

    Boolean up = false;
    int stars = 1;

    private ActivityResultLauncher<String> pickImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.sc_creator);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        currentStarCollection = new StarCollection(new ArrayList<>(), "", "");

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        CustomAdapter adapter = new CustomAdapter(starsCreator);
        recyclerView.setAdapter(adapter);

        createStar = findViewById(R.id.createNew);
        SCCreatorActivity context = this;
        createStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundEffects.playButton();
                if (stars<10) {
                    adapter.addItem();
                    stars++;
                }else{
                    new AlertDialog.Builder(context)
                            .setTitle("Limit reached")
                            .setMessage("Upper limit of stars in star constelation is 10!")
                            .setPositiveButton("OK", null)
                            .show();
                }
            }
        });

        comeback = findViewById(R.id.button);
        comeback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SCCreatorActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        scNameInput = findViewById(R.id.scNameInput);
        selectBgBtn = findViewById(R.id.selectBgBtn);
        bgPreview = findViewById(R.id.bgPreview);

        goBack = findViewById(R.id.gobackB);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundEffects.playButton();
                String scName = scNameInput.getText().toString().trim();
                if (scName.isEmpty() || selectedBgFilename.isEmpty()) {
                    Toast.makeText(context, "Please enter name and select background", Toast.LENGTH_SHORT).show();
                    return;
                }
                int starCounter = 0;
                for (Star star : adapter.getAllItems()){
                    starCounter++;
                    System.out.println("debug star "+star.name+star.description);
                    if (star.description.equals("") || star.name.equals("")){
                        Toast.makeText(context, "Please fill out all added stars", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (starCounter<=3){
                    Toast.makeText(context, "Make at least 4 stars", Toast.LENGTH_SHORT).show();
                    return;
                }

                currentStarCollection.name = scName;
                currentStarCollection.bgIco = selectedBgFilename;
                currentStarCollection.stars = adapter.getAllItems();


                Intent intent = new Intent(SCCreatorActivity.this, MainActivity.class);
                intent.putExtra("create",currentStarCollection);
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


        selectBgBtn.setOnClickListener(v ->{ openGallery(); SoundEffects.playButton();} );

        hideB = findViewById(R.id.hideB);
        footer = findViewById(R.id.footer);
        buttonL = findViewById(R.id.buttonL);
        hideB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundEffects.playButton();
                if (!up) {
                    hideB.setBackground(ContextCompat.getDrawable(context,R.drawable.arrowup2));
                    buttonL.animate()
                            .translationY(footer.getHeight())
                            .setDuration(300)
                            .start();
                    footer.animate()
                            .translationY(footer.getHeight())
                            .setDuration(300)
                            .start();
                    up = true;
                }else {
                    hideB.setBackground(ContextCompat.getDrawable(context,R.drawable.arrowdown));
                    buttonL.animate()
                            .translationY(0)
                            .setDuration(300)
                            .start();
                    footer.animate()
                            .translationY(0)
                            .setDuration(300)
                            .start();
                    up = false;
                }
            }
        });



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
            bgPreview.setImageBitmap(bitmap);

            selectedBgFilename = filename;

            Toast.makeText(this, "Background selected", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
        }
    }
}



