package com.khoa.scheduleview;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Separate {
    public Paint separatePaint;

    public int strokeColor;
    public float strokeWidth;

    public float startX;
    public float startY;
    public float stopX;
    public float stopY;

    public Separate(int strokeColor, float strokeWidth) {
        this.strokeColor = strokeColor;
        this.strokeWidth = strokeWidth;

        separatePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        separatePaint.setStrokeWidth(strokeWidth);
        separatePaint.setColor(strokeColor);
    }

    public void draw(Canvas canvas){
        canvas.drawLine(startX, startY, stopX, stopY, separatePaint);
    }

}
