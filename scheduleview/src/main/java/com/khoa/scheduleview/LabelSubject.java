package com.khoa.scheduleview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.Log;

public class LabelSubject extends Label {

    public String roomName;
    public TextPaint textPaintRoom;
    public int colorRoom;

    public LabelSubject(String subjectName, String roomName, int backgroundColor, int textColor, float textSize, int colorRoom, float paddingHorizontal, float paddingVertical) {
        super(subjectName, backgroundColor, textColor, textSize, paddingHorizontal, paddingVertical);

        this.roomName = roomName;
        this.colorRoom = colorRoom;

        textPaintRoom = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaintRoom.setTextSize(textSize);
        textPaintRoom.setColor(colorRoom);
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(rect, rectPaint);

        widthText =  rect.right - rect.left - 2 * paddingHorizontal;
        heightText = rect.bottom - rect.top - 2 * paddingVertical;

        Rect r = new Rect();
        textPaint.getTextBounds("hg", 0, 2, r);
        float h = r.height();

        float widthSpace = textPaint.measureText(" ");

        String[] words = name.split(" ");
        float[] lengths = new float[words.length];
        for (int i = 0; i < words.length; i++) {
            lengths[i] = textPaint.measureText(words[i]);
        }

        x = rect.left + paddingHorizontal;
        y = rect.top + h + paddingVertical;

        if (textPaint.measureText(roomName) > widthText) {
            canvas.drawText("...", x, y, textPaint);
            return;
        }

        if (h > heightText) {
            canvas.drawText("...", x, y, textPaint);
            return;
        }

        // draw room name
        canvas.drawText(roomName , x, y, textPaintRoom);

        // draw subject name
        y += h + 2*widthSpace;      // down line
        for (int i = 0; i < words.length; i++) {

            // new line
            if ((x + widthSpace + lengths[i]) > (rect.right - paddingHorizontal)) {
                x = rect.left + paddingHorizontal;
                y += h + widthSpace;

                if (y > (rect.bottom - paddingVertical)) {
                    break;
                } else {
                    canvas.drawText(words[i] + " ", x, y, textPaint);
                    x += widthSpace + lengths[i];
                }

            }
            // in line
            else {
                canvas.drawText(words[i] + " ", x, y, textPaint);
                x += widthSpace + lengths[i];
            }
        }
    }

    public void setAlpha(int alpha){
        textPaint.setAlpha(alpha);
        rectPaint.setAlpha(alpha);
        textPaintRoom.setAlpha(alpha);
    }
}
