package nl.rug.www.rugsummerschools.controller.generalinfo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.controller.ContentsLab;
import nl.rug.www.rugsummerschools.model.GeneralInfo;
import nl.rug.www.rugsummerschools.networking.NetworkingService;

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
    private HashMap<String, Integer> mPicHashMap;
    private String[] mStrings = {"weather", "visa", "house", "departure", "insurance", "financial", "do", "welcome", "diet", "internet", "information", "location"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPicHashMap = new HashMap<>();
        mPicHashMap.put(mStrings[0], R.mipmap.icon_cloud);
        mPicHashMap.put(mStrings[1], R.mipmap.icon_creditcard);
        mPicHashMap.put(mStrings[2], R.mipmap.icon_building);
        mPicHashMap.put(mStrings[3], R.mipmap.icon_flight);
        mPicHashMap.put(mStrings[4], R.mipmap.icon_hospital);
        mPicHashMap.put(mStrings[5], R.mipmap.icon_money);
        mPicHashMap.put(mStrings[6], R.mipmap.icon_list);
        mPicHashMap.put(mStrings[7], R.mipmap.icon_home);
        mPicHashMap.put(mStrings[8], R.mipmap.ic_diet);
        mPicHashMap.put(mStrings[9], R.mipmap.ic_internet);
        mPicHashMap.put(mStrings[10], R.mipmap.ic_info);
        mPicHashMap.put(mStrings[11], R.mipmap.ic_loc);
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
            mTitleTextView.setSelected(true);
            mImageView.setImageResource(selectPicture(mGeneralInfo.getTitle().toLowerCase()));
        }

        private int selectPicture(String title) {
            for (String s : mStrings) {
                if (title.contains(s))
                    return mPicHashMap.get(s);
            }

            int idx = Math.abs(title.hashCode() % 4);

            switch (idx) {
                case 0:
                    return R.mipmap.ic_smile;
                case 1:
                    return R.mipmap.ic_smile;
                case 2:
                    return R.mipmap.ic_star;
                case 3:
                    return R.mipmap.ic_arrowup;
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
