package com.example.android.tellme.ui;

import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.tellme.databinding.ArticlesFragmentBinding;
import com.example.android.tellme.model.Article;

import java.util.ArrayList;

/**
 * Created by shafy on 15/02/2018.
 */

public class CashedFragment extends Fragment {

    private Cursor mData;
    private CashedListAdapter.OnItemClicked mClicked;
    private CashedListAdapter mAdapter;
    private GridLayoutManager mManager;
    private Parcelable mState;

    public CashedFragment() {
    }

    public Parcelable getState(){
        return mManager.onSaveInstanceState();
    }
    public void setState(Parcelable s) {
        mState = s;
    }

    public void setData(Cursor mData) {
        this.mData = mData;
    }

    public void setClicked(CashedListAdapter.OnItemClicked mClicked) {
        this.mClicked = mClicked;
    }

    public void notifyRecycler() {
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ArticlesFragmentBinding binding = ArticlesFragmentBinding.inflate(inflater,container,false);
        final RecyclerView rv = binding.rvArticlesList;
        mAdapter = new CashedListAdapter(mData, mClicked);
        mManager =new GridLayoutManager(this.getContext(),1,GridLayoutManager.VERTICAL,false);
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);
        rv.setLayoutManager(mManager);
        rv.setAdapter(mAdapter);
        return binding.getRoot();
    }
    @Override
    public void onResume() {
        super.onResume();
        if(mState!=null)
            mManager.onRestoreInstanceState(mState);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            mManager.setSpanCount(2);
        else
            mManager.setSpanCount(1);
    }
}
