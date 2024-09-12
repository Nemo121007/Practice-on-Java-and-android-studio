package com.example.rockpaperscissors;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.Gravity;

import java.util.Random;

import GameSettings.GameSettings;
import Signs.Signs;
import kotlin.time.Duration;

public class MainActivity extends AppCompatActivity {

    ImageButton canselButton, settingsButton, rockButton, scissorsButton, paperButton;
    TextView textMaxScore, enemyTextScore, separatorScore, userTextScore;
    ImageView enemy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        textMaxScore = findViewById(R.id.textMaxScore);
        enemyTextScore = findViewById(R.id.enemyTextScore);
        separatorScore = findViewById(R.id.separatorScore);
        userTextScore = findViewById(R.id.userTextScore);

        textMaxScore.setText(String.valueOf(GameSettings.getMaxScore()));
        enemyTextScore.setText(String.valueOf(GameSettings.getEnemyScore()));
        separatorScore.setText(":");
        userTextScore.setText(String.valueOf(GameSettings.getUserScore()));

        enemy = findViewById(R.id.enemy);

        canselButton = findViewById(R.id.canselButton);
        settingsButton = findViewById(R.id.settingsButton);
        rockButton = findViewById(R.id.rockButton);
        scissorsButton = findViewById(R.id.scissorsButton);
        paperButton = findViewById(R.id.paperButton);

        this.canselButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        this.settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        this.rockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rockButton.setBackgroundResource(R.drawable.green_border);
                SelectionAnalysis(Signs.ROCK);
            }
        });

        this.scissorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scissorsButton.setBackgroundResource(R.drawable.green_border);
                SelectionAnalysis(Signs.SCISSORS);
            }
        });

        this.paperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paperButton.setBackgroundResource(R.drawable.green_border);
                SelectionAnalysis(Signs.PAPER);
            }
        });
    }

    private void SelectionAnalysis (Signs userSigns){

        Random random = new Random();
        int randomNumber = random.nextInt(3) + 1;

        Signs enemySigns = Signs.fromValue(randomNumber);

        switch (enemySigns) {
            case ROCK:
                enemy.setImageResource(R.drawable.rock);
                break;
            case SCISSORS:
                enemy.setImageResource(R.drawable.scissors);
                break;
            case PAPER:
                enemy.setImageResource(R.drawable.paper);
                break;
        }

        enemy.postDelayed(new Runnable() {
            @Override
            public void run() {
                enemy.setImageDrawable(getResources().getDrawable(R.drawable.question));
            }
        }, 4000);

        if (userSigns == enemySigns) {
            // Ничья
        }
        else if ((userSigns == Signs.ROCK && enemySigns == Signs.SCISSORS) ||
                (userSigns == Signs.SCISSORS && enemySigns == Signs.PAPER) ||
                (userSigns == Signs.PAPER && enemySigns == Signs.ROCK)) {
            // Победа пользователя

            GameSettings.setUserScore(GameSettings.getUserScore() + 1);
            userTextScore.setText(String.valueOf(GameSettings.getUserScore()));
        }
        else {
            // Победа противника
            GameSettings.setEnemyScore(GameSettings.getEnemyScore() + 1);
            enemyTextScore.setText(String.valueOf(GameSettings.getEnemyScore()));
        }

        if (GameSettings.getUserScore() == GameSettings.getMaxScore() || GameSettings.getEnemyScore() == GameSettings.getMaxScore()) {
            EndDialog endDialog = new EndDialog(this);
            endDialog.show();

            GameSettings.setUserScore(0);
            GameSettings.setEnemyScore(0);

            enemyTextScore.setText(String.valueOf(GameSettings.getEnemyScore()));
            userTextScore.setText(String.valueOf(GameSettings.getUserScore()));
        }
        else{
            // Показать диалог с результатом
            CustomDialog dialog = new CustomDialog(this, userSigns, enemySigns);
            dialog.show();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        int a = GameSettings.getMaxScore();
        textMaxScore.setText(String.valueOf(GameSettings.getMaxScore()));
    }
}

