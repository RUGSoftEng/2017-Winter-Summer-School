package nl.rug.www.summerschool.controller.announcement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import nl.rug.www.summerschool.controller.ContentsLab;
import nl.rug.www.summerschool.R;
import nl.rug.www.summerschool.model.Announcement;

/**
 * Announcement fragment is to show the details of announcement
 * when any item is clicked on AnnouncementListFragment.
 *
 * @since 13/04/2017
 * @author Jeongkyun Oh
 */

public class AnnouncementFragment extends Fragment {

    /** key for transferring announcement id */
    private static final String ARG_ANNOUNCEMENT_ID = "announcement_id";

    /** instance of the announcement shown on this fragment */
    private Announcement mAnnouncement;

    public static AnnouncementFragment newInstance(String announcementId) {
        Bundle args = new Bundle();
        args.putString(ARG_ANNOUNCEMENT_ID, announcementId);

        AnnouncementFragment fragment = new AnnouncementFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String announcementId = getArguments().getString(ARG_ANNOUNCEMENT_ID);
        mAnnouncement = ContentsLab.get().getAnnouncement(announcementId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_announcement, container, false);

        TextView mTitle = (TextView)view.findViewById(R.id.announcement_title);
        mTitle.setText(mAnnouncement.getTitle());
        TextView mDescription = (TextView)view.findViewById(R.id.announcement_detail);
        mDescription.setText(Html.fromHtml(mAnnouncement.getDescription()));
        TextView mPoster = (TextView)view.findViewById(R.id.announcement_author);
        mPoster.setText(mAnnouncement.getPoster());
        TextView mDate = (TextView)view.findViewById(R.id.announcement_date);
        /* fetched date is ISO string. Spliting by "T", the date can be gotten */
        String[] part = mAnnouncement.getDate().split("T");
        mDate.setText(part[0]);

        return view;
    }
}
