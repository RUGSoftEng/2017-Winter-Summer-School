package nl.rug.www.rugsummerschools.controller;

import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.databinding.FragmentListBinding;
import nl.rug.www.rugsummerschools.model.Content;


/**
 * Created by jk on 17. 11. 27.
 */

@Deprecated
public abstract class ContentsListFragment<T extends Content> extends Fragment {

    /** recycler view inflates list of contents by using viewholder */
    protected FragmentListBinding mBinding;

    /** instance of the contents list */
    protected List<T> mItems = new ArrayList<>();

    /** refresh layout in order to update lists of contents */
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    protected abstract void bindViews();
    protected abstract int getSectionStringId();
    protected abstract List<T> fetchContents();
    protected abstract void update(List<T> contents);
    protected abstract void holderbind(ContentsHolder holder, T item);
    protected abstract View getViews(LayoutInflater inflater, ViewGroup parent);
    protected abstract ContentsHolder generateContentsHolder(LayoutInflater inflater, ViewGroup parent);

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
            mSwipeRefreshLayout.setRefreshing(true);

        bindViews();
        setupAdatper();

        new FetchTask().execute();
        return mBinding.getRoot();
    }

    private void setupAdatper() {
        if (isAdded()) {
            mBinding.recyclerView.setAdapter(new ContentsAdapter(mItems));
        }
    }

    protected abstract class ContentsHolder extends RecyclerView.ViewHolder {
        public ContentsHolder(LayoutInflater inflater, ViewGroup parent) {
            super(getViews(inflater, parent));
        }
    }

    private class ContentsAdapter extends RecyclerView.Adapter<ContentsHolder> {

        private List<T> mContents;

        private ContentsAdapter(List<T> contents) {
            mContents = contents;
        }

        @Override
        public ContentsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return generateContentsHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(ContentsHolder holder, int position) {
            T content = mContents.get(position);
            holderbind(holder, content);
        }

        @Override
        public int getItemCount() {
            return mContents.size();
        }
    }

    private class FetchTask extends AsyncTask<Void, Void, List<T>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mSwipeRefreshLayout.setRefreshing(true);
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
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    }
}
