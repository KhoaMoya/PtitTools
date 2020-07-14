package com.khoa.ptittools.ui.note.view;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.khoa.ptittools.R;

public class NoteFragment extends Fragment {


    public static final String TAG = "ghichu_fragment";
    public NoteFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ghi_chu, container, false);
    }

}
