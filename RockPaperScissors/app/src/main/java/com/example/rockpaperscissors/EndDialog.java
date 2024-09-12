package com.example.rockpaperscissors;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import GameSettings.GameSettings;

public class EndDialog extends Dialog {

    public EndDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (GameSettings.getMaxScore() == GameSettings.getUserScore())
        {
            setContentView(R.layout.win_game_dialog_layout);
        }
        else {
            setContentView(R.layout.lose_game_dialog_layout);
        }

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // Закрываем диалог через 2 секунды
        getWindow().getDecorView().postDelayed(this::dismiss, 2000);
    }
}
