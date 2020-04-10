package com.khoa.ptittools.exam.adapter;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khoa.ptittools.base.model.Exam;
import com.khoa.ptittools.databinding.ItemExamBinding;

import java.util.List;

public class ExamRecyclerViewAdapter extends RecyclerView.Adapter<ExamRecyclerViewAdapter.ExamViewHoler> {

    private List<Exam> examList;

    public ExamRecyclerViewAdapter(List<Exam> examList) {
        this.examList = examList;
    }

    @NonNull
    @Override
    public ExamViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemExamBinding binding = ItemExamBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ExamViewHoler(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamViewHoler holder, int position) {
        Exam exam = examList.get(holder.getAdapterPosition());
        holder.bind(exam);
    }

    @Override
    public int getItemCount() {
        return examList.size();
    }

    public class ExamViewHoler extends RecyclerView.ViewHolder {

        ItemExamBinding binding;

        public ExamViewHoler(@NonNull ItemExamBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Exam exam) {
            binding.txtTitle.setText(exam.tenMon);
            binding.txtNgayThi.setText(exam.ngayThi);
            binding.txtGio.setText(exam.gioBD);
            binding.txtPhong.setText(exam.phong);
            binding.txtSoLuong.setText("Số lượng " + exam.soLuong);
            binding.txtSoPhut.setText(exam.soPhut + " phút");
            binding.txtGhiChu.setText(exam.ghiChu);

//            binding.getRoot().setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View view, MotionEvent motionEvent) {
//                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                        view.setScaleX(0.95f);
//                        view.setScaleY(0.95f);
//                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
//                        view.setScaleX(1f);
//                        view.setScaleY(1f);
//                    }
//                    return true;
//                }
//            });
        }
    }
}
