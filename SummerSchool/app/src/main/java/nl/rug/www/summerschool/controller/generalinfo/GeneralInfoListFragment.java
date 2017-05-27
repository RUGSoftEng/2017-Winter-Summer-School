package nl.rug.www.summerschool.controller.generalinfo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import nl.rug.www.summerschool.controller.ContentsLab;
import nl.rug.www.summerschool.networking.NetworkingService;
import nl.rug.www.summerschool.R;
import nl.rug.www.summerschool.model.GeneralInfo;

/**
 * This class is a fragment on main pager activity.
 * It shows a list of titles of all general infomation in database.
 *
 * @since 13/04/2017
 * @author Jeongkyun Oh
 */

public class GeneralInfoListFragment extends Fragment {

    private RecyclerView mGeneralInfoRecyclerView;
    private List<GeneralInfo> mItems = new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        TextView section = (TextView)v.findViewById(R.id.section_name);
        section.setText(R.string.general_info);

        mSwipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchGeneralInfosTask().execute();
            }
        });

        if (mItems == null)
            mSwipeRefreshLayout.setRefreshing(true);

        mGeneralInfoRecyclerView = (RecyclerView)v.findViewById(R.id.recycler_view);
        mGeneralInfoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        setupAdatper();
        new FetchGeneralInfosTask().execute();
        return v;
    }

    private void setupAdatper() {
        if (isAdded()) {
            mGeneralInfoRecyclerView.setAdapter(new GeneralInfoAdapter(mItems));
        }
    }

    private class GeneralInfoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private GeneralInfo mGeneralInfo;
        private TextView mTitleTextView;
        private ImageView mImageView;

        private GeneralInfoHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_generalinfo, parent, false));

            mTitleTextView = (TextView)itemView.findViewById(R.id.content_title);
            mImageView = (ImageView)itemView.findViewById(R.id.icon_image_view);
            itemView.setOnClickListener(this);
        }

        private void bind(GeneralInfo generalInfo){
            mGeneralInfo = generalInfo;
            mTitleTextView.setText(mGeneralInfo.getTitle());
            mImageView.setImageResource(selectPicture(mGeneralInfo.getTitle().toLowerCase()));
        }

        private int selectPicture(String title) {
            String[] strings = {"weather", "visa", "housing", "departure", "insurance", "financial", "do", "welcome"};
            if (title.contains(strings[0])) {
                return R.mipmap.icon_cloud;
            } else if (title.contains(strings[1])) {
                return R.mipmap.icon_creditcard;
            } else if (title.contains(strings[2])) {
                return R.mipmap.icon_building;
            } else if (title.contains(strings[3])) {
                return R.mipmap.icon_flight;
            } else if (title.contains(strings[4])) {
                return R.mipmap.icon_hospital;
            } else if (title.contains(strings[5])) {
                return R.mipmap.icon_money;
            } else if (title.contains(strings[6])) {
                return R.mipmap.icon_list;
            } else if (title.contains(strings[7])) {
                return R.mipmap.icon_home;
            } else {
                int idx = Math.abs(title.hashCode() % 4);

                Log.d("Generalinfo", idx + "is it possible");
                switch (idx) {
                    case 0 :
                        return R.mipmap.ic_smile;
                    case 1 :
                        return R.mipmap.ic_smile;
                    case 2 :
                        return R.mipmap.ic_star;
                    case 3 :
                        return R.mipmap.ic_arrowup;
                }
            }
            return 0;
        }

        @Override
        public void onClick(View v) {
            Intent intent = GeneralInfoPagerActivity.newIntent(getActivity(), mGeneralInfo.getId());
            startActivity(intent);
        }
    }

    private class GeneralInfoAdapter extends RecyclerView.Adapter<GeneralInfoHolder> {

        private List<GeneralInfo> mGeneralInfos;

        private GeneralInfoAdapter(List<GeneralInfo> generalInfos) {
            mGeneralInfos = generalInfos;
        }

        @Override
        public GeneralInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new GeneralInfoHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(GeneralInfoHolder holder, int position) {
            GeneralInfo generalInfo = mGeneralInfos.get(position);
            holder.bind(generalInfo);
        }

        @Override
        public int getItemCount() {
            return mGeneralInfos.size();
        }
    }


    private class FetchGeneralInfosTask extends AsyncTask<Void, Void, List<GeneralInfo>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mSwipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected List<GeneralInfo> doInBackground(Void... params) {
            return new NetworkingService().fetchGeneralInfos();
        }

        @Override
        protected void onPostExecute(List<GeneralInfo> generalInfos) {
            mItems = generalInfos;
            setupAdatper();
            ContentsLab.get().updateGeneralInfos(mItems);
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    }
}
