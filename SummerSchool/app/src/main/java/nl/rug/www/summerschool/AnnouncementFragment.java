package nl.rug.www.summerschool;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 * Created by jk on 3/31/17.
 */

public class AnnouncementFragment extends Fragment {

    private static final String ARG_ANNOUNCEMENT_ID = "announcement_id";

    private Announcement mAnnouncement;
    private TextView mTitle;
    private TextView mDescription;
    private TextView mPoster;
    private TextView mDate;

    public static AnnouncementFragment newInstance(String announcementId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_ANNOUNCEMENT_ID, announcementId);

        AnnouncementFragment fragment = new AnnouncementFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String announcementId = (String) getArguments().getSerializable(ARG_ANNOUNCEMENT_ID);
        mAnnouncement = ContentsLab.get(getActivity()).getAnnouncement(announcementId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_announcement, container, false);

        mTitle = (TextView)view.findViewById(R.id.announcement_title);
        mTitle.setText(mAnnouncement.getTitle());
        mDescription = (TextView)view.findViewById(R.id.announcement_detail);
        mDescription.setText(Html.fromHtml(mAnnouncement.getDescription()));
        mPoster = (TextView)view.findViewById(R.id.announcement_author);
        mPoster.setText(mAnnouncement.getPoster());
        mDate = (TextView)view.findViewById(R.id.announcement_date);
        String[] part = mAnnouncement.getDate().split("T");
        mDate.setText(part[0]);

        return view;
    }
}
