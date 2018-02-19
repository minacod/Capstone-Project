package com.example.android.tellme.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import com.example.android.tellme.utils.NetworkUtils;

import java.util.ArrayList;

/**
 * Created by shafy on 15/02/2018.
 */

public class CashedListAdapter extends RecyclerView.Adapter<CashedListAdapter.ViewHolder> {

    private Cursor mData;
    private OnItemClicked mItemClicked;

    public CashedListAdapter(Cursor data, OnItemClicked itemClicked) {
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
            return mData.getCount();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ArticleListItemBinding mBinding;

        public ViewHolder(ArticleListItemBinding binding) {
            super(binding.getRoot());
            mBinding=binding;
        }

        public void bindView(int position){
            mBinding.getRoot().setOnClickListener(this);
            mData.moveToPosition(position);
            Article article = new Article(
                    mData.getString(mData.getColumnIndex(ArticlesContract.ArticlesEntry.SOURCE_NAME)),
                    mData.getString(mData.getColumnIndex(ArticlesContract.ArticlesEntry.ARTICLE_AUTHOR)),
                    mData.getString(mData.getColumnIndex(ArticlesContract.ArticlesEntry.ARTICLE_TITLE)),
                    mData.getString(mData.getColumnIndex(ArticlesContract.ArticlesEntry.DESCRIPTION)),
                    mData.getString(mData.getColumnIndex(ArticlesContract.ArticlesEntry.IMAGE_URL)),
                    mData.getString(mData.getColumnIndex(ArticlesContract.ArticlesEntry.URL)),
                    mData.getString(mData.getColumnIndex(ArticlesContract.ArticlesEntry.DATE))
            );
            mBinding.tvArticleTitle.setText(article.getTitle());
            mBinding.tvArticleDate.setText(article.getDate().substring(0,10));
            if(!NetworkUtils.isConnected(mBinding.getRoot().getContext()))
                mBinding.ivOpenInBrowser.setVisibility(View.INVISIBLE);
            else
                mBinding.ivOpenInBrowser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri webpage = Uri.parse(mData.getString(mData.getColumnIndex(ArticlesContract.ArticlesEntry.URL)));
                        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                        mBinding.getRoot().getContext().startActivity(intent);
                    }
                });
            GlideApp.with(mBinding.getRoot().getContext())
                    .load(article.getImageUrl())
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
