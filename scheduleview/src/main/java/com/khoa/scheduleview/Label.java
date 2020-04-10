package com.khoa.scheduleview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.Log;

public class Label {

    public String name;

    public float width;
    public float height;

    public float widthText;
    public float heightText;

    public float x;
    public float y;

    public int backgroundColor;
    public int textColor;
    public float textSize;

    public float paddingHorizontal;
    public float paddingVertical;

    public RectF rect;

    public TextPaint textPaint;
    public Paint rectPaint;

    public Label(String name, int backgroundColor, int textColor, float textSize, float paddingHorizontal, float paddingVertical) {
        this.name = name;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.textSize = textSize;
        this.paddingHorizontal = paddingHorizontal;
        this.paddingVertical = paddingVertical;

        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textSize);
        textPaint.setColor(textColor);

        rect = new RectF();

        rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectPaint.setColor(backgroundColor);
        rectPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        computeSize();
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(rect, rectPaint);

        x = rect.left + (rect.right - rect.left) / 2 - width / 2;
        y = rect.top + (rect.bottom - rect.top) / 2 + height / 2;
        canvas.drawText(name, x, y, textPaint);
    }

    private void computeSize() {
        Rect r = new Rect();
        textPaint.getTextBounds(name, 0, name.length(), r);
        width = r.width();
        height = r.height();
    }
}
