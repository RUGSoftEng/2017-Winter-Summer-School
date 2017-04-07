package nl.rug.www.summerschool;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jk on 3/29/17.
 */

public class GeneralInfoListFragment extends Fragment {

    private static final String TAG = "GeneralInfoListFragment";

    private RecyclerView mGeneralInfoRecyclerView;
    private List<GeneralInfo> mItems = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        new FetchGeneralInfosTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        TextView section = (TextView)v.findViewById(R.id.section_name);
        section.setText(R.string.general_info);

        mGeneralInfoRecyclerView = (RecyclerView)v.findViewById(R.id.recycler_view);
        mGeneralInfoRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setupAdatper();

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

        public GeneralInfoHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_content, parent, false));

            mTitleTextView = (TextView)itemView.findViewById(R.id.content_title);
            itemView.setOnClickListener(this);
        }

        public void bind(GeneralInfo generalInfo){
            mGeneralInfo = generalInfo;
            mTitleTextView.setText(mGeneralInfo.getTitle());
        }

        @Override
        public void onClick(View v) {
            Intent intent = GeneralInfoPagerActivity.newIntent(getActivity(), mGeneralInfo.getId());
            startActivity(intent);
        }
    }

    private class GeneralInfoAdapter extends RecyclerView.Adapter<GeneralInfoHolder> {

        private List<GeneralInfo> mGeneralInfos;

        public GeneralInfoAdapter(List<GeneralInfo> generalInfos) {
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
        protected List<GeneralInfo> doInBackground(Void... params) {
            return new NetworkingService().fetchGeneralInfos();
        }

        @Override
        protected void onPostExecute(List<GeneralInfo> generalInfos) {
            mItems = generalInfos;
            setupAdatper();
            ContentsLab.get(getActivity()).updateGeneralInfos(mItems);
        }
    }
}
