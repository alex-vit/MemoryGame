package com.alexvit.memorygame;

import android.graphics.PorterDuff;
import android.os.Handler;
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

    private final Handler handler = new Handler();
    private final int DELAY = 2000;

    // game state
    private Button selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        board = (SquareGridLayout) findViewById(R.id.board);

        addButtons();
        reset(board);
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
        final Button button = (Button) findViewById(id);

        if (noneSelected()) {
            // flipped 1st tile
            select(button);
        } else if (sameSelected(button)) {
            // same button clicked twice
            Log.v(LOG_TAG, "same button clicked twice");
        } else {
            // flipped 2nd tile
            paintButton(button);
            boolean match = matchingTiles(button, selected);
            if (match) {
                class HideRunnable implements Runnable {
                    private final Button b1;
                    private final Button b2;

                    public HideRunnable(Button b1, Button b2) {
                        this.b1 = b1;
                        this.b2 = b2;
                    }
                    @Override
                    public void run() {
                        hideButton(b1);
                        hideButton(b2);
                    }
                }
                // remove both
                handler.postDelayed(new HideRunnable(button, selected), DELAY);
            } else {
                // flip both
                class ResetRunnable implements Runnable {
                    private final Button b1;
                    private final Button b2;

                    public ResetRunnable(Button b1, Button b2) {
                        this.b1 = b1;
                        this.b2 = b2;
                    }
                    @Override
                    public void run() {
                        resetColor(b1);
                        resetColor(b2);
                    }
                }
                handler.postDelayed(new ResetRunnable(button, selected), DELAY);
            }
            selected = null;
        }

        Log.v(LOG_TAG, "Button " + id + " clicked. Color: " + COLORS[id % 8]);

    }

    private void hideButton(Button button) {
        button.setVisibility(View.INVISIBLE);
    }
    private void showButton(Button button) {
        button.setVisibility(View.VISIBLE);
    }

    private boolean sameSelected(Button button) {
        return selected == button;
    }

    private void select(Button button) {
        selected = button;
        paintButton(button);
    }

    private void paintButton(Button button) {
        int id = button.getId();
        int tile = tiles.get(id);
        int color = COLORS[tile];
        button.getBackground().setColorFilter(
                ContextCompat.getColor(this, color),
                PorterDuff.Mode.MULTIPLY
        );
    }

    private boolean noneSelected() {
        return sameSelected(null);
    }

    private boolean matchingTiles(Button b1, Button b2) {
        int tile1 = tiles.get(b1.getId());
        int tile2 = tiles.get(b2.getId());
        return tile1 == tile2;
    }

    public void reset(View v) {
        moves = 0;
        selected = null;
        Collections.shuffle(tiles);

        grayOutAllButtons();
    }

    private void grayOutAllButtons() {
        int childCount = board.getChildCount();
        for (int i = 0; i < childCount; i++) {
            Button button = (Button) board.getChildAt(i);
            showButton(button);
            resetColor(button);
        }
    }

    private void resetColor(Button button) {
        button.getBackground().setColorFilter(null);
    }
}
