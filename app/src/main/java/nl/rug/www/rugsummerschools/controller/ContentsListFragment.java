package nl.rug.www.rugsummerschools.controller;

import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.databinding.FragmentListBinding;
import nl.rug.www.rugsummerschools.model.Content;


/**
 * This class is general contents list fragment that inflates main activity.
 *
 * @since 10/02/2018
 * @author Jeongkyun Oh
 * @version 2.0.0
 */

public abstract class ContentsListFragment<T extends Content, K extends ContentHolder<T>> extends Fragment {

    private static final String TAG = "ContentsListFragment";

    /** recycler view inflates list of contents by using viewholder */
    protected FragmentListBinding mBinding;

    /** instance of the contents list */
    protected List<T> mItems = new ArrayList<>();

    protected abstract void bindViews();
    protected abstract int getSectionStringId();
    protected abstract List<T> fetchContents();
    protected abstract void update(List<T> contents);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_list, container, false);

        mBinding.sectionName.setText(getSectionStringId());
        mBinding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchTask().execute();
            }
        });

        if (mItems == null)
            mBinding.refreshLayout.setRefreshing(true);

        bindViews();
        setupAdatper();

        new FetchTask().execute();
        return mBinding.getRoot();
    }

    protected abstract void setupAdatper();

    private class FetchTask extends AsyncTask<Void, Void, List<T>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mBinding.refreshLayout.setRefreshing(true);
        }

        @Override
        protected List<T> doInBackground(Void... params) {
            return fetchContents();
        }

        @Override
        protected void onPostExecute(List<T> contents) {
            mItems = contents;
            setupAdatper();
            update(mItems);
            if (mBinding.refreshLayout.isRefreshing()) {
                mBinding.refreshLayout.setRefreshing(false);
            }
        }
    }
}
