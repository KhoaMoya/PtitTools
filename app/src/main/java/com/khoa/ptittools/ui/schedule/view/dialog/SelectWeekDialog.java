package com.khoa.ptittools.ui.schedule.view.dialog;

import android.content.Context;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.khoa.ptittools.R;
import com.khoa.ptittools.base.dialog.MyDialog;
import com.khoa.ptittools.base.model.Week;

import java.util.List;

public class SelectWeekDialog extends MyDialog {

    private List<Week> mListWeek;
    private RadioGroup mRadioGroup;
    private int mCurrentIndex;

    public SelectWeekDialog(Context context, int currentIndex, List<Week> listWeek) {
        super(context);
        this.mCurrentIndex = currentIndex;
        this.mListWeek = listWeek;

        setContentView(R.layout.dialog_select_tuan);
        mRadioGroup = findViewById(R.id.group);
    }


    public void showSelectWeekDialog(final OnCheckWeekRadioButtonListener listener) {
        for (int i = 0; i < mListWeek.size(); i++) {
            Week week = mListWeek.get(i);
            mRadioGroup.addView(createItem(week.weekName + " : " + week.startDate + " - " + week.endDate), i);
        }
        RadioButton radioButton = (RadioButton) mRadioGroup.getChildAt(mCurrentIndex);
        radioButton.setChecked(true);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                RadioButton checkedRB = radioGroup.findViewById(id);
                int index = radioGroup.indexOfChild(checkedRB);
                listener.onWeekSelected(index);
                dismiss();
            }
        });
        show();
    }

    private RadioButton createItem(String title) {
        RadioButton radioButton = new RadioButton(getContext());
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 10, 0, 0);
        radioButton.setLayoutParams(params);
        radioButton.setText(title);
        radioButton.setBackground(getContext().getResources().getDrawable(R.drawable.background_clickable));
        return radioButton;
    }

    public interface OnCheckWeekRadioButtonListener {
        void onWeekSelected(int index);
    }
}
