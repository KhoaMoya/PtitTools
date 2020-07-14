package com.khoa.ptittools.ui.score.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.khoa.ptittools.R;
import com.khoa.ptittools.base.model.SemesterScore;
import com.khoa.ptittools.base.model.SubjectScore;
import com.khoa.recycleviewexpandable.Adapter.ExpandableRecyclerAdapter;
import com.khoa.recycleviewexpandable.Model.ParentListItem;
import com.khoa.recycleviewexpandable.ViewHolder.ChildViewHolder;
import com.khoa.recycleviewexpandable.ViewHolder.ParentViewHolder;

import java.util.List;

public class SemesterScoreRecycleViewAdapter extends ExpandableRecyclerAdapter<SemesterScoreRecycleViewAdapter.SemesterViewHolder, SemesterScoreRecycleViewAdapter.SubjectViewHolder> {

    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATED_POSITION = 90f;
    private LayoutInflater mLayoutInflater;

    public SemesterScoreRecycleViewAdapter(Context context, @NonNull List<? extends ParentListItem> semesterScoreList) {
        super(semesterScoreList);
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public void setParentListItem(List<? extends ParentListItem> semesterScoreList) {
        notifyParentItemRangeRemoved(0, this.mParentItemList.size());
        this.mParentItemList = semesterScoreList;
        notifyParentItemRangeInserted(0, this.mParentItemList.size());
    }

    @Override
    public SemesterViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View view = mLayoutInflater.inflate(R.layout.item_parent_semester_score, parentViewGroup, false);
        return new SemesterViewHolder(view);
    }

    @Override
    public SubjectViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View view = mLayoutInflater.inflate(R.layout.item_child_subject_score, childViewGroup, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(SemesterViewHolder parentViewHolder, int position, ParentListItem parentListItem) {
        parentViewHolder.bind((SemesterScore) parentListItem);
    }

    @Override
    public void onBindChildViewHolder(SubjectViewHolder childViewHolder, int position, Object childListItem) {
        childViewHolder.bind((SubjectScore) childListItem);
    }

    public static class SemesterViewHolder extends ParentViewHolder {

        TextView txtTenHocKy;
        ImageView imgExpand;

        SemesterViewHolder(View itemView) {
            super(itemView);
            txtTenHocKy = itemView.findViewById(R.id.txt_ten_hoc_ky);
            imgExpand = itemView.findViewById(R.id.img_expand);
        }

        void bind(SemesterScore hocKy) {
            txtTenHocKy.setText(hocKy.tenHocKy);
        }

        @Override
        public void setExpanded(boolean expanded) {
            super.setExpanded(expanded);
            if (expanded) imgExpand.setRotation(90);
            else imgExpand.setRotation(0);
        }

        @Override
        public void onExpansionToggled(boolean expanded) {
            super.onExpansionToggled(expanded);
            RotateAnimation rotateAnimation;
            if (expanded) { // rotate clockwise
                rotateAnimation = new RotateAnimation(ROTATED_POSITION, INITIAL_POSITION,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            } else { // rotate counterclockwise
                rotateAnimation = new RotateAnimation(-1 * ROTATED_POSITION, INITIAL_POSITION,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            }

            rotateAnimation.setDuration(200);
            rotateAnimation.setFillAfter(true);
            imgExpand.startAnimation(rotateAnimation);
        }
    }


    static class SubjectViewHolder extends ChildViewHolder {

        TextView txtTenMonHoc;
        TextView txtSTC;
        TextView txtThi;
        TextView txtTongKet;

        SubjectViewHolder(View itemView) {
            super(itemView);

            txtTenMonHoc = itemView.findViewById(R.id.txt_ten_mon_hoc);
            txtSTC = itemView.findViewById(R.id.txt_stc);
            txtThi = itemView.findViewById(R.id.txt_diem_thi10);
            txtTongKet = itemView.findViewById(R.id.txt_tong_ket4);
        }

        void bind(SubjectScore monHoc) {
            txtTenMonHoc.setText(monHoc.tenMonHoc);
            txtSTC.setText(monHoc.soTinChi);
            txtThi.setText(monHoc.thi);
            txtTongKet.setText(monHoc.TK4);
        }
    }
}
