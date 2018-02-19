package com.example.android.tellme.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.android.tellme.databinding.SourceListItemBinding;
import com.example.android.tellme.model.Source;

import java.util.ArrayList;

/**
 * Created by shafy on 13/02/2018.
 */

public class SourcesListAdapter extends RecyclerView.Adapter<SourcesListAdapter.ViewHolder> {
    private ArrayList<Source> mData;
    private OnItemClicked mItemClicked;

    public SourcesListAdapter(ArrayList<Source> data, OnItemClicked itemClicked) {
        this.mData = data;
        this.mItemClicked = itemClicked;
    }

    interface OnItemClicked{
        void onItemClickListener(int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context =parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        SourceListItemBinding binding = SourceListItemBinding.inflate(inflater,parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        if(mData!=null)
            return mData.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private SourceListItemBinding mBinding;
        public ViewHolder(SourceListItemBinding binding) {
            super(binding.getRoot());
            mBinding=binding;
        }

        public void bindView(int position){
            mBinding.getRoot().setOnClickListener(this);
            mBinding.tvSourceName.setText(mData.get(position).getName());
            mBinding.tvSourceDescription.setText(mData.get(position).getDescription());
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mItemClicked.onItemClickListener(position);
        }
    }
}
