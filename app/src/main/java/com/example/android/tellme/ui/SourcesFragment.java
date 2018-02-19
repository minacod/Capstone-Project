package com.example.android.tellme.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.tellme.databinding.SourcesFragmentBinding;
import com.example.android.tellme.model.Source;

import java.util.ArrayList;

/**
 * Created by shafy on 14/02/2018.
 */

public class SourcesFragment extends Fragment{

    private ArrayList<Source> mData;
    private SourcesListAdapter.OnItemClicked mClicked;
    private SourcesListAdapter mAdapter;
    private GridLayoutManager mManager;
    private Parcelable mState;

    public SourcesFragment() {
    }

    public Parcelable getState(){
        return mManager.onSaveInstanceState();
    }
    public void setState(Parcelable s) {
        mState = s;
    }


    public void setData(ArrayList<Source> mData) {
        this.mData = mData;
    }

    public void setClicked(SourcesListAdapter.OnItemClicked mClicked) {
        this.mClicked = mClicked;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SourcesFragmentBinding binding = SourcesFragmentBinding.inflate(inflater,container,false);
        final RecyclerView rv = binding.rvSourcesList;
        mAdapter = new SourcesListAdapter(mData, mClicked);
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
        if (mState != null)
            mManager.onRestoreInstanceState(mState);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            mManager.setSpanCount(2);
        else
            mManager.setSpanCount(1);
    }
}
