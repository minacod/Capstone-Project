package com.example.android.tellme.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.tellme.R;
import com.example.android.tellme.data.ArticlesContract;
import com.example.android.tellme.databinding.ArticleListItemBinding;
import com.example.android.tellme.model.Article;
import com.example.android.tellme.utils.GlideApp;

import java.util.ArrayList;

/**
 * Created by shafy on 12/02/2018.
 */

public class ArticlesListAdapter extends RecyclerView.Adapter<ArticlesListAdapter.ViewHolder> {

    private ArrayList<Article> mData;
    private OnItemClicked mItemClicked;

    public ArticlesListAdapter(ArrayList<Article> data, OnItemClicked itemClicked) {
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
        ArticleListItemBinding binding = ArticleListItemBinding.inflate(inflater,parent, false);
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
        private final ArticleListItemBinding mBinding;

        public ViewHolder(ArticleListItemBinding binding) {
            super(binding.getRoot());
            mBinding=binding;
        }

        public void bindView(final int position){
            mBinding.ivArticlePlaceholder.setOnClickListener(this);
            mBinding.tvArticleTitle.setOnClickListener(this);
            mBinding.tvArticleTitle.setText(mData.get(position).getTitle());
            mBinding.tvArticleDate.setText(mData.get(position).getDate().substring(0,10));
            mBinding.ivOpenInBrowser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri webpage = Uri.parse(mData.get(position).getUrl());
                    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                    mBinding.getRoot().getContext().startActivity(intent);
                }
            });
            GlideApp.with(mBinding.getRoot().getContext())
                    .load(mData.get(position).getImageUrl())
                    .placeholder(R.drawable.ic_tell_me)
                    .error(R.drawable.ic_tell_me)
                    .into(mBinding.ivArticlePlaceholder);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mItemClicked.onItemClickListener(position);
        }
    }
}
