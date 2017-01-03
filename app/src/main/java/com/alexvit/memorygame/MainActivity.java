package com.alexvit.memorygame;

import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import com.alexvit.util.layouts.SquareGridLayout;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int NUM_CELLS = 16;

    private static final int[] COLORS = {
            android.R.color.holo_blue_dark,
            android.R.color.holo_blue_light,
            android.R.color.holo_green_dark,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_dark,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light,
            android.R.color.holo_purple
    };

    private int moves;
    private final List<Integer> tiles = Arrays.asList(0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7);
    private SquareGridLayout board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        board = (SquareGridLayout) findViewById(R.id.board);

        addButtons();
        reset();
    }

    private void addButtons() {
        board.removeAllViews();

        int total = 16;
        int column = 4;

        board.setColumnCount(column);
        board.setRowCount(column);

        for (int i = 0, c = 0, r = 0; i < total; i++, c++) {
            if (c == column) {
                c = 0;
                r++;
            }

            Button button = new Button(this);
            SquareGridLayout.LayoutParams params = new SquareGridLayout.LayoutParams();
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.width = GridLayout.LayoutParams.WRAP_CONTENT;
            params.columnSpec = SquareGridLayout.spec(c, 1f);
            params.rowSpec = SquareGridLayout.spec(r, 1f);
            button.setLayoutParams(params);
            button.setId(i);

            View.OnClickListener buttonOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClicked(v.getId());
                }
            };
            button.setOnClickListener(buttonOnClickListener);

            board.addView(button);
        }
    }

    private void buttonClicked(int id) {
        Log.v(LOG_TAG, "Button " + id + " clicked. Color: " + COLORS[id % 8]);
        Button button = (Button) findViewById(id);
        button.getBackground().setColorFilter(
                ContextCompat.getColor(this, COLORS[id % 8]),
                PorterDuff.Mode.MULTIPLY
        );
    }

    private void reset() {
        moves = 0;
        Collections.shuffle(tiles);
    }}
