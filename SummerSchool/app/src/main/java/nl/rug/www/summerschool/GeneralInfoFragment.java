package nl.rug.www.summerschool;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * This class is to show the details of general information
 * when any item is clicked on GeneralInfoListFragment.
 *
 * @since 13/04/2017
 * @author Jeongkyun Oh
 */

public class GeneralInfoFragment extends Fragment {

    private static final String ARG_GENERAL_INFO_ID = "generalinfo_id";

    private GeneralInfo mGeneralInfo;
    private TextView mTitle;
    private TextView mDescription;

    public static GeneralInfoFragment newInstance(String generalinfoId) {
        Bundle args = new Bundle();
        args.putString(ARG_GENERAL_INFO_ID, generalinfoId);

        GeneralInfoFragment fragment = new GeneralInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String generalInfoId = getArguments().getString(ARG_GENERAL_INFO_ID);
        mGeneralInfo = ContentsLab.get(getActivity()).getGeneralInfo(generalInfoId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_generalinfo, container, false);

        mTitle = (TextView)view.findViewById(R.id.generalinfo_title);
        mTitle.setText(mGeneralInfo.getTitle());
        mDescription = (TextView)view.findViewById(R.id.generalinfo_detail);
        mDescription.setText(Html.fromHtml(mGeneralInfo.getDescription()));

        return view;
    }
}
