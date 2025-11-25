package com.example.hackheroes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.view.View;
import android.view.WindowManager;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    String PREFS = "USERDATA";
    String KEY_USERNAME = "username";
    String KEY_PROFILEIMG = "profile_image";
    String KEY_STARS ="stars";
    String KEY_SCS="scs";

    Button profileButton;
    Button hideButton;
    Button setttingsB;
    ConstraintLayout buttonL;
    ConstraintLayout footer;
    LinearLayout linearparent;
    Boolean up = false;
    ArrayList<StarCollection> starsCollections;
    String fileName = "data.txt";
    String userName = "";
    String profileimg = "";
    int stars =0;
    int scs = 0;
    int volEfect=100;
    int volMusic=100;
    int volMaster=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent musicIntent = new Intent(this, BackgroundMusicService.class);
        startService(musicIntent);

        SoundEffects.init(this);

        starsCollections = new ArrayList<>();
        ArrayList<Star> stars1 = new ArrayList<>(
                Arrays.asList(
                        new Star("Learn basics", "Look at your instrument, try to play single note on different string. If you are playing electric look also at your amplifier, change some setings and see how your instrument sounds", false, 0.5F),
                        new Star("Tune your instrument", "Tune your guitar by turning pegs, standard guitar tuning is E-A-D-G-B-E. You can remeber it easier by learning some kind of mnemonic for example Eddie Ate Dynamite Good Bye Eddie", false, 0.5F),
                        new Star("Play clean notes", "Play clean notes by pressing your finger just above fret(metallic bar on fret board), then strumming string. Try to make it sound clean so no ringing or buzzing sound", false, 0.5F),
                        new Star("Learn Chords", "Try to play few strings at the same time, then find how to play G-chord, and try to do it similar to last step no ringing or buzzing", false, 0.5F),
                        new Star("Learn 7 nation army", "7 nation army by the white stripes, is probably the best song to start your playing, look up how to play it, and try. If it's too difficult try to do it slower ", false, 0.2F),
                        new Star("Do it faster", "Try to play 7 nation army at 150% speed, to make it easier do it progressively so first do it at 110% then at 120% up to 150%  ", false, 0.8F),
                        new Star("Learn music theory basic", "As simple as that, learn some basics. It will take some time so don't stop playing in the meantime ", false, 0.3F),
                        null,
                        new Star("Practice with metronome", "Put up metronome on whatever tempo you want, and play with it. It will be harder to do at higher BPM so don't worry if you make some mistakes ", false, 0.2F),
                        new Star("Don't stop", "Practice, practice and practice. Try to make metronome faster and faster and learn some new songs good examples can be Smoke on the Water by Deep Purple, or I Love Rock N' Roll by Joan Jett", false, 0.7F)
                        )
        );
        StarCollection sc1 = new StarCollection(stars1, "learnin Guitar", "guitar.png");

        ArrayList<Star> starsChess = new ArrayList<>(
                Arrays.asList(
                        new Star("Learn the Basics", "Recognize the pieces and their moves: king, queen, rook, bishop, knight, pawn.", false, 0.5F),
                        new Star("Game Rules", "Board setup, castling, en passant, pawn promotion.", false, 0.2F),
                        new Star("Legal and Clean Moves", "Play correctly and don’t give away pieces for free.", false, 0.5F),
                        new Star("Basic Checkmates", "Checkmate with queen and king, two rooks, and other simple patterns.", false, 0.2F),
                        new Star("Simple Opening", "Learn one opening and its basic ideas.", false, 0.5F),
                        new Star("Play Faster", "Increase your pace — shorter time controls.", false, 0.5F),
                        new Star("Tactics", "Forks, pins, double attacks, discovered attacks — practice puzzles regularly.", false, 0.4F),
                        null,
                        new Star("Puzzles", "Solve tactical and endgame puzzles.", false, 0.8F),
                        new Star("Regular Play", "Play games, analyze mistakes, and keep improving.", false, 0.1F)
                )
        );
        StarCollection scChess = new StarCollection(starsChess, "Chess Learning", "chess.png");

        ArrayList<Star> starsFootball = new ArrayList<>(
                Arrays.asList(
                        new Star("Learn Football", "Ball control — dribbling with inside and outside of the foot.", false, 0.9F),
                        new Star("Positioning", "Learn your place on the field and basic roles.", false, 0.0F),
                        new Star("Clean Passes", "Practice accuracy of short and long passes.", false, 0.5F),
                        new Star("Dribbling Basics", "Change direction, keep close control.", false, 0.2F),
                        new Star("One Type of Shot", "Master the technique of one specific shot.", false, 0.8F),
                        new Star("Increased Intensity", "More sprints, more intervals, speed exercises.", false, 0.2F),
                        new Star("Team Tactics", "Pressing, wide play, maintaining position.", false, 0.8F),
                        null,
                        new Star("Tempo Training", "30s work / 15s rest exercises, improve conditioning.", false, 0.5F),
                        new Star("Consistency", "Regular matches, training, and recovery.", false, 0.5F)
                )
        );
        StarCollection scFootball = new StarCollection(starsFootball, "Football Training", "football.png");

        ArrayList<Star> starsGym = new ArrayList<>(
                Arrays.asList(
                        new Star("Learn the Equipment", "Learn safety, machines, and free weights.", false, 0.0F),
                        new Star("Warm-up", "Mobility, joint preparation, and light cardio.", false, 0.1F),
                        new Star("Basic Technique", "Squat, deadlift, bench press — with light weight.", false, 0.2F),
                        new Star("Controlled Training", "Perform movements slowly and with correct technique.", false, 0.4F),
                        new Star("3-Day Plan", "Push/Pull/Legs — stick to one plan.", false, 0.8F),
                        new Star("Increasing Weight", "Gradually add load (progressive overload).", false, 0.9F),
                        new Star("Nutrition Basics", "Macros, protein, muscle recovery.", false, 0.2F),
                        null,
                        new Star("Metabolic Training", "High-intensity circuits, short breaks.", false, 0.7F),
                        new Star("Consistency", "Sleep, recovery, and continuous training.", false, 0.9F)
                )
        );
        StarCollection scGym = new StarCollection(starsGym, "Strength Training", "gym.png");

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;

        profileButton = findViewById(R.id.profilB);

        hideButton = findViewById(R.id.hideB);
        footer = findViewById(R.id.footer);
        buttonL = findViewById(R.id.buttonL);
        MainActivity context = this;
        hideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundEffects.playButton();
                if (!up) {
                    hideButton.setBackground(ContextCompat.getDrawable(context,R.drawable.arrowup2));
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
                    hideButton.setBackground(ContextCompat.getDrawable(context,R.drawable.arrowdown));
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

//<editor-fold desc="sc creator">


        File file = new File(getFilesDir(), "data.txt");
        decodeFile();

        if ((StarCollection) getIntent().getSerializableExtra("sc")!=null) {
            Boolean rewrite= false;
            System.out.println("not emptty");
            StarCollection sc = (StarCollection) getIntent().getSerializableExtra("sc");
            System.out.println(sc.stars.get(0).status);
            System.out.println("first of all "+starsCollections.size());
            for (int i = 0; i < starsCollections.size(); i++) {
                System.out.println(" main sc name "+starsCollections.get(i).name+" intent sc name "+sc.name);
                if (Objects.equals(starsCollections.get(i).name, sc.name)) {
                    starsCollections.set(i, new StarCollection(sc));
                    rewrite = true;
                    break;
                }
            }
            if (rewrite){
                encodeFile();
                decodeFile();
            }
        }

        if ((int[]) getIntent().getSerializableExtra("VolSet")!=null) {
            int[] volset = (int[]) getIntent().getSerializableExtra("VolSet");
            assert volset != null;
            volMaster=volset[0];
            volMusic=volset[1];
            volEfect=volset[2];
        }



        if ((StarCollection) getIntent().getSerializableExtra("create")!=null) {
            System.out.println("not emptty creat");
            StarCollection sc = (StarCollection) getIntent().getSerializableExtra("create");
            System.out.println(sc.stars.get(0).status);
            System.out.println("first of all "+starsCollections.size());

            boolean flasee = false;
            for (int i = 0; i < starsCollections.size(); i++){
                if (Objects.equals(starsCollections.get(i).name, sc.name)){
                    flasee = true;
                }
            }
            if (flasee){
                Toast.makeText(context, "Uhm, sc with this name already exists", Toast.LENGTH_SHORT).show();
            }else {
                starsCollections.add(sc);

                encodeFile();
                decodeFile();
            }
        }

        if ((StarCollection) getIntent().getSerializableExtra("Shit_to_delte")!=null) {
            System.out.println("not emptty deleter");
            StarCollection sc = (StarCollection) getIntent().getSerializableExtra("Shit_to_delte");
            System.out.println(sc.stars.get(0).status);
            System.out.println("first of all "+starsCollections.size());

            for (int i =0;i < starsCollections.size();i++){
                if (starsCollections.get(i).name.equals(sc.name)){
                    starsCollections.remove(i);
                }
            }

            encodeFile();
            decodeFile();
        }
        if (getIntent().hasExtra("order66")){
            file.delete();
            decodeFile();
        }

        if (starsCollections == null || starsCollections.isEmpty()) {
            System.out.println("File missing or empty. Creating default data.");

            starsCollections = new ArrayList<>();
            starsCollections.add(sc1);
            starsCollections.add(scChess);
            starsCollections.add(scFootball);
            starsCollections.add(scGym);

            copyDrawableToExternal("guitar", "guitar.png");
            copyDrawableToExternal("football", "football.png");
            copyDrawableToExternal("chess", "chess.png");
            copyDrawableToExternal("gym", "gym.png");

            encodeFile();
        }
        copyDrawableToExternal("guitar", "guitar.png");
        copyDrawableToExternal("football", "football.png");
        copyDrawableToExternal("chess", "chess.png");
        copyDrawableToExternal("gym", "gym.png");
        for (StarCollection sc:starsCollections){
            Boolean valid= true;
            for (Star star: sc.stars){
                if (star!=null) {
                    if (star.status) {
                        stars++;
                    } else {
                        valid = false;
                        break;
                    }
                }
            }
            if (valid){scs++;}
        }

        loadUserData();

        if (getIntent().getSerializableExtra("username")!=null) {
            userName = (String) getIntent().getSerializableExtra("username");
        }
        if (getIntent().getSerializableExtra("profileIco")!=null) {
            profileimg = (String) getIntent().getSerializableExtra("profileIco");
        }
        saveUserData(userName, profileimg);


        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundEffects.playButton();
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                if (!userName.isEmpty()){
                    intent.putExtra("name",userName);
                }
                if (!profileimg.isEmpty()){
                    intent.putExtra("img",profileimg);
                }
                intent.putExtra("stars",stars);
                intent.putExtra("scs",scs);
                startActivity(intent);
            }
        });


        setttingsB = findViewById(R.id.settingsB);
        setttingsB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundEffects.playButton();
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                intent.putExtra("volset", new int[]{volMaster,volMusic,volEfect});
                startActivity(intent);
            }
        });


        drawSCs();
//        </editor-fold>

    }
    public void drawSCs(){
        LinearLayout hContainer = findViewById(R.id.Linearparent);
        Log.d("TEST", "hContainer = " + hContainer);
        Log.d("TEST", "childCount before = " + hContainer.getChildCount());

        float density = getResources().getDisplayMetrics().density;

        HorizontalScrollView.LayoutParams params = new HorizontalScrollView.LayoutParams(
                HorizontalScrollView.LayoutParams.WRAP_CONTENT,
                HorizontalScrollView.LayoutParams.MATCH_PARENT
        );
        hContainer.setLayoutParams(params);




        Button firstButton = new Button(this);
        firstButton.setText("");
        firstButton.setSoundEffectsEnabled(false);

        Drawable bg2 = ContextCompat.getDrawable(this, R.drawable.plus);
        firstButton.setBackground(bg2);
        bg2.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);

        firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundEffects.playButton();
                Intent intent = new Intent(MainActivity.this, SCCreatorActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        hContainer.addView(firstButton, buttonParams);



        Log.d("TEST", "childCount after button = " + hContainer.getChildCount());

        for (StarCollection sc : starsCollections) {

            Log.d("LINES", "Adding collection: " + sc.name);

            FrameLayout wrapper = new FrameLayout(this);
            LinearLayout.LayoutParams collParams =
                    new LinearLayout.LayoutParams(dp(300), LinearLayout.LayoutParams.WRAP_CONTENT);
            collParams.setMargins(dp(12), dp(12), dp(12), dp(12));
            wrapper.setLayoutParams(collParams);

            wrapper.setSoundEffectsEnabled(false);
            wrapper.setOnClickListener(v -> {

                SoundEffects.playButton();
                Intent intent = new Intent(this, StarCollectionActivity.class);
                intent.putExtra("sc", sc);
                startActivity(intent);

            });
            int actualStars=0;
            int completedStars=0;
            for (Star star : sc.stars){
                if (star!=null){
                    actualStars++;
                    if (star.status){
                        completedStars++;
                    }
                }
            }
            float alpha =  ((float) completedStars /actualStars)*0.5f;

            ImageView bg = new ImageView(this);

            File imgFile = new File(getExternalFilesDir(null), sc.bgIco);

            Log.d("BG_DEBUG", sc.bgIco+" looking for " + imgFile.getAbsolutePath());
            Log.d("BG_DEBUG", "Exists? " + imgFile.exists());

            if (imgFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                if (bitmap == null) {
                    Log.e("BG_DEBUG", "Bitmap decode FAILED (null). File corrupted?");
                    bg.setImageResource(R.drawable.guitar);
                } else {
                    Log.d("BG_DEBUG", "Bitmap decoded OK → width=" + bitmap.getWidth() + " height=" + bitmap.getHeight());
                    bg.setImageBitmap(bitmap);
                }
            } else {
                Log.e("BG_DEBUG", "Background file missing! Using fallback.");
                bg.setImageResource(R.drawable.guitar);
            }


            bg.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            bg.setColorFilter(Color.argb(255, 255, 255, 255));
            bg.setAlpha(alpha);

            wrapper.addView(bg, 0);

            LinearLayout collectionLayout = new LinearLayout(this);
            collectionLayout.setOrientation(LinearLayout.VERTICAL);
            collectionLayout.setPadding(dp(12), dp(12), dp(12), dp(12));

            wrapper.addView(collectionLayout);
            hContainer.addView(wrapper);

            LineView lineView = new LineView(this);
            FrameLayout.LayoutParams lp =
                    new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT
                    );
            wrapper.addView(lineView, 0, lp);


            TextView title = new TextView(this);
            title.setText(sc.name);
            title.setTextColor(Color.WHITE);
            title.setBackgroundResource(R.drawable.panelbg);
            title.setGravity(Gravity.CENTER);
            title.setTextSize(18);
            title.setPadding(0, 0, 0, dp(8));
            collectionLayout.addView(title);

            List<LineView.Node> nodes = new ArrayList<>();

            int rowIndex = 0;
            for (Star star : sc.stars) {

                Log.d("LINES", "Row " + rowIndex + " star = " + star);

                ConstraintLayout row = new ConstraintLayout(this);
                row.setId(View.generateViewId());
                row.setPadding(0, dp(6), 0, dp(6));

                LinearLayout.LayoutParams rowParams =
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                dp((500 - 70) / sc.stars.size())
                        );
                rowParams.setMargins(0, dp(6), 0, 0);

                collectionLayout.addView(row, rowParams);

                if (star == null) {
                    Log.d("LINES", "Row " + rowIndex + " is EMPTY (null star)");
                    rowIndex++;
                    continue;
                }

                Log.d("LINES", "Row " + rowIndex + " contains star: " + star.name);

                ImageView icon = new ImageView(this);
                icon.setId(View.generateViewId());
                icon.setImageResource(R.drawable.starico2);
                if (!star.status) icon.setAlpha(0.4f);

                ConstraintLayout.LayoutParams iconParams =
                        new ConstraintLayout.LayoutParams(0, 0);

                iconParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
                iconParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
                iconParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;

                iconParams.height = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT;
                iconParams.dimensionRatio = "1:1";

                row.addView(icon, iconParams);

                TextView name = new TextView(this);
                name.setId(View.generateViewId());
                name.setText(star.name);
                name.setTextColor(Color.WHITE);

                ConstraintLayout.LayoutParams nameParams =
                        new ConstraintLayout.LayoutParams(
                                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                                ConstraintLayout.LayoutParams.WRAP_CONTENT
                        );
                nameParams.topToTop = icon.getId();
                nameParams.bottomToBottom = icon.getId();
                row.addView(name, nameParams);

                row.post(() -> {

                    int iconSize = icon.getHeight();
                    int rowWidth = row.getWidth();

                    float p = Math.max(0f, Math.min(1f, star.position));

                    int x = Math.round(p * rowWidth) - iconSize / 2;
                    x = Math.max(0, Math.min(x, rowWidth - iconSize));
                    icon.setX(x);

                    if (p > 0.5f)
                        name.setX(x - name.getWidth() - dp(4));
                    else
                        name.setX(x + iconSize + dp(4));

                    int[] rowLoc = new int[2];
                    int[] wrapperLoc = new int[2];

                    row.getLocationOnScreen(rowLoc);
                    wrapper.getLocationOnScreen(wrapperLoc);

                    float cx = (rowLoc[0] - wrapperLoc[0]) + icon.getX() + iconSize / 2f;
                    float cy = (rowLoc[1] - wrapperLoc[1]) + icon.getY() + iconSize / 2f;

                    nodes.add(new LineView.Node(cx, cy, star.status));
                    lineView.setNodes(nodes);
                });


                rowIndex++;
            }
        }
    }
    private int dp(int v) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(v * density);
    }
    private void decodeFile(){
        File file = new File(getFilesDir(), "data.txt");
        Log.d("DEBUG", "Internal file path: " + file.getAbsolutePath());
        starsCollections = new ArrayList<>();
        try {
            if (file.exists()) {
                Scanner scanner = new Scanner(file);
                Boolean name = true;
                Boolean bg = true;
                String SCname = "default so error";
                String bgIco = "1";
                ArrayList<Star> starsList = new ArrayList<>();
                while(scanner.hasNextLine()){

                    String line = scanner.nextLine();
                    if (name){ SCname = line;name = false;continue;}
                    if (bg){bgIco = line;bg=false;continue;}
                    switch (line){
                        case "":
                            name = true;
                            bg = true;
                            starsCollections.add(new StarCollection(starsList, SCname,bgIco));
                            starsList = new ArrayList<>();
                            break;
                        case "0":
                            starsList.add(null);
                            break;
                        default:
                            String[] starproperties = line.split("#");
                            starsList.add(new Star(starproperties[0], starproperties[1], Boolean.parseBoolean(starproperties[2]), Float.parseFloat(starproperties[3]) ));
                            break;
                    }
                }
                scanner.close();
            }else {
                System.out.println("File "+file+" doesnt exist");
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }
    private void encodeFile(){
        File file = new File(getFilesDir(), "data.txt");
        Log.d("DEBUG", "Internal file path: " + file.getAbsolutePath());
        try {
            if (!file.exists()) {
                boolean created = file.createNewFile();
                if (created) {
                    System.out.println("File created successfully!");
                } else {
                    System.out.println("File already exists.");
                }
            }
            try (FileWriter writer = new FileWriter(file, false)) {
                for (StarCollection starCollection : starsCollections) {
                    writer.write(starCollection.name+"\n");
                    writer.write(starCollection.bgIco+"\n");
                    for (Star star : starCollection.stars) {
                        if (star==null){
                            writer.write("0\n");
                        }else {
                            writer.write(star.name + "#" + star.description + "#" + star.status + "#" + star.position+"\n");
                        }
                    }
                    writer.write("\n");
                }
                System.out.println("File written successfully!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("File location: " + file.getAbsolutePath());
    }
    private void copyDrawableToExternal(String drawableName, String outFileName) {
        int resId = getResources().getIdentifier(drawableName, "drawable", getPackageName());
        Log.d("BG_DEBUG", "Requested drawable: " + drawableName + " → resId = " + resId);

        if (resId == 0) {
            Log.e("BG_DEBUG", "Drawable NOT FOUND in resources: " + drawableName);
            return;
        }

        File outFile = new File(getExternalFilesDir(null), outFileName);
        Log.d("BG_DEBUG", "Target file path = " + outFile.getAbsolutePath());

        if (outFile.exists()) {
            Log.d("BG_DEBUG", "File already exists, size = " + outFile.length() + " bytes");
            return;
        }

        try (InputStream is = getResources().openRawResource(resId);
             FileOutputStream os = new FileOutputStream(outFile)) {

            byte[] buffer = new byte[4096];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }

            Log.d("BG_DEBUG", "Successfully copied → size = " + outFile.length() + " bytes");

        } catch (Exception e) {
            Log.e("BG_DEBUG", "Error copying drawable to external files", e);
        }
    }


    private void saveUserData(String username, String profileImageFile) {
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        prefs.edit()
                .putString(KEY_USERNAME, username)
                .putString(KEY_PROFILEIMG, profileImageFile)
                .apply();
    }

    private void loadUserData() {
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        userName = prefs.getString(KEY_USERNAME, "");
        profileimg = prefs.getString(KEY_PROFILEIMG, "");

    }

    public void cleanUpExternalFiles() {
        File externalDir = getExternalFilesDir(null);
        if (externalDir != null && externalDir.exists()) {
            deleteRecursive(externalDir);
            Log.d("CLEANUP", "All external files deleted.");
        } else {
            Log.d("CLEANUP", "External directory does not exist or is null.");
        }
    }

    private void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            File[] children = fileOrDirectory.listFiles();
            if (children != null) {
                for (File child : children) {
                    deleteRecursive(child);
                }
            }
        }
        boolean deleted = fileOrDirectory.delete();
        if (!deleted) {
            Log.w("CLEANUP", "Failed to delete: " + fileOrDirectory.getAbsolutePath());
        }
    }


}