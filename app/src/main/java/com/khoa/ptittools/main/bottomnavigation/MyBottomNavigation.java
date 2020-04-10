package com.khoa.ptittools.main.bottomnavigation;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import com.khoa.ptittools.R;
import com.khoa.ptittools.databinding.ItemBottomNavigationBinding;

import java.util.ArrayList;
import java.util.List;

public class MyBottomNavigation {

    private List<MyItem> itemList;
    private List<ViewGroup> itemViewList;
    private Context context;
    private ViewGroup parentView;
    private BottomNavigationItemSelectListener listener;

    public MyBottomNavigation(ViewGroup parentView, List<MyItem> list, BottomNavigationItemSelectListener listener) {
        this.context = parentView.getContext();
        this.parentView = parentView;
        this.itemList = list;
        this.listener = listener;
        this.itemViewList = new ArrayList<>();

        drawItemList();
    }

    private void drawItemList() {
        if (itemList == null || parentView == null) return;
        for (MyItem item : itemList) {
            View view = createItemView(item);
            ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
            view.setLayoutParams(params);
            itemViewList.add((ViewGroup) view);
            parentView.addView(view);
        }
    }

    private View createItemView(final MyItem item) {
        ItemBottomNavigationBinding binding = ItemBottomNavigationBinding.inflate(LayoutInflater.from(context));
        binding.icon.setImageResource(item.iconId);
        binding.title.setText(item.title);

        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null) listener.onSelectedItemChanged(item.tag);
            }
        });

        return binding.getRoot();
    }

    public void setItemSelected(String tag){
        int index = -1;
        for(int i=0; i<itemList.size(); i++){
            if(itemList.get(i).tag.equals(tag)){
                index = i;
                break;
            }
        }

        if(index<0) return;

        ViewGroup seletedView = itemViewList.get(index);
        resetAllItemSelect();
        setItemViewSelected(seletedView);
    }

    private void resetAllItemSelect(){
        for(ViewGroup viewGroup : itemViewList){
            ItemBottomNavigationBinding binding = ItemBottomNavigationBinding.bind(viewGroup);
            binding.icon.setColorFilter(Color.GRAY);
            binding.title.setTextColor(Color.GRAY);
        }
    }

    private void setItemViewSelected(View view){
        ItemBottomNavigationBinding binding = ItemBottomNavigationBinding.bind(view);
        binding.icon.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary));
        binding.title.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
    }

}
