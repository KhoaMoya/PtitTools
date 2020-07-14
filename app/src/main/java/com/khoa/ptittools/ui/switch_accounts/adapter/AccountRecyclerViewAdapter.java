package com.khoa.ptittools.ui.switch_accounts.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khoa.ptittools.MyApplication;
import com.khoa.ptittools.base.model.User;
import com.khoa.ptittools.databinding.ItemAccountBinding;

import java.util.List;

public class AccountRecyclerViewAdapter extends RecyclerView.Adapter<AccountRecyclerViewAdapter.AccountViewHolder> {

    private List<User> userList;
    private User currentAccount;
    private AccountOnClickListener listener;

    public AccountRecyclerViewAdapter(List<User> userList) {
        this.userList = userList;
        this.currentAccount = MyApplication.getAppRepository().takeUser();
    }

    public void setAccountClickListener(AccountOnClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAccountBinding binding = ItemAccountBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AccountViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        User user = userList.get(holder.getAdapterPosition());
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList == null ? 0 : userList.size();
    }

    public class AccountViewHolder extends RecyclerView.ViewHolder {

        private ItemAccountBinding binding;
        private User user;

        public AccountViewHolder(@NonNull ItemAccountBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(final User user) {
            this.user = user;
            binding.txtName.setText(user.ten);
            binding.txtCode.setText(user.maSV);

            if (user.maSV.equals(currentAccount.maSV)) {
                binding.imgDelete.setVisibility(View.INVISIBLE);
            }

            binding.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null) listener.onClickDeleteAccount(user);
                }
            });

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null && !currentAccount.maSV.equals(user.maSV)) listener.onClickAccount(user);
                }
            });
        }

    }

}
