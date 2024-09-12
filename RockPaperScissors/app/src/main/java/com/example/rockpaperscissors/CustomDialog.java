package com.example.rockpaperscissors;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import Signs.Signs;

public class CustomDialog extends Dialog {

    private Signs userSigns;
    private Signs enemySigns;

    public CustomDialog(@NonNull Context context, Signs userSigns, Signs enemySigns) {
        super(context);
        this.userSigns = userSigns;
        this.enemySigns = enemySigns;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (userSigns == enemySigns)
        {
            setContentView(R.layout.draw_dialog_layout);
        } else if ((userSigns == Signs.ROCK && enemySigns == Signs.SCISSORS) ||
                (userSigns == Signs.SCISSORS && enemySigns == Signs.PAPER) ||
                (userSigns == Signs.PAPER && enemySigns == Signs.ROCK)) {
            setContentView(R.layout.win_dialog_layout);
        }
        else {
            setContentView(R.layout.lose_dialog_layout);
        }
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // Закрываем диалог через 2 секунды
        getWindow().getDecorView().postDelayed(this::dismiss, 2000);
    }
}
