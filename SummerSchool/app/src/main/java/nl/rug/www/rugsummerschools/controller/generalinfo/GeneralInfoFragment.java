package nl.rug.www.rugsummerschools.controller.generalinfo;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.controller.ContentsLab;
import nl.rug.www.rugsummerschools.model.GeneralInfo;

/**
 * This class is to show the details of general information
 * when any item is clicked on GeneralInfoListFragment.
 *
 * @since 13/04/2017
 * @author Jeongkyun Oh
 */

@Deprecated
public class GeneralInfoFragment extends Fragment {

    private static final String ARG_GENERAL_INFO_ID = "generalinfo_id";

    private GeneralInfo mGeneralInfo;

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
        mGeneralInfo = ContentsLab.get().getGeneralInfo(generalInfoId);
    }

    @SuppressWarnings("deprecation")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_generalinfo, container, false);

        TextView mDescription = (TextView)view.findViewById(R.id.generalinfo_detail);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mDescription.setText(Html.fromHtml(mGeneralInfo.getDescription(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            mDescription.setText(Html.fromHtml(mGeneralInfo.getDescription()));
        }

        return view;
    }
}
