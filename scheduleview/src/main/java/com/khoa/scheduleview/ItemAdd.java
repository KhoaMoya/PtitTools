package com.khoa.scheduleview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class ItemAdd {

    public boolean show;

    public RectF rectF;
    public Paint paintRect;

    public Paint paintLine;

    public ItemAdd() {
        paintRect = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintRect.setColor(Color.parseColor("#1A000000"));
        paintRect.setStyle(Paint.Style.FILL);

        paintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintLine.setColor(Color.BLACK);
        paintLine.setStyle(Paint.Style.FILL);
        paintLine.setStrokeWidth(3);

        rectF = new RectF();
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(rectF, paintRect);

        float legth = Math.min(rectF.height() * 0.4f, rectF.width() * 0.4f);
        canvas.drawLine(rectF.centerX(), rectF.centerY()-legth/2, rectF.centerX(), rectF.centerY() + legth/2, paintLine);
        canvas.drawLine(rectF.centerX() - legth/2, rectF.centerY(), rectF.centerX() + legth/2, rectF.centerY(), paintLine);
    }
}
