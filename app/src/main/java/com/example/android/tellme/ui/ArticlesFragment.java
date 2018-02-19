package com.example.android.tellme.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.tellme.databinding.ArticlesFragmentBinding;
import com.example.android.tellme.model.Article;
import com.example.android.tellme.utils.NetworkUtils;

import java.util.ArrayList;

/**
 * Created by shafy on 14/02/2018.
 */

public class ArticlesFragment extends Fragment {

    private ArrayList<Article> mData;
    private ArticlesListAdapter.OnItemClicked mClicked;
    private ArticlesListAdapter mAdapter;
    private GridLayoutManager mManager;
    private Parcelable mState;
    private ArticlesFragmentBinding mBinding;

    public ArticlesFragment() {
    }

    public Parcelable getState() {
        return mManager.onSaveInstanceState();
    }

    public void setState(Parcelable s) {
        mState = s;
    }

    public void setData(ArrayList<Article> mData) {
        this.mData = mData;
    }

    public void setClicked(ArticlesListAdapter.OnItemClicked mClicked) {
        this.mClicked = mClicked;
    }

    public void notifyRecycler() {
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = ArticlesFragmentBinding.inflate(inflater, container, false);
        final RecyclerView rv = mBinding.rvArticlesList;
        mAdapter = new ArticlesListAdapter(mData, mClicked);
        mManager = new GridLayoutManager(this.getContext(), 1, GridLayoutManager.VERTICAL, false);
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);
        rv.setLayoutManager(mManager);
        rv.setAdapter(mAdapter);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!rv.canScrollVertically(1)) {
                    if(NetworkUtils.isConnected(ArticlesFragment.this.getContext()))
                    ((MainActivity) getActivity()).loadMoreArticle();
                }
            }
        });
        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mState != null)
            mManager.onRestoreInstanceState(mState);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            mManager.setSpanCount(2);
        else
            mManager.setSpanCount(1);
    }
}
