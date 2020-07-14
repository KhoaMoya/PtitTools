package com.khoa.ptittools.ui.news.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khoa.ptittools.base.model.News;
import com.khoa.ptittools.databinding.ItemNewsBinding;
import com.squareup.picasso.Picasso;

import java.util.List;


public class NewsRecycleViewAdapter extends RecyclerView.Adapter<NewsRecycleViewAdapter.NewsViewHolder> {

    private ItemClickListener listener;
    private List<News> newsList;
    private Activity activity;

    public NewsRecycleViewAdapter(Activity activity) {
        this.activity = activity;
    }

    public void setNewsList(List<News> list) {
        this.newsList = list;
        notifyDataSetChanged();
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemNewsBinding binding = ItemNewsBinding.inflate(layoutInflater, parent, false);
        return new NewsViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final NewsViewHolder holder, final int position) {

        holder.binding.txtTitle.setText(newsList.get(holder.getAdapterPosition()).title);
        holder.binding.txtTime.setText(newsList.get(holder.getAdapterPosition()).time);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
//                    Pair<View, String> imgPair = Pair.create((View) holder.binding.newsImg, "img");
//                    Pair<View, String> titlePair = Pair.create((View) holder.binding.txtTitle, "title");
//                    Pair<View, String> timePair = Pair.create((View) holder.binding.txtTime, "time");
//                    Pair<View, String> containerPair = Pair.create((View) holder.itemView, "container");
//                    ActivityOptionsCompat optionsCompat =
//                            ActivityOptionsCompat.makeSceneTransitionAnimation(activity, imgPair, titlePair, timePair, containerPair);
//                    listener.onClickItem(holder.getAdapterPosition(), optionsCompat);
                    listener.onClickItem(holder.getAdapterPosition());
                }
            }
        });

        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    view.setScaleX(0.95f);
                    view.setScaleY(0.95f);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    view.setScaleX(1f);
                    view.setScaleY(1f);
                }
                return false;
            }
        });

        try {
            Picasso.get()
                    .load(newsList.get(holder.getAdapterPosition()).imageUrl)
                    .into(holder.binding.newsImg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return newsList == null ? 0 : newsList.size();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        ItemNewsBinding binding;

        NewsViewHolder(ItemNewsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
