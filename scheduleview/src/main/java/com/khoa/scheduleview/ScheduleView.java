package com.khoa.scheduleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

public class ScheduleView extends ViewGroup implements View.OnTouchListener, View.OnLongClickListener, View.OnClickListener {

    private int DEFAULT_LABEL_TEXT_COLOR = Color.WHITE;
    private int DEFAULT_LABEL_BACKGROUND_COLOR = Color.parseColor("#0286b3");
    private int DEFAULT_SEPARATE_COLOR = Color.BLACK;
    private float DEFAULT_SEPARATE_WIDTH = Util.convertDpToPixel(getContext(), 0.2f);
    private float DEFAULT_LABEL_TEXT_SIZE = Util.convertDpToPixel(getContext(), 10);
    private float DEFAULT_PADDING_HORIZONTAL_LABEL = Util.convertDpToPixel(getContext(), 3);
    private float DEFAULT_PADDING_VERTICAL_LABEL = Util.convertDpToPixel(getContext(), 5);

    private int DEFAULT_SUBJECT_BACKGROUND_COLOR = Color.parseColor("#0286b3");
    private int DEFAULT_SUBJECT_TEXT_COLOR = Color.WHITE;
    private int DEFAULT_ROOM_TEXT_COLOR = Color.parseColor("#ffffff");
    private float DEFAULT_SUBJECT_TEXT_SIZE = Util.convertDpToPixel(getContext(), 9);

    private float widthDay;
    private float heightDay;

    private float widthLesson;
    private float heightLesson;

    private int labelTextColor = DEFAULT_LABEL_TEXT_COLOR;
    private int labelBackgroundColor = DEFAULT_LABEL_BACKGROUND_COLOR;
    private float labelTextSize = DEFAULT_LABEL_TEXT_SIZE;

    private int separateColor = DEFAULT_SEPARATE_COLOR;
    private float separateWidth = DEFAULT_SEPARATE_WIDTH;

    private float paddingHorizontalLabel = DEFAULT_PADDING_HORIZONTAL_LABEL;
    private float paddingVerticalLabel = DEFAULT_PADDING_VERTICAL_LABEL;

    private int subjectBackgroundColor = DEFAULT_SUBJECT_BACKGROUND_COLOR;
    private int subjectTextColor = DEFAULT_SUBJECT_TEXT_COLOR;
    private float subjectTextSize = DEFAULT_SUBJECT_TEXT_SIZE;
    private int roomTextColor = DEFAULT_ROOM_TEXT_COLOR;

    private Label[] days;
    private Label[] lessons;

    private Separate[] horSeparates;
    private Separate[] verSeparates;

    private SimpleSubject[] simpleSubjects;
    private ItemAdd itemAdd;

    private int currentIndex = -1;
    private int selectesDay = 2;
    private int selectedStartLesson = 1;

    private boolean addableEvent = false;

    private OnScheduleClickListener listener;

    public ScheduleView(Context context) {
        super(context);
        init();
    }

    public ScheduleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScheduleView);
        labelTextSize = typedArray.getDimension(R.styleable.ScheduleView_schedule_labelTextSize, DEFAULT_LABEL_TEXT_SIZE);
        labelBackgroundColor = typedArray.getColor(R.styleable.ScheduleView_schedule_labelBackgroundColor, DEFAULT_LABEL_BACKGROUND_COLOR);
        labelTextColor = typedArray.getColor(R.styleable.ScheduleView_schedule_labelTextColor, DEFAULT_LABEL_TEXT_COLOR);

        separateColor = typedArray.getColor(R.styleable.ScheduleView_schedule_separateColor, DEFAULT_SEPARATE_COLOR);
        separateWidth = typedArray.getDimension(R.styleable.ScheduleView_schedule_separateWidth, DEFAULT_SEPARATE_WIDTH);

        paddingHorizontalLabel = typedArray.getDimension(R.styleable.ScheduleView_schedule_paddingHorizontalLabel, DEFAULT_PADDING_HORIZONTAL_LABEL);
        paddingVerticalLabel = typedArray.getDimension(R.styleable.ScheduleView_schedule_paddingVerticalLabel, DEFAULT_PADDING_VERTICAL_LABEL);

        subjectBackgroundColor = typedArray.getColor(R.styleable.ScheduleView_schedule_subjectBackgroundColor, DEFAULT_SUBJECT_BACKGROUND_COLOR);
        subjectTextColor = typedArray.getColor(R.styleable.ScheduleView_schedule_subjectTextColor, DEFAULT_SUBJECT_TEXT_COLOR);
        subjectTextSize = typedArray.getDimension(R.styleable.ScheduleView_schedule_subjectTextSize, DEFAULT_SUBJECT_TEXT_SIZE);
        roomTextColor = typedArray.getColor(R.styleable.ScheduleView_schedule_roomTextColor, DEFAULT_ROOM_TEXT_COLOR);

        addableEvent = typedArray.getBoolean(R.styleable.ScheduleView_schedule_addableEvent, false);

        typedArray.recycle();
        init();
    }

    public ScheduleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ScheduleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        this.setOnTouchListener(this);
        this.setOnLongClickListener(this);
        this.setOnClickListener(this);

        itemAdd = new ItemAdd();
        simpleSubjects = new SimpleSubject[0];

        horSeparates = new Separate[8];
        for (int i = 0; i < horSeparates.length; i++)
            horSeparates[i] = new Separate(separateColor, separateWidth);

        verSeparates = new Separate[12];
        for (int i = 0; i < verSeparates.length; i++)
            verSeparates[i] = new Separate(separateColor, separateWidth);

        days = new Label[7];
        for (int i = 0; i < days.length; i++) {
            String name = "";
            if (i == days.length - 1) name = "CN";
            else name = "Thứ " + (i + 2);

            days[i] = new Label(name, labelBackgroundColor, labelTextColor, labelTextSize, paddingHorizontalLabel, paddingVerticalLabel);
        }

        lessons = new Label[12];
        for (int i = 0; i < lessons.length; i++) {
            String name = "Tiết " + (i + 1);
            lessons[i] = new Label(name, labelBackgroundColor, labelTextColor, labelTextSize - 5, paddingHorizontalLabel, paddingVerticalLabel);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        widthDay = (float) getWidth() / 15 * 2;
        widthLesson = widthDay / 2;

        heightLesson = (float) getHeight() / 25 * 2;
        heightDay = heightLesson / 2;

        for (int i = 0; i < days.length; i++) {
            if (i == 0) days[i].rect.left = widthLesson;
            else days[i].rect.left = days[i - 1].rect.right;

            days[i].rect.right = days[i].rect.left + widthDay;
            days[i].rect.top = 0;
            days[i].rect.bottom = heightDay;

        }

        for (int i = 0; i < lessons.length; i++) {
            if (i == 0) {
                lessons[i].rect.top = heightDay;
            } else {
                lessons[i].rect.top = lessons[i - 1].rect.bottom;
            }

            lessons[i].rect.bottom = lessons[i].rect.top + heightLesson;
            lessons[i].rect.left = 0;
            lessons[i].rect.right = widthLesson;
        }

        for (int i = 0; i < horSeparates.length; i++) {
            horSeparates[i].startX = horSeparates[i].stopX = widthLesson + i * widthDay;
            horSeparates[i].startY = 0;
            horSeparates[i].stopY = getHeight();
        }

        for (int i = 0; i < verSeparates.length; i++) {
            verSeparates[i].startX = 0;
            verSeparates[i].stopX = getWidth();
            verSeparates[i].startY = verSeparates[i].stopY = heightDay + i * heightLesson;
        }

        for (int i = 0; i < simpleSubjects.length; i++) {
            simpleSubjects[i].labelSubject = new LabelSubject(simpleSubjects[i].subjectName, simpleSubjects[i].roomName, subjectBackgroundColor, subjectTextColor, subjectTextSize, roomTextColor, paddingHorizontalLabel, paddingVerticalLabel);

            simpleSubjects[i].labelSubject.rect.top = heightDay + (simpleSubjects[i].startLesson - 1) * heightLesson + 1;
            simpleSubjects[i].labelSubject.rect.bottom = simpleSubjects[i].labelSubject.rect.top + heightLesson * simpleSubjects[i].durationLesson - 2;
            simpleSubjects[i].labelSubject.rect.left = widthLesson + (simpleSubjects[i].day - 2) * widthDay + 1;
            simpleSubjects[i].labelSubject.rect.right = simpleSubjects[i].labelSubject.rect.left + widthDay - 2;
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawDays(canvas);
        drawLessons(canvas);

        drawHorSeparate(canvas);
        drawVerSeparate(canvas);

        drawSubjects(canvas);

        drawItemAdd(canvas);
    }

    private void drawDays(Canvas canvas) {
        for (Label day : days) {
            day.draw(canvas);
        }
    }

    private void drawLessons(Canvas canvas) {
        for (Label lesson : lessons) {
            lesson.draw(canvas);
        }
    }

    private void drawHorSeparate(Canvas canvas) {
        for (Separate separate : horSeparates) separate.draw(canvas);
    }

    private void drawVerSeparate(Canvas canvas) {
        for (Separate separate : verSeparates) separate.draw(canvas);
    }

    private void drawSubjects(Canvas canvas) {
        for (SimpleSubject subject : simpleSubjects) subject.draw(canvas);
    }

    private void drawItemAdd(Canvas canvas) {
        if (itemAdd.show) itemAdd.draw(canvas);
    }

    public void setSubjects(SimpleSubject[] subs) {
        simpleSubjects = subs;
    }

    public void setOnScheduleClickListener(OnScheduleClickListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        float x = motionEvent.getX();
        float y = motionEvent.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                currentIndex = computeCurrentClick(x, y);
                if (currentIndex >= 0) fadeOutActionDown();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (currentIndex >= 0) {
                    fadeInActionUp();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (currentIndex >= 0) {
                    fadeInActionUp();
                }
                break;

            default:
                break;
        }

        return false;
    }

    private int computeCurrentClick(float x, float y) {
        for (int i = 0; i < simpleSubjects.length; i++) {
            if (x > simpleSubjects[i].labelSubject.rect.left && x < simpleSubjects[i].labelSubject.rect.right) {
                if (y > simpleSubjects[i].labelSubject.rect.top && y < simpleSubjects[i].labelSubject.rect.bottom) {
                    return i;
                }
            }
        }

        if (x > itemAdd.rectF.left && x < itemAdd.rectF.right && y > itemAdd.rectF.top && y < itemAdd.rectF.bottom) {
            return -2;
        }

        selectesDay = (int) ((x - widthLesson) / widthDay) + 2;
        selectedStartLesson = (int) ((y - heightDay) / heightLesson) + 1;

        return -1;
    }

    private void showItemAdd() {
        if (selectesDay < 2 || selectedStartLesson < 1) return;

        itemAdd.show = true;

        itemAdd.rectF.left = widthLesson + (selectesDay - 2) * widthDay;
        itemAdd.rectF.right = itemAdd.rectF.left + widthDay;
        itemAdd.rectF.top = heightDay + (selectedStartLesson - 1) * heightLesson;
        itemAdd.rectF.bottom = itemAdd.rectF.top + heightLesson;

        invalidate();
    }

    private void fadeOutActionDown() {
        if (currentIndex < 0) return;

        SimpleSubject subject = simpleSubjects[currentIndex];
        subject.labelSubject.setAlpha((int) (255 * 0.5));
        invalidate();
    }

    private void fadeInActionUp() {
        if (currentIndex < 0) return;

        SimpleSubject subject = simpleSubjects[currentIndex];
        subject.labelSubject.setAlpha(255);
        invalidate();
    }

    @Override
    public void onClick(View view) {
        if (addableEvent && currentIndex < 0) {
            if (currentIndex == -1) {
                showItemAdd();
            } else if (currentIndex == -2) {
                itemAdd.show = false;
                itemAdd.rectF.left = itemAdd.rectF.right = itemAdd.rectF.top = itemAdd.rectF.bottom = 0;
                invalidate();
                onClickAddEvent(selectesDay, selectedStartLesson);
            }
        } else if (currentIndex >= 0) {
            onClickSubject();
        }
    }

    @Override
    public boolean onLongClick(View view) {
        onLongClickSubject();
        return true;
    }

    private void onClickSubject() {
        if (currentIndex < 0) return;
        if (listener == null) return;

        listener.onClickSubject(currentIndex);
    }

    public void onLongClickSubject() {
        if (currentIndex < 0) return;
        if (listener == null) return;

        listener.onLongClickSubject(currentIndex);
    }

    public void onClickAddEvent(int day, int startLesson) {
        if (listener == null) return;
        listener.onClickAddEvent(day, startLesson);
    }
}
