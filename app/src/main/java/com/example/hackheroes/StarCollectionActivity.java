package com.example.hackheroes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StarCollectionActivity extends AppCompatActivity {

    Button profileButton;
    Button hideButton;
    ConstraintLayout footer;
    ConstraintLayout buttonL;
    Boolean up = false;
    StarCollection sc;
    TextView title;
    Button deleteSc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.star_collection);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.star_collection), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sc = (StarCollection) getIntent().getSerializableExtra("sc");

        profileButton = findViewById(R.id.gobackB);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundEffects.playButton();
                Intent intent = new Intent(StarCollectionActivity.this, MainActivity.class);
                intent.putExtra("sc",sc);
                startActivity(intent);
            }
        });
        hideButton = findViewById(R.id.hideB);
        footer = findViewById(R.id.footer);
        buttonL = findViewById(R.id.buttonL);
        StarCollectionActivity context = this;
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

        deleteSc = findViewById(R.id.deleteSc);
        deleteSc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundEffects.playButton();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("are you sure?");
                builder.setMessage("Are you sure you want to delete this Star Collection?");

                builder.setPositiveButton("delete", ((dialog, which) -> {
                    Intent intent = new Intent(StarCollectionActivity.this, MainActivity.class);
                    intent.putExtra("Shit_to_delte", sc);
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


        title = findViewById(R.id.textView);
        title.setText(sc.name);
        drawSingleCollection(sc);
    }
    private void drawSingleCollection(StarCollection sc) {

        LinearLayout container = findViewById(R.id.Linearparent);
        container.removeAllViews();

        FrameLayout wrapper = new FrameLayout(this);
        FrameLayout.LayoutParams wrapperParams =
                new FrameLayout.LayoutParams(dp(300), FrameLayout.LayoutParams.WRAP_CONTENT);
        wrapperParams.gravity = Gravity.CENTER_HORIZONTAL;
        wrapperParams.setMargins(0, dp(24), 0, dp(24));
        wrapper.setLayoutParams(wrapperParams);

        container.addView(wrapper);

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

        if (imgFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            bg.setImageBitmap(bitmap);
        } else {
            bg.setImageResource(R.drawable.guitar);
        }

        bg.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        bg.setColorFilter(Color.argb(255, 255, 255, 255));
        bg.setAlpha(alpha);

        wrapper.addView(bg, 0);

        LineView lineView = new LineView(this);
        wrapper.addView(lineView,
                new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                ));

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(16), dp(16), dp(16), dp(16));
        wrapper.addView(content);

        List<LineView.Node> nodes = new ArrayList<>();

        int index = 0;
        for (Star star : sc.stars) {

            ConstraintLayout row = new ConstraintLayout(this);
            row.setPadding(0, dp(8), 0, dp(8));

            LinearLayout.LayoutParams rowParams =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            dp((500 - 70) / sc.stars.size())
                    );
            rowParams.setMargins(0, dp(8), 0, 0);
            content.addView(row, rowParams);

            if (star == null) {
                index++;
                continue;
            }

            // icon
            ImageView icon = new ImageView(this);
            icon.setId(View.generateViewId());
            icon.setImageResource(R.drawable.starico2);
            if (!star.status) icon.setAlpha(0.3f);

            icon.setSoundEffectsEnabled(false);

            int finalIndex = index;
            icon.setOnClickListener(v -> {
                SoundEffects.playButton();
                boolean canComplete = true;
                if (finalIndex != sc.stars.size()) {
                    for (int i = 0; i < finalIndex; i++) {
                        Star prev = sc.stars.get(i);
                        if (prev != null && !prev.status) {
                            canComplete = false;
                            break;
                        }
                    }
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(star.name);
                String message = (star.description != null ? star.description : "No description.");
                if (!canComplete) {
                    message += "\nCannot complete: previous stars are not completed.";
                }
                builder.setMessage(message);

                builder.setPositiveButton("Complete", null);
                builder.setNegativeButton("Incomplete", (dialog, which) -> {
                    star.status = false;
                    if (finalIndex != sc.stars.size()) {
                        for (int i = finalIndex; i < sc.stars.size(); i++) {
                            Star next = sc.stars.get(i);
                            if (next != null) next.status = false;
                        }
                    }
                    drawSingleCollection(sc);
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                boolean finalCanComplete = canComplete;
                positiveButton.setOnClickListener(view -> {
                    SoundEffects.playButton();
                    if (finalCanComplete) {
                        star.status = true;
                        drawSingleCollection(sc);
                        dialog.dismiss();
                    } else {
                    }
                });
            });



            ConstraintLayout.LayoutParams iconParams =
                    new ConstraintLayout.LayoutParams(0, 0);

            iconParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
            iconParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
            iconParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;

            iconParams.height = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT;
            iconParams.dimensionRatio = "1:1";

            row.addView(icon, iconParams);

            TextView name = new TextView(this);
            name.setText(star.name);
            name.setTextColor(Color.WHITE);

            name.setId(View.generateViewId());

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
                int[] wrapLoc = new int[2];

                row.getLocationOnScreen(rowLoc);
                wrapper.getLocationOnScreen(wrapLoc);

                float cx = (rowLoc[0] - wrapLoc[0]) + icon.getX() + iconSize / 2f;
                float cy = (rowLoc[1] - wrapLoc[1]) + icon.getY() + iconSize / 2f;

                nodes.add(new LineView.Node(cx, cy, star.status));
                lineView.setNodes(nodes);
            });

            index++;
        }
    }

    private int dp(int v) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(v * density);
    }

}
