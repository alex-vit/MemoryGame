package com.alexvit.util.layouts;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridLayout;

public class SquareGridLayout extends GridLayout {
    public SquareGridLayout(Context context) {
        super(context);
    }

    public SquareGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Become square, with smaller side size.
     */
    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int widthPx = MeasureSpec.getSize(widthSpec);
        int heightPx = MeasureSpec.getSize(heightSpec);
        int smallerSpec = widthPx < heightPx ? widthSpec : heightSpec;
        setMeasuredDimension(smallerSpec, smallerSpec);
    }
}